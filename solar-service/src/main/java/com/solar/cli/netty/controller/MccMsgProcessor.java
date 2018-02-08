package com.solar.cli.netty.controller;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.cli.netty.session.ISession;
import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAppVersion;

public abstract class MccMsgProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MccMsgProcessor.class);

	public void handle(ISession<?> session, String msgCode, String msg, String[] reqs) {
		try {
			process(session, msgCode,msg, reqs);
		} catch (Exception e) {
			logger.error("消息处理出错，msg code:" + msgCode, e);
			session.sendMsg(ServerMccResponse.buildError());
		}
	}

	public abstract void process(ISession<?> session, String msgCode, String msg,String[] reqs) throws Exception;

	public void sendUpdataWareData(ISession<?> session, int blockNo) throws ExecutionException, IOException {
		SoAppVersion deviceWareVerion = SolarCache.getInstance().getDeviceWareVerion();
		int blockCount = deviceWareVerion.getBlockCount();
		if (blockNo >= blockCount + 1) {
			logger.error("blockNo toooo large");
			return;
		}
		byte[] block = SolarCache.getInstance().getDeviceWareDataBlock(blockNo);
		if (block.length > 0) {
			ByteArrayOutputStream byteStream = null;
			DataOutputStream dataStream = null;
			try {
				byteStream = new ByteArrayOutputStream(block.length + 5);
				dataStream = new DataOutputStream(byteStream);
				dataStream.writeBytes(ConnectAPI.MC_UPDATE_WARE_BLOCK_RESPONE);
				dataStream.writeBytes(",");
				if (blockCount == blockNo) {
					dataStream.writeShort(0x7FFF);
				} else
					dataStream.writeShort(blockNo);
				dataStream.write(block);
				dataStream.flush();
				byte[] byteArray = byteStream.toByteArray();
				session.sendMsg(byteArray);
			} finally {
				if (byteStream != null)
					byteStream.close();
				if (dataStream != null)
					dataStream.close();
			}
		}
	}
}
