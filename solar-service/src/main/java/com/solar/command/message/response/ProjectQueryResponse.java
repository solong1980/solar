package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class ProjectQueryResponse extends ServerResponse {
	public ProjectQueryResponse(String json) {
		super(0, ConnectAPI.PROJECT_QUERY_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output add project fail", e);
		} finally {
			output.close();
		}
	}
}
