package com.solar.server.mina.net.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

public class SolarMmcMsgEncoder extends TextLineEncoder {

	public SolarMmcMsgEncoder() {
		super(Charset.defaultCharset(), LineDelimiter.UNIX);
	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message instanceof String) {
			super.encode(session, message, out);
		} else {
			byte[] data = (byte[]) message;
			IoBuffer buf = IoBuffer.allocate(data.length).setAutoExpand(true);
			buf.put(data);
			if (buf.position() > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Line length: " + buf.position());
			}
			buf.flip();
			out.write(buf);
		}
	}
}