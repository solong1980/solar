package com.solar.command.processor.app;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountFindQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.SuccessCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoAccount;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_AUDIT_COMMAND)
public class AccountAuditCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;

	public AccountAuditCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		Integer status = account.getStatus();
		AuditResult auditResult = AuditResult.status(status);
		switch (auditResult) {
		case AGREE:
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
