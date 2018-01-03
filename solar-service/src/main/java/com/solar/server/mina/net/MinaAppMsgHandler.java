package com.solar.server.mina.net;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;
import com.solar.server.mina.bootstrap.SolarServer;

public class MinaAppMsgHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MinaAppMsgHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		AppSession appSession = new AppSession(session);
		AppSessionManager.getInstance().putAppSessionToHashMap(appSession);
		// logger.info("a session create from ip {}", appSession.getAddress());
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
			SolarServer.msgDispatcher.returnCloseMsg(session);
		} else
			SolarServer.msgDispatcher.dispatchMsg(appSession, clientRequest);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// 强制退出
		logger.error("服务器出错 {}", cause.getMessage());
		cause.printStackTrace();
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		AppSession appSession = AppSession.getInstance(session);
		if (appSession == null) {
			session.closeNow();
			return;
		}
		appSession.addTime(1);
		if (appSession.getTime() > 60) {
			appSession.close();
			return;
		}
	}

	/**
	 * 关闭SESSION
	 * 
	 * @param session
	 * @throws Exception
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// logger.info("a session closed ip:{}", session.getRemoteAddress());
		AppSession appSession = AppSession.getInstance(session);
		if (appSession != null) {
			AppSessionManager.getInstance().rmAppSession(appSession);
			appSession.close();
		}
		session.closeNow();
	}

}
