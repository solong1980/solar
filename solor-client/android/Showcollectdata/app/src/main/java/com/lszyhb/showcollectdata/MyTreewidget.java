package com.lszyhb.showcollectdata;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lszyhb.addresswidget.model.AddressDtailsEntity;
import com.lszyhb.addresswidget.model.AddressModel;
import com.lszyhb.addresswidget.utils.JsonUtil;
import com.lszyhb.addresswidget.utils.Utils;
import com.lszyhb.basicclass.ShowProject;
import com.lszyhb.treewidget.Dept;
import com.lszyhb.treewidget.Node;
import com.lszyhb.treewidget.NodeHelper;
import com.lszyhb.treewidget.NodeTreeAdapter;
import com.lszyhb.treewidget.addNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by kkk8199 on 12/16/17.
 */

public class MyTreewidget extends RelativeLayout {

    private ListView mListView;
    private ListView mListView1;
    List<AddressDtailsEntity.ProvinceEntity> province;
    String provinceStr;
    String city;
    String arae;
    private static Context mcontext;
   // private volatile static MyTreewidget instance;
   // private List<ShowProject> lstproject;

    public MyTreewidget(Context context) {
        super(context);
        mcontext =context;
        final LayoutInflater inflater = LayoutInflater.from(mcontext);
        inflater.inflate(R.layout.mytree,this,true);
        mListView = findViewById(R.id.id_tree);
        mListView1 = findViewById(R.id.id_tree1);
    }

    public MyTreewidget(Context context,AttributeSet attrs) {
        super(context,attrs);
        mcontext =context;
        final LayoutInflater inflater = LayoutInflater.from(mcontext);
        inflater.inflate(R.layout.mytree,this,true);
        mListView = (ListView)findViewById(R.id.id_tree);
        mListView1 = findViewById(R.id.id_tree1);
    }


    public void treewidgetinit(List<ShowProject> oldvlstproject, Handler handler){
        List<ShowProject> vlstproject = new ArrayList<>() ;
        List<ShowProject> vlstproject1 = new ArrayList<>() ;
        LinkedList<Node> mLinkedList = new LinkedList<>();
        LinkedList<Node> mLinkedList1= new LinkedList<>();
        int i;
        for(i=0;i<oldvlstproject.size();i++){
     //       Log.i("kkk8199","oldvlstproject.get(i).getType()="+oldvlstproject.get(i).getType());
            if(oldvlstproject.get(i).getType()==ShowProject.ES_A){
                vlstproject.add(oldvlstproject.get(i));
            }
            else if(oldvlstproject.get(i).getType()==ShowProject.ES_B){
                vlstproject1.add(oldvlstproject.get(i));
            }
        }
        NodeTreeAdapter mAdapter = new NodeTreeAdapter(mcontext,mListView,mLinkedList,handler);//太阳能
        mListView.setAdapter(mAdapter);
        NodeTreeAdapter mAdapter1 = new NodeTreeAdapter(mcontext,mListView1,mLinkedList1,handler);//智能运维系统
        mListView1.setAdapter(mAdapter1);
        initneedData(mcontext);
        initData(mAdapter,mLinkedList,vlstproject);
        initData(mAdapter1,mLinkedList1,vlstproject1);
    }

    private void initData(NodeTreeAdapter mAdapter,LinkedList<Node> mLinkedList,List<ShowProject> vlstproject){
        List<Node> data = new ArrayList<>();
        addOne(data,vlstproject);
        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();
    }


    private void initneedData(Context mcontext) {

        String   address  = Utils.readAssert(mcontext, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity addressdata = model.Result;
            province = addressdata.ProvinceItems.Province;
        }
    }

    /**********查询是哪个省哪个市,方便展开**********/
    public void queryid(String id){
   //     Log.i("kkk8199","into query id="+id+"province.size()="+province.size());
        for (int i = 0; i < province.size(); i++) {
            AddressDtailsEntity.ProvinceEntity provinces = province.get(i);
       //     Log.i("kkk8199","provinces1="+provinces.Id);
            if (provinces != null) {
                if(!provinces.Id.substring(0,2).equals(id.substring(0,2)))
                    continue;
           //     Log.i("kkk8199","provinces="+provinces.Id+"provinces.name="+provinces.Name);
                List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = provinces.City;

                for (int j = 0; j < citys.size(); j++) {
                    AddressDtailsEntity.ProvinceEntity.CityEntity cityEntity = citys.get(j);
                    if (cityEntity != null) {
                        if(!cityEntity.Id.substring(0,4).equals(id.substring(0,4)))
                            continue;
                        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> areas = cityEntity.Area;
                        for (int k = 0; k < areas.size(); k++) {
                            AddressDtailsEntity.ProvinceEntity.AreaEntity areaEntity = areas.get(k);
                            if (areaEntity != null && areaEntity.Id.equals(id)) {
                                arae=areaEntity.Name;
                            }
                        }
                        city=cityEntity.Name;
                    }
                }
                provinceStr = provinces.Name;
            }
        }
      //  Log.i("kkk8199","arae="+arae+"city="+city+"provinceStr="+provinceStr);
    }

    private void addOne(List<Node> data,List<ShowProject> vlstproject){
        List<addNode> proviceslist=new  ArrayList<>();
        List<addNode> citylist =new  ArrayList<>();
        List<addNode> arealist = new  ArrayList<>();
        int getprojectnum;
        int maxnum=0;
        int i=0;
        int provicesnum=1;
        int citynum=100;
        int areanum=10000;
        int projectnum=100000;
        try {
            maxnum =vlstproject.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(getprojectnum=0;getprojectnum<maxnum;getprojectnum++){
            String id =vlstproject.get(getprojectnum).getLocationId();
            String project_name = vlstproject.get(getprojectnum).getProjectName();
            Long project_id = vlstproject.get(getprojectnum).getId();
            int reacordprovicesnum=0;
            int reacordcitynum=0;
            int reacordareanum=0;
  //         Log.i("kkk8199","id="+id+"project_name="+project_name+"proviceslist.size()="+proviceslist.size()
  //                 +"project_id="+project_id);
            queryid(id);
            for (i=0; i<proviceslist.size(); i++) {
                if (proviceslist.get(i).getname().equals(provinceStr)) {
                    reacordprovicesnum=proviceslist.get(i).getId();
                    break;
                }
            }
  //          Log.i("kkk8199","i="+i+"proviceslist.size()="+proviceslist.size()+"reacordprovicesnum="+reacordprovicesnum);
            if(i==proviceslist.size()){
                provicesnum++;
                addNode nodevalue=new addNode();
                nodevalue.setname(provinceStr);
                nodevalue.setId(provicesnum);
                proviceslist.add(nodevalue);
                data.add((new Dept(provicesnum,1,project_id,provinceStr,provinceStr)));
            }
            for (i=0; i <citylist.size(); i++) {
                if (citylist.get(i).getname().equals(city)) {
                    reacordcitynum=citylist.get(i).getId();
                    break;
                }
            }
    //      Log.i("kkk8199","i="+i+"citylist.size()="+citylist.size());
            if(i==citylist.size()){
                citynum++;
                addNode nodecity=new addNode();
                nodecity.setname(city);
                nodecity.setId(citynum);
                citylist.add(nodecity);
       //         Log.i("kkk8199","citynum="+citynum +"reacordprovicesnum="+reacordprovicesnum+"provicesnum="+provicesnum);
                if(reacordprovicesnum!=0)
                    data.add((new Dept(citynum,reacordprovicesnum,project_id,provinceStr+"-"+city,city)));
                else
                    data.add((new Dept(citynum,provicesnum,project_id,provinceStr+"-"+city,city)));
            }

            for (i=0; i < arealist.size(); i++) {
                if (arealist.get(i).getname().equals(arae)) {
                    reacordareanum=arealist.get(i).getId();
                    break;
                }
            }
    //        Log.i("kkk8199","i="+i+"arealist.size()="+arealist.size());
            if(i==arealist.size()){
                areanum++;
                addNode nodearea=new addNode();
                nodearea.setname(arae);
                nodearea.setId(areanum);
                arealist.add(nodearea);
    //            Log.i("kkk8199","areanum="+areanum +"reacordcitynum="+reacordcitynum+"citynum="+citynum);
                if(reacordcitynum!=0)
                    data.add((new Dept(areanum,reacordcitynum,project_id,provinceStr+"-"+city+"-"+arae,arae)));
                else
                    data.add((new Dept(areanum,citynum,project_id,provinceStr+"-"+city+"-"+arae,arae)));
            }
  //          Log.i("kkk8199","projectnum="+projectnum + "reacordareanum="+reacordareanum
  //                  +"areanum="+areanum);
   //         Log.i("kkk8199","provinceStr="+provinceStr + "city="+city
   //                 +"arae="+arae+"project_name="+project_name);
            if(reacordareanum!=0)
                data.add((new Dept(projectnum,reacordareanum,project_id,provinceStr+"-"+city+"-"+arae,project_name)));
            else
                data.add((new Dept(projectnum,areanum,project_id,provinceStr+"-"+city+"-"+arae,project_name)));
            projectnum++;

        }

    }

}
