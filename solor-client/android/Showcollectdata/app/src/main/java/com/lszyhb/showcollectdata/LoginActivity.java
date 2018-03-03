package com.lszyhb.showcollectdata;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.basicclass.UserInfo;
import com.lszyhb.update.UpdateManager;

import java.io.Serializable;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 登陆界面
 * @author kkk8199
 *
 */
public class LoginActivity extends Activity {

    //	private static String TAG = "Login";
    // private ModifyPwdDialogHelper mModifyPwdDialogHelper = null;
    //自动登陆
    public static final String TAG_AUTOCHECK = "auto_ischeck";
    //记住密码
    public static final String TAG_ISCHECK = "ischeck";

    public static final String USERINFO = "userinfo";

    public static final String TAG_NAME = "name";

    public static final String TAG_PWD = "pwd";


    public static ClientSocket msocket;

    public static final String RETURN_INFO = "com.lszyhb.showcollectdata.info";

    public static final String RETURN_USERTYPE = "com.lszyhb.showcollectdata.usertype";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hideProgressDialog();

            if( msg.arg1 == 1 ){
	    	  		Log.d(TAG,"loginin error");
                //网络连接出错
                if( showNetWorkFailed(LoginActivity.this) == false ){
                    return;
                }
                else{
                     showNetDataFailedTip(LoginActivity.this);
                }
            }else{
                if( msg.arg1 == 0 ) {//登陆应答
                    Log.i("kkk8199", "msg.obj=" + msg.obj);
                        switch(msg.arg2) {
                            case 128://error
                                if (msg.obj != null && msg.obj instanceof String) {
                                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                break;
                            case 10://管理员
                            case 20://维护员
                            case 30://环保人员
                                //loginAdminIn();
                                loginUserIn((List<ShowProject>) msg.obj,msg.arg2);
                                break;
                            default:
                                break;
                        }
                }
            }
            //未能处理的全部按照获取网络数据失败处理
           // showNetDataFailedTip(LoginActivity.this);
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if( handler != null ){
            handler.removeCallbacksAndMessages(null);
        }
        if( mReceiver != null ){
            this.unregisterReceiver(mReceiver);
        }
        hideProgressDialog();
    }



    private void loginUserIn(List<ShowProject> projectslist,int type){
        //start main activity
          Intent intent = new Intent();
          intent.setClass(LoginActivity.this, UserMainActivity.class);
          Log.i("kkk8199","projectslistname="+projectslist.get(0).getProjectName());
          intent.putExtra(LoginActivity.RETURN_INFO, (Serializable)projectslist);
          intent.putExtra(LoginActivity.RETURN_USERTYPE,type);
          LoginActivity.this.startActivity(intent);

        this.finish();
    }


    public static void showNetDataFailedTip(Context context){
        Toast.makeText(context, R.string.get_netdata_failed, Toast.LENGTH_LONG).show();
    }

    public static boolean showNetWorkFailed(Context context){
        boolean networkState = ClientSocket.isNetworkConnected(context);
        if( networkState == false ){
            Toast.makeText(context, R.string.network_failed, Toast.LENGTH_LONG).show();
        }
        Log.i(TAG,"networkState="+networkState);
        return networkState;
    }

    private Dialog mWaitDialog = null;

    private void hideProgressDialog(){
        if( mWaitDialog != null ){
            mWaitDialog.dismiss();
            mWaitDialog = null;
        }
    }

    private void showProgressDialog(){
        mWaitDialog = new Dialog(this,R.style.new_circle_progress);
        mWaitDialog.setContentView(R.layout.dialog_wait);
        mWaitDialog.show();
    }

    private SharedPreferences mSharedPrefrences = null;
    private void loadAccount(){
        // 重新读取存储的帐号和密码
        mSharedPrefrences = getSharedPreferences(USERINFO, Context.MODE_PRIVATE); //私有数据
        String name = mSharedPrefrences.getString(TAG_NAME,"");
        String pwd = mSharedPrefrences.getString(TAG_PWD,"");
        boolean isCheck = mSharedPrefrences.getBoolean(TAG_ISCHECK, true);
        boolean isAutoCheck = mSharedPrefrences.getBoolean(TAG_AUTOCHECK, false);
        EditText et_name = (EditText)(findViewById(R.id.username_edit));
        EditText et_pwd = (EditText)(findViewById(R.id.password_edit));
        CheckBox rem_pw = (CheckBox) findViewById(R.id.cb_mima);
        CheckBox auto_login = (CheckBox) findViewById(R.id.cb_auto);
        et_name.setText(name);
        et_pwd.setText(pwd);
        rem_pw.setChecked(isCheck);
        auto_login.setChecked(isAutoCheck);
    }


    public static void  saveUserInfo(Activity activity,String name,String pwd){
        if( activity == null  )  return;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(USERINFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(TAG_NAME,name);
        editor.putString(TAG_PWD, pwd);
        editor.commit();
    }

    BroadcastReceiver mReceiver = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        msocket = ClientSocket.getInstance();
        msocket.initSocket();
        Log.i("kkk8199","msocket122211111="+msocket);
        loadAccount();
      //  mModifyPwdDialogHelper = new ModifyPwdDialogHelper(this);
        initView();
    }

    private void initView(){

        final CheckBox rem_pw = (CheckBox) findViewById(R.id.cb_mima);
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                mSharedPrefrences.edit().putBoolean(TAG_ISCHECK,rem_pw.isChecked()).commit();
            }

        });
        final CheckBox auto_login = (CheckBox) findViewById(R.id.cb_auto);
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                mSharedPrefrences.edit().putBoolean(TAG_AUTOCHECK,auto_login.isChecked()).commit();
            }

        });

        Button loginBtn = (Button)this.findViewById(R.id.signin_button);
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                loginClicked();
            }

        });


        TextView forgetView = (TextView)this.findViewById(R.id.forget_passwd_text);
        forgetView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, FinduserActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        TextView registerView = (TextView)this.findViewById(R.id.register_text);
        registerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				Log.d(TAG, "register text view clicked");
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        try {
            Thread.sleep(500);//等待连接
        } catch (InterruptedException e) {
            return;
        }
        //自动登陆按钮选中
        if( auto_login.isChecked() == true ){

            loginClicked();
            UpdateManager.mUpdateFlag = false;
        }else{
            //检测软件更新
               UpdateManager manager = new UpdateManager(this);
               manager.checkUpdate(msocket);
              // manager.getVersionCode(this);
        }
    }

    void loginClicked(){
        String name = ((TextView)(LoginActivity.this.findViewById(R.id.username_edit))).getText().toString();
        String pwd = ((TextView)(LoginActivity.this.findViewById(R.id.password_edit))).getText().toString();
        if( name == null || name.isEmpty() ){
            Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.name_is_not_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if( pwd == null || pwd.isEmpty() ){
            Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.password_is_not_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        CheckBox rem_pw = (CheckBox) findViewById(R.id.cb_mima);

        //保存用户名和密码
        SharedPreferences.Editor editor = mSharedPrefrences.edit();//获取编辑器
        editor.putString(TAG_NAME,name);
        if( rem_pw.isChecked() == false  ){
            editor.putString(TAG_PWD, "");
        }else{
            editor.putString(TAG_PWD, pwd);
        }
        editor.commit();

        UserInfo userinfo = new UserInfo();
        userinfo.pwd = pwd;
        userinfo.userName = name;

        SupplyConnectAPI.getInstance().loginIn(msocket,handler,userinfo);
        //显示ProgressDialog
        showProgressDialog();
    }

}

