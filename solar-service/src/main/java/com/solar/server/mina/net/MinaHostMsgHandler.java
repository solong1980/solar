package com.solar.server.mina.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.server.commons.session.AppSession;
import com.solar.server.mina.bootstrap.SolarServer;

public class MinaHostMsgHandler extends IoHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MinaHostMsgHandler.class);

	@SuppressWarnings("resource")
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		new AppSession(session);
		logger.info("a session create from ip {}", session.getRemoteAddress());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		ClientRequest clientRequest = (ClientRequest) message;
		AppSession appSession = AppSession.getInstance(session);
		if (appSession == null) {
			logger.info("gameSession == null");
			return;
		}

		SolarServer.msgDispatcher.dispatchMsg(appSession, clientRequest);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// 强制退出
		logger.error("服务器出错 {}", cause.getMessage());
		cause.printStackTrace();
	}

	/**
	 * 关闭SESSION
	 * 
	 * @param session
	 * @throws Exception
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("a session closed ip:{}", session.getRemoteAddress());
		AppSession appSession = AppSession.getInstance(session);
		if (appSession != null) {
			appSession.close();
		}
	}

}
