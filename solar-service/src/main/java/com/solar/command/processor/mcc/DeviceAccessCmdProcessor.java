package com.solar.command.processor.mcc;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MccMsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;

/**
 * 
 * @author long lianghua
 *
 */
public class DeviceAccessCmdProcessor extends MccMsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DeviceAccessCmdProcessor.class);
	private SoDevicesService devicesService;

	public DeviceAccessCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, String msgCode, String[] reqs) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("device access : {}", Arrays.toString(reqs));
		}
		SoDevices devices = null;
		String devNo = reqs[1];
		if (devNo != null) {
			// 设备终端连接
			devices = devicesService.selectByDevNo(devNo);
			if (devices == null) {
				devices = new SoDevices();
				devices.setDevNo(devNo);
				//insert device
			} else {
				devices = new SoDevices();
				devices.setMsg(Consts.SUCCESS);
				appSession.setEnti(devices);
				appSession.setLogin(true);
				//add to session map
				AppSessionManager.getInstance().putDevSessionToHashMap(appSession);
				
			}
			appSession.sendMsg(ServerMccResponse.buildSuccess());
		} else {
			devices = new SoDevices();
			devices.setRetCode(SoDevices.ACCESS_FAILURE);
			devices.setMsg(ErrorCode.Error_000006);
			appSession.sendMsg(ServerMccResponse.buildError());
		}
	}
}
