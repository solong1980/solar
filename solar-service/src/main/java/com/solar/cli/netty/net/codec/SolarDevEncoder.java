package com.solar.cli.netty.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class SolarDevEncoder extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof String) {
			String m = (String) msg;
			byte[] b = m.getBytes();
			ByteBuf encoded = ctx.alloc().buffer(b.length);
			encoded.writeBytes(b);
			ctx.write(encoded, promise);
		} else if (msg instanceof byte[]){
			byte[] b = (byte[])msg;
			ByteBuf encoded = ctx.alloc().buffer(b.length);
			encoded.writeBytes(b);
			ctx.write(encoded, promise);
		}else {
			super.write(ctx, msg, promise);
		}
	}

}