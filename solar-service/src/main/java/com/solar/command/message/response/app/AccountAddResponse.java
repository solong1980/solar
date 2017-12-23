package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class AccountAddResponse extends ServerResponse {

	public AccountAddResponse(String msg) {
		super(0, ConnectAPI.ACCOUNT_ADD_RESPONSE);
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			logger.error("output write fail", e);
		} finally {
			output.close();
		}
	}

}
