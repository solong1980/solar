package com.lszyhb.showcollectdata;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lszyhb.basicclass.DevicesCollectData;
import com.lszyhb.basicclass.ShowAccount;
import com.lszyhb.basicclass.ShowDevices;

import java.util.List;

/**
 * Created by kkk8199 on 3/2/18.
 */

public class MaprodeviceslistAdapter  extends BaseAdapter {
    private Context mContext;
    private List<ShowDevices> mImgIds;
    private LayoutInflater mInflater;
    private int type;

    public MaprodeviceslistAdapter(Context ctx, List<ShowDevices> ImgIds,int type) {
        mContext = ctx;
        this.mImgIds = ImgIds;
        mInflater = LayoutInflater.from(mContext);
        this.type = type;
        Log.i("kkk8199","mImgIds="+mImgIds);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mImgIds.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mImgIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class MaprodeviceslistHolder{
        TextView devicelist_textview;
    }

    //* 得到View *//*
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final MaprodeviceslistHolder maprodeviceslistholder;
        Log.i("kkk8199","position="+position+"(mImgIds.get(position).getDevNo())="+
                (mImgIds.get(position).getDevNo()));
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mainancerdata_devicelist_item,null);
            maprodeviceslistholder = new MaprodeviceslistHolder();
            TextView devicelist_textview =convertView.findViewById(R.id.devicelist_textview);
            devicelist_textview.setText((mImgIds.get(position).getDevNo()));
            maprodeviceslistholder.devicelist_textview=devicelist_textview;
            convertView.setTag(maprodeviceslistholder);

        } else {
            maprodeviceslistholder =  (MaprodeviceslistHolder)convertView.getTag();
        }
        maprodeviceslistholder.devicelist_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Log.i("kkk8199","into onclick ");
                DevicesCollectData mcollectdata= new DevicesCollectData();
                mcollectdata.setUuid(mImgIds.get(position).getDevNo());
                SupplyConnectAPI.getInstance().queryrundata(UserMainActivity.musermainsocket,
                        UserMainActivity.musermainhandler,mcollectdata);
                if(type==1)
                    Maprodatamenufragment.setvisibledeviceorinfo(false);
                else
                    Enprostatusmenufragment.setvisibledeviceorinfo(false);
            }
        });

        return convertView;
    }

}
