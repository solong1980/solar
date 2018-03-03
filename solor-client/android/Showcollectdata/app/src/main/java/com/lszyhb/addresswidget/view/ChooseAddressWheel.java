package com.lszyhb.addresswidget.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lszyhb.addresswidget.adapter.AreaWheelAdapter;
import com.lszyhb.addresswidget.adapter.CityWheelAdapter;
import com.lszyhb.addresswidget.adapter.ProvinceWheelAdapter;
import com.lszyhb.addresswidget.model.AddressDtailsEntity;
import com.lszyhb.addresswidget.model.AddressModel;
import com.lszyhb.addresswidget.utils.JsonUtil;
import com.lszyhb.addresswidget.utils.Utils;
import com.lszyhb.addresswidget.view.listener.OnAddressChangeListener;
import com.lszyhb.addresswidget.view.wheelview.MyOnWheelChangedListener;
import com.lszyhb.addresswidget.view.wheelview.MyWheelView;
import com.lszyhb.showcollectdata.R;

import org.json.JSONStringer;

import java.util.LinkedList;
import java.util.List;



public class ChooseAddressWheel implements MyOnWheelChangedListener {

    MyWheelView provinceWheel;
    MyWheelView cityWheel;
    MyWheelView districtWheel;

    TextView confirm_button;
    TextView cancel_button;

    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;

    private List<AddressDtailsEntity.ProvinceEntity> province = null;

    private OnAddressChangeListener onAddressChangeListener = null;

    public ChooseAddressWheel(Activity context1) {
        this.context = context1;
        Log.i("kkk8199","context="+context);
        init();
    }

    private void init() {
        Log.i("kkk8199","context="+context );
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();
    }
    /****滚轮设置的默认值**/
    public void setdefault(){
        provinceWheel.setCurrentItem(16);//湖北省
        cityWheel.setCurrentItem(0);     //
        districtWheel.setCurrentItem(0); //

    }

    private void initView() {
        parentView = layoutInflater.inflate(R.layout.choose_city_layout, null);
        provinceWheel =  parentView.findViewById(R.id.province_wheel);
        cityWheel =      parentView.findViewById(R.id.city_wheel);
        districtWheel   =parentView.findViewById(R.id.district_wheel);
        confirm_button   =parentView.findViewById(R.id.confirm_button);
        cancel_button   =parentView.findViewById(R.id.cancel_button);

        provinceWheel.addChangingListener(this);
        cityWheel.addChangingListener(this);
        districtWheel.addChangingListener(this);

        confirm_button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {
                confirm();
            }

        });

        cancel_button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {
                cancel();
            }

        });

    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (Utils.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }

    private void bindData() {
        provinceWheel.setViewAdapter(new ProvinceWheelAdapter(context, province));
        updateCitiy();
       // updateDistrict();
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceWheel) {
            updateCitiy();//省份改变后城市和地区联动
        } else if (wheel == cityWheel) {
            updateDistrict();//城市改变后地区联动
        } else if (wheel == districtWheel) {
        }
    }


    private void updateCitiy() {
        int index = provinceWheel.getCurrentItem();

        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(index).City;

        if (citys != null && citys.size() > 0) {
//            Log.i("kkk8199","index="+index + "citys.size()="+citys.size() +"citys.get(0).City_id="
//                    + citys.get(0).Name);
            if(!citys.get(0).Name.equals("可选")) {
                AddressDtailsEntity.ProvinceEntity.CityEntity addcity;
                String newaddress="{Name:可选,City_id:000000,City:null}";
                addcity=JsonUtil.parseJson(newaddress, AddressDtailsEntity.ProvinceEntity.CityEntity.class);
                citys.add(0, addcity);
            //    Log.i("kkk8199", "citys1111111.size()=" + citys.size());
            }
            cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
            cityWheel.setCurrentItem(0);
            updateDistrict();
        }
    }

    private void updateDistrict() {
        int provinceIndex = provinceWheel.getCurrentItem();
        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(provinceIndex).City;
        int cityIndex = cityWheel.getCurrentItem();
        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> districts = citys.get(cityIndex).Area;

        if (districts != null && districts.size() > 0) {
            if(!districts.get(0).Name.equals("可选")){
                AddressDtailsEntity.ProvinceEntity.AreaEntity addarea;
                String newaddress ="{Name:可选,Area_id:000000}";
                addarea = JsonUtil.parseJson(newaddress,AddressDtailsEntity.ProvinceEntity.AreaEntity.class);
                districts.add(0,addarea);
            }
            districtWheel.setViewAdapter(new AreaWheelAdapter(context, districts));
            districtWheel.setCurrentItem(0);
        }

    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void setProvince(List<AddressDtailsEntity.ProvinceEntity> province) {
        this.province = province;
        bindData();
    }

    public void defaultValue(String provinceStr, String city, String arae)  {
      //  Log.i("kkk8199","into defaultValue"+provinceStr +"city="+city+"arae="+arae);
        if (TextUtils.isEmpty(provinceStr)) return;
        for (int i = 0; i < province.size(); i++) {
            AddressDtailsEntity.ProvinceEntity provinces = province.get(i);
            if (provinces != null && provinces.Name.equalsIgnoreCase(provinceStr)) {
                provinceWheel.setCurrentItem(i);
                if (TextUtils.isEmpty(city)) return;
                List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = provinces.City;

                for (int j = 0; j < citys.size(); j++) {
                    AddressDtailsEntity.ProvinceEntity.CityEntity cityEntity = citys.get(j);

                    if (cityEntity != null && cityEntity.Name.equalsIgnoreCase(city)) {

                        cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
                        cityWheel.setCurrentItem(j);
                        if (TextUtils.isEmpty(arae)) return;
                        List<AddressDtailsEntity.ProvinceEntity.AreaEntity> areas = cityEntity.Area;
                        for (int k = 0; k < areas.size(); k++) {
                            AddressDtailsEntity.ProvinceEntity.AreaEntity areaEntity = areas.get(k);
                            if (areaEntity != null && areaEntity.Name.equalsIgnoreCase(arae)) {
                                districtWheel.setViewAdapter(new AreaWheelAdapter(context, areas));
                                districtWheel.setCurrentItem(k);
                            }
                        }
                    }
                }
            }
        }
    }

  //  @OnClick(R.id.confirm_button)
    public void confirm() {
        if (onAddressChangeListener != null) {
            int provinceIndex = provinceWheel.getCurrentItem();
            int cityIndex = cityWheel.getCurrentItem();
            int areaIndex = districtWheel.getCurrentItem();

            String provinceName = null, cityName = null, areaName = null;
            String provinceid =null,  cityid =null , areaid =null;

            List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = null;
            if (province != null && province.size() > provinceIndex) {
                AddressDtailsEntity.ProvinceEntity provinceEntity = province.get(provinceIndex);
                citys = provinceEntity.City;
                provinceName = provinceEntity.Name;
                provinceid = provinceEntity.Id;
             //   Log.i("kkk8199","provinceName="+provinceName+"provinceid="+provinceid);

            }
            List<AddressDtailsEntity.ProvinceEntity.AreaEntity> districts = null;
            if (citys != null && citys.size() > cityIndex) {

                AddressDtailsEntity.ProvinceEntity.CityEntity cityEntity = citys.get(cityIndex);
                districts = cityEntity.Area;
                cityName = cityEntity.Name;
                cityid    = cityEntity.Id;
            }

            if (districts != null && districts.size() > areaIndex) {
                AddressDtailsEntity.ProvinceEntity.AreaEntity areaEntity = districts.get(areaIndex);
                areaName = areaEntity.Name;
                areaid   = areaEntity.Id;
            }
        //    onAddressChangeListener.onAddressChange(provinceName,cityName, areaName);
               onAddressChangeListener.onAddressChange(provinceName, provinceid,cityName,cityid, areaName,areaid);
        }
        cancel();
    }

   // @OnClick(R.id.cancel_button)
    public void cancel() {

        popupWindow.dismiss();
    }

    public void setOnAddressChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }
}