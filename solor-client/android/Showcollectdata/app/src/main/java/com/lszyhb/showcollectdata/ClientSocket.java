package com.lszyhb.showcollectdata;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lszyhb.basicclass.ApkVersion;
import com.lszyhb.basicclass.ConnectAPI;
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowAbt;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowPage;
import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.basicclass.ShowVCode;
import com.lszyhb.common.JsonUtilTool;
import com.lszyhb.update.UpdateManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by kkk8199 on 11/16/17. 连接客户端
 */

public class ClientSocket {
    private static final int SOCKET_TIMEOUT = 100000;//超时时间
    public static final String SERVER_IP_ADDR = "39.107.24.81";// 服务器地址123.56.76.77
    public static final int PORT = 10122;// 服务器端口号
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    public Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    public volatile static ClientSocket instance;

    public static ClientSocket getInstance() {
        if(instance ==null) {
            synchronized (ClientSocket.class) {
                instance = new ClientSocket();
            }
        }
        return instance;

    }

//建立连接,并初始化input和out
    public void  initSocket(){
        executorService.submit(new Runnable() {
            public void run() {
                Log.i("kkk8199","initsocket");
                boolean bSocket= bSocket();
                if (!bSocket)
                    throw new RuntimeException("");
                try {
                    //input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    //       socket.getOutputStream())), true);
                    // 向服务器端发送数据
                    out = new DataOutputStream(socket.getOutputStream());
                    // 读取服务器端数据
                    input = new DataInputStream(socket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                    if (socket == null || socket.isClosed() || !socket.isConnected()) {
                        Clientsocketclose();
                    }
                }
            }
        });
    }
//关闭资源
    public void Clientsocketclose() {
        try {
            if (out != null) {
                out.close();
            }
            if (input != null) {
                input.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("客户端 finally 异常:" + e.getMessage());
        } finally {
            input = null;
            out = null;
            socket = null;
        }
    }
//发送command并接收
    public void sendandrecv(final int command, final Handler mhandler, final String jsonData) {
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    Log.i("kkk8199", "outnew111=" + out);
                    if (out == null) {
                        Message msg = mhandler.obtainMessage();
                        msg.arg1 = 1;//网络不通
                        mhandler.sendMessage(msg);
                      // initSocket();//重新建立连接
                        return;
                    }
                    ClientSendRequest loginSend = new ClientSendRequest(command);
                    loginSend.output.writeUTF(jsonData);
                    Log.i("kkk8199", "jsonData=" + jsonData.toString());
                    out.write(loginSend.entireMsg().array());//
                    out.flush();

                } catch (Exception e) {
                    System.out.println("客户端异常:" + e.getMessage());
                    Message msg = mhandler.obtainMessage();
                    msg.arg1 = 1;//网络不通
                    mhandler.sendMessage(msg);
                    e.printStackTrace();
                    // 关闭重连
                    if (socket == null || socket.isClosed() || !socket.isConnected()) {
                        Clientsocketclose();
                    }
                }
                recevfrom(mhandler);//返回值
                return ;
            }
        });
    }
//不带json数据的发送接收
    public void sendandrecv(final int command, final Handler mhandler) {
        executorService.submit(new Runnable() {
            public void run() {
                try {
                       Log.i("kkk8199", "outnew=" + out);
                    if (out == null) {
                        Message msg = mhandler.obtainMessage();
                        msg.arg1 = 1;//网络不通
                        mhandler.sendMessage(msg);
                    //    initSocket();//重新建立连接
                        return;
                    }
                    ClientSendRequest loginSend = new ClientSendRequest(command);
                    out.write(loginSend.entireMsg().array());//
                    out.flush();

                } catch (Exception e) {
                    System.out.println("客户端异常:" + e.getMessage());
                    Message msg = mhandler.obtainMessage();
                    msg.arg1 = 1;//网络不通
                    mhandler.sendMessage(msg);
                    e.printStackTrace();
                    // 关闭重连
                    if (socket == null || socket.isClosed() || !socket.isConnected()) {
                        Clientsocketclose();
                    }
                }
                recevfrom(mhandler);//返回值
                return ;
            }
        });
    }


//请求连接服务器

    private boolean bSocket() {
        try {
            socket = new Socket(SERVER_IP_ADDR, PORT);
            socket.setSoTimeout(SOCKET_TIMEOUT);

            int tr = 5;
            while (!socket.isConnected() && (tr--) >= 0) {
                Thread.sleep(100);
            }
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    //从服务器接收数据
    public String recevfrom(Handler mhandler) {
        try {
            if(input == null){
                Message msg = mhandler.obtainMessage();
                msg.arg1 = 1;//网络不通
                mhandler.sendMessage(msg);
                return null;
            }
          //  Log.i("kkk8199","input="+input);
        //     System.out.println("服务器端返回过来的是: ");
            byte flag = input.readByte();
            System.out.println(flag);//开始标志
            int len = input.readInt();//长度
             System.out.println(len);
            int code = input.readInt();//返回码
             System.out.println(code);
             int status = input.readInt();//状态
             System.out.println(status);
            String ret = input.readUTF();
            System.out.println("服务器端返回过来的信息是: " + ret);
            Message msg = mhandler.obtainMessage();
            msg.arg1 = 0;//登陆成功
                switch (code){
                    case ConnectAPI.LOGIN_RESPONSE://登陆
                        ShowAccount account= JsonUtilTool.fromJson(ret,ShowAccount.class);
                  //      Log.i("kkk8199","account.getRetCode()="+account.getRetCode());
                        if(account.getRetCode()==1) {
                      //      Log.i("kkk8199","account.getmsg()="+account.getmsg());
                            msg.arg2 = 128;//error
                            msg.obj = account.getMsg();
                        }
                        else{
                            Log.i("kkk8199","account.getProjects()="+account.getProjects());
                            msg.arg2 = account.getRole();
                            msg.obj = account.getProjects();
                        }
                        break;
                    case ConnectAPI.APP_VERSION_QUERY_RESPONSE://apk版本查询
                      //  Log.i("kkk81991","into APP_VERSION_QUERY_RESPONSE");
                        ApkVersion appVersion=  JsonUtilTool.fromJson(ret,ApkVersion.class);
                          UpdateManager.downid = appVersion.getId();
                     //   Log.i("kkk8199","into APP_VERSION_QUERY_RESPONSE1111");
                           msg.arg2=0;//比较版本
                           msg.obj =Integer.toString(appVersion.getVerNo());
                     //   Log.i("kkk8199","into app_version re="+msg.obj);
                        break;
                    case ConnectAPI.VCODE_GET_RESPONSE:
                        msg.arg2= LoginHelper.MSG_GET_VERTIFY_NUM;//获取验证码
                        ShowVCode showvcode = JsonUtilTool.fromJson(ret,ShowVCode.class);
                        int retcode = showvcode.getRetCode();
                        if(retcode!=0) {//验证码异常
                            msg.obj=showvcode.getMsg();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_ADD_RESPONSE:
                        msg.arg2= LoginHelper.MSG_REGISTER;//注册
                        ShowAccount showaccount = JsonUtilTool.fromJson(ret,ShowAccount.class);
                        int retcode1 = showaccount.getRetCode();
                        if(retcode1!=0) {//注册异常
                            msg.obj=showaccount.getMsg();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_FINDBACK_RESPONSE:
                        msg.arg2= LoginHelper.MSG_FIND_USER;//用户找回
                        ShowAccount showaccount1 = JsonUtilTool.fromJson(ret,ShowAccount.class);
                        int retcode2 = showaccount1.getRetCode();
                        if(retcode2!=0) {//找回异常
                            msg.obj=showaccount1.getMsg();
                        }
                        break;
                    case ConnectAPI.PROJECT_ADD_RESPONSE://新增项目返回
                        ShowProject showproject = JsonUtilTool.fromJson(ret,ShowProject.class);
                         Log.i("kkk8199","showProject="+showproject.toString());
                         msg.arg2= UserMainActivity.MSG_ADD_PROJECT;//新增项目
                        int shoeprojectretcode = showproject.getRetCode();
                        if(shoeprojectretcode!=0) {//新增项目返回异常
                            msg.arg2=0;//异常
                            msg.obj=showproject.getMsg();
                        }
                        else{
                            msg.obj=showproject.getMsg();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_AUDIT_QUERY_RESPONSE://注册审核查询返回
                        ShowPage showPage = JsonUtilTool.fromJson(ret,ShowPage.class);
                        Log.i("kkk8199","showPage="+showPage.toString());
                         msg.arg2= UserMainActivity.MSG_REGISTER;//注册审核
                        int showPageRetCode = showPage.getRetCode();
                        if(showPageRetCode!=0) {//注册审核返回异常
                            msg.arg2=0;//异常
                            msg.obj=showPage.getMsg();
                        }
                        else{
                            Log.i("kkk8199","msg.obj="+showPage.getT().toString());
                            msg.obj=showPage.getT();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_AUDIT_RESPONSE://注册审核通过否决返回
                        Log.i("kkk8199","into ACCOUNT_AUDIT_RESPONSE");
                        ShowVCode showabt= JsonUtilTool.fromJson(ret,ShowVCode.class);
                        Log.i("kkk8199","showabt="+showabt.toString());
                        msg.arg2= UserMainActivity.MSG_REGISTER_REJECT;
                        int showabtRetCode = showabt.getRetCode();
                        if(showabtRetCode!=0) {
                            msg.arg2=0;
                            msg.obj=showabt.getMsg();
                        }
                        else{//成功
                            msg.obj=showabt.getMsg();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_FINDBACK_QUERY_RESPONSE://找回审核查询返回
                        //Log.i("kkk8199","into ACCOUNT_FINDBACK_QUERY_RESPONSE");
                        ShowPage findbackshowabt= JsonUtilTool.fromJson(ret,ShowPage.class);
                       // Log.i("kkk8199","showabt="+findbackshowabt.toString());
                        msg.arg2= UserMainActivity.MSG_FIND_USER;
                        int findbackshowabtRetCode = findbackshowabt.getRetCode();
                        if(findbackshowabtRetCode!=0) {
                            msg.arg2=0;
                            msg.obj=findbackshowabt.getMsg();
                        }
                        else{//成功
                            msg.obj=findbackshowabt.getT();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_FINDBACK_AUDIT_RESPONSE://找回审核通过否决返回
                        Log.i("kkk8199","into ACCOUNT_FINDBACK_AUDIT_RESPONSE");
                        ShowVCode findbackaudit= JsonUtilTool.fromJson(ret,ShowVCode.class);
                        Log.i("kkk8199","findbackaudit="+findbackaudit.toString());
                        msg.arg2= UserMainActivity.MSG_REGISTER_REJECT;//可以使用通一个
                        int findbackauditRetCode = findbackaudit.getRetCode();
                        if(findbackauditRetCode!=0) {
                            msg.arg2=0;
                            msg.obj=findbackaudit.getMsg();
                        }
                        else{//成功
                            msg.obj=findbackaudit.getMsg();
                        }
                        break;
                    case ConnectAPI.PROJECT_UPDATE_RESPONSE://项目修改
                        Log.i("kkk8199","into PROJECT_UPDATE_RESPONSE");
                        ShowProject projectmodifyreturn= JsonUtilTool.fromJson(ret,ShowProject.class);
                        msg.arg2= UserMainActivity.MSG_MODIFY_PROJECT;//可以使用通一个
                        int projectmodifyreturnRetCode = projectmodifyreturn.getRetCode();
                        if(projectmodifyreturnRetCode!=0) {
                            msg.arg2=0;
                            msg.obj=projectmodifyreturn.getMsg();
                        }
                        else{//成功
                            msg.obj=projectmodifyreturn.getMsg();
                        }
                        break;
                    case ConnectAPI.ACCOUNT_QUERY_RESPONSE://查询维护人员
                        Log.i("kkk8199","into ACCOUNT_QUERY_COMMAND");
                        ShowPage querymainenancerinfo= JsonUtilTool.fromJson(ret,ShowPage.class);
                        Log.i("kkk8199","querymainenancerinfo="+querymainenancerinfo.toString());
                        msg.arg2= UserMainActivity.MSG_QUERY_MAINEANCERINFO;//
                        int querymainenancerinfoRetCode = querymainenancerinfo.getRetCode();
                        if(querymainenancerinfoRetCode!=0) {
                            msg.arg2=0;
                            msg.obj=querymainenancerinfo.getMsg();
                        }
                        else{//成功
                            msg.obj=querymainenancerinfo.getT();
                        }
                        break;
                    case ConnectAPI.WORKING_MODE_UPDATE_RESPONSE://得到修改定时启动的返回
                        Log.i("kkk8199","into WORKING_MODE_UPDATE_RESPONSE");
                        ShowAbt ShowAbtreturn = JsonUtilTool.fromJson(ret,ShowAbt.class);
                        Log.i("kkk8199","ShowAbtreturn="+ShowAbtreturn.toString());
                        msg.arg2= UserMainActivity.MSG_MODIGY_RUNTIMING;//
                        int ShowAbtreturncode = ShowAbtreturn.getRetCode();
                        if(ShowAbtreturncode!=0) {
                            msg.arg2=0;
                            msg.obj=ShowAbtreturn.getMsg();
                        }
                        else{//成功

                        }
                        break;
                    case ConnectAPI.GET_WORKING_MODE_RESPONSE://查询工作模式
                        Log.i("kkk8199","into GET_WORKING_MODE_RESPONSE");
                       ProjectWorkingMode ProjectWorkingModereturn = JsonUtilTool.fromJson(ret,ProjectWorkingMode.class);
                        Log.i("kkk8199","ShowAbtreturn="+ProjectWorkingModereturn.toString());
                        msg.arg2= UserMainActivity.MSG_QUERY_RUNTIMING;//
                      /*  int ProjectWorkingModereturncode = ProjectWorkingModereturn.getRetCode();
                        if(ProjectWorkingModereturncode!=0) {
                            msg.arg2=0;
                            msg.obj=ProjectWorkingModereturn.getMsg();
                        }
                        else{//成功
                            msg.obj= ProjectWorkingModereturn;
                        }*/
                        msg.obj= ProjectWorkingModereturn;

                        break;
                    case ConnectAPI.DEVICES_RUNNINGDATA_RESPONSE://查询运行数据
                        Log.i("kkk8199","into DEVICES_RUNNINGDATA_RESPONSE");
                    //    ProjectWorkingMode ProjectWorkingModereturn = JsonUtilTool.fromJson(ret,ProjectWorkingMode.class);
                  //      Log.i("kkk8199","ShowAbtreturn="+ProjectWorkingModereturn.toString());
                        msg.arg2= UserMainActivity.MSG_QUERY_RUNNINGDATA;//
                      /*  int ProjectWorkingModereturncode = ProjectWorkingModereturn.getRetCode();
                        if(ProjectWorkingModereturncode!=0) {
                            msg.arg2=0;
                            msg.obj=ProjectWorkingModereturn.getMsg();
                        }
                        else{//成功
                            msg.obj= ProjectWorkingModereturn;
                        }*/
                        break;
                    default:
                        break;
                }
                mhandler.sendMessage(msg);
                return ret;
        } catch (IOException e) {
            System.out.println("客户端异常:" + e.getMessage());
            Message msg = mhandler.obtainMessage();
            msg.arg1 = 1;//网络不通
            mhandler.sendMessage(msg);
            e.printStackTrace();
            if (socket == null || socket.isClosed() || !socket.isConnected()) {
                Clientsocketclose();
            }
        }
            return null;
    }


    //获取当前网络状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
