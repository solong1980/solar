package com.solar.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MccMsgProcessor;
import com.solar.controller.common.MccMsgProcessorRegister;
import com.solar.server.commons.session.AppSession;

/**
 * 消息分发器，根据消息号，找到相应的消息处理器
 * 
 *
 */
public class MccMsgDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(MccMsgDispatcher.class);

	private Map<String, MccMsgProcessor> processorsMap = new HashMap<String, MccMsgProcessor>();

	public MccMsgDispatcher() {
		for (MccMsgProcessorRegister register : MccMsgProcessorRegister.values()) {
			processorsMap.put(register.getMsgCode(), register.getMsgProcessor());
		}
		logger.info("初始化 消息处理器成功。。。");
	}

	/**
	 * 通过协议号得到MsgProcessor
	 * 
	 * @param msgCode
	 * @return
	 */
	public MccMsgProcessor getMsgProcessor(String msgCode) {
		return processorsMap.get(msgCode);
	}

	/**
	 * 派发消息协议
	 * 
	 * @param appSession
	 * @param clientRequest
	 */
	public void dispatchMsg(AppSession appSession, String msgCode, String msg, String[] mcMsg) {
		MccMsgProcessor processor = getMsgProcessor(msgCode);
		if (appSession == null || processor == null) {
			logger.warn("none processer for msgcode:" + msgCode);
			return;
		} else if (appSession.isLogin() || processor instanceof INotAuthProcessor) {
			processor.handle(appSession, msgCode, msg, mcMsg);
		} else {
			logger.warn("device not login msg:" + Arrays.toString(mcMsg));
			appSession.sendMsg(ServerMccResponse.buildError());
			return;
		}
	}

	public void returnCloseMsg(IoSession session) {
		session.closeNow();
	}

}
