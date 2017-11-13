package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class SuccessResponse extends ServerResponse {

	private SuccessResponse(String msg) {
		super(0, ConnectAPI.SUCCESS_RESPONSE);
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			logger.error("output write fail", e);
		} finally {
			output.close();
		}
	}
	
	public static SuccessResponse success(String msg) {
		return new SuccessResponse(msg);
	}
}
