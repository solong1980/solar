package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class DevicesInProjectResponse extends ServerResponse {
	public DevicesInProjectResponse(String json) {
		super(0, ConnectAPI.DEVICES_IN_PROJECT_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output add project fail", e);
		} finally {
			output.close();
		}
	}
}
