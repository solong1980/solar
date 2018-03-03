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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lszyhb.addresswidget.addresspopwindow;
import com.lszyhb.basicclass.Consts;
import com.lszyhb.basicclass.ProjectWorkingMode;
import com.lszyhb.basicclass.ShowDevConfig;
import com.lszyhb.basicclass.ShowDevices;
import com.lszyhb.basicclass.ShowProject;

import java.util.List;

/**
 * Created by kkk8199 on 2/27/18.管理-项目信息修改
 */

public class ManagerModifyproject  extends Fragment implements View.OnClickListener {

    private View managermodifyprojectview;
    private Context mcontext;
    private Spinner manager_modifyprojectname;
    private TextView manager_modifyprojecttype; //不能修改
    private Button manager_modifyproject_commit;
    private TextView manager_modifyprojectdevicelist;
    private TextView manager_modifyprojectaddress;
    private EditText manager_modifyprojectstreet;
    private Spinner manager_modifyprojectdesign;
    private Spinner manager_modifyprojectputlevel;
    private TextView manager_modifyprojectmanername;
    private TextView manager_modifyprojectconnect;
    private ShowProject mshowproject;
    private addresspopwindow addresspopwindow0;
    private List<ShowProject> mlsproject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        managermodifyprojectview= inflater.inflate(R.layout.manager_modifyproject, container, false);
        initfragment();
        mshowproject= new ShowProject();
        return managermodifyprojectview;
    }

    /**************初始化元素**********************/
    private void initfragment(){
        manager_modifyprojectname=managermodifyprojectview.findViewById(R.id.manager_modifyprojectname);
        String[] numbers=new String[mlsproject.size()];
        for(int i=0;i<mlsproject.size();i++){
            numbers[i]=mlsproject.get(i).getProjectName();
            Log.i("kkk8199","number="+numbers[i]);
        }

        final SpinnerAdapter projectnameadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, numbers);
        manager_modifyprojectname.setAdapter(projectnameadapter);

        manager_modifyprojecttype = managermodifyprojectview.findViewById(R.id.manager_modifyprojecttype);
        manager_modifyprojectmanername=managermodifyprojectview.findViewById(R.id.manager_modifyprojectmanername);
        manager_modifyprojectconnect=managermodifyprojectview.findViewById(R.id.manager_modifyprojectconnect);
        manager_modifyprojectstreet=managermodifyprojectview.findViewById(R.id.manager_modifyprojectstreet);
        //  Log.i("kkk8199","manager_addprojectconnect="+manager_addprojectconnect);

        manager_modifyprojectaddress = managermodifyprojectview.findViewById(R.id.manager_modifyprojectaddress);

        manager_modifyprojectdevicelist = managermodifyprojectview.findViewById(R.id.manager_modifyprojectdevicelist);
        /******* 确认******/
        manager_modifyproject_commit=managermodifyprojectview.
                findViewById(R.id.manager_modifyproject_commit);
        manager_modifyproject_commit.setOnClickListener(this);

        /*****设计处理量**********/
        manager_modifyprojectdesign  = managermodifyprojectview.findViewById(R.id.manager_modifyprojectdesign);
        String[] designlevel={String.valueOf(Consts.CAPS[0])+"D/T", String.valueOf(Consts.CAPS[1])+"D/T",
                String.valueOf(Consts.CAPS[2])+"D/T", String.valueOf(Consts.CAPS[3])+"D/T",
                String.valueOf(Consts.CAPS[4])+"D/T", String.valueOf(Consts.CAPS[5])+"D/T"};
        final SpinnerAdapter designadapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, designlevel);
        manager_modifyprojectdesign.setAdapter(designadapter);

        manager_modifyprojectdesign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                manager_modifyprojectdevicelist.setText(showtext);

            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /******** 排放标准*********/
        manager_modifyprojectputlevel  = managermodifyprojectview.findViewById(R.id.manager_modifyprojectputlevel);
        String[] putlevel={String.valueOf(Consts.EMISSION_STANDARDS[0]+"D/T"),
                String.valueOf(Consts.EMISSION_STANDARDS[1]+"D/T")};
        final SpinnerAdapter putleveladapter=new SpinnerAdapter(mcontext,
                android.R.layout.simple_spinner_item, putlevel);
        manager_modifyprojectputlevel.setAdapter(putleveladapter);


        /****选中是哪个项目进行处理**********/
        manager_modifyprojectname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowProject nowproject = mlsproject.get(position);
                Log.i("kkk8199","position="+position);
                String nowprojecttype;
                if(nowproject.getType()==ShowProject.PROJ_TYPE_SUNPOWER)
                    nowprojecttype=getResources().getString(R.string.firsttitile);
                else
                    nowprojecttype=getResources().getString(R.string.firsttitile1);
                manager_modifyprojecttype.setText(nowprojecttype);
                manager_modifyprojectmanername.setText(nowproject.getWorkerName());
                manager_modifyprojectconnect.setText(nowproject.getWorkerPhone());
                manager_modifyprojectstreet.setText(nowproject.getStreet());
                ParseAddress parseaddress=ParseAddress.getInstance();
                String address = parseaddress.queryid(nowproject.getLocationId());
                manager_modifyprojectaddress.setText(address);
                int capability= nowproject.getCapability();
                switch (capability){
                    case 5:
                        manager_modifyprojectdesign.setSelection(0);
                        break;
                    case 10:
                        manager_modifyprojectdesign.setSelection(1);
                        break;
                    case 20:
                        manager_modifyprojectdesign.setSelection(2);
                        break;
                    case 30:
                        manager_modifyprojectdesign.setSelection(3);
                        break;
                    case 50:
                        manager_modifyprojectdesign.setSelection(4);
                        break;
                    case 100:
                        manager_modifyprojectdesign.setSelection(5);
                        break;
                }
               // Log.i("kkk8199","capability="+capability);
                int emission = nowproject.getEmissionStandards();
                if(emission==Consts.EMISSION_STANDARDS[0])
                    manager_modifyprojectputlevel.setSelection(0);
                else
                    manager_modifyprojectputlevel.setSelection(1);
                Log.i("kkk8199","emission="+emission);
              //  manager_modifyprojectputlevel
            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void setManagerModifyproject(Context context, List<ShowProject> lsproject) {
        Log.i("kkk8199", "into setManagerModifyproject"+"lsproject="+lsproject);
        mcontext = context;
        mlsproject = lsproject;
        Log.i("kkk8199","mcontext="+mcontext);
        // projectnamelist=new String[lsproject.size()];

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.manager_modifyproject_commit:
                List<ShowDevices> mnewshowdevconfig=null;
                int nameposition = manager_modifyprojectname.getSelectedItemPosition();
                mshowproject.setProjectName(mlsproject.get(nameposition).getProjectName());
                mshowproject.setWorkerName((manager_modifyprojectmanername.getText().toString()));
                mshowproject.setWorkerPhone((manager_modifyprojectconnect.getText().toString()));
                mshowproject.setStreet((manager_modifyprojectstreet.getText().toString()));
                mshowproject.setDevConfigures(mnewshowdevconfig);
              //  mshowproject.setProjectWorkingMode(mprojectworkmode);
                int position = manager_modifyprojectputlevel.getSelectedItemPosition();
                mshowproject.setEmissionStandards(Consts.EMISSION_STANDARDS[position]);
                int position1 = manager_modifyprojectdesign.getSelectedItemPosition();
                mshowproject.setCapability(Consts.CAPS[position1]);
                mshowproject.setType(mlsproject.get(nameposition).getType());;
                mshowproject.setLocationId(mlsproject.get(nameposition).getLocationId());
                mshowproject.setId(mlsproject.get(nameposition).getId());

                SupplyConnectAPI.getInstance().modityprojectinfo(UserMainActivity.musermainsocket,
                        UserMainActivity.musermainhandler,mshowproject);
                break;
        }
    }
}
