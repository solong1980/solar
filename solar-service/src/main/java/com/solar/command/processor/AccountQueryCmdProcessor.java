package com.solar.command.processor;

import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AccountFindQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoAccount;
import com.solar.entity.SoPage;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_QUERY_COMMAND)
public class AccountQueryCmdProcessor extends MsgProcessor {
	private SoAccountService accountService;

	public AccountQueryCmdProcessor() {
		super();
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();

		SoPage<SoAccount, List<SoAccount>> accountPage = JsonUtilTool.fromJson(json,
				new TypeReference<SoPage<SoAccount, List<SoAccount>>>() {
				});
		if (accountPage == null) {
			accountPage = new SoPage<>(null);
		}
		accountPage = accountService.queryAccount(accountPage);

		json = JsonUtilTool.toJson(accountPage);
		appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_QUERY_RESPONSE, json));
	}

}
