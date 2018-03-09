package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class ProjectCalcIChgResponse extends ServerResponse {
	public ProjectCalcIChgResponse(String json) {
		super(0, ConnectAPI.PROJECT_CALC_ICHG_RESPONSE);
		try {
			output.writeUTF(json);
		} catch (IOException e) {
			logger.error("output calc project ichg fail", e);
		} finally {
			output.close();
		}
	}
}
