package com.solar;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Created with antnest-platform
 * User: chenyuan
 * Date: 12/22/14
 * Time: 4:39 PM
 */
public class DateDeserializer implements JsonDeserializer<java.util.Date> {

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
    }
}