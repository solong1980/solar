package com.lszyhb.showcollectdata;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowPage;
import com.lszyhb.basicclass.ShowProject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by kkk8199 on 12/2/17.
 */


public class UserMainActivity extends Activity implements View.OnClickListener {
    private MyTreewidget mytree;

    private Enproinfomenufragment einfomenufragment;
    private Enprostatusmenufragment erunmenufragment;
    private Enprodatamenufragment edatamenufragment;
    private static Maprodatamenufragment mdatamenufragment;
    private static ManagerModifymainercerinfo managermodifymainercerinfo;
    private ManagerAddproject manageraddproject;
    private ManagerModifyproject managermodifyproject;
    private ManagerModifydeviceinfo managermodifydeviceinfo;
    private static ManagerAuditing mmanagerauditing;
    private TextView envirmentsecondtitle;
    private TextView project_firsttextview;
    private TextView project_secondtextview;
    private ImageView imageButton;
    private ImageView envirmentmenubutton;
    private List<ShowProject> lstproject;
    private boolean firsttime=false;
    private ShowProject nowproject=null;
    private int status=0;
    private TextView project_name;
    static public int usertype;
    private BottomHorizontalScrollView mHorizontalScrollView;
    private  LinearLayout mHorizontallinearlayout;
    private HorizontalScrollViewAdapter mAdapter;
    public  static ClientSocket musermainsocket;
    public static final int MSG_ADD_PROJECT = 2; //增加项目
    public static final int MSG_FIND_USER = 3;   //用户找回列表
    public static final int MSG_REGISTER = 4;    //注册列表
    public static final int MSG_REGISTER_REJECT = 5;  //审核通过或者拒绝列表
    public static final int MSG_QUERY_MAINEANCERINFO=6;//查询维护人员信息
    public static final int MSG_MODIGY_RUNTIMING=7;//修改定时运行信息
    public static final int MSG_QUERY_RUNTIMING=8;//查询定时运行信息
    public static final int MSG_QUERY_RUNNINGDATA=9;//查询运行数据
    public static final int MSG_MODIFY_PROJECT=10;//项目修改
    public static final int MSG_QUERY_DEVICES=11;//查询项目中的设备

    private static Context usermaincontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);
        usermaincontext=this;
        // mytree = new MyTreewidget(this);
        musermainsocket =  ClientSocket.getInstance();
        ParseAddress parseaddress=ParseAddress.getInstance();
        parseaddress.initneedData(usermaincontext);
        Log.i("kkk8199","musermainsocket1111111="+musermainsocket);
        Intent intentGet = getIntent();
        lstproject = (List<ShowProject>) intentGet.getSerializableExtra(LoginActivity.RETURN_INFO);
        usertype = intentGet.getIntExtra(LoginActivity.RETURN_USERTYPE,10);
        Log.i("kkk8199","lstproject="+lstproject+"usertype="+usertype);
        initneedid();
        setDefaultFragment();
        createView();
    }
    @Override
    protected void onDestroy(){
        Log.i("kkk8199","into onDestroy");
        NotShowfloatwindow();
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        Log.i("kkk8199","into onStop");
        NotShowfloatwindow();
        super.onStop();
    }
/**********初始化元素*********/
    private void initneedid(){
            envirmentsecondtitle   =  findViewById(R.id.envirmentsecondtitle);
            project_firsttextview  =  findViewById(R.id.project_firsttextview);
            project_secondtextview  =  findViewById(R.id.project_secondtextview);
            initbottomviews();
            imageButton=findViewById(R.id.imageButton);
            imageButton.setOnClickListener(this);
            imageButton.setClickable(true);
            envirmentmenubutton=findViewById(R.id.envirmentmenubutton);
            envirmentmenubutton.setOnClickListener(this);
            envirmentmenubutton.setClickable(true);
        }


/*********显示第2和第3title****************/
    private void settitlesvisibile(boolean isshow){
        if(isshow){
            project_firsttextview.setVisibility(View.VISIBLE);
            project_secondtextview.setVisibility(View.VISIBLE);
        }
        else{
            project_firsttextview.setVisibility(View.GONE);
            project_secondtextview.setVisibility(View.GONE);
        }
    }

/**************初始化底部控件**********/
    private void initbottomviews() {

        List<String>  mTimeList= new ArrayList<String>();
        mTimeList.add(getResources().getString(R.string.projecttetail));
        mTimeList.add(getResources().getString(R.string.projectstatus));
        mTimeList.add(getResources().getString(R.string.projectdata));
        if (usertype== Consts.ACCOUNT_TYPE[0]) {
            //管理员
            mTimeList.add(getResources().getString(R.string.projectadd));
            mTimeList.add(getResources().getString(R.string.projectinfomodify));
            mTimeList.add(getResources().getString(R.string.engineerinfomodify));
        //    mTimeList.add(getResources().getString(R.string.workermodify));//根据需求取消
            mTimeList.add(getResources().getString(R.string.registerfindinfo));
        }
        mHorizontalScrollView = (BottomHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
       mHorizontallinearlayout = (LinearLayout) findViewById(R.id.horizonlinearlayout);
        mHorizontalScrollView.setSelected(true);
       mHorizontallinearlayout.setSelected(true);
        mAdapter = new HorizontalScrollViewAdapter(this, mTimeList);
        mHorizontalScrollView.initDatas(mAdapter,mHorizontallinearlayout);
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new BottomHorizontalScrollView.OnItemClickListener()
        {

            @Override
            public void onClick(View view, int position)
            {
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0://项目信息
                        if (status != 0) {
                            settitlesvisibile(true);
                            status=0;
                            project_secondtextview.setText(R.string.projecttetail);
                            if (einfomenufragment == null) {
                                einfomenufragment=new Enproinfomenufragment();
                            }
                            // 使用当前Fragment的布局替代id_content的控件
                            transaction.replace(R.id.idfragment, einfomenufragment);
                            syncdata();
                        }
                        break;
                    case 1://运行状态
                        if (status != 1) {
                            settitlesvisibile(true);
                            status=1;
                            project_secondtextview.setText(R.string.projectstatus);
                            if (erunmenufragment == null) {
                                erunmenufragment=new Enprostatusmenufragment();
                            }
                            // 使用当前Fragment的布局替代id_content的控件
                            transaction.replace(R.id.idfragment, erunmenufragment);
                            syncdata();
                        }
                        break;
                    case 2:
                        if (status != 2) {
                            settitlesvisibile(true);
                            status = 2;
                            project_secondtextview.setText(R.string.projectdata);
                            Log.i("kkk8199","usertype="+usertype);
                            if (usertype == Consts.ACCOUNT_TYPE[1]) {//环保人员
                                if (edatamenufragment == null) {
                                    edatamenufragment = new Enprodatamenufragment();
                                }
                                transaction.replace(R.id.idfragment, edatamenufragment);
                            } else if (usertype == Consts.ACCOUNT_TYPE[0])//管理员
                                {
                                    Log.i("kkk8199","mdatamenufragment="+mdatamenufragment);
                                    if (mdatamenufragment == null) {
                                        mdatamenufragment = new Maprodatamenufragment();
                                    }
                                    transaction.replace(R.id.idfragment, mdatamenufragment);
                                    syncdata();
                                }
                            else {//维护员
                              /*  if (mdatamenufragment == null) {
                                    mdatamenufragment = new Enprodatamenufragment();
                                }*/
                            }

                        }
                        break;
                    case 3:
                        if (status != 3) {
                            settitlesvisibile(false);
                            status=3;
                         //   project_secondtextview.setText(R.string.projectadd);
                            if (manageraddproject == null) {
                                manageraddproject=new ManagerAddproject();
                            }
                            transaction.replace(R.id.idfragment, manageraddproject);
                            syncdata();
                        }
                        break;

                    case 4://项目信息修改
                        if (status != 4) {
                            settitlesvisibile(false);
                            status=4;
                           // project_secondtextview.setText(R.string.projectinfomodify);
                            if (managermodifyproject == null) {
                                managermodifyproject=new ManagerModifyproject();
                            }
                            transaction.replace(R.id.idfragment, managermodifyproject);
                            syncdata();
                        }
                        break;
                        /*****设备信息修改*/
                    case 5:
                        if (status != 5) {
                            settitlesvisibile(false);
                            status=5;
                            if (managermodifydeviceinfo == null) {
                                managermodifydeviceinfo=new ManagerModifydeviceinfo();
                            }
                            transaction.replace(R.id.idfragment, managermodifydeviceinfo);
                            syncdata();
                        }
                        break;
    /*                case 6:
                        if (status != 6) {
                            settitlesvisibile(false);
                            status=6;
                        //    project_secondtextview.setText(R.string.workermodify);
                            if (managermodifymainercerinfo == null) {
                                managermodifymainercerinfo=new ManagerModifymainercerinfo();
                            }

                            transaction.replace(R.id.idfragment, managermodifymainercerinfo);
                            syncdata();
                        }
                        break;*/

                    case 6:
                        if (status != 6) {
                            settitlesvisibile(false);
                            status=6;
                        //    project_secondtextview.setText(R.string.findinfo);
                            if (mmanagerauditing == null) {
                                mmanagerauditing=new ManagerAuditing();
                            }
                            transaction.replace(R.id.idfragment, mmanagerauditing);
                            syncdata();
                        }
                        break;

                }
                // 事务提交
                transaction.commit();
            //    Log.i("kkk8199","status1111="+status);
            }
        });

    }


/************默认显示*****************/
    private void setDefaultFragment()
    {
        Log.i("kkk8199","into setdefaultfragment");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        einfomenufragment = new Enproinfomenufragment();
        transaction.replace(R.id.idfragment, einfomenufragment);
        transaction.commit();
    }

    private ShowProject findProject(long projectid){
        for(int i=0;i<=lstproject.size();i++){
            if(lstproject.get(i).getId()==projectid)

                return  lstproject.get(i);
        }
        return null;
    }


     private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                if (msg.obj != null && msg.obj instanceof String) {
                    Log.i("kkk8199", "msg.obj=" + msg.obj);
                    NotShowfloatwindow();
                    nowproject=findProject((long)msg.arg2);
                    project_firsttextview.setText(msg.obj.toString()+"-"+nowproject.getProjectName());
                    syncdata();
                    return;
                }
            }
        }
    };

    private void syncdata(){
        if(nowproject!=null) {

            if(nowproject.getType()==ShowProject.PROJ_TYPE_SUNPOWER)//太阳能
                envirmentsecondtitle.setText(R.string.firsttitile);
            else
                envirmentsecondtitle.setText(R.string.firsttitile1);//智能运维
            switch (status) {
                case 0:
                    Log.i("kkk8199","nowproject.getProjectName()="+nowproject.getProjectName());
                    einfomenufragment.SetEnproinfoproject(nowproject);
                    break;
                case 1:
                    break;
                case 2:
                    Log.i("kkk8199","into ok!");
                    mdatamenufragment.setMaprodataproject(this,nowproject,usertype);
                    break;
                case 3:
                    Log.i("kkk8199","into  manageraddproject.setManagerAddproject");
                    manageraddproject.setManagerAddproject(this);
                    envirmentsecondtitle.setText(R.string.projectadd);
                    break;
                case 4:
                    Log.i("kkk8199","into  manageraddproject.setManagerAddproject");
                    managermodifyproject.setManagerModifyproject(this,lstproject);
                    envirmentsecondtitle.setText(R.string.projectinfomodify);
                    break;
                case 5:
                    managermodifydeviceinfo.setManagerModifydeviceinfo(this,lstproject);
                    envirmentsecondtitle.setText(R.string.engineerinfomodify);
                    break;
                case 6:
                    mmanagerauditing.setManagerauditingregisterview(this);
                    envirmentsecondtitle.setText(R.string.registerfindinfo);
                    break;
                default:
                    break;
            }
        }
    }


    /************创建悬浮式窗口**********/
     private void createView(){
         mytree = new MyTreewidget(this);
         mytree.treewidgetinit(lstproject,handler);
         Showfloatwindow();
     }

    /******************显示悬浮窗口*********
     *
     */
    private void Showfloatwindow() {
            //获取WindowManager
            WindowManager wm=(WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            //设置LayoutParams(全局变量）相关参数
            WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
            wmParams.type=WindowManager.LayoutParams.TYPE_PHONE; //设置window type
            wmParams.format=PixelFormat.RGBA_8888; //设置图片格式，效果为背景透明RGBA_8888
            //设置Window flag
            wmParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity=Gravity.LEFT | Gravity.TOP; //调整悬浮窗口至左上角
            wmParams.alpha=0.8f;
            int width=wm.getDefaultDisplay().getWidth();
            int heigth=wm.getDefaultDisplay().getHeight();
            Log.i("kkk8199", width + "");
            Log.i("kkk8199", heigth + "");

            //以屏幕左上角为原点，设置x、y初始值
            wmParams.x=width / 3;
            wmParams.y=5 * heigth / 32;
            //设置悬浮窗口长宽数据
            wmParams.width=width * 2 / 3;
            wmParams.height=3 * heigth / 4;
            //显示myFloatView图像
            wm.addView(mytree, wmParams);
            firsttime=false;

    }
    /******************隐藏悬浮窗口*********
     *
     */
    private void NotShowfloatwindow(){
            if(!firsttime) {
                WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                wm.removeView(mytree);
                firsttime = true;
            }
    }


    public static Handler musermainhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if( msg.arg1 == 1 ){//网络断开
                if( LoginActivity.showNetWorkFailed(usermaincontext) == false ){
                    return;
                }
                else{
                    LoginActivity.showNetDataFailedTip(usermaincontext);
                }

            }else{//网络正常
                if( msg.arg2 == 0 ) {//业务应答失败原因
                        if (msg.obj != null && msg.obj instanceof String) {
                            Toast.makeText(usermaincontext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                else {//业务应答成功
                    if (msg.arg2 == MSG_ADD_PROJECT) {//新增项目
                        if (msg.obj != null && msg.obj instanceof String) {
                            Toast.makeText(usermaincontext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else if (msg.arg2 == MSG_REGISTER) {//注册审核列表显示
                        Log.i("kkk8199","into MSG_REGISTER");
                        List<ShowAccount> listauditing=(List<ShowAccount>) msg.obj;
                        mmanagerauditing.setMangerauditingregisterlist(listauditing);
                    } else if (msg.arg2 == MSG_REGISTER_REJECT) {//审核通过或者拒绝
                        Log.i("kkk8199","into MSG_REGISTER_REJECT");
                        mmanagerauditing.setMangerauditingsuccess();
                        Toast.makeText(usermaincontext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    }
                    else if (msg.arg2 == MSG_FIND_USER) {//找回审核列表显示
                        Log.i("kkk8199","into MSG_FIND_USER");
                        List<ShowAccount> listauditing=(List<ShowAccount>) msg.obj;
                        mmanagerauditing.setManagerauditingfinduserlist(listauditing);
                    }
               /*     else if (msg.arg2 == MSG_QUERY_MAINEANCERINFO) {//查询维护人员信息
                        Log.i("kkk8199","into MSG_QUERY_MAINEANCERINFO");
                        List<ShowAccount> listauditing=(List<ShowAccount>) msg.obj;
                        managermodifymainercerinfo.setManagermodifymainercerlistinfo(listauditing);
                    }*/
                    else if (msg.arg2 == MSG_MODIGY_RUNTIMING) {//修改定时运行
                        Log.i("kkk8199","into MSG_MODIGY_RUNTIMING");
                        Toast.makeText(usermaincontext,"修改成功", Toast.LENGTH_LONG).show();
                    }
                    else if (msg.arg2 == MSG_QUERY_RUNTIMING) {//查询定时运行模式
                        Log.i("kkk8199","into MSG_MODIGY_RUNTIMING");
                        ProjectWorkingMode mprojectworkmode = (ProjectWorkingMode)msg.obj;
                        mdatamenufragment.setnowprojectworkmodeinfo(mprojectworkmode);
                    }
                    else if (msg.arg2 == MSG_MODIFY_PROJECT) {//项目修改
                        Log.i("kkk8199","into MSG_MODIFY_PROJECT");
                        Toast.makeText(usermaincontext, "修改成功", Toast.LENGTH_LONG).show();
                    }
                    else if (msg.arg2 == MSG_QUERY_DEVICES) {//项目中设备查询
                        Log.i("kkk8199","into MSG_QUERY_DEVICES");
                        List<ShowDevices> listdevices = ( List<ShowDevices>)msg.obj;
                        mdatamenufragment.updatedevicesadapter(listdevices);
                    }
                }

            }
            }
    };



    @Override
    public void onClick(View v) {
       Log.i("kkk8199","v11111.getId()="+v.getId());
        switch (v.getId()) {
            case R.id.imageButton:
                this.finish();
                return ;
            case R.id.envirmentmenubutton:
                if(firsttime) {
                    Showfloatwindow();
                }
                else {
                    NotShowfloatwindow();
                }
                return;
        }
    }
}

