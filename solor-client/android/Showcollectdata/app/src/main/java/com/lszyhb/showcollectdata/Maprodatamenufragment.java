package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.common.TimeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkk8199 on 1/9/18.
 */

public class Maprodatamenufragment extends Fragment implements View.OnClickListener
    {

        private View convertView;
        private  ShowProject nowproject;
        private LinearLayout mlinearlayout;
        private Madatarunerrorfragment madatarunerrorfragment;
        private Context mcontext;
        private TextView notifyerrortext;//故障报警修改为设备信息,变量直接沿用
        private TextView urgentopenorclosetext;
        private TextView timingruntext;
        private Button gridbutton;
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
            gridbutton.setVisibility(View.GONE);
            notifyerrortext.setSelected(true);
            urgentopenorclosetext.setSelected(false);
            timingruntext.setSelected(false);
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            View deviceslist = inflater.inflate(
                    R.layout.mainenancerdata_deviceslist, null,false);
            GridView mainenancerdata_devicesgridview = deviceslist.
                                        findViewById(R.id.mainenancerdata_devicesgridview);
            List<ShowDevices> listnoweviceinfo;
            if(nowproject.getDevConfiures()!=null)
                listnoweviceinfo=nowproject.getDevConfiures();
            else
                listnoweviceinfo=new ArrayList<ShowDevices>();
            MaprodeviceslistAdapter  maprodeviceslistadapter=
                    new MaprodeviceslistAdapter(mcontext, listnoweviceinfo);
            mainenancerdata_devicesgridview.setAdapter(maprodeviceslistadapter);
            mlinearlayout.removeAllViews();
            mlinearlayout.addView(deviceslist);

        }

    /*************设置紧急启停****/
    private void seturgentstopandstart(){
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
        // View datarunerror=inflater.inflate(R.layout.mainenancerdata_runerror, container, false);
        mlinearlayout.removeAllViews();
        mlinearlayout.addView(urgentstartorstop);
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
                seturgentstopandstart();
                return;
            case R.id.timingruntext:
                settimeingrun();
                return;
            case R.id.gridbutton:
                settimingcommit();
                break;
            case R.id.fanstart:
                if(!fanstartstatus) {
                    fanstartstatus=true;
                    fanstart.setImageResource(R.drawable.but_on);
                    fanstopstatus=false;
                    fanstop.setImageResource(R.drawable.but_off);
                }else{
                    fanstartstatus=false;
                    fanstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.fanstop:
                if(!fanstopstatus) {
                    fanstopstatus=true;
                    fanstop.setImageResource(R.drawable.but_on);
                    fanstartstatus=false;
                    fanstart.setImageResource(R.drawable.but_off);
                }else{
                    fanstopstatus=false;
                    fanstop.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakfanstart:
                if(!fanstartstatus) {
                    bakfanstartstatus=true;
                    bakfanstart.setImageResource(R.drawable.but_on);
                    bakfanstopstatus=false;
                    bakfanstop.setImageResource(R.drawable.but_off);
                }else{
                    bakfanstartstatus=false;
                    bakfanstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakfanstop:
                if(!bakfanstopstatus) {
                    bakfanstopstatus=true;
                    bakfanstop.setImageResource(R.drawable.but_on);
                    bakfanstartstatus=false;
                    bakfanstart.setImageResource(R.drawable.but_off);
                }else{
                    bakfanstopstatus=false;
                    bakfanstop.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.pumpstart:
                if(!pumpstartstatus) {
                    pumpstartstatus=true;
                    pumpstart.setImageResource(R.drawable.but_on);
                    pumpstopstatus=false;
                    pumpstop.setImageResource(R.drawable.but_off);
                }else{
                    pumpstartstatus=false;
                    pumpstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.pumpstop:
                if(!pumpstopstatus) {
                    pumpstopstatus=true;
                    pumpstop.setImageResource(R.drawable.but_on);
                    pumpstartstatus=false;
                    pumpstart.setImageResource(R.drawable.but_off);
                }else{
                    pumpstopstatus=false;
                    pumpstop.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakpumpstart:
                if(!bakpumpstartstatus) {
                    bakpumpstartstatus=true;
                    bakpumpstart.setImageResource(R.drawable.but_on);
                    bakpumpstopstatus=false;
                    bakpumpstop.setImageResource(R.drawable.but_off);
                }else{
                    bakpumpstartstatus=false;
                    bakpumpstart.setImageResource(R.drawable.but_off);
                }
                break;
            case R.id.bakpumpstop:
                if(!bakpumpstopstatus) {
                    bakpumpstopstatus=true;
                    bakpumpstop.setImageResource(R.drawable.but_on);
                    bakpumpstartstatus=false;
                    bakpumpstart.setImageResource(R.drawable.but_off);
                }else{
                    bakpumpstopstatus=false;
                    bakpumpstop.setImageResource(R.drawable.but_off);
                }
                break;
        }
    }

}
