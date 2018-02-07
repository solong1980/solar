package com.solar.cli.netty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cli.netty.session.ISession;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.FailureResponse;

public abstract class MsgProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MsgProcessor.class);

	public void handle(ISession<?> session, ClientRequest request) {
		try {
			process(session, request);
		} catch (Exception e) {
			logger.error("消息处理出错，msg code:" + request.getMsgCode(), e);
			session.sendMsg(FailureResponse.failure(e.getMessage()));
		}
	}

	public abstract void process(ISession<?> appSession, ClientRequest request) throws Exception;
}
