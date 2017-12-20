package com.solar.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.solar.client.net.NetConf;
import com.solar.client.net.ShortClient;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProvinces;

public class DataClient extends ShortClient {

	public DataClient(NetConf netConf) {
		super(netConf);
	}

	@Override
	public <T> T recive() throws IOException {
		return serverCallBack(input);
	}

	public List<SoProvinces> getProvinces() {
		List<SoProvinces> soProvinces = null;
		try {
			soProvinces = doSendRecv(ConnectAPI.ADDR_PROVINCES_QUERY_COMMAND, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return soProvinces;
	}

	public List<SoCities> getCitiesIn(String provinceId) {
		List<SoCities> citiesList = null;
		SoCities cities = new SoCities();
		cities.setProvinceid(provinceId);
		try {
			citiesList = doSendRecv(ConnectAPI.ADDR_CITIES_QUERY_COMMAND, JsonUtilTool.toJson(cities));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return citiesList;
	}

	public List<SoAreas> getAreasIn(String cityId) {
		List<SoAreas> areasList = null;
		SoAreas areas = new SoAreas();
		areas.setCityid(cityId);
		try {
			areasList = doSendRecv(ConnectAPI.ADDR_AREAS_QUERY_COMMAND, JsonUtilTool.toJson(areas));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return areasList;
	}

	@SuppressWarnings("unchecked")
	public <T extends List<E>, E> T serverCallBack(DataInputStream input) throws IOException {
		byte flag = input.readByte();
		if (flag == 1) {
			int len = input.readInt();
			if (len > 0) {
				int code = input.readInt();
				int status = input.readInt();
				String ret = input.readUTF();
				if (status == 0 && code == ConnectAPI.ADDR_PROVINCES_QUERY_RESPONSE
						|| code == ConnectAPI.ADDR_CITIES_QUERY_RESPONSE
						|| code == ConnectAPI.ADDR_AREAS_QUERY_RESPONSE) {
					System.out.println(ret);
					switch (code) {
					case ConnectAPI.ADDR_PROVINCES_QUERY_RESPONSE:
						List<SoProvinces> provinces = JsonUtilTool.fromJsonArray(ret, SoProvinces.class);
						return (T) provinces;
					case ConnectAPI.ADDR_CITIES_QUERY_RESPONSE:
						List<SoCities> cities = JsonUtilTool.fromJsonArray(ret, SoCities.class);
						return (T) cities;
					case ConnectAPI.ADDR_AREAS_QUERY_RESPONSE:
						List<SoAreas> areas = JsonUtilTool.fromJsonArray(ret, SoAreas.class);
						return (T) areas;
					default:
						break;
					}
				}
			}
		}
		System.out.println("error");
		return (T) Collections.emptyList();
	}

	@Override
	public boolean countinueTry() {
		return false;
	}

	public List<SoDevices> getDeviceIn(String id) {
		return null;
	}

}