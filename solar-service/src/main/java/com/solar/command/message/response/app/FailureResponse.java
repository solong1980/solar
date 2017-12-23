package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class FailureResponse extends ServerResponse {

	private FailureResponse(String msg) {
		super(1, ConnectAPI.FAILURE_RESPONSE);
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			logger.error("output write fail", e);
		} finally {
			output.close();
		}
	}
	
	
	public static FailureResponse failure(String message) {
		return new FailureResponse(message);
	}
}
