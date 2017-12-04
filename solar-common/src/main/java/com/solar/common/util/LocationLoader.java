package com.solar.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class LocationLoader {

	public static JSONObject loadLocation() {
		try {
			URL resource = Thread.currentThread().getContextClassLoader().getResource("locations");
			String file = resource.getPath();
			String read = Files.asCharSource(new File(file), Charsets.UTF_8).read();
			JSONObject fromJson = JsonUtilTool.fromJson(read, JSONObject.class);
			return fromJson;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(loadLocation());
	}
}
