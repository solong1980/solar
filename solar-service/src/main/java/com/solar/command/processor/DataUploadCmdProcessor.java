package com.solar.command.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.SuccessResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoRunningDataService;
import com.solar.entity.SoRunningData;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.DATA_UPLOAD_COMMAND)
public class DataUploadCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DataUploadCmdProcessor.class);

	private SoRunningDataService runningDataService;

	public DataUploadCmdProcessor() {
		runningDataService = SoRunningDataService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("Data update : {}", json);
		}
		SoRunningData runningData = JsonUtilTool.fromJson(json, SoRunningData.class);
		runningDataService.insertRunningData(runningData);
		runningData = null;
		appSession.sendMsg(SuccessResponse.success(Consts.SUCCESS));
	}
}
