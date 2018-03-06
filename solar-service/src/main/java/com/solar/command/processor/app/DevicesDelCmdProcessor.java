package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.DevicesDelResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.SuccessCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_DEL_COMMAND)
public class DevicesDelCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesDelCmdProcessor.class);
	private SoDevicesService devicesService;

	public DevicesDelCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled())
			logger.debug("delete devices:" + json);

		SoDevices devices = JsonUtilTool.fromJson(json, SoDevices.class);
		devicesService.delete(devices);
		devices.setMsg(SuccessCode.Sucess_000005);
		appSession.sendMsg(new DevicesDelResponse(JsonUtilTool.toJson(devices)));
	}

}
