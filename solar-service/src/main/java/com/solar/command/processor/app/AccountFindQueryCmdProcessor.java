package com.solar.command.processor.app;

import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountFindQueryResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountFindService;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoPage;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_FINDBACK_QUERY_COMMAND)
public class AccountFindQueryCmdProcessor extends MsgProcessor {
	private SoAccountFindService accountFindService;

	public AccountFindQueryCmdProcessor() {
		super();
		accountFindService = SoAccountFindService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		
		SoPage<SoAccountFind, List<SoAccountFind>> accountFindPage = JsonUtilTool.fromJson(json,
				new TypeReference<SoPage<SoAccountFind, List<SoAccountFind>>>() {
				});
		if (accountFindPage == null) {
			accountFindPage = new SoPage<>(null);
		}
		
		accountFindPage = accountFindService.queryAccountFind(accountFindPage);
		json = JsonUtilTool.toJson(accountFindPage);
		appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_FINDBACK_QUERY_RESPONSE, json));
	}

}
