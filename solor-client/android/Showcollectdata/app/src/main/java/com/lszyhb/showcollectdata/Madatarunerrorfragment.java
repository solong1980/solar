package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lszyhb.basicclass.ShowProject;

/**
 * Created by kkk8199 on 1/22/18.
 */

public class Madatarunerrorfragment extends Fragment {

    private View convertView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.i("kkk8199","into Madatarunerrorfragment oncreateview");
        convertView =inflater.inflate(R.layout.mainenancerdata_runerror, container, false);
        Log.i("kkk8199","convertView111="+convertView);
        return convertView;
    }


    public void SetMadatarunerrorproject(ShowProject lsproject){

        Log.i("kkk8199","convertView222="+convertView);
        TextView solarpanelerror= (TextView)convertView.findViewById(R.id.solarpanelerror);
        TextView batteryerror= (TextView)convertView.findViewById(R.id.batteryerror);
        TextView fanrunerror= (TextView)convertView.findViewById(R.id.fanrunerror);
        TextView airpumperror= (TextView)convertView.findViewById(R.id.airpumperror);
        solarpanelerror.setVisibility(View.VISIBLE);
        batteryerror.setVisibility(View.VISIBLE);
        fanrunerror.setVisibility(View.VISIBLE);
        airpumperror.setVisibility(View.VISIBLE);
        Log.i("kkk8199","convertView="+convertView);
    }

}
