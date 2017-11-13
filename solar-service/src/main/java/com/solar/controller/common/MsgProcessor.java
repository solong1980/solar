package com.solar.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.FailureResponse;
import com.solar.server.commons.session.AppSession;

public abstract class MsgProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MsgProcessor.class);

	public void handle(AppSession appSession, ClientRequest request) {
		try {
			process(appSession, request);
		} catch (Exception e) {
			logger.error("消息处理出错，msg code:" + request.getMsgCode(), e);
			appSession.sendMsg(FailureResponse.failure(e.getMessage()));
		}
	}

	public abstract void process(AppSession appSession, ClientRequest request) throws Exception;
}
