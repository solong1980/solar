package com.solar.command.processor;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AppUpgradeFileResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.entity.SoAppUpgradeInfo;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.APP_UPGRADE_COMMAND)
public class AppUpgradeCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(AppUpgradeCmdProcessor.class);

	private static final String APP_UPGRADE_COMMAND = ":app upgrade command";

	public AppUpgradeCmdProcessor() {
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String address = appSession.getAddress();
		logger.info(address + APP_UPGRADE_COMMAND);
		String json = request.getString();
		// 客户端版本信息
		SoAppUpgradeInfo appUpgradeInfo = JsonUtilTool.fromJson(json, SoAppUpgradeInfo.class);
		// 查询最新版本信息
		appUpgradeInfo.setVersion("2");
		appUpgradeInfo.setVersionNo(2);
		// 对比版本号
		// 读取文件内容s
		ByteSource asByteSource = Files.asByteSource(new File("hka-oj-dev(New).zip"));
		byte[] read = asByteSource.read();
		// 写 入响应
		AppUpgradeFileResponse apkDataResponse = new AppUpgradeFileResponse(0, json, read);
		appSession.sendMsg(apkDataResponse);
	}
}
