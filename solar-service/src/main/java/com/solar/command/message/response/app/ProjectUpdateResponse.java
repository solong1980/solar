package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class ProjectUpdateResponse extends ServerResponse {
	public ProjectUpdateResponse(String json) {
		super(0, ConnectAPI.PROJECT_UPDATE_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output update project fail", e);
		} finally {
			output.close();
		}
	}
}
