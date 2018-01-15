package com.solar.command.processor.app;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.WorkingModelUpdateResponse;
import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoProjectWorkingModeService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.WORKING_MODE_UPDATE_COMMAND)
public class WorkingModeUpdateCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(WorkingModeUpdateCmdProcessor.class);

	private SoProjectWorkingModeService workingModeService;
	private SoDevicesService devicesService;
	private SolarCache solarCache;

	public WorkingModeUpdateCmdProcessor() {
		devicesService = SoDevicesService.getInstance();
		workingModeService = SoProjectWorkingModeService.getInstance();
		solarCache = SolarCache.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		SoProjectWorkingMode workingMode = JsonUtilTool.fromJson(json, SoProjectWorkingMode.class);
		Long id = workingMode.getId();
		if (id == null) {
			workingModeService.insertWorkingMode(workingMode);
		} else {
			workingModeService.updateWorkingMode(workingMode);
		}
		Long projectId = workingMode.getProjectId();
		solarCache.updateWorkingMode(projectId);
		List<SoDevices> projectDevs = devicesService.selectProjectDevs(projectId);
		if (projectDevs != null && !projectDevs.isEmpty()) {
			String mmcMsg = workingMode.buildMmcMsg();
			logger.info("update project projectId=" + projectId + " device:"
					+ Arrays.toString(projectDevs.toArray(new SoDevices[0])) + " running mode:" + mmcMsg);
			for (SoDevices device : projectDevs) {
				String devNo = device.getDevNo();
				AppSession deviceSession = AppSessionManager.getInstance().devNoSessionMap.get(devNo);
				if (deviceSession == null) {
					logger.error("device uuid=" + devNo + " not connect");
					continue;
				} else {
					deviceSession.sendMsg(ServerMccResponse.build(ConnectAPI.MC_PROJECT_RUNNINGMODE_RESPONSE, mmcMsg));
				}
			}
		}
		appSession.sendMsg(new WorkingModelUpdateResponse(JsonUtilTool.toJson(new SoProjectWorkingMode())));
	}
}
