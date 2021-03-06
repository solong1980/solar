package com.solar.cli.netty.command.mcc;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.cli.netty.controller.MccMsgProcessor;
import com.solar.cli.netty.session.NettyExtSessionManager;
import com.solar.cli.netty.session.ISession;
import com.solar.cli.netty.session.NettySession;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoRunningDataService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoRunningData;
 

/**
 * 
 * @author long lianghua
 *
 */
public class DataUploadCmdProcessor extends MccMsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DataUploadCmdProcessor.class);
	private SoDevicesService devicesService;
	private SoRunningDataService runningDataService;

	public DataUploadCmdProcessor() {
		runningDataService = SoRunningDataService.getInstance();
		devicesService = SoDevicesService.getInstance();
	}

	public boolean access(ISession<?> session, String devNo) {
		if (logger.isInfoEnabled()) {
			logger.info("device access : {}", devNo);
		}
		SoDevices devices = null;
		if (devNo != null) {
			// 设备终端连接
			devices = devicesService.selectByDevNo(devNo);
			if (devices == null) {
				logger.info("device UUID=" + devNo + " is not found");
			} else {
				session.setEnti(devices);
				session.setLogin(true);
				// add to session map
				logger.info("add device UUID=" + devNo + " to device session");
				NettyExtSessionManager.getInstance().putDevSessionToHashMap((NettySession) session);
				/**
				 * devices.setSw0((short) 1); devices.setSw1((short) 1); devices.setSw2((short)
				 * 1); devices.setSw3((short) 1); devices.setSw4((short) 1);
				 * devices.setSw5((short) 1); devices.setSw6((short) 1); devices.setSw7((short)
				 * 1); appSession.sendMsg(
				 * ServerMccResponse.build(ConnectAPI.MC_DEVICES_RUNNING_CTRL_RESPONSE,
				 * devices.buildMmcMsg()));
				 */
				return true;
			}
		} else {
			logger.info("device no is null");
		}
		return false;
	}

	@Override
	public void process(ISession<?> session, String msgCode, String msg, String[] reqs) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Data update : {}", Arrays.toString(reqs));
		}
		if (reqs.length < 20) {
			logger.error("data len is not enought");
			return;
		}
		String uuid = reqs[1];
		// 如果设备未登陆,择登陆
		if (!session.isLogin()) {
			synchronized (session) {
				if (!session.isLogin()) {
					boolean access = access(session, uuid);
					if (!access)
						return;
				}
			}
		}

		String ver = reqs[2];
		long verInt = Long.parseLong(ver, 16);
		int deviceWareVerion = SolarCache.getInstance().getDeviceWareVerionNo();
		if (SolarCache.getInstance().accessDownload(uuid) && deviceWareVerion > verInt) {
			// doSendUpgradeData
			logger.debug("send update data blockNo=0 for devNo=" + uuid);
			sendUpdataWareData(session, 0);
		}

		try {
			SoRunningData runningData = new SoRunningData();
			WeakReference<SoRunningData> wf = new WeakReference<SoRunningData>(runningData);
			// 01,17DD5E6E,FFFFFFFF,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,83.872,30.473689

			runningData.setUuid(uuid);
			runningData.setReq(msg);
			runningData.setFmid(reqs[2]); // 固件版本
			runningData.setVssun(reqs[3]); // 太阳能板电压
			runningData.setIchg(reqs[4]); // 电池充电电流
			runningData.setVbat(reqs[5]); // 电池电压
			runningData.setLevel(reqs[6]); // 电池剩余容量
			runningData.setPchg(reqs[7]); // 电池剩余容量
			runningData.setPdis(reqs[8]); // 电池剩余容量

			runningData.setIld1(reqs[9]); // 负载1电流
			runningData.setIld2(reqs[10]); // 负载2电流
			runningData.setIld3(reqs[11]); // 负载3电流
			runningData.setIld4(reqs[12]); // 负载4电流

			runningData.setTemp(reqs[13]); // 环境温度

			runningData.setAin1(reqs[14]); // 第1路4-20mA
			runningData.setAin2(reqs[15]); // 第2路4-20mA
			runningData.setAin3(reqs[16]); // 第3路4-20mA

			runningData.setStat(reqs[17]); // 控制器状态
			runningData.setUtcTime(reqs[18]);// GPS时间
			runningData.setAltitude(reqs[19]);// GPS纬度
			runningData.setLongitude(reqs[20]);// GPS经度
			runningDataService.insertRunningData(wf.get());
			runningData = null;
		} catch (Exception e) {
			logger.error("add running data failure", e);
		}
		// // 02,分机1控制,分机2控制,水泵1控制,水泵2控制,继电器1控制,继电器2控制,继电器3控制,继电器4控制
		// java.util.Random random = new java.util.Random();
		//
		// int nextInt = random.nextInt(20);
		// if (nextInt % 2 == 0)
		// appSession.sendMsg(ServerMccResponse.build("02", "1,1,1,1,1,1,1,1"));
		// if (nextInt % 2 == 1)
		// appSession.sendMsg(ServerMccResponse.build("02", "0,0,0,0,0,0,0,0"));
	}
}
