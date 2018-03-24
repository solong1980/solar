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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.DevicesCollectData;
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowDevConfig;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowPage;
import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.common.Stringbuild;
import com.lszyhb.common.TimeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by kkk8199 on 1/9/18.
 */

public class Maprodatamenufragment extends Fragment implements View.OnClickListener
    {

        private View convertView;
        private  ShowProject nowproject;
        private LinearLayout mlinearlayout;
        private LinearLayout deviceslinearlayout;
        private Madatarunerrorfragment madatarunerrorfragment;
        private Context mcontext;
        private TextView notifyerrortext;//故障报警修改为设备信息,变量直接沿用
        private TextView urgentopenorclosetext;
        private TextView timingruntext;
        private Button gridbutton;
        private static Button backbutton;
        private ImageView fanstart;
        private ImageView fanstop;
        private ImageView bakfanstart;
        private ImageView bakfanstop;
        private ImageView pumpstart;
        private ImageView pumpstop;
        private ImageView bakpumpstart;
        private ImageView bakpumpstop;
        private GridAdapter timinggridviewAdapter;
        private boolean timingruntextstatus=true;
        private boolean fanstartstatus=false;
        private boolean fanstopstatus=false;
        private boolean bakfanstartstatus=false;
        private boolean bakfanstopstatus=false;
        private boolean pumpstartstatus=false;
        private boolean pumpstopstatus=false;
        private boolean bakpumpstartstatus=false;
        private boolean bakpumpstopstatus=false;
        ProjectWorkingMode mprojectworkmode;
        private GridView timinggridview;
        private static GridView mainenancerdata_devicesgridview;
        private static MaprodeviceslistAdapter deviveslistadapter;
        private static View deviceslist;
        private static View mainenancer_devicesinfo;
        private GifImageView devices_solar_panel;//太阳能板
        private TextView solar_voltage;//太阳板电压
        private TextView charging_current;//充电电流
        private TextView totalpower;// 总发电量
        private GifImageView solar_sewage_controller;//污水控制器
        private GifImageView battery_pack;
        private TextView battery_capacity;
        private GifImageView fan_status;
        private GifImageView bakfan_status;
        private GifImageView waterpump_status;
        private GifImageView bakwaterpump_status;

        private static List<ShowDevices> nowlistshowdevices;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
             convertView =inflater.inflate(R.layout.maintenancermain_projectdata, container, false);
            // Log.i("kkk8199","Maprodatamenufragment convertView="+convertView);
             initfragment();
            // setdatarunerror();
            setdeviceslistinfo();
             return convertView;
        }

        /************初始化fragment的元素**********/
        private void initfragment(){
            mlinearlayout=convertView.findViewById(R.id.idemadatalinearlayout);
            notifyerrortext=convertView.findViewById(R.id.notifyerrortext);//直接沿用以前变量,不修改
            urgentopenorclosetext=convertView.findViewById(R.id.urgentopenorclosetext);
            gridbutton    = convertView.findViewById(R.id.gridbutton);
            backbutton    = convertView.findViewById(R.id.backbutton);
            backbutton.setOnClickListener(this);
            notifyerrortext.setOnClickListener(this);
            urgentopenorclosetext.setOnClickListener(this);
            if(timingruntextstatus){
                timingruntext=convertView.findViewById(R.id.timingruntext);
               timingruntext.setVisibility(View.VISIBLE);
               timingruntext.setOnClickListener(this);
               /********动态修改位置***************/
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);//
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                layoutParams.leftMargin=15;
                layoutParams.bottomMargin=30;
                notifyerrortext.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);//
                layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                layoutParams1.rightMargin=15;
                layoutParams1.bottomMargin=30;
                urgentopenorclosetext.setLayoutParams(layoutParams1);
            }
        }

    /************设置故障报警信息页面*,
     * 取消故障报警信息界面,改成设备信息界面,函数保留不用****************/
    private void setdatarunerror()
    {
        backbutton.setVisibility(View.GONE);
        gridbutton.setVisibility(View.GONE);
        notifyerrortext.setSelected(true);
        urgentopenorclosetext.setSelected(false);
        timingruntext.setSelected(false);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View datarunerror = inflater.inflate(
               R.layout.mainenancerdata_runerror, null,false);
       // View datarunerror=inflater.inflate(R.layout.mainenancerdata_runerror, container, false);
        mlinearlayout.removeAllViews();
        mlinearlayout.addView(datarunerror);

    }

    /************需求更改,增加设备信息界面****************/
    private void setdeviceslistinfo()
    {
        backbutton.setVisibility(View.GONE);
        gridbutton.setVisibility(View.GONE);
        notifyerrortext.setSelected(true);
        urgentopenorclosetext.setSelected(false);
        timingruntext.setSelected(false);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View mainenancerdata_devicesfragment = inflater.inflate(
                R.layout.mainenancerdata_devicesfragment, null,false);
        deviceslinearlayout = mainenancerdata_devicesfragment.findViewById(R.id.deviceslinearlayout);
        deviceslist = inflater.inflate(
                R.layout.mainenancerdata_deviceslist, null,false);
         mainenancerdata_devicesgridview = deviceslist.
                                    findViewById(R.id.mainenancerdata_devicesgridview);
        deviceslinearlayout.addView(deviceslist); //增加第一个页面*/

        mainenancer_devicesinfo = inflater.inflate(
                R.layout.mainenancer_devicesinfo, null,false);
        deviceslinearlayout.addView(mainenancer_devicesinfo);//增加第二个页面
        initdevicesinfoitem();
        mainenancer_devicesinfo.setVisibility(View.GONE);

        /******发送查询项目设备列表命令******/
        ShowDevConfig mdevices = new ShowDevConfig();
        mdevices.setProjectId(nowproject.getId());
        mdevices.setMsg("Success");
        SupplyConnectAPI.getInstance().queryrdeviceslist(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,mdevices);
        mlinearlayout.removeAllViews();
        mlinearlayout.addView(mainenancerdata_devicesfragment);
    }

        /******初始化显示数据页面(第2个页面的元素)*********/
    private void initdevicesinfoitem(){
        devices_solar_panel = mainenancer_devicesinfo.findViewById(R.id.devices_solar_panel);
        solar_voltage = mainenancer_devicesinfo.findViewById(R.id.solar_voltage);
        charging_current = mainenancer_devicesinfo.findViewById(R.id.charging_current);
        totalpower = mainenancer_devicesinfo.findViewById(R.id.totalpower);
        solar_sewage_controller = mainenancer_devicesinfo.findViewById(R.id.solar_sewage_controller);
        battery_pack = mainenancer_devicesinfo.findViewById(R.id.battery_pack);
        battery_capacity =  mainenancer_devicesinfo.findViewById(R.id.battery_capacity);
        fan_status = mainenancer_devicesinfo.findViewById(R.id.fan_status);
        bakfan_status = mainenancer_devicesinfo.findViewById(R.id.bakfan_status);
        waterpump_status = mainenancer_devicesinfo.findViewById(R.id.waterpump_status);
        bakwaterpump_status =  mainenancer_devicesinfo.findViewById(R.id.bakwaterpump_status);
    }

        /***************从服务器获取到设备列表后，更新设备列表Adapter和list及其更新项目的状态*************/
    public void updatedevicesadapter(List<ShowDevices> mlistdevices){
        nowlistshowdevices = mlistdevices;
        if(mainenancerdata_devicesgridview!=null) {
            deviveslistadapter=new MaprodeviceslistAdapter(mcontext, mlistdevices,1);
            mainenancerdata_devicesgridview.setAdapter(deviveslistadapter);
        }
        if(fanstart!=null){
            initdevicescontrol();
        }
    }

    /*******显示设备列表还是设备信息***********/
    public static void setvisibledeviceorinfo(boolean islist){
        if(islist){
            deviceslist.setVisibility(View.VISIBLE);
            mainenancer_devicesinfo.setVisibility(View.GONE);
            backbutton.setVisibility(View.GONE);
            mquerydataHandler.removeCallbacks(querydatarun);
        }
        else{
            mainenancer_devicesinfo.setVisibility(View.VISIBLE);
            deviceslist.setVisibility(View.GONE);
            backbutton.setVisibility(View.VISIBLE);
            mquerydataHandler.postDelayed(querydatarun,10000);
        }
    }

    /****************更新设备的采集数据*************/
    public void setcollectdata(DevicesCollectData mcollectdata){
            //mainenancer_devicesinfo.findViewById();
        Log.i("kkk8199","vssum="+mcollectdata.getVssun());
        solar_voltage.setText("电压:"+mcollectdata.getVssun()+"V");
        charging_current.setText("充电电流:" + mcollectdata.getIchg()+"A");
        totalpower.setText("发电量:" + mcollectdata.getPchg()+"度");
        battery_capacity.setText("电池剩余容量:" + mcollectdata.getLevel()+"%");
       // Integer state = Integer.parseInt(mcollectdata.getStat());
       //String statestring = Stringbuild.toHex(mcollectdata.getStat());
        int[] needstate;
        needstate = Stringbuild.BinstrToIntArray(mcollectdata.getStat());
        int state;
        if(needstate.length>3)
            state = needstate[0];
        else
            state = 0;
        switch (state & 0x8){
            case 0:
                fan_status.setImageResource(R.drawable.fan_good);
                break;
            case 8:
                fan_status.setImageResource(R.drawable.fan_bad);
                break;
        }
        switch (state & 0x4){
            case 0:
                bakfan_status.setImageResource(R.drawable.bakfan_good);
                break;
            case 4:
                bakfan_status.setImageResource(R.drawable.bakfan_bad);
                break;
        }
        switch (state & 0x2){
            case 0:
                waterpump_status.setImageResource(R.drawable.waterpump_good);
                break;
            case 2:
                waterpump_status.setImageResource(R.drawable.waterpump_bad);
                break;
        }
        switch (state & 0x1){
            case 0:
                bakwaterpump_status.setImageResource(R.drawable.bakwaterpump_good);
                break;
            case 1:
                bakwaterpump_status.setImageResource(R.drawable.bakwaterpump_bad);
                break;
        }

    }

        /********自动刷新数据*****/
        private static Handler mquerydataHandler = new Handler();
        static Runnable querydatarun = new Runnable() {
        @Override
        public void run() {
            //do something
            //每隔10s循环执行run方法
            /******发送查询项目设备列表命令******/
            DevicesCollectData mcollectdata= new DevicesCollectData();
            //environmentermain_devicesgridview.getSelectedItem().getDevNo();
            mcollectdata.setUuid(deviveslistadapter.getnowuuid());
            SupplyConnectAPI.getInstance().queryrundata(UserMainActivity.musermainsocket,
                    UserMainActivity.musermainhandler,mcollectdata);
            mquerydataHandler.postDelayed(this, 10000);
        }
    };

        @Override
        public void  onPause() {
            mquerydataHandler.removeCallbacks(querydatarun);
        // Log.i("kkk8199", "into onpause");
            super.onPause();
        }

    /*************设置紧急启停****/
    private void seturgentstopandstart(){
        backbutton.setVisibility(View.GONE);
        gridbutton.setVisibility(View.GONE);
        notifyerrortext.setSelected(false);
        urgentopenorclosetext.setSelected(true);
        timingruntext.setSelected(false);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View urgentstartorstop = inflater.inflate(
                R.layout.mainenancerdata_urgentstartorstop, null,false);
        fanstart=urgentstartorstop.findViewById(R.id.fanstart);
        fanstart.setOnClickListener(this);
        fanstop=urgentstartorstop.findViewById(R.id.fanstop);
        fanstop.setOnClickListener(this);
        bakfanstart=urgentstartorstop.findViewById(R.id.bakfanstart);
        bakfanstart.setOnClickListener(this);
        bakfanstop=urgentstartorstop.findViewById(R.id.bakfanstop);
        bakfanstop.setOnClickListener(this);
        pumpstart=urgentstartorstop.findViewById(R.id.pumpstart);
        pumpstart.setOnClickListener(this);
        pumpstop=urgentstartorstop.findViewById(R.id.pumpstop);
        pumpstop.setOnClickListener(this);
        bakpumpstart=urgentstartorstop.findViewById(R.id.bakpumpstart);
        bakpumpstart.setOnClickListener(this);
        bakpumpstop=urgentstartorstop.findViewById(R.id.bakpumpstop);
        bakpumpstop.setOnClickListener(this);

        /******发送查询项目设备列表命令******/
        ShowDevConfig mdevices = new ShowDevConfig();
        mdevices.setProjectId(nowproject.getId());
        mdevices.setMsg("Success");
        SupplyConnectAPI.getInstance().queryrdeviceslist(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,mdevices);

        // View datarunerror=inflater.inflate(R.layout.mainenancerdata_runerror, container, false);
        mlinearlayout.removeAllViews();
        mlinearlayout.addView(urgentstartorstop);
    }

/*****************初始化当前的项目的状态,由于项目的一致性,只取一个读取就可以了*************/
    public void initdevicescontrol(){
        if(nowlistshowdevices.size()>0) {
            int device0=nowlistshowdevices.get(0).getSw0();
            int device1=nowlistshowdevices.get(0).getSw1();
            int device2=nowlistshowdevices.get(0).getSw2();
            int device3=nowlistshowdevices.get(0).getSw3();
            switch (device0) {
                case 0:
                    fanstart.setImageResource(R.drawable.but_on);
                    fanstop.setImageResource(R.drawable.but_off);
                    break;
                case 1:
                    fanstart.setImageResource(R.drawable.but_off);
                    fanstop.setImageResource(R.drawable.but_on);
                    break;
                case 2:
                    fanstart.setImageResource(R.drawable.but_off);
                    fanstop.setImageResource(R.drawable.but_off);
                    break;
            }
            switch (device1) {
                case 0:
                    bakfanstart.setImageResource(R.drawable.but_on);
                    bakfanstop.setImageResource(R.drawable.but_off);
                    break;
                case 1:
                    bakfanstart.setImageResource(R.drawable.but_off);
                    bakfanstop.setImageResource(R.drawable.but_on);
                    break;
                case 2:
                    bakfanstart.setImageResource(R.drawable.but_off);
                    bakfanstop.setImageResource(R.drawable.but_off);
                    break;
            }
            switch (device2) {
                case 0:
                    pumpstart.setImageResource(R.drawable.but_on);
                    pumpstop.setImageResource(R.drawable.but_off);
                    break;
                case 1:
                    pumpstart.setImageResource(R.drawable.but_off);
                    pumpstop.setImageResource(R.drawable.but_on);
                    break;
                case 2:
                    pumpstart.setImageResource(R.drawable.but_off);
                    pumpstop.setImageResource(R.drawable.but_off);
                    break;
            }
            switch (device3) {
                case 0:
                    bakpumpstart.setImageResource(R.drawable.but_on);
                    bakpumpstop.setImageResource(R.drawable.but_off);
                    break;
                case 1:
                    bakpumpstart.setImageResource(R.drawable.but_off);
                    bakpumpstop.setImageResource(R.drawable.but_on);
                    break;
                case 2:
                    bakpumpstart.setImageResource(R.drawable.but_off);
                    bakpumpstop.setImageResource(R.drawable.but_off);
                    break;
            }
        }
    }

    /*********控制子设备的启停
     * 参数devices 代表设备,0,1,2,3分别为风机,备用风机,水泵,备用水泵
     * status     代表需要的状态,0,1,2 ,强制启动,强制停止,自动运行
     * ********/
    public void Setcontroldevices(int devices , short status){
        for(int i = 0 ; i <nowlistshowdevices.size();i++){
            switch (devices){
                case 0:
                    nowlistshowdevices.get(i).setSw0(status);
                    break;
                case 1:
                    nowlistshowdevices.get(i).setSw1(status);
                    break;
                case 2:
                    nowlistshowdevices.get(i).setSw2(status);
                    break;
                case 3:
                    nowlistshowdevices.get(i).setSw3(status);
                    break;
            }

        }
        SupplyConnectAPI.getInstance().controlprojectstartorstop(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,nowlistshowdevices);
    }

        /********************设置定时运行时间**********/
    private void settimeingrun(){
        getMaproprojectmode();
         String[] mImgIds = new String[] { "0:00-0:30","0:30-1:00",
                "1:00-1:30",  "1:30-2:00",  "2:00-2:30","2:30-3:00",  "3:00-3:30",
                 "3:30-4:00",  "4:00-4:30",  "4:30-5:00","5:00-5:30",  "5:30-6:00",
                 "6:00-6:30",  "6:30-7:00",  "7:00-7:30","7:30-8:00",  "8:00-8:30",
                 "8:30-9:00",  "9:00-9:30",  "9:30-10:00","10:00-10:30",  "10:30-11:00",
                 "11:00-11:30",  "11:30-12:00",  "12:00-12:30","12:30-13:00",  "13:00-13:30",
                 "13:30-14:00",  "14:00-14:30",  "14:30-15:00", "15:00-15:30",  "15:30-16:00",
                 "16:30-17:00",  "17:00-17:30",  "17:30-17:30","17:30-18:00",  "18:00-18:30",
                 "18:30-19:00",  "19:00-19:30",  "19:30-20:00","20:00-20:30",  "20:30-21:00",
                 "21:00-21:30",  "21:30-22:00",  "22:00-22:30","22:30-23:00",  "23:00-23:30",
                 "23:30-24:00",
         };
        backbutton.setVisibility(View.GONE);
        gridbutton.setVisibility(View.VISIBLE);
        gridbutton.setSelected(true);
        notifyerrortext.setSelected(false);
        urgentopenorclosetext.setSelected(false);
        timingruntext.setSelected(true);
        gridbutton.setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View managerdata_timingruntext = inflater.inflate(
                R.layout.managerdata_timingruntext, null,false);
          timinggridview  = (GridView)managerdata_timingruntext.findViewById(R.id.timinggridview);
         timinggridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);// 设置为多选模式
     //   timinggridview.setChoiceMode(GridView.CHOICE_MODE_NONE);
        timinggridviewAdapter = new GridAdapter(mcontext,mImgIds);
        timinggridview.setAdapter(timinggridviewAdapter);// 数据适配
            mlinearlayout.removeAllViews();
            mlinearlayout.addView(managerdata_timingruntext);
    }

/*********获取当前项目的模式*********/
    public void getMaproprojectmode(){
        mprojectworkmode = new ProjectWorkingMode();
        mprojectworkmode.setProjectId(nowproject.getId());
        SupplyConnectAPI.getInstance().getruntime(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,mprojectworkmode);
    }


    /************设置当前项目查询到的工作模式****************/

    public void setnowprojectworkmodeinfo(ProjectWorkingMode projectWorkingMode){
        Log.i("kkk8199","projectWorkingMode.getId()="+projectWorkingMode.getId());
        mprojectworkmode.setId(projectWorkingMode.getId());
        for(int i=0;i<48;i++) {
            try {
                Method method=ProjectWorkingMode.class.getMethod("getH_" + i);
                Object v = method.invoke(projectWorkingMode);
                String isset = v.toString();
               if(isset.equals("1"))
                   timinggridviewAdapter.SetSelectitem(i,true);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e1) {
                throw new RuntimeException(e1.getMessage());
            }
        }
                timinggridviewAdapter.notifyDataSetChanged();
    }

/********初始化传入项目和type*********/
    public void setMaprodataproject(Context context , ShowProject lsproject,int type) {
        Log.i("kkk8199","into setMaprodataproject lsproject="+lsproject);
        mcontext=context;
        if (type == Consts.ACCOUNT_TYPE[0])//管理员
        {
            timingruntextstatus=true;
        }
        nowproject=lsproject;
    //    Log.i("kkk8199","lsproject.id="+nowproject.getId());
    }

/*****************提交设置自动运行时间******************/
    public void settimingcommit(){
        List<Integer> mTimeList;
        mTimeList=timinggridviewAdapter.GetSelectitem();
        for(int i=0;i<mTimeList.size();i++) {
            Log.i("kkk8199", "mTimeList=" + mTimeList.get(i));
            try {
                Method method=ProjectWorkingMode.class.getMethod("setH_" + mTimeList.get(i), new Class[] { Short.class});
                Object result=method.invoke(mprojectworkmode, new Object[]{new Short(String.valueOf(1))});

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e1) {
                throw new RuntimeException(e1.getMessage());
            }
        }
        mprojectworkmode.buildMmcMsg();
        Log.i("kkk8199","nowproject="+nowproject+"nowproject.id="+nowproject.getId());
      /*  mprojectworkmode.setProjectId(nowproject.getId());
        mprojectworkmode.setId((long) 1);*/
        mprojectworkmode.setUpdateTime(TimeUtils.getNowTime());
        SupplyConnectAPI.getInstance().modityruntime(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,mprojectworkmode);
    }


    @Override
    public void onClick(View v) {
        Log.i("kkk8199","v="+v);
        switch (v.getId()) {
            case R.id.notifyerrortext://直接沿用眼前的变量,不修改了
                //setdatarunerror();
                setdeviceslistinfo();
                return ;
            case R.id.urgentopenorclosetext:
                mquerydataHandler.removeCallbacks(querydatarun);
                seturgentstopandstart();
                return;
            case R.id.timingruntext:
                mquerydataHandler.removeCallbacks(querydatarun);
                settimeingrun();
                return;
            case R.id.gridbutton:
                settimingcommit();
                break;
            case R.id.backbutton:
                setvisibledeviceorinfo(true);
                break;
            case R.id.fanstart:
                if(!fanstartstatus) {
                    Setcontroldevices(0,(short)0);
                    fanstartstatus=true;
                    fanstart.setImageResource(R.drawable.but_on);
                    fanstopstatus=false;
                    fanstop.setImageResource(R.drawable.but_off);

                }else{
                    Setcontroldevices(0,(short)2);
                    fanstartstatus=false;
                    fanstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.fanstop:
                if(!fanstopstatus) {
                    Setcontroldevices(0,(short)1);
                    fanstopstatus=true;
                    fanstop.setImageResource(R.drawable.but_on);
                    fanstartstatus=false;
                    fanstart.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(0,(short)2);
                    fanstopstatus=false;
                    fanstop.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakfanstart:
                if(!fanstartstatus) {
                    Setcontroldevices(1,(short)0);
                    bakfanstartstatus=true;
                    bakfanstart.setImageResource(R.drawable.but_on);
                    bakfanstopstatus=false;
                    bakfanstop.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(1,(short)2);
                    bakfanstartstatus=false;
                    bakfanstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakfanstop:
                if(!bakfanstopstatus) {
                    Setcontroldevices(1,(short)1);
                    bakfanstopstatus=true;
                    bakfanstop.setImageResource(R.drawable.but_on);
                    bakfanstartstatus=false;
                    bakfanstart.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(1,(short)2);
                    bakfanstopstatus=false;
                    bakfanstop.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.pumpstart:
                if(!pumpstartstatus) {
                    Setcontroldevices(2,(short)0);
                    pumpstartstatus=true;
                    pumpstart.setImageResource(R.drawable.but_on);
                    pumpstopstatus=false;
                    pumpstop.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(2,(short)2);
                    pumpstartstatus=false;
                    pumpstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.pumpstop:
                if(!pumpstopstatus) {
                    Setcontroldevices(2,(short)1);
                    pumpstopstatus=true;
                    pumpstop.setImageResource(R.drawable.but_on);
                    pumpstartstatus=false;
                    pumpstart.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(2,(short)2);
                    pumpstopstatus=false;
                    pumpstop.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakpumpstart:
                if(!bakpumpstartstatus) {
                    Setcontroldevices(3,(short)0);
                    bakpumpstartstatus=true;
                    bakpumpstart.setImageResource(R.drawable.but_on);
                    bakpumpstopstatus=false;
                    bakpumpstop.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(3,(short)2);
                    bakpumpstartstatus=false;
                    bakpumpstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakpumpstop:
                if(!bakpumpstopstatus) {
                    Setcontroldevices(3,(short)1);
                    bakpumpstopstatus=true;
                    bakpumpstop.setImageResource(R.drawable.but_on);
                    bakpumpstartstatus=false;
                    bakpumpstart.setImageResource(R.drawable.but_off);
                }else{
                    Setcontroldevices(3,(short)2);
                    bakpumpstopstatus=false;
                    bakpumpstop.setImageResource(R.drawable.but_off);
                }
                break;
        }
    }

}
