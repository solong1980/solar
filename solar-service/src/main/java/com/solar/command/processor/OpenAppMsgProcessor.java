package com.solar.command.processor;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.FailureResponse;
import com.solar.common.context.Consts;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.server.commons.session.AppSession;

public class OpenAppMsgProcessor extends MsgProcessor implements INotAuthProcessor {
	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		appSession.sendMsg(FailureResponse.failure("welecome !"));
	}
}
