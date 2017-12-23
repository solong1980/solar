package com.solar.command.processor.app;

import java.util.List;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountSelectResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.db.services.SoPrivilegeService;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_SELECT_COMMAND)
public class AccountSelectCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;
	private SoPrivilegeService privilegeService;
	public AccountSelectCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
		privilegeService = SoPrivilegeService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		account = accountService.selectById(account.getId());
		Long id = account.getId();
		
		//query account location
		List<SoAccountLocation> accountLocations = accountService.queryGovernmentLocation(id);
		account.setLocations(accountLocations);
		// query project from privilege
		List<SoProject> projects = privilegeService.queryOwnerProjects(id);
		account.setProjects(projects);
		
		
		json = JsonUtilTool.toJson(account);
		appSession.sendMsg(new AccountSelectResponse(ConnectAPI.ACCOUNT_SELECT_RESPONSE, json));
	}

}
