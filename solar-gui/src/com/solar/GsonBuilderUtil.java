package com.solar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.entity.SoRunningData;

import java.text.DateFormat;

/**
 * Created with antnest-platform User: chenyuan Date: 12/22/14 Time: 4:33 PM
 */
public class GsonBuilderUtil {

	public static Gson create() {
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
		gb.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
		Gson gson = gb.create();
		return gson;
	}
	
	public static void main(String[] args) {
		
		Gson gson = GsonBuilderUtil.create();
		String a = "[{\"ain1\":\"0\",\"ain2\":\"0\",\"ain3\":\"0\",\"altitude\":\"30.026174\",\"breakTime\":1517443654000,\"createTime\":1516849921000,\"fmid\":\"07\",\"ichg\":\"0\",\"id\":269440,\"ild1\":\"5\",\"ild2\":\"5\",\"ild3\":\"6\",\"ild4\":\"4\",\"level\":\"38\",\"longitude\":\"114.128143\",\"pchg\":\"0\",\"pdis\":\"202162\",\"req\":\"01,199E804C,07,337,0,243,38,0,202162,5,5,6,4,2,0,0,0,81,20180125031159,30.026174,114.128143\",\"safeTime\":-622535,\"stat\":\"81\",\"temp\":\"2\",\"utcTime\":\"20180125031159\",\"uuid\":\"199E804C\",\"vbat\":\"243\",\"vssun\":\"337\"}]";
		
		SoRunningData[] fromJson = gson.fromJson(a, SoRunningData[].class);
		System.out.println(fromJson[0].getBreakTime());
		System.out.println(fromJson[0].getCreateTime());
		
	}
}