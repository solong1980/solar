package com.solar.server.mina.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

public class SolarMccProtocolcodecFactory extends TextLineCodecFactory {
	private final SolarMmcMsgEncoder encoder;

	public SolarMccProtocolcodecFactory() {
		super();
		encoder = new SolarMmcMsgEncoder();
	}

	public ProtocolEncoder getEncoder(IoSession session) {
		return encoder;
	}

	public int getEncoderMaxLineLength() {
		return encoder.getMaxLineLength();
	}

	public void setEncoderMaxLineLength(int maxLineLength) {
		encoder.setMaxLineLength(maxLineLength);
	}

}
