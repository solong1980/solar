package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class ProjectDeleteResponse extends ServerResponse {
	public ProjectDeleteResponse(String json) {
		super(0, ConnectAPI.PROJECT_DELETE_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output delete project fail", e);
		} finally {
			output.close();
		}
	}
}
