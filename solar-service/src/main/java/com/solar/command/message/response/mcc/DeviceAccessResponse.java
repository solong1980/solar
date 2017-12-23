package com.solar.command.message.response.mcc;

import java.io.IOException;

import com.solar.common.context.ConnectAPI;

public class DeviceAccessResponse extends ServerMccResponse {

	public DeviceAccessResponse(String msg) {
		super(ConnectAPI.MC_DEVICES_ACCESS_RESPONSE);
		try {
			output.writeBytes(msg.getBytes());
		} catch (IOException e) {
			logger.error("output write fail", e);
		} finally {
			output.close();
		}
	}

}
