package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.DevicesUpdateResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.ErrorCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_UPDATE_COMMAND)
public class DevicesUpdateCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesUpdateCmdProcessor.class);
	private SoDevicesService devicesService;

	public DevicesUpdateCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled())
			logger.debug("update devices:" + json);

		SoDevices devices = JsonUtilTool.fromJson(json, SoDevices.class);
		devicesService.update(devices);
		// get device
		String devNo = devices.getDevNo();
		AppSession deviceSession = AppSessionManager.getInstance().devNoSessionMap.get(devNo);
		SoDevices returnO = new SoDevices();
		if (deviceSession == null) {
			returnO.setRole(1);
			returnO.setMsg(ErrorCode.Error_000014);
		} else {
			deviceSession.sendMsg("02," + devices.buildMmcMsg());
		}
		appSession.sendMsg(new DevicesUpdateResponse(JsonUtilTool.toJson(returnO)));
	}

}
