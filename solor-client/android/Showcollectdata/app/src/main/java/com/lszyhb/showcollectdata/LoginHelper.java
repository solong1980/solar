package com.lszyhb.showcollectdata;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowAccountLocation;
import com.lszyhb.common.JsonUtilTool;
import com.lszyhb.common.UtilHelper;

import java.util.ArrayList;
import java.util.List;

//登陆，注册，修改密码，修改用户名业务流程整合
public class LoginHelper {
	private Context mContext;
	private static final String TAG = "LoginHelper";
	public static final int MSG_GET_VERTIFY_NUM = 0;
	public static final int MSG_FIND_USER = 1;
	public static final int MSG_REGISTER = 2;
	private static ClientSocket nsocket;
	public interface ModifyPwdResponse{
		void response_cb(boolean success, String name, String pwd);
	};
	
	public LoginHelper(Context context) {
		mContext = context;
		 nsocket = new ClientSocket();
	}
	
	private int mRefreshTime = 1000;
	private int mDelayTimes = 120;
	
	Runnable runnable = new Runnable() {  
		  
        @Override  
        public void run() {  
            // handler自带方法实现定时器  
        	Log.d(TAG,"run mDelayTimes:"+mDelayTimes);
        	if( mGetVertifyBtn == null || handler == null ){
        		return;
        	}
        	if( mDelayTimes >= 1 ){
        		mDelayTimes--;
        		handler.postDelayed(runnable, mRefreshTime);
        	}else{	        	
	        	mGetVertifyBtn.setEnabled(true);
        	}
        	updateGetVertifyBtn();
        }  
    };  
    
    private void updateGetVertifyBtn(){
    	if( mGetVertifyBtn == null ) return;
    	if( mDelayTimes > 0 ){
    		String text = mContext.getResources().getString(R.string.vertify_time)+"("+mDelayTimes+"s)";
    		mGetVertifyBtn.setText(text);
    	}else{
    		mGetVertifyBtn.setText(R.string.get_vertify_num);
    	}
    }
	
	private void startGetVertifyNumTimer(){
		if( handler == null ) return;
		handler.postDelayed(runnable, mRefreshTime);
	}
	
	private void cancelGetVertifyNumTimer(){
		if( handler == null ) return;
		handler.removeCallbacks(runnable);
	}
	
	public void destroy(){
		cancelGetVertifyNumTimer();
	}

	private Handler handler = new Handler(){
		 @Override
	      public void handleMessage(Message msg) {
			  if( mContext == null ) return;
		      Log.d(TAG,"terminal list helper handleMessage");		
		      hideProgressDialog();
		      if( msg.arg1 != 0 ){
		    	  if( mGetVertifyBtn != null ){
		    		  mGetVertifyBtn.setEnabled(true);
		    	  }
		    	  //网络连接出错
		    	  if( LoginActivity.showNetWorkFailed(mContext) == false ){
		    		  return;
		    	  }
		      }else {
				  if (msg.arg2 == MSG_GET_VERTIFY_NUM) {//验证码
					  if (msg.obj != null && msg.obj instanceof String) {
						  Toast.makeText(mContext.getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
						  return;
					  }
					  else {
						  //收验证码成功
						//  Toast.makeText(mContext.getApplicationContext(), R.string.vertify_num_success, Toast.LENGTH_LONG).show();
						  startGetVertifyNumTimer();
						  return;
					  }
				  }
				  else  if (msg.arg2 == MSG_REGISTER) {//注册
					  if (msg.obj != null && msg.obj instanceof String) {
						  Toast.makeText(mContext.getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
						  return;
					  }
					  else {
						  //注册成功
						    Toast.makeText(mContext.getApplicationContext(), R.string.register_success, Toast.LENGTH_LONG).show();
						  return;
					  }
				  }
				  else  if (msg.arg2 == MSG_FIND_USER) {//用户找回
					  if (msg.obj != null && msg.obj instanceof String) {
						  Toast.makeText(mContext.getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
						  return;
					  }
					  else {
						  //找回成功
						  Toast.makeText(mContext.getApplicationContext(), R.string.finduser_success, Toast.LENGTH_LONG).show();
						  return;
					  }
				  }

			  }

			}
	};

	//获取验证码按钮
	private Button mGetVertifyBtn = null;
	private EditText mPhoneEditText = null;
	
	//封装获取验证码业务流程
	public void processGetVertify(EditText phoneEditText, Button getVertifyBtn, final int type){
		if( phoneEditText == null || getVertifyBtn == null ) return;
		mGetVertifyBtn = getVertifyBtn;
		mPhoneEditText = phoneEditText;
		mDelayTimes = 120;
		
		//手机号输入框失去焦点时判断手机号输入是否有效
		mPhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if( hasFocus == false ){//失去焦点时
					UtilHelper.checkPhoneValid(mPhoneEditText.getText().toString(),mContext);
				}
			}
		});
		
		mGetVertifyBtn.setEnabled(true);
		mGetVertifyBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = mPhoneEditText.getText().toString();
				if( UtilHelper.checkPhoneValid(text,mContext)== true ){//有效的手机号码
					SupplyConnectAPI.getInstance().setvertifycode(LoginActivity.msocket,handler
							,mPhoneEditText.getText().toString(),type);
					mGetVertifyBtn.setEnabled(false);
				}
			}
		});	
	}
	
	
	private boolean checkVertifyValid(String inputVertify){
		if( inputVertify!= null && inputVertify.trim().length() > 0 ){//输入非空验证码
			return true;
		}
		Toast.makeText(mContext.getApplicationContext(), R.string.vertify_num_is_empty, Toast.LENGTH_SHORT).show();
		return false;
	}
	
	private Dialog mWaitDialog = null;
	
	private void hideProgressDialog(){
		if( mWaitDialog != null ){
			mWaitDialog.dismiss();
			mWaitDialog = null;
		}
	}
	
	private void showProgressDialog(){
		mWaitDialog = new Dialog(mContext,R.style.new_circle_progress);    
		mWaitDialog.setContentView(R.layout.dialog_wait);    
		mWaitDialog.show();
	}
	
	private String mName = null;
	private String mNewPwd = null;
	private ModifyPwdResponse mResponseCb = null;
	public void processfindUser(String et_name,String et_phone,String et_oldphone,String et_vertify,String usertypetext,
								String project0,String project1){
		if( UtilHelper.checkPhoneValid(et_phone,mContext) == false){//输入无效电话号码
			return;
		}
		if( checkVertifyValid(et_vertify) == false){//输入无效验证码
			return;
		}
		showProgressDialog();
		ShowAccount account = new ShowAccount();
		account.setName(et_name);
		Log.i("kkk8199","et_oldphone="+et_oldphone);
		account.setOldPhone(et_oldphone);
		account.setPhone(et_phone);
		account.setVcode(et_vertify);
		int typeIndex = Integer.parseInt(usertypetext) + 1;
		account.setType(Consts.ACCOUNT_TYPE[typeIndex]);
		List<ShowAccountLocation> accountLocations = new ArrayList<>();
		if (typeIndex == 2) {//运维
			ShowAccountLocation accountLocation = new ShowAccountLocation();
			accountLocation.setLocationId(project1);
			accountLocations.add(accountLocation);

		} else if (typeIndex == 1) {//环保
			ShowAccountLocation accountLocation = new ShowAccountLocation();
			accountLocation.setLocationId(project0);
			accountLocations.add(accountLocation);
			//String json = JsonUtilTool.toJson(accountLocations);
			//Log.i("kkk8199","json="+json);
		}
		account.setLocations(accountLocations);

		SupplyConnectAPI.getInstance().finduser(LoginActivity.msocket,handler,account);

	}
	
	public boolean checkNameValid(String name){
		if( name == null || name.trim().length() == 0 ){//提示用户名不能为空
			Toast.makeText(mContext, R.string.name_is_not_empty, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public void processRegisterUser(String accountname,String name,String phone,String vertifyCode,String newpwdStr,String confirmnewpwdStr,
									String mail_edit,String usertype,String project0,String project1,String project2,
									String project3) {

		if (checkNameValid(name) == false) {//输入无效的用户名
			return;
		}

		if (UtilHelper.checkPhoneValid(phone, mContext) == false) {//输入无效电话号码
			return;
		}
		if (checkVertifyValid(vertifyCode) == false) {//输入无效验证码
			return;
		}
		//密码判断
		if (newpwdStr == null || newpwdStr.isEmpty() || confirmnewpwdStr == null || confirmnewpwdStr.isEmpty()) {
			Toast.makeText(mContext.getApplicationContext(), R.string.newpwd_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (newpwdStr.equals(confirmnewpwdStr) == false) {
			Toast.makeText(mContext.getApplicationContext(), R.string.newpwd_is_not_equal, Toast.LENGTH_SHORT).show();
			return;
		}
		showProgressDialog();

		//Log.i("kkk8199", "name=" + name + "vertifyCode=" + vertifyCode + "newpwdStr=" + newpwdStr + "confirmnewpwdStr=" + confirmnewpwdStr
		//		+ "mail_edit=" + mail_edit + "usertype=" + usertype + "project0=" + project0 + "project1=" + project1 + "project2=" + project2 + "project3=" + project3);
		ShowAccount account = new ShowAccount();
		account.setAccount(accountname);
		account.setName(name);
		account.setPhone(phone);
		account.setEmail(mail_edit);
		account.setVcode(vertifyCode);
		account.setPassword(newpwdStr);
		int typeIndex = Integer.parseInt(usertype) + 1;
		account.setType(Consts.ACCOUNT_TYPE[typeIndex]);
		List<ShowAccountLocation> accountLocations = new ArrayList<>();
		if (typeIndex == 2) {//运维
			ShowAccountLocation accountLocation = new ShowAccountLocation();
			accountLocation.setLocationId(project1);
			accountLocations.add(accountLocation);
			if (project2 != null) {
				accountLocation = new ShowAccountLocation();
				accountLocation.setLocationId(project2);
				accountLocations.add(accountLocation);
			}
			if (project3 != null) {
				accountLocation = new ShowAccountLocation();
				accountLocation.setLocationId(project3);
				accountLocations.add(accountLocation);
			}

		} else if (typeIndex == 1) {//环保
			ShowAccountLocation accountLocation = new ShowAccountLocation();
			accountLocation.setLocationId(project0);
			accountLocations.add(accountLocation);
			//String json = JsonUtilTool.toJson(accountLocations);
			//Log.i("kkk8199","json="+json);
		}
		account.setLocations(accountLocations);

		SupplyConnectAPI.getInstance().setregisteruser(LoginActivity.msocket,handler,account);
	}
}
