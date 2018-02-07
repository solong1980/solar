package com.solar.cli.netty.net.codec;

import org.apache.mina.core.buffer.IoBuffer;

import com.solar.command.message.response.ResponseMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class SolarAPPEncoder extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof ResponseMsg) {
			ResponseMsg m = (ResponseMsg) msg;
			IoBuffer entireMsg = m.entireMsg();

			ByteBuf encoded = ctx.alloc().buffer(entireMsg.limit());
			encoded.writeBytes(entireMsg.array());
			ctx.write(encoded, promise);
		} else {
			super.write(ctx, msg, promise);
		}
	}

}