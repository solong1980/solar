package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.basicclass.ShowProjectinfo;

/**
 * Created by kkk8199 on 2018/1/21.
 */

public class Enproinfomenufragment extends Fragment{
    private View convertView;
    private TextView project_name;
    private TextView solargive;
    private TextView designprocess;
    private TextView projectaddress;
    private TextView emissionstandard;
    private TextView standarddischarge;
    private ShowProject nowlsproject;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        convertView= inflater.inflate(R.layout.environmentermain_projectinfo, container, false);
        initview();
        return convertView;
    }


    public void initview(){
        project_name= (TextView)convertView.findViewById(R.id.project_name);
        projectaddress= (TextView)convertView.findViewById(R.id.project_address);
        designprocess= (TextView)convertView.findViewById(R.id.design_process);
        emissionstandard= (TextView)convertView.findViewById(R.id.emissionstandard);
        standarddischarge= (TextView)convertView.findViewById(R.id.standarddischarge);
        solargive= (TextView)convertView.findViewById(R.id.solargive);
    }

    public void SetEnproinfoproject(ShowProject lsproject){
        nowlsproject = lsproject;
        ShowProjectinfo ShowProjectinfo = new ShowProjectinfo();
        ShowProjectinfo.setId(lsproject.getId());
        SupplyConnectAPI.getInstance().getgeneratingcapacity(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,ShowProjectinfo);
    }

    public void setgeneratingcapacity(ShowProjectinfo mshowproject){
        project_name.setText(nowlsproject.getProjectName());
        ParseAddress parseaddress=ParseAddress.getInstance();
        String address = parseaddress.queryid(nowlsproject.getLocationId());
        projectaddress.setText(address);
        standarddischarge.setText(Integer.toString(nowlsproject.getState())+"D/T");
    //    Log.i("kkk8199","designprocess="+designprocess+"lsproject.getCapability()="+mshowproject.getCapability());
        designprocess.setText(Integer.toString(nowlsproject.getCapability())+"D/T");
        emissionstandard.setText(Integer.toString(nowlsproject.getEmissionStandards())+"D/T");
        Long totalpchg = mshowproject.getprojectTotalPChg();
        String value = String.valueOf((float)(totalpchg/1000))+"°";
        solargive.setText(value);
    }
}
