package com.solar.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import com.solar.client.msg.ClientSendRequest;
import com.solar.client.net.MinaClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;

public class DataClient extends MinaClient {

	public DataClient(NetConf netConf) {
		super(netConf);
	}

	@Override
	public void recive() {
		try {
			serverCallBack(input);
		} catch (IOException e) {
			System.err.println("客户端异常:" + e.getMessage());
		}
	}

	public void send(int command, String jsonData) {
		try {
			ClientSendRequest loginSend = new ClientSendRequest(command);
			loginSend.output.writeUTF(jsonData);
			send(loginSend.entireMsg().array());
		} catch (Exception e) {
			System.err.println("客户端异常:" + e.getMessage());
			sleep();
			// 关闭重连
			if (e instanceof SocketException || e instanceof ConnectException) {
				initSocket();
			}
			send(command, jsonData);
		}
	}

	public List<SoProvinces> getProvinces() {
		List<SoProvinces> soProvinces = null;
		send(ConnectAPI.ADDR_PROVINCES_QUERY_COMMAND, "");
		try {
			soProvinces = serverCallBack(input);
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
		send(ConnectAPI.ADDR_CITIES_QUERY_COMMAND, JsonUtilTool.toJson(cities));
		try {
			citiesList = serverCallBack(input);
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
		send(ConnectAPI.ADDR_AREAS_QUERY_COMMAND, JsonUtilTool.toJson(areas));
		try {
			areasList = serverCallBack(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return areasList;
	}

	public <T> List<T> serverCallBack(DataInputStream input) throws IOException {
		byte flag = input.readByte();
		if (flag == 1) {
			int len = input.readInt();
			if (len > 0) {
				int code = input.readInt();
				int status = input.readInt();
				String ret = input.readUTF();
				if (status == 0 && code == ConnectAPI.ADDR_PROVINCES_QUERY_RESPONSE||code == ConnectAPI.ADDR_CITIES_QUERY_RESPONSE||code == ConnectAPI.ADDR_AREAS_QUERY_RESPONSE) {
					System.out.println(ret);
					List<SoProvinces> ja = JsonUtilTool.fromJsonArray(ret, SoProvinces.class);
					return (List<T>) ja;
				}
			}
		}
		System.out.println("error");
		return Collections.emptyList();
	}

	@Override
	public boolean countinueTry() {
		return false;
	}

}