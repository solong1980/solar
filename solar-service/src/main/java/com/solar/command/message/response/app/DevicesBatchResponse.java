package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class DevicesBatchResponse extends ServerResponse {
	public DevicesBatchResponse(String json) {
		super(0, ConnectAPI.DEVICES_BATCH_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("add and del devices fail", e);
		} finally {
			output.close();
		}
	}
}
