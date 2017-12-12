package com.solar.command.processor;

import java.util.List;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AccountFindQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.ErrorCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoAccount;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_PROJECT_CHECK_COMMAND)
public class AccountProjectCheckCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;
	private SoProjectService projectService;

	public AccountProjectCheckCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		// query
		Long id = account.getId();
		List<String> locationIds = accountService.queryGovernmentLocationIds(id);
		List<SoProject> projectInGovLocationIds = projectService.queryProjectByLocationIds(locationIds);
		if (projectInGovLocationIds == null || projectInGovLocationIds.isEmpty()) {
			throw new RuntimeException(ErrorCode.Error_000013);
		}
		
		account.setProjects(projectInGovLocationIds);
		
		json = JsonUtilTool.toJson(account);
		appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_PROJECT_CHECK_RESPONSE, json));
	}

}
