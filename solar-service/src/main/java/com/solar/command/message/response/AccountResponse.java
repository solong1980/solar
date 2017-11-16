package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class AccountResponse extends ServerResponse {

	public AccountResponse(String msg) {
		super(0, ConnectAPI.LOGIN_RESPONSE);
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			logger.error("output write fail", e);
		} finally {
			output.close();
		}
	}

}
