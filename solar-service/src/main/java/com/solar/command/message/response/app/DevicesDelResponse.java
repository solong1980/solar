package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class DevicesDelResponse extends ServerResponse {
	public DevicesDelResponse(String json) {
		super(0, ConnectAPI.DEVICES_DEL_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("del device fail", e);
		} finally {
			output.close();
		}
	}
}
