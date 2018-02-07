package com.solar.command.processor.app;

import java.util.List;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountFindQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.SuccessCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoAccount;
import com.solar.entity.SoProject;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_AUDIT_COMMAND)
public class AccountAuditCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;
	private SoProjectService projectService;

	public AccountAuditCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		Integer status = account.getStatus();
		AuditResult auditResult = AuditResult.status(status);
		switch (auditResult) {
		case AGREE:
			// query
			Long id = account.getId();
			List<String> locationIds = accountService.queryGovernmentLocationIds(id);
			List<SoProject> projectInGovLocationIds = projectService.queryProjectByLocationIds(locationIds);
			account.setProjects(projectInGovLocationIds);
			accountService.auditAgree(account);
			break;
		case REJECT:
			accountService.auditReject(account);
			break;
		default:
			break;
		}

		account = new SoAccount();
		account.setMsg(SuccessCode.Sucess_000003);
		json = JsonUtilTool.toJson(account);
		appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_AUDIT_RESPONSE, json));
	}

}
