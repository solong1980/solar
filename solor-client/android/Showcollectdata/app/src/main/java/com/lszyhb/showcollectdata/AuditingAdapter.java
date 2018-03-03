package com.lszyhb.showcollectdata;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lszyhb.basicclass.AppType;
import com.lszyhb.basicclass.AuditResult;
import com.lszyhb.basicclass.ConnectAPI;
import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowPage;
import com.lszyhb.common.JsonUtilTool;

import java.util.List;

/**
 * Created by kkk8199 on 2/5/18.
 */

public class AuditingAdapter extends BaseAdapter{

    private List<ShowAccount> listauditing;
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageView imageview;
    private int type;
    class AuditingHolder{
        TextView text;
        ImageView auditingpassview;
        ImageView auditingrejectview;
    }


    public AuditingAdapter(Context ctx, List<ShowAccount> listauditing,int type) {
        mContext = ctx;
        this.listauditing = listauditing;
        mInflater = LayoutInflater.from(mContext);
        this.type=type;
    }



    @Override
    public int getCount() {
        if(listauditing.size()==0)
            return 1;
        else
            return listauditing.size();
    }

    @Override
    public Object getItem(int position) {
        return listauditing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //* 得到View *//*
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AuditingHolder gridviewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.auditing_item,null);
            gridviewholder = new AuditingHolder();
            TextView auditingregisterinfotext = convertView.findViewById(R.id.auditingregisterinfotext);
            if(listauditing.size()==0){
                auditingregisterinfotext.setText("没有需要审核");
                gridviewholder.text=auditingregisterinfotext;
                gridviewholder.auditingpassview=convertView.findViewById(R.id.auditingpassview);
                gridviewholder.auditingpassview.setVisibility(View.GONE);
                gridviewholder.auditingrejectview=convertView.findViewById(R.id.auditingrejectview);
                gridviewholder.auditingrejectview.setVisibility(View.GONE);
                convertView.findViewById(R.id.registerviewpass).setVisibility(View.GONE);
                convertView.findViewById(R.id.registerviewreject).setVisibility(View.GONE);
                convertView.setTag(gridviewholder);
            }
            else {
                ShowAccount showaccount=listauditing.get(position);
                //    Log.i("kkk8199","showaccount="+showaccount.getCreateTime());
                String timetext="申请时间：" + showaccount.getCreateTime() + "\n";
                //   Log.i("kkk8199","timetext="+timetext);
                String nametext="用户姓名：" + showaccount.getName() + "\n";
                String phonenum="电话号码：" + showaccount.getPhone() + "\n";
                String mailaddress="邮箱地址：" + showaccount.getEmail() + "\n";
                //  Log.i("kkk8199", "mailaddress=" + mailaddress);
                int usertype=showaccount.getType();
                String typename=null;
                switch (usertype) {
                    case 30:
                        typename="用户类型：" + "运维人员" + "\n";
                        break;
                    case 20:
                        typename="用户类型：" + "环保人员" + "\n";
                        break;
                }
                String idall=showaccount.getlocationIds();
                  Log.i("kkk8199","idall="+idall);
                String id="";
                if(idall.contains(",")) {
                    Log.i("kkk","into");
                    id=idall.substring(0, idall.indexOf(","));//显示一个
                }
                else
                    id=idall;
                // Log.i("kkk8199","id="+id);
                ParseAddress parseaddress=ParseAddress.getInstance();
                String address=parseaddress.queryid(id);
                String addressnew="项目地址：" + address + "\n";
                String text=nametext + phonenum + mailaddress + typename + addressnew + timetext;
                //   Log.i("kkk8199", "text=" + text);
                auditingregisterinfotext.setText(text);
                gridviewholder.text=auditingregisterinfotext;
                gridviewholder.auditingpassview=convertView.findViewById(R.id.auditingpassview);
                gridviewholder.auditingrejectview=convertView.findViewById(R.id.auditingrejectview);
                convertView.setTag(gridviewholder);
            }

        } else {
            gridviewholder =  (AuditingAdapter.AuditingHolder)convertView.getTag();
        }

        gridviewholder.auditingpassview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Log.i("kkk8199","pass");
                   imageview=  (ImageView)(v);
                    ShowAccount showaccount = listauditing.get(position);
                    showaccount.setStatus(AuditResult.AGREE.type());
                    if(type==0)//注册审核
                        SupplyConnectAPI.getInstance().registerauditpassorreject(UserMainActivity.musermainsocket,
                            UserMainActivity.musermainhandler,showaccount);
                    else//找回审核
                        SupplyConnectAPI.getInstance().finduserpassorreject(UserMainActivity.musermainsocket,
                                UserMainActivity.musermainhandler,showaccount);
            }
        });

        gridviewholder.auditingrejectview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //    Log.i("kkk8199","no");
                 imageview=  (ImageView)(v);
                ShowAccount showaccount = listauditing.get(position);
                showaccount.setStatus(AuditResult.REJECT.type());
                if(type==0)//注册审核
                    SupplyConnectAPI.getInstance().registerauditpassorreject(UserMainActivity.musermainsocket,
                            UserMainActivity.musermainhandler,showaccount);
                else//找回审核
                    SupplyConnectAPI.getInstance().finduserpassorreject(UserMainActivity.musermainsocket,
                            UserMainActivity.musermainhandler,showaccount);

            }
        });

        return convertView;
    }

    public void setnowview(){
        Log.i("kkk8199","imageview="+imageview);
        imageview.setImageResource(R.drawable.but_on);
    }
}
