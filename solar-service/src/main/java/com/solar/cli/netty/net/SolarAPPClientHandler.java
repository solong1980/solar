package com.solar.cli.netty.net;

import com.solar.cli.netty.bootstrap.NettyServer;
import com.solar.cli.netty.session.AppExtSessionManager;
import com.solar.cli.netty.session.NettySession;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.ServerResponse;
import com.solar.command.message.response.app.ErrorResponse;
import com.solar.command.message.response.app.FailureResponse;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.ErrorCode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

public class SolarAPPClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		NettySession session = new NettySession(ctx);
		AppExtSessionManager.getInstance().putNettySessionToHashMap(session);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ClientRequest clientRequest = (ClientRequest) msg;
		NettySession session = NettySession.getInstance(ctx);
		if (session == null) {
			NettyServer.msgDispatcher.returnCloseMsg(ctx);
		} else
			NettyServer.msgDispatcher.dispatchMsg(session, clientRequest);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Attribute<NettySession> attr = ctx.channel().attr(NettySession.NETTY_CHANNEL_SESSION_KEY);
		NettySession session = attr.getAndSet(null);
		AppExtSessionManager.getInstance().rmNettySession(session);
		super.handlerRemoved(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		Attribute<NettySession> attr = ctx.channel().attr(NettySession.NETTY_CHANNEL_SESSION_KEY);
		NettySession session = attr.get();
		ServerResponse error = ErrorResponse.build(1, ConnectAPI.ERROR_RESPONSE, ErrorCode.Error_000002);
		session.sendMsg(FailureResponse.failure(cause.getMessage()));
	}

}