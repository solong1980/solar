package com.lszyhb.addresswidget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lszyhb.addresswidget.model.AddressDtailsEntity;
import com.lszyhb.addresswidget.model.AddressModel;
import com.lszyhb.addresswidget.utils.JsonUtil;
import com.lszyhb.addresswidget.utils.Utils;
import com.lszyhb.addresswidget.view.ChooseAddressWheel;
import com.lszyhb.addresswidget.view.listener.OnAddressChangeListener;
import com.lszyhb.showcollectdata.FinduserActivity;
import com.lszyhb.showcollectdata.R;
import com.lszyhb.showcollectdata.RegisterActivity;


public class addresspopwindow  implements OnAddressChangeListener {

    EditText chooseAddress;

    private ChooseAddressWheel chooseAddressWheel = null;
    private Activity mcontext;
    private String  nowid=null;
    private String   address =null;

    public addresspopwindow(FinduserActivity finduserActivity) {
        mcontext = finduserActivity;

    }

    public addresspopwindow(RegisterActivity registerActivity) {
        mcontext = registerActivity;
    }

    public addresspopwindow(Context context) {
        mcontext= (Activity) context;
      //  Log.i("kkk8199","mcontext1111="+mcontext);
    }

    public void setfragmentedittext( View editText ,int viewid){
        chooseAddress=editText.findViewById(R.id.manager_addprojectaddress);
       // Log.i("kkk8199","chooseAddress="+chooseAddress+"editText="+editText);
    }


    public void setedittext( EditText editText){
        chooseAddress=editText;
    //    Log.i("kkk8199","chooseAddress="+chooseAddress+"editText="+editText);
    }

    public String getnowid(){
        return nowid;
    }

    public void init() {
        initWheel();
        initData();
    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(mcontext);
        chooseAddressWheel.setOnAddressChangeListener(this);
    }

    private void initData() {

        String   address  = Utils.readAssert(mcontext, "address.txt");
    //    Log.i("kkk8199","address="+address);
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
            chooseAddress.setText(data.Province + " " + data.City + " " + data.Area);
       //     Log.i("kkk8199","data.ProvinceItems="+data.ProvinceItems +"data.ProvinceItems="+data.ProvinceItems.Province+"data.Area="+data.Area);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }

   // @OnClick(R.id.choose_address)
    public void addressClick(View view) {
        Utils.hideKeyBoard(mcontext);
      //   Log.i("kkk8199","into addressClick");
        chooseAddressWheel.setdefault();
        chooseAddressWheel.show(view);
    }

    public int getnowid(View view) {
            return Integer.parseInt(nowid);
    }

    @Override
    public void onAddressChange(String province, String provinceid, String city,String cityid,
                                String district,String districtid) {
        String content;
// /       Log.i("kk8199","provinceid="+provinceid);
        if(city.equals("可选")){
             content=province;
            nowid=provinceid;
        }
        else if(district.equals("可选")) {
            content=province + city;
            nowid=cityid;
        }
        else{
             content =province + city  + district;
             nowid=districtid;
        }
        chooseAddress.setText(content);
        chooseAddress.setSelection(content.length());
    }
}
