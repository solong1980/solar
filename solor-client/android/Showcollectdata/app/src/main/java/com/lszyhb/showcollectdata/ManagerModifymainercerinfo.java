package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowPage;
import com.lszyhb.basicclass.ShowProject;

import java.util.List;

/**
 * Created by kkk8199 on 1/30/18.
 */

public class ManagerModifymainercerinfo extends Fragment implements View.OnClickListener{

    private View managermodigyview;
    private Context mcontext;
    private Spinner maineancername;
    private TextView maineancernumber;
    private Button manager_modifymaincencerinfomod_commit;
    private Button manager_modifymaincencerinfodel_commit;
    private Spinner maineancerprojectname;
    private TextView maineancerprojectaddress;
    private Spinner maineancerprojecttype;
    private String [] projectnamelist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        managermodigyview= inflater.inflate(R.layout.manager_modifymainencerinfo, container, false);
        initfragment();
        return managermodigyview;

    }

/***************初始化元素**********************/
    private void initfragment(){
        maineancername=managermodigyview.findViewById(R.id.maineancername);
        maineancernumber=managermodigyview.findViewById(R.id.maineancernumber);
        manager_modifymaincencerinfomod_commit=managermodigyview.
                findViewById(R.id.manager_modifymaincencerinfomod_commit);
        manager_modifymaincencerinfomod_commit.setOnClickListener(this);
        manager_modifymaincencerinfodel_commit=managermodigyview.
                findViewById(R.id.manager_modifymaincencerinfodel_commit);
        manager_modifymaincencerinfodel_commit.setOnClickListener(this);
        maineancerprojectname = managermodigyview.findViewById(R.id.maineancerprojectname);
      /*  final SpinnerAdapter projecctnameadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, projectnamelist);
        maineancerprojectname.setAdapter(projecctnameadapter);*/
        maineancerprojectaddress = managermodigyview.findViewById(R.id.maineancerprojectaddress);
        maineancerprojecttype  = managermodigyview.findViewById(R.id.maineancerprojecttype);
        String[] numbers={getResources().getString(R.string.firsttitile),
                getResources().getString(R.string.firsttitile1)};
        final SpinnerAdapter adapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, numbers);
        maineancerprojecttype.setAdapter(adapter);
    }

/*********查询所有运维人员信息*************/
    public void setManagerModifymainercerinfo(Context context) {
        Log.i("kkk8199", "into setManagerModifymainercerinfo");
        mcontext = context;
        ShowAccount showaccount = new ShowAccount();
        showaccount.setStatus(50);//协议规定
        showaccount.setType(Consts.ACCOUNT_TYPE[2]);//维护人员
        ShowPage<ShowAccount> showmanagermodifymainercerinfo=new ShowPage<>(showaccount);
        SupplyConnectAPI.getInstance().querymainenancer(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,showmanagermodifymainercerinfo);
    }

    /********获取运维人员信息***************/
    public void setManagermodifymainercerlistinfo(final List<ShowAccount> listauditing){
        String[] numbers=new String[listauditing.size()];
        for(int i=0;i<listauditing.size();i++){
            numbers[i]=listauditing.get(i).getName();
            Log.i("kkk8199","number="+numbers[i]);
        }
        final SpinnerAdapter mainercernameadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, numbers);
        maineancername.setAdapter(mainercernameadapter);
        maineancername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowAccount nowaccount = listauditing.get(position);
                maineancernumber.setText(nowaccount.getPhone());

                Log.i("kkk8199","position="+position);

            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.manager_modifymaincencerinfomod_commit:

                break;
            case R.id.manager_modifymaincencerinfodel_commit:

                break;
        }
    }
}
