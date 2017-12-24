package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.GetWorkingModeResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.GET_WORKING_MODE_COMMAND)
public class GetProjectWorkingModeCmdProcessor extends MsgProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetProjectWorkingModeCmdProcessor.class);
	private SolarCache solarCache;

	public GetProjectWorkingModeCmdProcessor() {
		super();
		solarCache = SolarCache.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		SoProjectWorkingMode projectWorkingMode = JsonUtilTool.fromJson(json, SoProjectWorkingMode.class);
		try {
			projectWorkingMode = solarCache.getWorkingMode(projectWorkingMode.getProjectId());
		} catch (Exception e) {
			LOGGER.error("get working mode from guava error", e);
			projectWorkingMode = null;
		}
		if (projectWorkingMode == null) {
			appSession.sendMsg(new GetWorkingModeResponse(0, ""));
		} else {
			json = JsonUtilTool.toJson(projectWorkingMode);
			appSession.sendMsg(new GetWorkingModeResponse(0, json));
		}
	}

}
