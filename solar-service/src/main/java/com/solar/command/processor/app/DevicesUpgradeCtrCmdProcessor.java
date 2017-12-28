package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.ServerResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.SuccessCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.entity.SoAbtAuth;
import com.solar.server.commons.session.AppSession;

/**
 * 失效升级文件数据块
 * 
 * @author longlh
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.DEVICES_UPGRADECTR_COMMAND)
public class DevicesUpgradeCtrCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesUpgradeCtrCmdProcessor.class);

	public DevicesUpgradeCtrCmdProcessor() {
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("invalidte device last update data blocks,SolarCache.getInstance().updateDeviceWareDataBlock");
		SolarCache.getInstance().updateDeviceWareVerion();
		SolarCache.getInstance().updateDeviceWareDataBlock();

		SoAbtAuth abtAuth = new SoAbtAuth();
		abtAuth.setMsg(SuccessCode.Sucess_000004);
		appSession
				.sendMsg(ServerResponse.build(0, ConnectAPI.DEVICES_UPGRADECTR_RESPONSE, JsonUtilTool.toJson(abtAuth)));
	}

}
