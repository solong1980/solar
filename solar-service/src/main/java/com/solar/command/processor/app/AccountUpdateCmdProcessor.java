package com.solar.command.processor.app;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountUpdateResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoAccount;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_UPDATE_COMMAND)
public class AccountUpdateCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;

	public AccountUpdateCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		accountService.updateAccount(account);

		json = JsonUtilTool.toJson(new SoAccount());
		appSession.sendMsg(new AccountUpdateResponse(ConnectAPI.ACCOUNT_UPDATE_RESPONSE, json));
	}

}
