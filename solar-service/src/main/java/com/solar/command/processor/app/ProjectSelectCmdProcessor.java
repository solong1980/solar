package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.ProjectSelectResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.PROJECT_SELECT_COMMAND)
public class ProjectSelectCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ProjectSelectCmdProcessor.class);
	private SoProjectService projectService;

	public ProjectSelectCmdProcessor() {
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		logger.debug("select project," + json);
		SoProject project = JsonUtilTool.fromJson(json, SoProject.class);
		project = projectService.selectById(project.getId());
		appSession.sendMsg(new ProjectSelectResponse(JsonUtilTool.toJson(project)));
	}

}
