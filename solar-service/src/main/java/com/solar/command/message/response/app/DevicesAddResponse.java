package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class DevicesAddResponse extends ServerResponse {
	public DevicesAddResponse(String json) {
		super(0, ConnectAPI.DEVICES_ADD_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("add device fail", e);
		} finally {
			output.close();
		}
	}
}
