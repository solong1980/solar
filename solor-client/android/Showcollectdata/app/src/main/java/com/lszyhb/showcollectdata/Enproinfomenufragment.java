package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.os.Bundle;
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
    private   View convertView;
    private TextView solargive;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        convertView= inflater.inflate(R.layout.environmentermain_projectinfo, container, false);
        return convertView;
    }




    public void SetEnproinfoproject(ShowProject lsproject){
        TextView project_name= (TextView)convertView.findViewById(R.id.project_name);
        TextView projectaddress= (TextView)convertView.findViewById(R.id.project_address);
        TextView designprocess= (TextView)convertView.findViewById(R.id.design_process);
        TextView emissionstandard= (TextView)convertView.findViewById(R.id.emissionstandard);
        TextView standarddischarge= (TextView)convertView.findViewById(R.id.standarddischarge);
        solargive= (TextView)convertView.findViewById(R.id.solargive);
        project_name.setText(lsproject.getProjectName());
        ParseAddress parseaddress=ParseAddress.getInstance();
        String address = parseaddress.queryid(lsproject.getLocationId());
        projectaddress.setText(address);
        // Log.i("kkk8199","designprocess="+designprocess+"lsproject.getCapability()="+lsproject.getCapability());
        designprocess.setText(Integer.toString(lsproject.getCapability())+"D/T");
        emissionstandard.setText(Integer.toString(lsproject.getEmissionStandards())+"D/T");
        standarddischarge.setText(Integer.toString(lsproject.getState()));
        ShowProjectinfo ShowProjectinfo = new ShowProjectinfo();
        ShowProjectinfo.setId(lsproject.getId());
        SupplyConnectAPI.getInstance().getgeneratingcapacity(UserMainActivity.musermainsocket,
                UserMainActivity.musermainhandler,ShowProjectinfo);
//        solargive.setText(lsproject.getStreet());
    }

    public void setgeneratingcapacity(ShowProjectinfo mshowproject){
        Long totalpchg = mshowproject.getprojectTotalPChg();
        String value = String.valueOf((float)(totalpchg/1000))+"°";
        solargive.setText(value);
    }
}
