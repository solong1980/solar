package com.solar.command.processor.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.ProjectCalcIChgResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.PROJECT_CALC_ICHG_COMMAND)
public class ProjectCalcIChgCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ProjectCalcIChgCmdProcessor.class);
	private SoProjectService projectService;
	private SoDevicesService devicesService;

	public ProjectCalcIChgCmdProcessor() {
		projectService = SoProjectService.getInstance();
		devicesService = SoDevicesService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		logger.debug("add project," + json);
		SoProject project = JsonUtilTool.fromJson(json, SoProject.class);
		Long projectId = project.getId();
		List<SoDevices> projectDevs = devicesService.selectProjectDevs(projectId);
		String pchg = "0";
		if (projectDevs != null && !projectDevs.isEmpty()) {
			pchg = projectService.calcProjectPchg(projectDevs);
		}
		project.setProjectTotalPChg(pchg);

		appSession.sendMsg(new ProjectCalcIChgResponse(JsonUtilTool.toJson(project)));
	}

}
