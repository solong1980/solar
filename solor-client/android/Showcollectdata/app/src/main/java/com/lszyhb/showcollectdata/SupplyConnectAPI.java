package com.lszyhb.showcollectdata;


import android.os.Handler;

import com.lszyhb.basicclass.ApkVersion;
import com.lszyhb.basicclass.AppType;
import com.lszyhb.basicclass.ConnectAPI;
import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.DevicesCollectData;
import com.lszyhb.basicclass.GenVCodeType;
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowAccountLocation;
import com.lszyhb.basicclass.ShowCommitBatch;
import com.lszyhb.basicclass.ShowDevConfig;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowPage;
import com.lszyhb.basicclass.ShowPageProject;
import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.basicclass.ShowProjectinfo;
import com.lszyhb.basicclass.ShowVCode;
import com.lszyhb.basicclass.UserInfo;
import com.lszyhb.common.JsonUtilTool;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kkk8199 on 11/17/17.
 */

public class SupplyConnectAPI {
    private volatile static SupplyConnectAPI instance;

    public SupplyConnectAPI() {

    }
    public static SupplyConnectAPI getInstance() {
        if (instance == null) {
            synchronized (SupplyConnectAPI.class) {
                if (instance == null) {
                    instance = new SupplyConnectAPI();
                }
            }
        }
        return instance;
    }
/*****登陆接口*******************/
    public void loginIn(ClientSocket msocket, Handler mhandler, UserInfo muserinfo){

        ShowAccount account = new ShowAccount();
        account.setAccount(muserinfo.userName);
        account.setPassword(muserinfo.pwd);
        String json = JsonUtilTool.toJson(account);
        msocket.sendandrecv(ConnectAPI.LOGIN_COMMAND, mhandler,json);
    }

/*************得到程序版本号接口**********/
    public void getprogramversion(ClientSocket msocket,Handler mhandler){
            ApkVersion appVersion = new ApkVersion();
            appVersion.setType(AppType.APK.type());
            String json = JsonUtilTool.toJson(appVersion);

        msocket.sendandrecv(ConnectAPI.APK_VERSION_QUERY_COMMAND, mhandler,json);
    }

    /*************得到验证码接口**********/
    public void setvertifycode(ClientSocket msocket,Handler mhandler,String phonenum,int type){
        ShowVCode vcode = new ShowVCode();
        if(type==0)
            vcode.setType(GenVCodeType.REGIEST.getType());
        else if(type==1)
            vcode.setType(GenVCodeType.ACCOUNT_FIND.getType());
        vcode.setPhone(phonenum);
        String json = JsonUtilTool.toJson(vcode);
         msocket.sendandrecv(ConnectAPI.VCODE_GET_COMMAND, mhandler,json);
    }

    /*************发送注册账户**********/
    public void setregisteruser(ClientSocket msocket,Handler mhandler,ShowAccount account){


        String json = JsonUtilTool.toJson(account);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_ADD_COMMAND, mhandler,json);
    }
/**************找回用户*****************/
    public void finduser(ClientSocket msocket,Handler mhandler,ShowAccount account){


        String json = JsonUtilTool.toJson(account);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_FINDBACK_COMMAND, mhandler,json);
    }

    /************查询项目列表*******************/
    public void queryproject(ClientSocket msocket, Handler mhandler, ShowPageProject showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.PROJECT_QUERY_COMMAND, mhandler,json);
    }


/************新增项目*******************/
    public void addproject(ClientSocket msocket, Handler mhandler, ShowProject showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.PROJECT_ADD_COMMAND, mhandler,json);
    }

    /************查询注册审核*******************/
    public void registeraudit(ClientSocket msocket, Handler mhandler, ShowPage showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_AUDIT_QUERY_COMMAND, mhandler,json);
    }

    /***********注册审核通过与否决*******************/
    public void registerauditpassorreject(ClientSocket msocket, Handler mhandler, ShowAccount showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_AUDIT_COMMAND, mhandler,json);
    }


    /************查询找回用户审核*******************/
    public void finduseraudit(ClientSocket msocket, Handler mhandler, ShowPage showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_FINDBACK_QUERY_COMMAND, mhandler,json);
    }

    /************找回用户审核通过与否决*******************/
    public void finduserpassorreject(ClientSocket msocket, Handler mhandler, ShowAccount showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_FINDBACK_AUDIT_COMMAND, mhandler,json);
    }

    /************查询运维人员*******************/
    public void querymainenancer(ClientSocket msocket, Handler mhandler, ShowPage showproject){

        String json = JsonUtilTool.toJson(showproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.ACCOUNT_QUERY_COMMAND, mhandler,json);
    }

    /************获取运行模式,得到定时运行的状态*******************/
    public void getruntime(ClientSocket msocket, Handler mhandler, ProjectWorkingMode projectworkingmode){

        String json = JsonUtilTool.toJson(projectworkingmode);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.GET_WORKING_MODE_COMMAND, mhandler,json);
    }


    /************定时功能更新*******************/
    public void modityruntime(ClientSocket msocket, Handler mhandler, ProjectWorkingMode projectworkingmode){

        String json = JsonUtilTool.toJson(projectworkingmode);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.WORKING_MODE_UPDATE_COMMAND, mhandler,json);
    }


    /************项目信息修改 *******************/
    public void modityprojectinfo(ClientSocket msocket, Handler mhandler, ShowProject mshowproject){

        String json = JsonUtilTool.toJson(mshowproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.PROJECT_UPDATE_COMMAND, mhandler,json);
    }

    /************查询运行数据*******************/
    public void queryrundata(ClientSocket msocket, Handler mhandler, DevicesCollectData collectdata){

        String json = JsonUtilTool.toJson(collectdata);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.DEVICES_RUNNINGDATA_COMMAND, mhandler,json);
    }

    /************查询项目中设备列表*******************/
    public void queryrdeviceslist(ClientSocket msocket, Handler mhandler, ShowDevConfig nowprojectdev){

        String json = JsonUtilTool.toJson(nowprojectdev);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.DEVICES_IN_PROJECT_COMMAND, mhandler,json);
    }

    /************设备信息修改*******************/
    public void modifydevices(ClientSocket msocket, Handler mhandler, ShowCommitBatch mshowcommitbatch){

        String json = JsonUtilTool.toJson(mshowcommitbatch);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.DEVICES_BATCH_COMMAND, mhandler,json);
    }

    /************项目启停*******************/
    public void controlprojectstartorstop(ClientSocket msocket, Handler mhandler,
                                          List<ShowDevices> listshowdevices){

       String json = JsonUtilTool.toJson(listshowdevices);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.PROJECT_DEVICES_CTRL_COMMAND, mhandler,json);
    }

    /************得到项目总发电量*******************/
    public void getgeneratingcapacity(ClientSocket msocket, Handler mhandler,
                                      ShowProjectinfo mshowproject){

        String json = JsonUtilTool.toJson(mshowproject);
        System.out.println(json);
        msocket.sendandrecv(ConnectAPI.PROJECT_CALC_PCHG_COMMAND, mhandler,json);
    }
}
