package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class ProjectSelectResponse extends ServerResponse {
	public ProjectSelectResponse(String json) {
		super(0, ConnectAPI.PROJECT_SELECT_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output add project fail", e);
		} finally {
			output.close();
		}
	}
}
