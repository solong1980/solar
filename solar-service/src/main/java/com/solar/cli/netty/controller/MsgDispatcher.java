package com.solar.cli.netty.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cli.common.MccMsgProcessorRegister;
import com.solar.cli.common.MsgProcessorRegister;
import com.solar.cli.netty.session.ISession;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.ErrorResponse;
import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.controller.ZeroResponse;
import com.solar.controller.common.INotAuthProcessor;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息分发器，根据消息号，找到相应的消息处理器
 * 
 *
 */
public class MsgDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(MsgDispatcher.class);

	private Map<Integer, MsgProcessor> processorsAppMap = new HashMap<Integer, MsgProcessor>();

	private Map<String, MccMsgProcessor> processorsMccMap = new HashMap<String, MccMsgProcessor>();

	public MsgDispatcher() {
		for (MsgProcessorRegister register : MsgProcessorRegister.values()) {
			processorsAppMap.put(register.getMsgCode(), register.getMsgProcessor());
		}
		logger.info("初始化 APP消息处理器成功。。。");

		for (MccMsgProcessorRegister register : MccMsgProcessorRegister.values()) {
			processorsMccMap.put(register.getMsgCode(), register.getMsgProcessor());
		}
		logger.info("初始化 MCC消息处理器成功。。。");
	}

	public void returnCloseMsg(ChannelHandlerContext ctx) {
		ctx.write(new ZeroResponse(Consts.REQUEST_TOOOOO_FAST));
		ctx.close();
	}

	public MsgProcessor getMsgProcessor(int msgCode) {
		return processorsAppMap.get(msgCode);
	}

	/**
	 * 派发消息协议
	 * 
	 * @param appSession
	 * @param clientRequest
	 */
	public void dispatchMsg(ISession<?> session, ClientRequest request) {
		int msgCode = request.getMsgCode();
		if (msgCode == 1000) {
			// 客户端请求断开链接
			session.close();
		}

		MsgProcessor processor = getMsgProcessor(msgCode);
		if (processor == null) {
			// 未找到对应的processor
			getMsgProcessor(ConnectAPI.ZERO_RESPONSE).handle(session, request);
			return;
		} else if (session.isLogin() || processor instanceof INotAuthProcessor) {
			processor.handle(session, request);
		} else {
			if (!session.isLogin()) {
				session.sendMsg(ErrorResponse.build(1, ConnectAPI.ERROR_RESPONSE, ErrorCode.Error_000002));
				return;
			}
		}
	}

	public MccMsgProcessor getMccMsgProcessor(String msgCode) {
		return processorsMccMap.get(msgCode);
	}

	/**
	 * 派发消息协议
	 * 
	 * @param appSession
	 * @param clientRequest
	 */
	public void dispatchMsg(ISession<?> session, String msgCode, String msg, String[] mcMsg) {
		MccMsgProcessor processor = getMccMsgProcessor(msgCode);
		if (session == null || processor == null) {
			logger.warn("none processer for msgcode:" + msgCode);
			return;
		} else if (session.isLogin() || processor instanceof INotAuthProcessor) {
			processor.handle(session, msgCode, msg, mcMsg);
		} else {
			logger.warn("device not login msg:" + Arrays.toString(mcMsg));
			session.sendMsg(ServerMccResponse.buildError());
			return;
		}
	}

}
