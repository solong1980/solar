package com.lszyhb.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

/**
 * Created by kkk8199 on 3/15/18.
 */

public class GsonBuilderUtil {

    public static Gson create() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
        gb.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        Gson gson = gb.create();
        return gson;
    }
}
