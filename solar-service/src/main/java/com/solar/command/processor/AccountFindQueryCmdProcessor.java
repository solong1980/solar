package com.solar.command.processor;

import java.util.Collections;
import java.util.List;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AccountFindQueryResponse;
import com.solar.command.message.response.LocationResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountFindService;
import com.solar.entity.SoAccountFind;
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
		
		SoAccountFind accountFind = JsonUtilTool.fromJson(json, SoAccountFind.class);
		List<SoAccountFind> accountFinds = accountFindService.queryAccountFind(accountFind);

		if (accountFinds == null) {
			appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_FINDBACK_QUERY_RESPONSE,
					JsonUtilTool.toJson(Collections.EMPTY_LIST)));
		} else {
			json = JsonUtilTool.toJson(accountFinds);
			appSession.sendMsg(new AccountFindQueryResponse(ConnectAPI.ACCOUNT_FINDBACK_QUERY_RESPONSE, json));
		}
	}

}
