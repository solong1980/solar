package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;

public class LocationResponse extends ServerResponse {

	public LocationResponse(int resCmd, String msg) {
		super(0, resCmd);
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			logger.error("output write fail", e);
		} finally {
			output.close();
		}
	}

}
