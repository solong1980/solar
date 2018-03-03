package com.lszyhb.showcollectdata;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lszyhb.addresswidget.addresspopwindow;
import com.lszyhb.addresswidget.utils.Utils;

import static android.widget.EditText.*;

/**
 * 注册界面
 * @author kkk8199
 *
 */
public class RegisterActivity extends Activity {
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if( mLoginHelper != null ){
			mLoginHelper.destroy();
			mLoginHelper = null;
		} 
	}


	private void sendLoginAccountUpdate(){		
		Intent intent = new Intent();
		intent.setAction("login_account_update");
		this.sendBroadcast(intent);
	}
	
	private LoginHelper mLoginHelper = null;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);	
		findViewById(R.id.back_login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
			}
		});
		
		mLoginHelper = new LoginHelper(this);
		
		final EditText et_name = (EditText)findViewById(R.id.name_edit);//用户名
		//check name valid 
		et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if( hasFocus == false ){//失去焦点时
					mLoginHelper.checkNameValid(et_name.getText().toString());
				}
			}
		});
		
		final EditText et_phone = (EditText)findViewById(R.id.phone_edit);
		//处理获取验证码的业务流程
		mLoginHelper.processGetVertify(et_phone, (Button)(findViewById(R.id.get_vertify_num_text)),0);
		final EditText accountname= (EditText)findViewById(R.id.accountname);//邮箱地址
		final EditText mail_edit= (EditText)findViewById(R.id.mail_edit);//邮箱地址
		final EditText et_vertify = (EditText)findViewById(R.id.vertify_edit);////验证码
		final EditText et_newpwd  = (EditText)findViewById(R.id.passwd_edit);//密码
		final EditText et_confirmnewpwd = (EditText)findViewById(R.id.confirm_passwd_edit);//确认密码
		final Spinner usertype = findViewById(R.id.usertype);    //用户类型
		final Button registerBtn = (Button)findViewById(R.id.btn_register);
		String[] numbers = { "环保部门工作人员", "系统运维人员" };

		final SpinnerAdapter adapter = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, numbers);
		usertype.setAdapter(adapter);

		final EditText project_address0 = (EditText)findViewById(R.id.project_address0);//环保局id
		final addresspopwindow addresspopwindow0 = new addresspopwindow(RegisterActivity.this);
		addresspopwindow0.setedittext(project_address0);
		addresspopwindow0.init();
		project_address0.setOnFocusChangeListener(new android.view.View.
				OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
					addresspopwindow0.addressClick(v);
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});
		//Log.i("kkk8199","project_address0="+project_address0.getText());

		final EditText project_address1 = (EditText)findViewById(R.id.project_address1);//位置1
		final addresspopwindow addresspopwindow1 = new addresspopwindow(RegisterActivity.this);
		addresspopwindow1.setedittext(project_address1);
		addresspopwindow1.init();
		project_address1.setOnFocusChangeListener(new android.view.View.
				OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
				//	Log.i("kkk8199","into 112222222222222222211111111111111111");
					addresspopwindow1.addressClick(v);
				} else {
				//	Log.i("kkk8199","into 111111133333333333311111111111111111111");
					// 此处为失去焦点时的处理内容
				}
			}
		});

		final EditText project_address2 = (EditText)findViewById(R.id.project_address2);//位置2
		final addresspopwindow addresspopwindow2 = new addresspopwindow(RegisterActivity.this);
		addresspopwindow2.setedittext(project_address2);
		addresspopwindow2.init();
		project_address2.setOnFocusChangeListener(new android.view.View.
				OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
					addresspopwindow2.addressClick(v);
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});
		final EditText project_address3 = (EditText)findViewById(R.id.project_address3);//位置3
		final addresspopwindow addresspopwindow3 = new addresspopwindow(RegisterActivity.this);
		addresspopwindow3.setedittext(project_address3);
		addresspopwindow3.init();
		project_address3.setOnFocusChangeListener(new android.view.View.
				OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容

					addresspopwindow3.addressClick(v);
				} else {

					// 此处为失去焦点时的处理内容
				}
			}
		});


		project_address0.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//Log.i("kkk8199","actionId="+actionId);
				// TODO Auto-generated method stub

				if(actionId==EditorInfo.IME_ACTION_DONE){
					Utils.hideKeyBoard(RegisterActivity.this);
					//registerBtn.requestFocus();
				}
				return true;
			}
		});

		project_address1.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//Log.i("kkk8199","actionId="+actionId);
				// TODO Auto-generated method stub

				if(actionId==EditorInfo.IME_ACTION_NEXT){
					project_address2.requestFocus();
				}
				return true;
			}
		});

		project_address2.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//	Log.i("kkk8199","actionId="+actionId);
				// TODO Auto-generated method stub

				if(actionId==EditorInfo.IME_ACTION_NEXT){
					project_address3.requestFocus();
				}
				return true;
			}
		});

		project_address3.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//Log.i("kkk8199","actionId="+actionId);
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_DONE){
					Utils.hideKeyBoard(RegisterActivity.this);
				}
				return true;
			}
		});

		final LinearLayout prolayout0 =(LinearLayout)findViewById(R.id.prolayout0);
		final LinearLayout prolayout1 =(LinearLayout)findViewById(R.id.prolayout1);
		final LinearLayout prolayout2 =(LinearLayout)findViewById(R.id.prolayout2);
		final LinearLayout prolayout3 =(LinearLayout)findViewById(R.id.prolayout3);
		final ImageView    line0      =(ImageView)   findViewById(R.id.line0);
		final ImageView    line1      =(ImageView)   findViewById(R.id.line1);
		final ImageView    line2      =(ImageView)   findViewById(R.id.line2);
		final ImageView    line3      =(ImageView)   findViewById(R.id.line3);

		usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			// parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			//	TextView choosetype = (TextView) findViewById(R.id.choosetype);
				//获取Spinner控件的适配器
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				//choosetype.setText(adapter.getItem(position));
		//		Log.i("kkk8199","position="+position);
				TextView tv = (TextView)view;
					tv.setTextSize(14.0f);
					if (position == 0) {
						prolayout0.setVisibility(View.VISIBLE);
						line0.setVisibility(View.VISIBLE);
						prolayout1.setVisibility(View.INVISIBLE);
						line1.setVisibility(View.INVISIBLE);
						prolayout2.setVisibility(View.INVISIBLE);
						line2.setVisibility(View.INVISIBLE);
						prolayout3.setVisibility(View.GONE);
						line3.setVisibility(View.GONE);

					} else {
						prolayout0.setVisibility(View.GONE);
						line0.setVisibility(View.GONE);
						prolayout1.setVisibility(View.VISIBLE);
						line1.setVisibility(View.VISIBLE);
						prolayout2.setVisibility(View.VISIBLE);
						line2.setVisibility(View.VISIBLE);
						prolayout3.setVisibility(View.VISIBLE);
						line3.setVisibility(View.VISIBLE);
					}

			}
			//没有选中时的处理
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


		registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String usertypetext = String.valueOf(usertype.getSelectedItemPosition());

				mLoginHelper.processRegisterUser(accountname.getText().toString(),
						et_name.getText().toString(),
						et_phone.getText().toString(), 
						et_vertify.getText().toString(), 
						et_newpwd.getText().toString(),
						et_confirmnewpwd.getText().toString(),
						mail_edit.getText().toString(),
						usertypetext,
						addresspopwindow0.getnowid(),
						addresspopwindow1.getnowid(),
						addresspopwindow2.getnowid(),
						addresspopwindow3.getnowid());

			}
		});

	}
}
