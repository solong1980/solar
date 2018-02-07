package com.solar.cli.netty.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

	// Return a unreleasable view on the given ByteBuf
	// which will just ignore release and retain calls.
	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("\n", CharsetUtil.UTF_8));

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			switch (event.state()) {
			case READER_IDLE:
				// Read空闲增加计数
				break;
			case WRITER_IDLE:
				// Write空闲写心跳
				// ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
				break;
			case ALL_IDLE:
				break;
			default:
				break;
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
