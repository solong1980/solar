package com.solar;

import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Created with antnest-platform
 * User: chenyuan
 * Date: 12/22/14
 * Time: 4:38 PM
 */
public class DateSerializer implements JsonSerializer<Date> {
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
    }
}