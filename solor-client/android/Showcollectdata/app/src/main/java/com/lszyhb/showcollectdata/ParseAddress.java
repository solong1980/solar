package com.lszyhb.showcollectdata;

import android.content.Context;
import android.util.Log;

import com.lszyhb.addresswidget.model.AddressDtailsEntity;
import com.lszyhb.addresswidget.model.AddressModel;
import com.lszyhb.addresswidget.utils.JsonUtil;
import com.lszyhb.addresswidget.utils.Utils;

import java.util.List;

/**
 * Created by kkk8199 on 2018/2/6.
 */

public class ParseAddress {
    public volatile static ParseAddress instance;
    List<AddressDtailsEntity.ProvinceEntity> province;

    public static ParseAddress getInstance() {
        if(instance ==null) {
            synchronized (ParseAddress.class) {
                instance = new ParseAddress();
            }
        }
        return instance;
    }

    public void initneedData(Context mcontext) {

        String   address  = Utils.readAssert(mcontext, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity addressdata = model.Result;
            province = addressdata.ProvinceItems.Province;
        }
    }

    /**********查询是哪个省哪个市**********/
    public String queryid(String id){
        //     Log.i("kkk8199","into query id="+id+"province.size()="+province.size());
        String provinceStr = null;
        String city=null;
        String arae=null;
        for (int i = 0; i < province.size(); i++) {
            AddressDtailsEntity.ProvinceEntity provinces = province.get(i);
            //     Log.i("kkk8199","provinces1="+provinces.Id);
            if (provinces != null) {
                if(!provinces.Id.substring(0,2).equals(id.substring(0,2)))
                    continue;
            //    Log.i("kkk8199","provinces="+provinces.Id+"provinces.name="+provinces.Name);
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
        if(provinceStr == null)
            return "";
        else if(city == null)
            return provinceStr;
        else if(arae == null)
            return provinceStr+city;
        else
            return provinceStr+city+arae;
    }
}
