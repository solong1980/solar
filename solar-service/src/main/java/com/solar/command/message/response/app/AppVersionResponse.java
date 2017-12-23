package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class AppVersionResponse extends ServerResponse {
	public AppVersionResponse(String json) {
		super(0, ConnectAPI.APP_VERSION_QUERY_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output write app version fail", e);
		} finally {
			output.close();
		}
	}
}
