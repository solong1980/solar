package com.lszyhb.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lszyhb.showcollectdata.ClientSocket;
import com.lszyhb.showcollectdata.LoginActivity;
import com.lszyhb.showcollectdata.R;
import com.lszyhb.showcollectdata.SupplyConnectAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;


/**
 * Created by kkk8199 on 11/21/17. 升级apk
 */

public class UpdateManager  {
    private static final int GET_VERSION = 0;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

//	private static String packageName;

    public static boolean mUpdateFlag = false;

    private AppVersionDetail mBean;

    public static long downid=0;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if( msg.arg1 == 1 ){
                Log.d(TAG,"loginin error");
                //网络连接出错
                if( LoginActivity.showNetWorkFailed(mContext) == false ){
                    return;
                }
                else{
                    LoginActivity.showNetDataFailedTip(mContext);
                }
            }
            else{
                switch (msg.arg2)
                {

                    case GET_VERSION://得到版本
                        if( msg.obj != null  && msg.obj instanceof String ){
                            int serviceCode =  Integer.parseInt(msg.obj.toString());
                            // 获取当前软件版本
                            int versionCode = getVersionCode(mContext);
                            Log.d("updateManager","get version.txt file versionCode:"+versionCode+",serviceCode:"+serviceCode);
                            if ( serviceCode > versionCode)//平台上有高版本可更新
                            {
                                showNoticeDialog();//显示提示对话框
                            }
                        }
                        break;
                    // 正在下载
                    case DOWNLOAD:
                        // 设置进度条位置
                        mProgress.setProgress(progress);
                        break;
                    case DOWNLOAD_FINISH:
    //					Log.d(TAG,"download apk finished");
                        // 安装文件
                        installApk();
                        break;
                    default:
                        break;
                }
            }
        };
    };


    public UpdateManager(Context context)
    {

        this.mContext = context;
        mBean = new AppVersionDetail();
        mBean.name="com.lszyhb.showcollecdata.apk";
       // mBean.name="app-debug.apk";
        mBean.url="http://123.56.76.77:8080/solar-web/file/download/";
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(ClientSocket msocket)
    {
        //获取客户端升级信息
        getVersionInfo(msocket);
        this.mUpdateFlag = true;
    }

    /**
     * 检查平台软件版本
     *
     * @return
     */
    private void getVersionInfo(ClientSocket mscocket)
    {
         SupplyConnectAPI.getInstance().getprogramversion(mscocket,mHandler);

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
		String packageName=info.applicationInfo.packageName;
            Log.d(TAG,"versionCode="+versionCode+"packageName="+packageName);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog()
    {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(mContext.getResources().getString(R.string.soft_update_info)+"\n");
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new Dialog.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new Dialog.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new Dialog.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk()
    {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

/**
 * 下载文件线程
 *
 *
 */
private class downloadApkThread extends Thread
{
    @Override
    public void run()
    {
        try
        {
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                mSavePath = sdpath + "download";
                String newurl = mBean.url + String.valueOf(downid);
                URL url = new URL(newurl);
					Log.d(TAG,"download apk url:"+newurl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.setRequestMethod("GET");
                conn.connect();
                Log.i(TAG,"conn="+conn);

                // 获取文件大小
                int length = conn.getContentLength();
                Log.i(TAG,"length="+length);
                // 创建输入流
                InputStream is = conn.getInputStream();

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists())
                {
                    file.mkdir();
                }
                String name =  mBean.name;
                File apkFile = new File(mSavePath,name);
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                do
                {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    Message msg = new Message();
                    msg.arg1 = 0;
                    msg.arg2= DOWNLOAD;
                    mHandler.sendMessage(msg);
                    if (numread <= 0)
                    {
                        // 下载完成
                        Log.i(TAG,"into download finish");
                        msg = new Message();
                        msg.arg1 = 0;
                        msg.arg2 = DOWNLOAD_FINISH;
                        mHandler.sendMessage(msg);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        // 取消下载对话框显示
        mDownloadDialog.dismiss();
    }
};

/**
 * 安装APK文件
 */
private void installApk()
        {
		Log.d(TAG,"installApk mBean.name:"+mBean.name);
        File apkfile = new File(mSavePath, mBean.name);
        if (!apkfile.exists())
        {
        return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
        }
}
