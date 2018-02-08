package com.solar.cli.netty.net.codec;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class SolarDevDecoder extends DelimiterBasedFrameDecoder {
	private static final ByteBuf DELIMITER = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("\n", CharsetUtil.UTF_8));

	public SolarDevDecoder() {
		super(2048, DELIMITER);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		Object decode = super.decode(ctx, buffer);
		if (decode instanceof ByteBuf) {
			ByteBuf bb = (ByteBuf) decode;
			CharSequence cs = bb.readCharSequence(bb.readableBytes(), Charset.forName("UTF-8"));
			return cs.toString();
		} else
			return super.decode(ctx, buffer);
	}

}