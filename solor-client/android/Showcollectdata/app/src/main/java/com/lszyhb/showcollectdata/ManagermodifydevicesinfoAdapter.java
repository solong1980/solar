package com.lszyhb.showcollectdata;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowProject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkk8199 on 2/28/18.
 */

public class ManagermodifydevicesinfoAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShowDevices> mImgIds;
    private LayoutInflater mInflater;

    public ManagermodifydevicesinfoAdapter(Context ctx, List<ShowDevices> ImgIds) {
        mContext=ctx;
        this.mImgIds=ImgIds;
        mInflater=LayoutInflater.from(mContext);
        Log.i("kkk8199", "mImgIds=" + mImgIds);

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

    private class ModifydeviceinfoHolder {
        Button modifydeviceinfo_del;
        TextView manager_modifydeviceinfo_uuid;
    }

    //* 得到View *//*
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
         final ModifydeviceinfoHolder modifydeviceinfoholder;
        if (convertView == null) {
          //  Log.i("kkk8199","postion="+position);
            convertView=mInflater.inflate(R.layout.manager_modifydeviceinfo_item, null);
            modifydeviceinfoholder=new ModifydeviceinfoHolder();
            Button manager_modifydeviceinfo_del=convertView.findViewById(R.id.manager_modifydeviceinfo_del);
            TextView manager_modifydeviceinfo_uuid= convertView.findViewById(R.id.manager_modifydeviceinfo_uuid);
            modifydeviceinfoholder.manager_modifydeviceinfo_uuid=manager_modifydeviceinfo_uuid;
            modifydeviceinfoholder.modifydeviceinfo_del=manager_modifydeviceinfo_del;
            convertView.setTag(modifydeviceinfoholder);
        } else {
            modifydeviceinfoholder=(ModifydeviceinfoHolder) convertView.getTag();
        }
        //Log.i("kkk8199","postion="+position+"mImgIds.get(position).getDevNo()="+mImgIds.get(position).getDevNo());

        if(mImgIds.get(position).getDevNo()!=null)
            modifydeviceinfoholder.manager_modifydeviceinfo_uuid.setText(mImgIds.get(position).getDevNo());
        else
            modifydeviceinfoholder.manager_modifydeviceinfo_uuid.setText("");

        modifydeviceinfoholder.modifydeviceinfo_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Log.i("kkk8199","del this view finalConvertView "
                        +"position="+position+"modifydeviceinfoholder="+modifydeviceinfoholder);*/
                mImgIds.remove(position);
                notifyDataSetChanged();
            }
        });
        modifydeviceinfoholder.manager_modifydeviceinfo_uuid.setOnKeyListener(new EditText.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if(event.getAction()==KeyEvent.ACTION_UP) {
                    EditText edittext=(EditText) v;
                    mImgIds.get(position).setDevNo(edittext.getText().toString());
                }
                return false;
            }
        });

        return convertView;
    }

    public  List<ShowDevices> GetnowItem(ShowProject mshowproject) {
        /********************/
        for(int i=0 ; i<mImgIds.size(); i++){
            mImgIds.get(i).setLocationId(mshowproject.getLocationId());
            mImgIds.get(i).setProjectId(mshowproject.getId());
            mImgIds.get(i).setMsg("success");
            mImgIds.get(i).setRetCode(0);
            mImgIds.get(i).setSw0((short)0);
            mImgIds.get(i).setSw1((short)0);
            mImgIds.get(i).setSw2((short)0);
            mImgIds.get(i).setSw3((short)0);
            mImgIds.get(i).setSw4((short)0);
            mImgIds.get(i).setSw5((short)0);
            mImgIds.get(i).setSw6((short)0);
            mImgIds.get(i).setSw7((short)0);
        }
        return  mImgIds;
    }

}
