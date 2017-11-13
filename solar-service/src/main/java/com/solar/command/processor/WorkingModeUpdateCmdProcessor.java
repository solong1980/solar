package com.solar.command.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.FailureResponse;
import com.solar.command.message.response.SuccessResponse;
import com.solar.common.context.Consts;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoWorkingModeService;
import com.solar.entity.SoWorkingMode;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
public class WorkingModeUpdateCmdProcessor extends MsgProcessor implements INotAuthProcessor {
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
		SoWorkingMode workingMode = JsonUtilTool.fromJson(json, SoWorkingMode.class);
		Integer result = workingModeService.insertWorkingMode(workingMode);
		if (result > 0) {
			solarCache.updateWorkingMode();
			appSession.sendMsg(SuccessResponse.success(Consts.SUCCESS));
		} else {
			appSession.sendMsg(FailureResponse.failure(Consts.FAILURE));
		}
	}
}
