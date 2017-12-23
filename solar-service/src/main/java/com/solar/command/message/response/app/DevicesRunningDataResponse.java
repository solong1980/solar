package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class DevicesRunningDataResponse extends ServerResponse {
	public DevicesRunningDataResponse(String json) {
		super(0, ConnectAPI.DEVICES_RUNNINGDATA_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("DevicesRunningDataResponse fail", e);
		} finally {
			output.close();
		}
	}
}
