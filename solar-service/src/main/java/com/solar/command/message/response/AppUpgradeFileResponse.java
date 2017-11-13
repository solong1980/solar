package com.solar.command.message.response;

import java.io.IOException;

import com.solar.command.message.ServerResponse;
import com.solar.common.context.ConnectAPI;

public class AppUpgradeFileResponse extends ServerResponse {
	public AppUpgradeFileResponse(int status,String json, byte[] apkDate) {
		super(status, ConnectAPI.APP_UPGRADE_RESPONSE);
		try {
			output.writeInt(json.getBytes().length);
			output.writeUTF(json);
			output.writeInt(apkDate.length);
			output.writeBytes(apkDate);
		} catch (IOException e) {
			logger.error("output write GET_WORKING_MODE_RESPONSE fail", e);
		} finally {
			output.close();
		}
	}
}
