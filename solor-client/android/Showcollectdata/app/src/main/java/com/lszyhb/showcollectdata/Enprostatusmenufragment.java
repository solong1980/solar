package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lszyhb.basicclass.DevicesCollectData;
import com.lszyhb.basicclass.ShowDevConfig;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.common.Stringbuild;

import java.util.Date;
import java.util.List;

/**
 * Created by kkk8199 on 1/9/18.
 */

public class Enprostatusmenufragment extends Fragment implements View.OnClickListener{

    private Context mcontext;
    private View convertView;
    private static ShowProject nowproject;
    private LinearLayout deviceslinearlayout;
    private static View environmentermain_projectstatus_info;
    private static View deviceslist;
    private static GridView environmentermain_devicesgridview;
    private static MaprodeviceslistAdapter deviveslistadapter;
    private static List<ShowDevices> nowlistshowdevices;
    private static View environmentermain_devicesfragment;
    private ImageView environmentermainrun_goodimage;
    private ImageView environmentermainrun_errorimage;
    private TextView  environmentermainrun_timeday;
    private TextView  environmentermainrun_timehour;
    private TextView  environmentermainrun_timeminutue;
    private TextView  environmentermainrun_erroryear;
    private TextView  environmentermainrun_errormonth;
    private TextView  environmentermainrun_errorday;
    private TextView  environmentermainrun_errorhour;
    private TextView  environmentermainrun_errorminute;
    private Button    status_backbutton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        convertView =inflater.inflate(R.layout.environmentermain_projectstatus, container, false);
        setdeviceslistinfo();
        return convertView;
    }


    /***************************/
    private void setdeviceslistinfo() {
        LinearLayout mlinearlayout = convertView.findViewById(R.id.environmentermainlinearlayout);

        LayoutInflater inflater=LayoutInflater.from(mcontext);
        environmentermain_devicesfragment=inflater.inflate(
                R.layout.environmentermain_projectstatus_deviceslist, null, false);
        environmentermain_devicesgridview = environmentermain_devicesfragment.
                findViewById(R.id.environmentermain_devicesgridview);
        mlinearlayout.addView(environmentermain_devicesfragment); //增加第一个页面*//*

        environmentermain_projectstatus_info = inflater.inflate(
        R.layout.environmentermain_projectstatus_info, null,false);
        mlinearlayout.addView(environmentermain_projectstatus_info);//增加第二个页面
        initdevicesinfoitem();
        environmentermain_projectstatus_info.setVisibility(View.GONE);

        /******发送查询项目设备列表命令******/
        ShowDevConfig mdevices = new ShowDevConfig();
            mdevices.setProjectId(nowproject.getId());
            mdevices.setMsg("Success");
            SupplyConnectAPI.getInstance().queryrdeviceslist(UserMainActivity.musermainsocket,
                                                             UserMainActivity.musermainhandler,mdevices);
    }

    /******初始化显示数据页面(第2个页面的元素)*********/
    private void initdevicesinfoitem(){
        environmentermainrun_goodimage = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_goodimage);
        environmentermainrun_errorimage = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_errorimage);
        environmentermainrun_timeday = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_timeday);
        environmentermainrun_timehour = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_timehour);
        environmentermainrun_timeminutue = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_timeminutue);
        environmentermainrun_erroryear = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_erroryear);
        environmentermainrun_errormonth = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_errormonth);
        environmentermainrun_errorday = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_errorday);
        environmentermainrun_errorhour = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_errorhour);
        environmentermainrun_errorminute = environmentermain_projectstatus_info.findViewById(R.id.environmentermainrun_errorminute);
        status_backbutton =  environmentermain_projectstatus_info.findViewById(R.id.status_backbutton);
        status_backbutton.setOnClickListener(this);
    }

    /*************更新信息状态页面显示＊＊＊＊＊＊＊＊＊＊＊＊＊／
     *
     */
    public void updateinfostatus(DevicesCollectData mcollectdata){
        if(mcollectdata!=null) {
            int[] needstate=new int[4];
            needstate=Stringbuild.BinstrToIntArray(mcollectdata.getStat());
            int state=needstate[needstate.length - 1];
            if ((state & 0x8) == 0x8) {
                environmentermainrun_goodimage.setImageResource(R.drawable.errorstatus);
                environmentermainrun_errorimage.setImageResource(R.drawable.goodstatus);
            } else {
                environmentermainrun_goodimage.setImageResource(R.drawable.goodstatus);
                environmentermainrun_errorimage.setImageResource(R.drawable.errorstatus);
            }
            Long safetime=mcollectdata.getSafeTime();
            Log.i("kkk8199","safetime="+safetime);
            long days=safetime / (60 * 60 * 24);
            long hours=(safetime % (60 * 60 * 24)) / (60 * 60);
            long minutes=(safetime % (60 * 60)) / 60;
            environmentermainrun_timeday.setText(String.valueOf(days));
            environmentermainrun_timehour.setText(String.valueOf(hours));
            environmentermainrun_timeminutue.setText(String.valueOf(minutes));
            Date breaktime = mcollectdata.getBreakTime();
            if(breaktime!=null) {
                long breakyear=breaktime.getYear()+1900;
                long breakmonth=breaktime.getMonth();
                long breakhour=breaktime.getHours();
                long breakday=breaktime.getDay();
              //  long breakminutes=breaktime.getMinutes();
                environmentermainrun_erroryear.setText(String.valueOf(breakyear));
                environmentermainrun_errormonth.setText(String.valueOf(breakmonth));
                environmentermainrun_errorday.setText(String.valueOf(breakday));
                environmentermainrun_errorhour.setText(String.valueOf(breakhour));
              //  environmentermainrun_errorminute.setText(String.valueOf(breakminutes));
            }
        }
        else {
            environmentermainrun_goodimage.setImageDrawable(null);
            environmentermainrun_errorimage.setImageDrawable(null);
            environmentermainrun_timeday.setText("");
            environmentermainrun_timehour.setText("");
            environmentermainrun_timeminutue.setText("");
            environmentermainrun_erroryear.setText("");
            environmentermainrun_errormonth.setText("");
            environmentermainrun_errorday.setText("");
            environmentermainrun_errorhour.setText("");
        }
    }

    /***************从服务器获取到设备列表后，更新设备列表Adapter和list及其更新项目的状态*************/
    public void updatedevicesadapter(List<ShowDevices> mlistdevices){
        nowlistshowdevices = mlistdevices;
        if(environmentermain_devicesgridview!=null) {
            deviveslistadapter=new MaprodeviceslistAdapter(mcontext, mlistdevices,0);
            environmentermain_devicesgridview.setAdapter(deviveslistadapter);
        }
    }

    /*******显示设备列表还是设备信息***********/
    public static void setvisibledeviceorinfo(boolean islist){
        if(islist){
            environmentermain_devicesfragment.setVisibility(View.VISIBLE);
            environmentermain_projectstatus_info.setVisibility(View.GONE);
            mquerystatusHandler.removeCallbacks(querydata);
        }
        else{
            environmentermain_projectstatus_info.setVisibility(View.VISIBLE);
            environmentermain_devicesfragment.setVisibility(View.GONE);
            mquerystatusHandler.postDelayed(querydata,10000);
        }
    }

    /********初始化传入项目和type*********/
    public void setEnprodataproject(Context context , ShowProject lsproject) {
        Log.i("kkk8199","into setEnprodataproject lsproject="+lsproject);
        mcontext=context;
        nowproject=lsproject;
        //    Log.i("kkk8199","lsproject.id="+nowproject.getId());
    }

    /********自动刷新数据*****/
    private static Handler mquerystatusHandler = new Handler();
    static Runnable querydata = new Runnable() {
        @Override
        public void run() {
            //do something
            //每隔10s循环执行run方法
            DevicesCollectData mcollectdata= new DevicesCollectData();
            //environmentermain_devicesgridview.getSelectedItem().getDevNo();
            Log.i("kkk8199","uuid="+deviveslistadapter.getnowuuid());
            mcollectdata.setUuid(deviveslistadapter.getnowuuid());
            SupplyConnectAPI.getInstance().queryrundata(UserMainActivity.musermainsocket,
                    UserMainActivity.musermainhandler,mcollectdata);
            mquerystatusHandler.postDelayed(this, 10000);
        }
    };


    @Override
    public void  onPause() {
        mquerystatusHandler.removeCallbacks(querydata);
       // Log.i("kkk8199", "into onpause");
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_backbutton:
                setvisibledeviceorinfo(true);
                break;
        }
    }
}