package com.solar.command.processor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.DevicesRunningDataResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoRunningDataService;
import com.solar.entity.SoRunningData;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.DEVICES_RUNNINGDATA_COMMAND)
public class DevicesRunningDataCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DevicesRunningDataCmdProcessor.class);
	private SoRunningDataService runningDataService;

	public DevicesRunningDataCmdProcessor() {
		runningDataService = SoRunningDataService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled())
			logger.debug("devices last running data:" + json);

		SoRunningData runningData = JsonUtilTool.fromJson(json, SoRunningData.class);
		//List<SoRunningData> runningDatas = runningDataService.getLastRunningData(runningData.getUuid(), 1);
		
		List<SoRunningData> runningDatas = new ArrayList<>();
		SoRunningData lastMonitorData = runningDataService.selectLastMonitorData(runningData.getUuid());
		runningDatas.add(lastMonitorData);
		appSession.sendMsg(new DevicesRunningDataResponse(JsonUtilTool.toJson(runningDatas)));
	}

}
