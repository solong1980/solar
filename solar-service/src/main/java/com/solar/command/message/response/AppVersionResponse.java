package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class AppVersionResponse extends ServerResponse {
	public AppVersionResponse(int status, String json) {
		super(status, ConnectAPI.APP_VERSION_QUERY_RESPONSE);
		try {
			output.writeInt(json.getBytes().length);
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output write app version fail", e);
		} finally {
			output.close();
		}
	}
}
