package com.solar.command.processor.mcc;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MccMsgProcessor;
import com.solar.entity.SoDevices;
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
	public void process(AppSession appSession, String msgCode, String msg, String[] reqs) throws Exception {
		// doSendData
		if (logger.isDebugEnabled())
			logger.info("device ware block req," + Arrays.toString(reqs));
		if (reqs.length < 3)
			return;
		String devNo = "";
		try {
			devNo = appSession.getEnti(SoDevices.class).getDevNo();
		} catch (Exception e) {
		}
		try {
			String state = reqs[1];
			String lastPackageNo = reqs[2];
			int blockNo = Integer.parseInt(lastPackageNo);
			if (state.equals("01")) {
				appSession.addBlockFailTime(0);
				blockNo = blockNo + 1;
				if (blockNo < 0) {
					if (logger.isDebugEnabled())
						logger.debug("device ware block, blockNo=" + blockNo + " error, blockNo(- [1,)");
					return;
				}
			} else if (state.equals("00")) {
				appSession.addBlockFailTime(1);
				if (appSession.getBlockFailTime() >= 3) {
					appSession.addBlockFailTime(0);
					return;
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("unknow state=" + state + " for devNo=" + devNo);
				return;
			}
			if (logger.isDebugEnabled())
				logger.debug("send update data blockNo=" + blockNo + " for devNo=" + devNo);
			sendUpdataWareData(appSession, blockNo);
		} catch (Exception e) {
			appSession.addBlockFailTime(1);
			logger.error("send update error", e);
		}

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
