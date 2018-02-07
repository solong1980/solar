package com.solar.cli.netty.session;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.response.ResponseMsg;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class NettySession implements ISession<ChannelFuture> {

	private static final Logger LOGGER = LoggerFactory.getLogger(NettySession.class);

	private String SessionID = UUID.randomUUID().toString();

	public String getSessionID() {
		return SessionID;
	}

	private ChannelHandlerContext ctx;
	/**
	 * 用户的服务器地址
	 */
	private String address;
	/**
	 * 记录心跳时间， 间隔，到达一定时间就表示前端断线了
	 */
	private int time = 0;

	/**
	 * 失败次数
	 */
	private int blockFailTime = 0;

	private Object enti;
	private boolean isLogin = false;

	public static final AttributeKey<NettySession> NETTY_CHANNEL_SESSION_KEY = AttributeKey.valueOf("netty.session");

	public NettySession(ChannelHandlerContext ctx) {
		this.ctx = ctx;

		Attribute<NettySession> attr = ctx.channel().attr(NETTY_CHANNEL_SESSION_KEY);
		attr.setIfAbsent(this);

		SocketAddress socketaddress = ctx.channel().remoteAddress();
		if (socketaddress != null) {
			InetSocketAddress s = (InetSocketAddress) socketaddress;
			// 存当前用户相关的服务器地址
			address = s.getAddress().getHostAddress();
		}
		LOGGER.debug("create session " + SessionID);
	}

	/**
	 * 得到一个AppSession的实例化对象
	 * 
	 * @param session
	 * @return
	 */
	public static NettySession getInstance(ChannelHandlerContext ctx) {
		Attribute<NettySession> attr = ctx.channel().attr(NETTY_CHANNEL_SESSION_KEY);
		return attr.get();

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
		if (ctx != null)
			ctx.close();
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

	public void addBlockFailTime(int i) {
		if (i == 0) {
			this.blockFailTime = i;
		} else {
			this.blockFailTime = this.blockFailTime + i;
		}
	}

	public int getBlockFailTime() {
		return blockFailTime;
	}

	@Override
	public ChannelFuture sendMsg(ResponseMsg msg) {
		return this.ctx.writeAndFlush(msg);
	}

	@Override
	public ChannelFuture sendMsg(String msg) {
		return this.ctx.writeAndFlush(msg);
	}

	@Override
	public ChannelFuture sendMsg(byte[] data) {
		return this.ctx.writeAndFlush(data);
	}

}
