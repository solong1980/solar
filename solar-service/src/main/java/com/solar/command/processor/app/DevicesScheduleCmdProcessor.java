package com.solar.command.processor.app;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.TypeReference;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_SCHEDULE_COMMAND)
public class DevicesScheduleCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesScheduleCmdProcessor.class);

	public DevicesScheduleCmdProcessor() {
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		List<SoDevices> devices = JsonUtilTool.fromJson(json, new TypeReference<List<SoDevices>>() {
		});
		// get device from session
		if (devices != null) {
			logger.info("schedule devices:" + Arrays.toString(devices.toArray(new SoDevices[0])));
			for (SoDevices device : devices) {
				String devNo = device.getDevNo();
				AppSession deviceSession = AppSessionManager.getInstance().devNoSessionMap.get(devNo);
				if (deviceSession == null) {
					logger.error("device uuid=" + devNo + " not connect");
					continue;
				} else {
					deviceSession.sendMsg(
							ServerMccResponse.build(ConnectAPI.MC_DEVICES_RUNNING_CTRL_RESPONSE, device.buildMmcMsg()));
				}
			}
		} else {
			logger.info("schedule none devices");
		}
	}

}
