package com.solar.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.server.commons.session.AppSession;

public abstract class MccMsgProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MccMsgProcessor.class);

	public void handle(AppSession appSession, String msgCode, String[] reqs) {
		try {
			process(appSession, msgCode, reqs);
		} catch (Exception e) {
			logger.error("消息处理出错，msg code:" + msgCode, e);
			appSession.sendMsg(ServerMccResponse.buildError());
		}
	}

	public abstract void process(AppSession appSession, String msgCode, String[] reqs) throws Exception;
}
