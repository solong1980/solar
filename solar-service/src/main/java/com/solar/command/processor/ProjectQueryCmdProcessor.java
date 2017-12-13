package com.solar.command.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.TypeReference;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.ProjectQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.PROJECT_QUERY_COMMAND)
public class ProjectQueryCmdProcessor extends MsgProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ProjectQueryCmdProcessor.class);
	private SoProjectService projectService;

	public ProjectQueryCmdProcessor() {
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		logger.debug("query project," + json);

		SoPage<SoProject, List<SoProject>> page = JsonUtilTool.fromJson(json,
				new TypeReference<SoPage<SoProject, List<SoProject>>>() {
				});
		page = projectService.queryProjects(page);
		appSession.sendMsg(new ProjectQueryResponse(JsonUtilTool.toJson(page)));
	}

}
