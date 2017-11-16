package com.solar.server.mina.net;

import java.net.SocketAddress;
import java.util.UUID;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;
import com.solar.server.mina.bootstrap.SolarServer;

public class MinaHostMsgHandler extends IoHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MinaHostMsgHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		AppSession appSession = new AppSession(session);
		logger.info("a session create from ip {}", session.getRemoteAddress());
		UUID randomUUID = UUID.randomUUID();
		AppSessionManager.getInstance().putGameSessionInHashMap(appSession, randomUUID.toString());
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		SocketAddress remoteAddress = session.getRemoteAddress();
		if (remoteAddress == null)
			logger.info("a session create from ip {}", remoteAddress);
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
