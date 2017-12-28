package com.solar.controller.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAppVersion;
import com.solar.server.commons.session.AppSession;

public abstract class MccMsgProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MccMsgProcessor.class);

	public void handle(AppSession appSession, String msgCode, String[] reqs) {
		try {
			process(appSession, msgCode, reqs);
		} catch (Exception e) {
			logger.error("消息处理出错，msg code:" + msgCode, e);
			appSession.sendMsg(ServerMccResponse.buildError());
		}
	}

	public abstract void process(AppSession appSession, String msgCode, String[] reqs) throws Exception;

	public void sendUpdataWareData(AppSession appSession, int blockNo) throws ExecutionException, IOException {
		SoAppVersion deviceWareVerion = SolarCache.getInstance().getDeviceWareVerion();
		int blockCount = deviceWareVerion.getBlockCount();
		if (blockNo >= blockCount + 2) {
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
				if (blockCount + 1 == blockNo) {
					dataStream.writeShort(0xFFFF);
				} else
					dataStream.writeShort(blockNo);
				dataStream.write(block);
				dataStream.flush();
				byte[] byteArray = byteStream.toByteArray();
				appSession.sendMsg(byteArray);
			} finally {
				if (byteStream != null)
					byteStream.close();
				if (dataStream != null)
					dataStream.close();
			}
		}
	}
}
