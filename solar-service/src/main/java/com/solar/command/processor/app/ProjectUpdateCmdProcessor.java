package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.ProjectUpdateResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.PROJECT_UPDATE_COMMAND)
public class ProjectUpdateCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ProjectUpdateCmdProcessor.class);
	private SoProjectService projectService;

	public ProjectUpdateCmdProcessor() {
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		logger.debug("update project," + json);
		SoProject project = JsonUtilTool.fromJson(json, SoProject.class);
		projectService.updataProject(project);

		project.setMsg(Consts.SUCCESS);
		appSession.sendMsg(new ProjectUpdateResponse(JsonUtilTool.toJson(project)));
	}

}
