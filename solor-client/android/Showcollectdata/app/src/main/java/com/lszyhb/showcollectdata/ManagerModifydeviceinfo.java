package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkk8199 on 2/28/18.
 */

public class ManagerModifydeviceinfo  extends Fragment implements View.OnClickListener{
    private View managermodifydevicesinfoview;
    private Context mcontext;
    private Spinner manager_modifydevicesinfoname;
    private TextView manager_modifydevicesinfotype; //不能修改
    private List<ShowProject> mlsproject;
    private Button manager_modifydevicesinfo_commit;
    private Button manager_modifydeviceinfo_add;
    private TextView manager_modifydevicesinfoaddress;
    private ManagermodifydevicesinfoAdapter modifydevicesinfoadapter;
    private GridView modifydevicesgridview;
    private List listadddeviceinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        managermodifydevicesinfoview= inflater.inflate(R.layout.manager_modifydeviceinfo, container, false);
        initfragment();
        //mshowproject= new ShowProject();
        return managermodifydevicesinfoview;
    }

    /**************初始化元素**********************/
    private void initfragment(){
        manager_modifydevicesinfoname=managermodifydevicesinfoview.findViewById(R.id.manager_modifydeviceinfoname);
        String[] numbers=new String[mlsproject.size()];
        for(int i=0;i<mlsproject.size();i++){
            numbers[i]=mlsproject.get(i).getProjectName();
        }

        modifydevicesgridview=managermodifydevicesinfoview.findViewById(R.id.modifydeviceinfogridview);


        manager_modifydevicesinfoaddress= managermodifydevicesinfoview.
                findViewById(R.id.manager_modifydeviceinfoaddress);
        final SpinnerAdapter projectnameadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, numbers);
        manager_modifydevicesinfoname.setAdapter(projectnameadapter);
        manager_modifydevicesinfotype = managermodifydevicesinfoview.findViewById(R.id.manager_modifydeviceinfotype);
        /******* 确认******/
        manager_modifydevicesinfo_commit=managermodifydevicesinfoview.
                findViewById(R.id.manager_modifydeviceinfo_commit);
        manager_modifydevicesinfo_commit.setOnClickListener(this);
        /*********新增********/
        manager_modifydeviceinfo_add=managermodifydevicesinfoview.
                findViewById(R.id.manager_modifydeviceinfo_add);
        manager_modifydeviceinfo_add.setOnClickListener(this);

        /****选中是哪个项目进行处理**********/
        manager_modifydevicesinfoname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowProject nowproject = mlsproject.get(position);
                Log.i("kkk8199","position="+position);
                String nowprojecttype;
                if(nowproject.getType()==ShowProject.PROJ_TYPE_SUNPOWER)
                    nowprojecttype=getResources().getString(R.string.firsttitile);
                else
                    nowprojecttype=getResources().getString(R.string.firsttitile1);
                manager_modifydevicesinfotype.setText(nowprojecttype);
                ParseAddress parseaddress=ParseAddress.getInstance();
                String address = parseaddress.queryid(nowproject.getLocationId());
                manager_modifydevicesinfoaddress.setText(address);
                if(mlsproject.get(position).getDevConfiures()!=null)
                    listadddeviceinfo=mlsproject.get(position).getDevConfiures();
                else
                    listadddeviceinfo=new ArrayList<String>();
                modifydevicesinfoadapter=new ManagermodifydevicesinfoAdapter(mcontext, listadddeviceinfo);
                modifydevicesgridview.setAdapter(modifydevicesinfoadapter);

            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setManagerModifydeviceinfo(Context context, List<ShowProject> lsproject) {
        Log.i("kkk8199", "into setManagerModifyproject"+"lsproject="+lsproject);
        mcontext = context;
        mlsproject = lsproject;
        Log.i("kkk8199","mcontext="+mcontext);
        // projectnamelist=new String[lsproject.size()];

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manager_modifydeviceinfo_add:
                ShowDevices mshowdevice=new ShowDevices();
                listadddeviceinfo.add(mshowdevice);
                Log.i("kkk8199","into mshowdevice");
                modifydevicesinfoadapter.notifyDataSetChanged();
                break;
            case R.id.manager_modifydeviceinfo_commit:
                break;
        }
    }
}
