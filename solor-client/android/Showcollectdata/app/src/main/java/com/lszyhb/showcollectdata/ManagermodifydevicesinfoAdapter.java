package com.lszyhb.showcollectdata;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkk8199 on 2/28/18.
 */

public class ManagermodifydevicesinfoAdapter extends BaseAdapter {
    private Context mContext;
    private  List mImgIds;
    private LayoutInflater mInflater;

    public ManagermodifydevicesinfoAdapter(Context ctx, List ImgIds) {
        mContext = ctx;
        this.mImgIds = ImgIds;
        mInflater = LayoutInflater.from(mContext);
        Log.i("kkk8199","mImgIds="+mImgIds);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mImgIds.size();
    }

    @Override
    public Integer getItem(int position) {
        // TODO Auto-generated method stub
        return Integer.valueOf((String) mImgIds.get(position));
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ModifydeviceinfoHolder{
        Button modifydeviceinfo_del;
    }

    //* 得到View *//*
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ModifydeviceinfoHolder modifydeviceinfoholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.manager_modifydeviceinfo_item,null);
            modifydeviceinfoholder = new ModifydeviceinfoHolder();
            Button  manager_modifydeviceinfo_del =convertView.findViewById(R.id.manager_modifydeviceinfo_del);
             modifydeviceinfoholder.modifydeviceinfo_del=manager_modifydeviceinfo_del;
            convertView.setTag(modifydeviceinfoholder);

        } else {
            modifydeviceinfoholder =  (ModifydeviceinfoHolder)convertView.getTag();
        }
        modifydeviceinfoholder.modifydeviceinfo_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Log.i("kkk8199","del this view finalConvertView "
                        +"position="+position);*/
               // finalConvertView.setVisibility(View.GONE);
                mImgIds.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}
