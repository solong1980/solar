package com.solar.server.commons.session;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.ResponseMsg;

public class AppSession implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppSession.class);

	private String SessionID = UUID.randomUUID().toString();

	public String getSessionID() {
		return SessionID;
	}

	/**
	 * IoSession
	 */
	private IoSession session;
	/**
	 * 用户的服务器地址
	 */
	private String address;
	/**
	 * 记录心跳时间， 间隔，到达一定时间就表示前端断线了
	 */
	private int time = 0;
	private Object enti;
	private boolean isLogin = false;
	private static final AttributeKey KEY_PLAYER_SESSION = new AttributeKey(AppSession.class, "player.session");

	public AppSession(IoSession session) {
		SocketAddress socketaddress = session.getRemoteAddress();
		InetSocketAddress s = (InetSocketAddress) socketaddress;
		// 存当前用户相关的服务器地址
		address = s.getAddress().getHostAddress();
		this.session = session;
		this.session.setAttribute(KEY_PLAYER_SESSION, this);
		LOGGER.debug("create session " + SessionID);
	}

	/**
	 * 得到一个AppSession的实例化对象
	 * 
	 * @param session
	 * @return
	 */
	public static AppSession getInstance(IoSession session) {
		Object appObject = session.getAttribute(KEY_PLAYER_SESSION);
		return (AppSession) appObject;
	}

	/**
	 * 发送消息给客户端
	 * 
	 * @param msg
	 * @return
	 * @throws InterruptedException
	 */
	public WriteFuture sendMsg(ResponseMsg msg) {
		if (session == null || !session.isConnected() || session.isClosing()) {
			// system.out.println("session == "+session+" session.isConnected ==
			// "+session.isConnected()+" session.isClosing =
			// "+session.isClosing());
			return null;
		}
		return session.write(msg);
	}

	/**
	 *
	 * @return
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 *
	 * @param isLogin
	 */
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	/**
	 * 是否登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return this.isLogin;
	}

	/**
	 * 保存会话鉴权信息
	 * 
	 * @param obj
	 */
	public void setEnti(Object obj) {
		this.enti = obj;
	}

	/**
	 * 得到会话鉴权信息
	 */
	@SuppressWarnings("unchecked")
	public <T> T getEnti(Class<T> t) {
		return (T) this.enti;
	}

	/**
	 * 关闭SESSION
	 */
	public void close() {
		if (session != null)
			session.closeOnFlush();
	}

	public int getTime() {
		return time;
	}

	public void addTime(int i) {
		if (i == 0) {
			this.time = i;
		} else {
			this.time = this.time + i;
		}
	}

}
