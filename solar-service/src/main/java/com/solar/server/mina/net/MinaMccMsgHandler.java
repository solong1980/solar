package com.solar.server.mina.net;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;
import com.solar.server.mina.bootstrap.SolarServer;

public class MinaMccMsgHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MinaMccMsgHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		SocketAddress remoteAddress = session.getRemoteAddress();
		if (remoteAddress == null)
			logger.info("a session create from ip {}", remoteAddress);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = (String) message;
		if (logger.isDebugEnabled())
			logger.info("mc_rev:" + msg);

		if (msg == null || msg.trim().isEmpty()) {
			return;
		} else {
			String[] split = msg.split(",");
			String msgCode = split[0];
			AppSession appSession = AppSession.getInstance(session);
			if (appSession == null) {
				synchronized (session) {
					appSession = AppSession.getInstance(session);
					if (appSession == null)
						appSession = new AppSession(session);
				}
			}
			appSession.addTime(0);
			SolarServer.mcMsgDispatcher.dispatchMsg(appSession, msgCode, split);
		}
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
			AppSessionManager.getInstance().rmDevSession(appSession);
			appSession.close();
		}
		session.closeNow();
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
		if (appSession.getTime() > 10) {
			appSession.close();
			return;
		}
	}

}
