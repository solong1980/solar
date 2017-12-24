package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class WorkingModelUpdateResponse extends ServerResponse {
	public WorkingModelUpdateResponse(String json) {
		super(0, ConnectAPI.WORKING_MODE_UPDATE_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("working mode update fail", e);
		} finally {
			output.close();
		}
	}
}
