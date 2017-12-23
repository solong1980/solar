package com.solar.command.processor.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.DevicesInProjectResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_IN_PROJECT_COMMAND)
public class DevicesInProjectCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesInProjectCmdProcessor.class);
	private SoDevicesService devicesService;

	public DevicesInProjectCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled())
			logger.debug("query devices:" + json);

		SoDevices devices = JsonUtilTool.fromJson(json, SoDevices.class);
		List<SoDevices> devicesList = devicesService.selectProjectDevs(devices.getProjectId());
		appSession.sendMsg(new DevicesInProjectResponse(JsonUtilTool.toJson(devicesList)));
	}

}
