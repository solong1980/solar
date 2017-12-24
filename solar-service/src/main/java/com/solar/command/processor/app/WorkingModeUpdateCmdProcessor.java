package com.solar.command.processor.app;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.WorkingModelUpdateResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoProjectWorkingModeService;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.WORKING_MODE_UPDATE_COMMAND)
public class WorkingModeUpdateCmdProcessor extends MsgProcessor {

	private SoProjectWorkingModeService workingModeService;
	private SolarCache solarCache;

	public WorkingModeUpdateCmdProcessor() {
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
		appSession.sendMsg(new WorkingModelUpdateResponse(JsonUtilTool.toJson(new SoProjectWorkingMode())));
	}
}
