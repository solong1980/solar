package com.solar.cli.netty.net;

import com.solar.cli.netty.bootstrap.NettyServer;
import com.solar.cli.netty.session.NettyExtSessionManager;
import com.solar.cli.netty.session.NettySession;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

public class SolarDevClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String m = (String) msg;
		NettySession session = NettySession.getInstance(ctx);
		if (m == null || m.trim().isEmpty()) {
			if (session != null) {
				session.addTime(0);
			}
			return;
		} else {
			if (session == null) {
				synchronized (ctx) {
					session = NettySession.getInstance(ctx);
					if (session == null)
						session = new NettySession(ctx);
				}
			}
			session.addTime(0);
			String[] split = m.split(",");
			String msgCode = split[0];
			NettyServer.msgDispatcher.dispatchMsg(session, msgCode, m, split);
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Attribute<NettySession> attr = ctx.channel().attr(NettySession.NETTY_CHANNEL_SESSION_KEY);
		NettySession session = attr.getAndSet(null);
		NettyExtSessionManager.getInstance().rmNettySession(session);
		super.handlerRemoved(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		Attribute<NettySession> attr = ctx.channel().attr(NettySession.NETTY_CHANNEL_SESSION_KEY);
		NettySession session = attr.get();
		if (session == null)
			return;
		session.sendMsg("");
	}

}