package com.solar.cli.netty.command.mcc;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cli.netty.controller.MccMsgProcessor;
import com.solar.cli.netty.session.ISession;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.entity.SoDevices;

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
	public void process(ISession<?> session, String msgCode, String msg, String[] reqs) throws Exception {
		// doSendData
		if (logger.isDebugEnabled())
			logger.info("device ware block req," + Arrays.toString(reqs));
		if (reqs.length < 3)
			return;
		String devNo = "";
		try {
			devNo = session.getEnti(SoDevices.class).getDevNo();
		} catch (Exception e) {
		}
		try {
			String state = reqs[1];
			String lastPackageNo = reqs[2];
			int blockNo = Integer.parseInt(lastPackageNo);
			if (state.equals("01")) {
				session.addBlockFailTime(0);
				blockNo = blockNo + 1;
				if (blockNo < 0) {
					if (logger.isDebugEnabled())
						logger.debug("device ware block, blockNo=" + blockNo + " error, blockNo(- [1,)");
					return;
				}
			} else if (state.equals("00")) {
				session.addBlockFailTime(1);
				if (session.getBlockFailTime() >= 3) {
					session.addBlockFailTime(0);
					return;
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("unknow state=" + state + " for devNo=" + devNo);
				return;
			}
			if (logger.isDebugEnabled())
				logger.debug("send update data blockNo=" + blockNo + " for devNo=" + devNo);
			sendUpdataWareData(session, blockNo);
		} catch (Exception e) {
			session.addBlockFailTime(1);
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
