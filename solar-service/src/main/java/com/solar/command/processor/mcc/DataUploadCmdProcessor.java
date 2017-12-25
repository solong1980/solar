package com.solar.command.processor.mcc;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.response.mcc.ServerMccResponse;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MccMsgProcessor;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoRunningDataService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoRunningData;
import com.solar.server.commons.session.AppSession;
import com.solar.server.commons.session.AppSessionManager;

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

	public boolean access(AppSession appSession, String devNo) {
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
				appSession.setEnti(devices);
				// add to session map
				AppSessionManager.getInstance().putDevSessionToHashMap(appSession);
				appSession.setLogin(true);
				return true;
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("device no is null");
			}
		}
		return false;
	}

	@Override
	public void process(AppSession appSession, String msgCode, String[] reqs) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Data update : {}", Arrays.toString(reqs));
		}
		SoRunningData runningData = new SoRunningData();
		WeakReference<SoRunningData> wf = new WeakReference<SoRunningData>(runningData);
		String uuid = reqs[1];
		// 如果设备未登陆,择登陆
		if (!appSession.isLogin()) {
			synchronized (appSession) {
				if (!appSession.isLogin()) {
					boolean access = access(appSession, uuid);
					if (!access)
						return;
				}
			}
		}
		//01,17DD5E6E,FFFFFFFF,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,83.872,30.473689
		runningData.setUuid(uuid);
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
		runningData.setTemp(reqs[12]); // 环境温度
		runningData.setAin1(reqs[13]); // 第1路4-20mA
		runningData.setAin2(reqs[14]); // 第2路4-20mA
		runningData.setAin3(reqs[15]); // 第3路4-20mA
		runningData.setStat(reqs[16]); // 控制器状态
		runningData.setUtcTime(reqs[17]);// GPS时间
		runningData.setAltitude(reqs[18]);// GPS纬度
		runningData.setLongitude(reqs[19]);// GPS经度
		runningDataService.insertRunningData(wf.get());
		runningData = null;
		// 02,分机1控制,分机2控制,水泵1控制,水泵2控制,继电器1控制,继电器2控制,继电器3控制,继电器4控制
		java.util.Random random = new java.util.Random();

		int nextInt = random.nextInt(20);
		if (nextInt % 2 == 0)
			appSession.sendMsg(ServerMccResponse.build("02", "1,1,1,1,1,1,1,1"));
		if (nextInt % 2 == 1)
			appSession.sendMsg(ServerMccResponse.build("02", "0,0,0,0,0,0,0,0"));
	}
}
