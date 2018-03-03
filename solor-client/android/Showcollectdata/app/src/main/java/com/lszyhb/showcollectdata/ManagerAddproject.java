package com.lszyhb.showcollectdata;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lszyhb.addresswidget.addresspopwindow;
import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowDevConfig;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowProject;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by kkk8199 on 2018/1/30.管理-项目新增,项目更改由于基本一致,直接用该类进行实现
 */

public class ManagerAddproject  extends Fragment implements View.OnClickListener{

    private View manageraddprojectview;
    private Context mcontext;
    private TextView manager_addprojectname;
    private Spinner manager_addprojecttype;
    private Button manager_addproject_commit;
    private TextView manager_addprojectdevicelist;
    private EditText manager_addprojectaddress;
    private EditText manager_addprojectstreet;
    private Spinner manager_addprojectdesign;
    private Spinner manager_addprojectputlevel;
    private TextView manager_addprojectmanername;
    private TextView manager_addprojectconnect;
    private ShowProject mshowproject;
    private ProjectWorkingMode mprojectworkmode;
    private   addresspopwindow addresspopwindow0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        manageraddprojectview= inflater.inflate(R.layout.manager_addproject, container, false);
        initfragment();
        mshowproject= new ShowProject();
        return manageraddprojectview;
    }

    /**************初始化元素**********************/
    private void initfragment(){
        manager_addprojectname=manageraddprojectview.findViewById(R.id.manager_addprojectname);
        manager_addprojectmanername=manageraddprojectview.findViewById(R.id.manager_addprojectmanername);
        manager_addprojectconnect=manageraddprojectview.findViewById(R.id.manager_addprojectconnect);
        manager_addprojectstreet=manageraddprojectview.findViewById(R.id.manager_addprojectstreet);
      //  Log.i("kkk8199","manager_addprojectconnect="+manager_addprojectconnect);


        manager_addprojectaddress = manageraddprojectview.findViewById(R.id.manager_addprojectaddress);
        Log.i("kkk8199","manager_addprojectaddress="+manager_addprojectaddress);
        addresspopwindow0 =new addresspopwindow(mcontext);
        addresspopwindow0.setfragmentedittext(manageraddprojectview,R.id.manager_addprojectaddress);
        addresspopwindow0.init();
        manager_addprojectaddress.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                    addresspopwindow0.addressClick(v);
                } else {

                    // 此处为失去焦点时的处理内容
                }
            }
        });


        manager_addprojectdevicelist = manageraddprojectview.findViewById(R.id.manager_addprojectdevicelist);
        /******* 确认******/
        manager_addproject_commit=manageraddprojectview.
                findViewById(R.id.manager_addproject_commit);
        manager_addproject_commit.setOnClickListener(this);

        /*********项目类型*************/
        manager_addprojecttype = manageraddprojectview.findViewById(R.id.manager_addprojecttype);
        String[] numbers={getResources().getString(R.string.firsttitile),
                getResources().getString(R.string.firsttitile1)};
        final SpinnerAdapter projeccttypeadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, numbers);
        manager_addprojecttype.setAdapter(projeccttypeadapter);

        /*****设计处理量**********/
        manager_addprojectdesign  = manageraddprojectview.findViewById(R.id.manager_addprojectdesign);
        String[] designlevel={String.valueOf(Consts.CAPS[0])+"D/T", String.valueOf(Consts.CAPS[1])+"D/T",
                String.valueOf(Consts.CAPS[2])+"D/T", String.valueOf(Consts.CAPS[3])+"D/T",
                String.valueOf(Consts.CAPS[4])+"D/T", String.valueOf(Consts.CAPS[5])+"D/T"};
        final SpinnerAdapter designadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, designlevel);
        manager_addprojectdesign.setAdapter(designadapter);

        manager_addprojectdesign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
               // Log.i("kkk8199","position="+position);
                String showtext="";
                for(int i=0;i<Consts.devices.length;i++){
                    String devicename = Consts.devices[i];
                    int count = Consts.devicecountOpts[i][position];
                    showtext=showtext+devicename+":"+String.valueOf(count)+"/";
                }
                manager_addprojectdevicelist.setText(showtext);

            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /******** 排放标准*********/
        manager_addprojectputlevel  = manageraddprojectview.findViewById(R.id.manager_addprojectputlevel);
        String[] putlevel={String.valueOf(Consts.EMISSION_STANDARDS[0]+"D/T"),
                String.valueOf(Consts.EMISSION_STANDARDS[1]+"D/T")};
        final SpinnerAdapter putleveladapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, putlevel);
        manager_addprojectputlevel.setAdapter(putleveladapter);

    /*******默认定时的规则是全0*********/
        mprojectworkmode = new ProjectWorkingMode();
    }

    public void setManagerAddproject(Context context) {
        Log.i("kkk8199", "into setManagerAddproject");
        mcontext = context;
        Log.i("kkk8199","mcontext="+mcontext);
       // projectnamelist=new String[lsproject.size()];

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.manager_addproject_commit:
                List<ShowDevices> mnewshowdevconfig=null;
                mshowproject.setProjectName((manager_addprojectname.getText().toString()));
                mshowproject.setWorkerName((manager_addprojectmanername.getText().toString()));
                mshowproject.setWorkerPhone((manager_addprojectconnect.getText().toString()));
                mshowproject.setStreet((manager_addprojectstreet.getText().toString()));
                mshowproject.setDevConfigures(mnewshowdevconfig);
                mshowproject.setProjectWorkingMode(mprojectworkmode);
                int position = manager_addprojectputlevel.getSelectedItemPosition();
                mshowproject.setEmissionStandards(Consts.EMISSION_STANDARDS[position]);
                int position1 = manager_addprojectdesign.getSelectedItemPosition();
                mshowproject.setCapability(Consts.CAPS[position1]);
                int position2 = manager_addprojecttype.getSelectedItemPosition();
                mshowproject.setType(Consts.PROJECT_TYPE[position2]);
                mshowproject.setLocationId(addresspopwindow0.getnowid());

                SupplyConnectAPI.getInstance().addproject(UserMainActivity.musermainsocket,
                        UserMainActivity.musermainhandler,mshowproject);
                break;
        }
    }
}
