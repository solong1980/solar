package com.solar.cli.netty.command;

import com.solar.cli.netty.controller.MsgProcessor;
import com.solar.cli.netty.session.ISession;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.FailureResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.controller.common.INotAuthProcessor;

@ProcessCMD(API_CODE = ConnectAPI.CTRL_COMMAND)
public class OpenAppMsgProcessor extends MsgProcessor implements INotAuthProcessor {
	@Override
	public void process(ISession<?> session, ClientRequest request) throws Exception {
		session.sendMsg(FailureResponse.failure("welecome !"));
	}
}
