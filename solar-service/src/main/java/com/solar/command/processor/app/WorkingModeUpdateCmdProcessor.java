package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoProjectWorkingModeService;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.WORKING_MODE_UPDATE_COMMAND)
public class WorkingModeUpdateCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(WorkingModeUpdateCmdProcessor.class);

	private SoProjectWorkingModeService workingModeService;
	private SolarCache solarCache;

	public WorkingModeUpdateCmdProcessor() {
		workingModeService = SoProjectWorkingModeService.getInstance();
		solarCache = SolarCache.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		 
	}
}
