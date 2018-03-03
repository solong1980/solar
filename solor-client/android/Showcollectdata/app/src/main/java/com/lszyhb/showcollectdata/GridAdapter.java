package com.lszyhb.showcollectdata;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkk8199 on 1/26/18.
 */

public class GridAdapter extends BaseAdapter {

        private Context mContext;
        private  String[] mImgIds;
        private boolean[] mCheckBoxManager;
        private LayoutInflater mInflater;
        private SparseBooleanArray selectArray = new SparseBooleanArray();

        public GridAdapter(Context ctx, String[] ImgIds) {
            mContext = ctx;
            this.mImgIds = ImgIds;
            mInflater = LayoutInflater.from(mContext);
            mCheckBoxManager = new boolean[mImgIds.length];
            Log.i("kkk8199","mImgIds="+mImgIds);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mImgIds.length;
        }

        @Override
        public Integer getItem(int position) {
            // TODO Auto-generated method stub
            return Integer.valueOf(mImgIds[position]);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

    class GridViewHolder{
        CheckBox checkBox;
    }

   //* 得到View *//*
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final GridViewHolder gridviewholder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.grid_item,null);
                gridviewholder = new GridViewHolder();
               // Log.i("kkk8199","convertView="+convertView+"position="+position);
                CheckBox  gridcheckbox =convertView.findViewById(R.id.grid_checkbox);
                gridviewholder.checkBox=gridcheckbox;
                convertView.setTag(gridviewholder);
            } else {
                gridviewholder =  (GridViewHolder)convertView.getTag();
            }
            gridviewholder.checkBox.setText(mImgIds[position]);
            gridviewholder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*    Log.i("kkk8199","position="+position+"isChecked="+isChecked
                                    +"buttonView="+buttonView);*/
                    mCheckBoxManager[position] = isChecked;
                 //   notifyDataSetChanged();//刷新界面信息
                }
            });
            gridviewholder.checkBox.setChecked(mCheckBoxManager[position]);
         return convertView;
         }

       public List<Integer> GetSelectitem(){
           List<Integer> mTimeList= new ArrayList<Integer>();
           for(int i=0;i<mImgIds.length;i++){
               if(mCheckBoxManager[i]){
                   mTimeList.add(i);
               }
           }
           return mTimeList;
       }

       public void SetSelectitem(int i,boolean isset){
           mCheckBoxManager[i]=isset;
       }

}

