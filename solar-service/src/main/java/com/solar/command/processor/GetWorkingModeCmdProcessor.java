package com.solar.command.processor;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.FailureResponse;
import com.solar.command.message.response.GetWorkingModeResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.entity.SoAbtAuth;
import com.solar.entity.SoWorkingMode;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.GET_WORKING_MODE_COMMAND)
public class GetWorkingModeCmdProcessor extends MsgProcessor {
	private SolarCache solarCache;

	public GetWorkingModeCmdProcessor() {
		super();
		solarCache = SolarCache.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		SoAbtAuth abtAuth = appSession.getEnti(SoAbtAuth.class);
		Long custId = abtAuth.getCustId();
		SoWorkingMode workingMode = solarCache.getWorkingMode(custId);
		if (workingMode == null) {
			appSession.sendMsg(FailureResponse.failure(Consts.FAILURE));
		} else {
			String json = JsonUtilTool.toJson(workingMode);
			appSession.sendMsg(new GetWorkingModeResponse(0, json));
		}
	}

}
