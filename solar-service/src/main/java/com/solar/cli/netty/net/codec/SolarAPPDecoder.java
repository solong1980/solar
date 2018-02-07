package com.solar.cli.netty.net.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.request.ClientRequest;
import com.solar.server.mina.net.codec.MsgProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

public class SolarAPPDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(SolarAPPDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		if (in.readableBytes() < MsgProtocol.flagSize + MsgProtocol.lengthSize + MsgProtocol.msgCodeSize) {
			return;
		}
		byte flag = in.getByte(0);
		if (flag == 1) {
			in.readByte();
			int length = in.readInt();
			// System.out.println("消息length："+length+"---"+session.getId());
			if (length <= 0 || length > MsgProtocol.maxPackLength) {
				// 长度字段异常
				logger.info("数据包长度异常");
				// send empty/error client request
				throw new DecoderException("data package length exception.");
			}
			if (in.readableBytes() >= length) {
				ByteBuf body = in.readBytes(length);
				byte[] req = new byte[body.readableBytes()];
				body.readBytes(req);
				ClientRequest message = new ClientRequest(req);
				out.add(message);
			} else {
				return;
			}
		} else {
			logger.info("flag 错误");
			in.readByte();
			return;
		}
	}
}