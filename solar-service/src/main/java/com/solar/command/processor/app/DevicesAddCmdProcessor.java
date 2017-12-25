package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.DevicesAddResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_ADD_COMMAND)
public class DevicesAddCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesAddCmdProcessor.class);
	private SoDevicesService devicesService;

	public DevicesAddCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled())
			logger.debug("update devices:" + json);

		SoDevices devices = JsonUtilTool.fromJson(json, SoDevices.class);
		devicesService.insert(devices);
		// get device from session
		/**
		 * String devNo = devices.getDevNo(); AppSession deviceSession =
		 * AppSessionManager.getInstance().devNoSessionMap.get(devNo); if (deviceSession
		 * == null) { devices.setRetCode(1); devices.setMsg(ErrorCode.Error_000014); }
		 * else { deviceSession.sendMsg("02," + devices.buildMmcMsg()); }
		 */
		appSession.sendMsg(new DevicesAddResponse(JsonUtilTool.toJson(devices)));
	}

}
