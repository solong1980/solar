package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowPage;

import java.util.List;


/**
 * Created by kkk8199 on 2/5/18.
 */

public class ManagerAuditing  extends Fragment implements View.OnClickListener{

    private Context mcontext;
    private View managerauditingview;
    private AuditingAdapter auditingadapter;
    private GridView auditinggridview;
    private TextView textregisterviewid;
    private TextView textrejectviewid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        managerauditingview= inflater.inflate(R.layout.manager_auditing, container, false);
        initfragment();
        return managerauditingview;
    }



    /**************初始化元素**********************/
    private void initfragment(){
                auditinggridview=managerauditingview.findViewById(R.id.auditinggridview);
                auditinggridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);// 设置为多选模式
                textregisterviewid=managerauditingview.findViewById(R.id.textregisterviewid);
                textrejectviewid=managerauditingview.findViewById(R.id.textrejectviewid);
                textregisterviewid.setOnClickListener(this);
                textregisterviewid.setSelected(true);
                textrejectviewid.setOnClickListener(this);
    }

/******发送注册审核查询列表的命令********/
    public void setManagerauditingregisterview(Context context) {
        Log.i("kkk8199","into setManagerauditingregisterview");
        mcontext=context;
       ShowAccount showaccount = new ShowAccount();
       showaccount.setStatus(10);
       ShowPage<ShowAccount> showauditing=new ShowPage<>(showaccount);
        showauditing.setPageNum(1);
        showauditing.setCount(100);//100足够大
        SupplyConnectAPI.getInstance().registeraudit(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,showauditing);
    }


    /********得到注册审核列表************/
    public void setMangerauditingregisterlist(List<ShowAccount> listauditing){
        Log.i("kkk8199","listauditing="+listauditing);
            auditingadapter=new AuditingAdapter(mcontext, listauditing,0);
            auditinggridview.setAdapter(auditingadapter);
    }


    /******发送用户找回查询列表的命令******/
    public void setManagerauditingfinduserview() {
        Log.i("kkk8199","into setManagerauditingfinduserview");
        ShowPage showauditing= new ShowPage();
        SupplyConnectAPI.getInstance().finduseraudit(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,showauditing);
    }

    /******用户找回得到列表*******/
    public void setManagerauditingfinduserlist(List<ShowAccount> listauditing) {
        Log.i("kkk8199","into setManagerauditingfinduserlist");
        auditingadapter=new AuditingAdapter(mcontext, listauditing,1);
        auditinggridview.setAdapter(auditingadapter);
    }


    /********得到审核成功的结果*******/
    public void setMangerauditingsuccess(){
        Log.i("kkk8199","into setMangerauditingsuccess");
        auditingadapter.setnowview();
    }

    @Override
    public void onClick(View v) {
        Log.i("kkk8199","v="+v);
        switch (v.getId()) {

            case R.id.textregisterviewid:
                    setManagerauditingregisterview(mcontext);
                    v.setSelected(true);
                    textrejectviewid.setSelected(false);
                break;

            case R.id.textrejectviewid:
                    setManagerauditingfinduserview();
                    v.setSelected(true);
                    textregisterviewid.setSelected(false);
                break;
        }
    }
}
