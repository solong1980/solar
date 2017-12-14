package com.solar.command.processor;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AccountSelectResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoAccount;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_SELECT_COMMAND)
public class AccountSelectCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;

	public AccountSelectCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		account = accountService.selectById(account.getId());
		json = JsonUtilTool.toJson(account);
		appSession.sendMsg(new AccountSelectResponse(ConnectAPI.ACCOUNT_SELECT_RESPONSE, json));
	}

}
