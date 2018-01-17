package com.solar.command.processor.app;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountFindQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.SuccessCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountFindService;
import com.solar.entity.SoAccountFind;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_FINDBACK_AUDIT_COMMAND)
public class AccountFindAuditCmdProcessor extends MsgProcessor {
	private SoAccountFindService accountFindService;

	public AccountFindAuditCmdProcessor() {
		super();
		accountFindService = SoAccountFindService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoAccountFind accountFind = JsonUtilTool.fromJson(json, SoAccountFind.class);

		int status = accountFind.getStatus();
		AuditResult auditResult = AuditResult.status(status);

		switch (auditResult) {
		case AGREE:
			accountFindService.auditAgree(accountFind);
			break;
		case REJECT:
			// update status
			accountFindService.auditReject(accountFind);
			// send
			break;
		default:
			break;
		}

		accountFind = new SoAccountFind();
		accountFind.setMsg(SuccessCode.Sucess_000003);
		json = JsonUtilTool.toJson(accountFind);
		appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_FINDBACK_AUDIT_RESPONSE, json));
	}

}
