package com.lszyhb.showcollectdata;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lszyhb.addresswidget.addresspopwindow;
import com.lszyhb.addresswidget.utils.Utils;

/**
 * Created by kkk8199 on 12/7/17.用户找回
 */

public class FinduserActivity extends Activity {

    private LoginHelper mLoginHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finduser);
        findViewById(R.id.fback_login).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FinduserActivity.this.finish();
            }
        });

        mLoginHelper=new LoginHelper(this);

        final EditText et_name=(EditText) findViewById(R.id.fname_edit);//用户名
        //check name valid
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {//失去焦点时
                    mLoginHelper.checkNameValid(et_name.getText().toString());
                }
            }
        });

        final EditText et_phone=(EditText) findViewById(R.id.fphone_edit);
        final EditText et_oldphone = (EditText) findViewById(R.id.foldphone_edit);
        //处理获取验证码的业务流程
        mLoginHelper.processGetVertify(et_phone, (Button) (findViewById(R.id.fget_vertify_num_text)),1);
        final EditText et_vertify=(EditText) findViewById(R.id.fvertify_edit);////验证码
        final Spinner usertype=findViewById(R.id.fusertype);    //用户类型
        final Button registerBtn=(Button) findViewById(R.id.fbtn_register);
        String[] numbers={"环保部门工作人员", "系统运维人员"};

        final SpinnerAdapter adapter=new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, numbers);
        usertype.setAdapter(adapter);

        final EditText project_address0=(EditText) findViewById(R.id.fproject_address0);//环保局id
        final addresspopwindow faddresspopwindow0=new addresspopwindow(FinduserActivity.this);
        faddresspopwindow0.setedittext(project_address0);
        faddresspopwindow0.init();
        project_address0.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    faddresspopwindow0.addressClick(v);
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });
        //Log.i("kkk8199","project_address0="+project_address0.getText());

        final EditText project_address1=(EditText) findViewById(R.id.fproject_address1);//位置1
        final addresspopwindow faddresspopwindow1=new addresspopwindow(FinduserActivity.this);
        faddresspopwindow1.setedittext(project_address1);
        faddresspopwindow1.init();
        project_address1.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    //	Log.i("kkk8199","into 112222222222222222211111111111111111");
                    faddresspopwindow1.addressClick(v);
                } else {
                    //	Log.i("kkk8199","into 111111133333333333311111111111111111111");
                    // 此处为失去焦点时的处理内容
                }
            }
        });


        project_address0.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Log.i("kkk8199","actionId="+actionId);
                // TODO Auto-generated method stub

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyBoard(FinduserActivity.this);
                    //registerBtn.requestFocus();
                }
                return true;
            }
        });

        project_address1.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Log.i("kkk8199","actionId="+actionId);
                // TODO Auto-generated method stub

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyBoard(FinduserActivity.this);
                }
                return true;
            }
        });

        et_vertify.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 Log.i("kkk8199","actionId="+actionId);
                // TODO Auto-generated method stub

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyBoard(FinduserActivity.this);
                }
                return true;
            }
        });


        final LinearLayout prolayout0=(LinearLayout) findViewById(R.id.fprolayout0);
        final LinearLayout prolayout1=(LinearLayout) findViewById(R.id.fprolayout1);

        usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //	TextView choosetype = (TextView) findViewById(R.id.choosetype);
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter=(ArrayAdapter<String>) parent.getAdapter();
                //choosetype.setText(adapter.getItem(position));
                //		Log.i("kkk8199","position="+position);
                TextView tv = (TextView)view;
                tv.setTextSize(14.0f);
                if (position == 0) {
                    prolayout0.setVisibility(View.VISIBLE);
                    prolayout1.setVisibility(View.GONE);
                } else {
                    prolayout0.setVisibility(View.GONE);
                    prolayout1.setVisibility(View.VISIBLE);
                }

            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String usertypetext=String.valueOf(usertype.getSelectedItemPosition());

                mLoginHelper.processfindUser(
                        et_name.getText().toString(),
                        et_phone.getText().toString(),
                        et_oldphone.getText().toString(),
                        et_vertify.getText().toString(),
                        usertypetext,
                        faddresspopwindow0.getnowid(),
                        faddresspopwindow1.getnowid());
            }

        });
     }

    @Override
        protected void onDestroy() {
        // TODO Auto-generated method stub
            super.onDestroy();
            if( mLoginHelper != null ){
            mLoginHelper.destroy();
            mLoginHelper = null;
            }
        }
}
