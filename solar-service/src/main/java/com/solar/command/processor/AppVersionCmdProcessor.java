package com.solar.command.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AppVersionResponse;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAppVersionService;
import com.solar.entity.SoAbt;
import com.solar.entity.SoAppVersion;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
public class AppVersionCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final String APP_UPGRADE_COMMAND = ":app upgrade command";
	private static final Logger logger = LoggerFactory.getLogger(AppVersionCmdProcessor.class);

	private SoAppVersionService appVersionService;

	public AppVersionCmdProcessor() {
		appVersionService = SoAppVersionService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		if (logger.isInfoEnabled()) {
			String address = appSession.getAddress();
			logger.info(address + APP_UPGRADE_COMMAND);
		}
		String json = request.getString();
		// 客户端版本信息
		SoAppVersion appVersion = JsonUtilTool.fromJson(json, SoAppVersion.class);

		SoAppVersion lastAppVersion = appVersionService.selectLastAppVersion(appVersion.getType());

		if (lastAppVersion == null) {
			lastAppVersion = new SoAppVersion();
			lastAppVersion.setRetCode(SoAbt.FAILURE);
		}
		AppVersionResponse appVersionResponse = new AppVersionResponse(0, JsonUtilTool.toJson(lastAppVersion));
		appSession.sendMsg(appVersionResponse);
	}
}
