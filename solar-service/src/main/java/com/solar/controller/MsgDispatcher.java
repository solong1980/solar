package com.solar.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.ErrorResponse;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.controller.common.MsgProcessorRegister;
import com.solar.server.commons.session.AppSession;

/**
 * 消息分发器，根据消息号，找到相应的消息处理器
 * 
 *
 */
public class MsgDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(MsgDispatcher.class);

	private Map<Integer, MsgProcessor> processorsMap = new HashMap<Integer, MsgProcessor>();

	public MsgDispatcher() {
		for (MsgProcessorRegister register : MsgProcessorRegister.values()) {
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
	public MsgProcessor getMsgProcessor(int msgCode) {
		// logger.info("receive msgcode:" + msgCode);
		return processorsMap.get(msgCode);
	}

	public void returnCloseMsg(IoSession ioSession) {
		ioSession.write(new ZeroResponse(Consts.REQUEST_TOOOOO_FAST));
		ioSession.closeOnFlush();
	}

	/**
	 * 派发消息协议
	 * 
	 * @param appSession
	 * @param clientRequest
	 */
	public void dispatchMsg(AppSession appSession, ClientRequest request) {
		int msgCode = request.getMsgCode();
		if (msgCode == 1000) {
			// 客户端请求断开链接
			appSession.close();
		}

		MsgProcessor processor = getMsgProcessor(msgCode);
		if (processor == null) {
			// 未找到对应的processor
			getMsgProcessor(ConnectAPI.ZERO_RESPONSE).handle(appSession, request);
			return;
		} else if (appSession.isLogin() || processor instanceof INotAuthProcessor) {
			processor.handle(appSession, request);
		} else {
			if (!appSession.isLogin()) {
				appSession.sendMsg(ErrorResponse.build(1, ConnectAPI.ERROR_RESPONSE, ErrorCode.Error_000002));
				return;
			}
		}
	}

}
