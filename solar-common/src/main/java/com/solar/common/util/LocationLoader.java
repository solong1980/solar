package com.solar.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class LocationLoader {

	private static LocationLoader locationLoader = new LocationLoader();

	private JSONObject fromJson = null;

	private Map<String, String> locationIdFullNameMap = new HashMap<>();

	public String getLocationFullName(String locationId) {

		if (locationIdFullNameMap.isEmpty()) {
			synchronized (locationIdFullNameMap) {
				if (locationIdFullNameMap.isEmpty()) {
					JSONObject locations = loadLocation();
					JSONArray provinces = locations.getJSONArray("Province");
					for (int i = 0; i < provinces.size(); i++) {
						JSONObject province = provinces.getJSONObject(i);
						String provinceid = province.getString("Id");
						String provinceName = province.getString("Name");
						locationIdFullNameMap.put(provinceid, provinceName);
						JSONArray cities = province.getJSONArray("City");
						for (int j = 0; j < cities.size(); j++) {
							JSONObject city = cities.getJSONObject(j);
							String cityid = city.getString("Id");
							String cityName = city.getString("Name");
							String cityFullName = provinceName + " \\ " + cityName;
							locationIdFullNameMap.put(cityid, cityFullName);

							JSONArray areas = city.getJSONArray("Area");
							for (int k = 0; k < areas.size(); k++) {
								JSONObject area = areas.getJSONObject(k);
								String areaid = area.getString("Id");
								String areaName = area.getString("Name");
								locationIdFullNameMap.put(areaid, cityFullName + "\\" + areaName);
							}
						}
					}
				}
			}
		}
		return locationIdFullNameMap.get(locationId);
	}

	public static LocationLoader getInstance() {
		return locationLoader;
	}

	private LocationLoader() {

	}

	public JSONObject loadLocation() {
		if (fromJson == null) {
			try {
				URL resource = Thread.currentThread().getContextClassLoader().getResource("locations");
				String file = resource.getPath();
				String read = Files.asCharSource(new File(file), Charsets.UTF_8).read();
				fromJson = JsonUtilTool.fromJson(read, JSONObject.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fromJson;
	}

	public static void main(String[] args) {
		System.out.println(LocationLoader.getInstance().loadLocation());
	}
}
