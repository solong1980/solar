package com.solar.command.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.DeviceAccessResponse;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
public class DeviceAccessCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DeviceAccessCmdProcessor.class);
	private SoDevicesService devicesService;

	public DeviceAccessCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("device access : {}", json);
		}
		SoDevices devices = JsonUtilTool.fromJson(json, SoDevices.class);

		String devNo = devices.getDevNo();
		if (devNo != null) {
			// 设备终端连接
			devices = devicesService.selectByDevNo(devNo);
			if (devices == null) {
				devices = new SoDevices();
				devices.setRetCode(SoDevices.ACCESS_FAILURE);
				devices.setMsg(ErrorCode.Error_000006);
			} else {
				devices.setMsg(Consts.SUCCESS);
			}
			appSession.setEnti(devices);
			appSession.setLogin(true);
			appSession.sendMsg(new DeviceAccessResponse(JsonUtilTool.toJson(devices)));
		} else {
			devices.setRetCode(SoDevices.ACCESS_FAILURE);
			devices.setMsg(ErrorCode.Error_000006);
			appSession.sendMsg(new DeviceAccessResponse(JsonUtilTool.toJson(devices)));
		}
	}
}
