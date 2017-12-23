package com.solar.command.processor.app;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.FailureResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.CTRL_COMMAND)
public class OpenAppMsgProcessor extends MsgProcessor implements INotAuthProcessor {
	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		appSession.sendMsg(FailureResponse.failure("welecome !"));
	}
}
