package com.solar.command.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AddProjectResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.PROJECT_ADD_COMMAND)
public class ProjectAddCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ProjectAddCmdProcessor.class);
	private SoProjectService projectService;

	public ProjectAddCmdProcessor() {
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		logger.debug("add project," + json);
		SoProject project = JsonUtilTool.fromJson(json, SoProject.class);
		projectService.addProject(project);

		project.setMsg(Consts.SUCCESS);
		appSession.sendMsg(new AddProjectResponse(JsonUtilTool.toJson(project)));
	}

}
