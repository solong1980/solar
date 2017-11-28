package com.solar.command.processor;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.ErrorResponse;
import com.solar.command.message.response.FailureResponse;
import com.solar.command.message.response.GetWorkingModeResponse;
import com.solar.command.message.response.SuccessResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoWorkingModeService;
import com.solar.entity.SoAbtAuth;
import com.solar.entity.SoWorkingMode;
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

	private SoWorkingModeService workingModeService;
	private SolarCache solarCache;

	public WorkingModeUpdateCmdProcessor() {
		workingModeService = SoWorkingModeService.getInstance();
		solarCache = SolarCache.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("Data update : {}", json);
		}
		SoAbtAuth abtAuth = appSession.getEnti(SoAbtAuth.class);
		SoWorkingMode workingMode = JsonUtilTool.fromJson(json, SoWorkingMode.class);
		Integer result = workingModeService.insertWorkingMode(workingMode);
		if (result > 0) {
			Long custId = abtAuth.getCustId();
			solarCache.updateWorkingMode(custId);
			workingMode = solarCache.getWorkingMode(custId);
			if (workingMode == null) {
				appSession.sendMsg(FailureResponse.failure(Consts.FAILURE));
			} else {
				// notify all other host
				try {
					List<AppSession> custSession = AppSessionManager.getInstance().getCustDevsSession(custId);
					json = JsonUtilTool.toJson(workingMode);
					for (AppSession otherSession : custSession) {
						// 排出自己
						if (otherSession.equals(appSession))
							continue;
						otherSession.sendMsg(new GetWorkingModeResponse(0, json));
					}
					appSession.sendMsg(SuccessResponse.success(Consts.SUCCESS));
				} catch (ExecutionException e) {
					appSession.sendMsg(ErrorResponse.build(1, ConnectAPI.ERROR_RESPONSE, ErrorCode.Error_000005));
				}
			}
		} else {
			appSession.sendMsg(FailureResponse.failure(Consts.FAILURE));
		}
	}
}
