package com.solar.server.mina.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/**
 * 一个对象工厂，提供了获取编码器和解码器的函数接口。
 * 
 * @author dyz
 * 
 */
public class SolarProtocolcodecFactory implements ProtocolCodecFactory {
	private final SolarMsgEncoder encoder;
	private final SolarMsgDecoder decoder;

	public SolarProtocolcodecFactory() {
		encoder = new SolarMsgEncoder();
		decoder = new SolarMsgDecoder();
	}

	public ProtocolEncoder getEncoder(IoSession session) {
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) {
		return decoder;
	}
}

