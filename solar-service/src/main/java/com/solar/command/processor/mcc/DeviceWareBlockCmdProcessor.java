package com.solar.command.processor.mcc;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MccMsgProcessor;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
public class DeviceWareBlockCmdProcessor extends MccMsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DeviceWareBlockCmdProcessor.class);

	public DeviceWareBlockCmdProcessor() {
	}

	@Override
	public void process(AppSession appSession, String msgCode, String[] reqs) throws Exception {
		// doSendData
		if (logger.isDebugEnabled())
			logger.info("device ware block req," + Arrays.toString(reqs));
		if (reqs.length < 2)
			return;
		String bno = reqs[1];
		int blockNo = Integer.parseInt(bno);
		sendUpdataWareData(appSession, blockNo);
		/**
		 * byte[] block = SolarCache.getInstance().getDeviceWareDataBlock(bno); if
		 * (block.length > 0) { ByteArrayOutputStream byteStream = null;
		 * DataOutputStream dataStream = null; try { byteStream = new
		 * ByteArrayOutputStream(block.length + 4); dataStream = new
		 * DataOutputStream(byteStream); dataStream.writeShort(block.length);
		 * dataStream.writeShort(bno); dataStream.write(block); dataStream.flush();
		 * byte[] byteArray = byteStream.toByteArray();
		 * appSession.sendMsg(ServerMccResponse.build(ConnectAPI.MC_UPDATE_WARE_BLOCK_COMMAND,
		 * new String(byteArray))); } finally { if (byteStream != null)
		 * byteStream.close(); if (dataStream != null) dataStream.close(); } }
		 */
	}
}
