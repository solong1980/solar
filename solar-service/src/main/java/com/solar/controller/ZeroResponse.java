package com.solar.controller;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class ZeroResponse extends ServerResponse {
	// 返回无语了
	public ZeroResponse(String noword) {
		super(1, ConnectAPI.ZERO_RESPONSE);
		try {
			output.writeUTF(noword);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
	}
}
