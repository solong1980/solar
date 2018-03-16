package com.lszyhb.common;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by kkk8199 on 11/21/17.
 * JSON转换工具类
 *
 */
public class JsonUtilTool {
        /**
         * 对象转换成JSON字符串
         *
         * @param obj
         *            需要转换的对象
         * @return 对象的string字符
         */
        public static String toJson(Object obj) {
            Gson gson = GsonBuilderUtil.create();
           // Gson gson = new Gson();
            return gson.toJson(obj);
        }

    /**
     * JSON字符串转换成对象
     *
     * @param jsonString
     *            需要转换的字符串
     * @param type
     *            需要转换的对象类型
     * @return 对象
     */
    public static <T> T fromJson(String jsonString, Class<T> type) {
        Gson gson = GsonBuilderUtil.create();
        return gson.fromJson(jsonString,type);
    }

    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        Gson gson = GsonBuilderUtil.create();
        List<T> ts = gson.fromJson(jsonString, new TypeToken<List<T>>(){}.getType());
        Log.i("kkk8199","ts="+ts);
     //   System.out.print(ts);
        return ts;
    }

    /**
     * List<T>转json
     */
    public static String Listtojson(List<?> list) {
        Gson gson = GsonBuilderUtil.create();
        String ts = gson.toJson(list);
        Log.i("kkk8199","ts="+ts);
        return ts;
    }

}


