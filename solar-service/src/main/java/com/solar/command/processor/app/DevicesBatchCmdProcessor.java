package com.solar.command.processor.app;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.DevicesBatchResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoDevicesBatch;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_BATCH_COMMAND)
public class DevicesBatchCmdProcessor extends MsgProcessor implements INotAuthProcessor {

	private SoDevicesService devicesService;

	public DevicesBatchCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		SoDevicesBatch devicesBatch = JsonUtilTool.fromJson(json, SoDevicesBatch.class);
		devicesService.batch(devicesBatch);
		appSession.sendMsg(new DevicesBatchResponse(JsonUtilTool.toJson(new SoDevices())));
	}

}
