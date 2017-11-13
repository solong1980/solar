package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class GetWorkingModeResponse extends ServerResponse {
	public GetWorkingModeResponse(int status, String json) {
		super(status, ConnectAPI.GET_WORKING_MODE_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output write GET_WORKING_MODE_RESPONSE fail", e);
		} finally {
			output.close();
		}
	}
}
