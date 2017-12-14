package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;

public class AccountQueryResponse extends ServerResponse {

	public AccountQueryResponse(int resCmd, String msg) {
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
