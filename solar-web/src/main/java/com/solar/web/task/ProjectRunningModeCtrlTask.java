package com.solar.web.task;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoProjectWorkingModeService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.web.net.HostClient;

@Component("projectRunningModeCtrlTask")
public class ProjectRunningModeCtrlTask {
	private static final Logger logger = LoggerFactory.getLogger(ProjectRunningModeCtrlTask.class.getClass());

	/** 默认flag值 */
	public byte defaultFlag = 1;
	/** 最大长度 */
	public int maxPackLength = 1024 * 5;
	/** 标识数占得 byte数 */
	public int flagSize = 1;//
	/** 协议中 长度部分 占用的byte数,其值表示( 协议号+内容) 的长度 */
	public int lengthSize = 4;//
	/** 消息号占用的byte数 */
	public int msgCodeSize = 4;
	@Autowired
	private HostClient hostClient;

	private SoDevicesService devicesService;
	private SoProjectWorkingModeService projectWorkingModeService;

	private Map<Long, SoProjectWorkingMode> map = new HashMap<>();

	public ProjectRunningModeCtrlTask() {
		super();
		devicesService = SoDevicesService.getInstance();
		projectWorkingModeService = SoProjectWorkingModeService.getInstance();
	}

	public IoBuffer entireMsg(int command, String jsonData) throws IOException {
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(byteout);

		dataOut.writeUTF(jsonData);
		byte[] body = byteout.toByteArray();

		/* 标志 byte 长度short */
		int length = flagSize + lengthSize + msgCodeSize + body.length;// 总长度
		IoBuffer buf = IoBuffer.allocate(length);
		buf.put(defaultFlag);// 消息开始标志,长连接分辨不同消息
		buf.putInt(body.length + msgCodeSize);// 消息长度+消息码大小
		buf.putInt(command);
		buf.put(body);
		buf.flip();
		return buf;
	}

	public void send(int command, String jsonData) {
		try {
			hostClient.send(entireMsg(command, jsonData).array());
		} catch (Exception e) {
			logger.error("客户端异常:", e);
		}
	}

	public void runPackFileProcess() {
		int start = 1;
		List<SoDevices> t = null;
		logger.info("start schedule devices");
		hostClient.initSocket();
		try {
			do {
				SoPage<SoDevices, List<SoDevices>> page = new SoPage<>(start, 50);
				page.setC(new SoDevices());
				devicesService.queryDevices(page);
				t = page.getT();
				if (t != null && !t.isEmpty()) {
					logger.info("get from page" + start + ",return " + t.size());
					Calendar calendar = Calendar.getInstance();
					int h = calendar.get(Calendar.HOUR_OF_DAY);
					int m = calendar.get(Calendar.MINUTE);
					int n = ((h * 2) + (m < 30 ? 0 : 1));
					for (SoDevices soDevices : t) {
						Long projectId = soDevices.getProjectId();
						// getmode
 						SoProjectWorkingMode projectWorkingMode = map.get(projectId);
//						if (projectWorkingMode == null) {
//							map.put(projectId, projectWorkingMode);
//						}

						projectWorkingMode = projectWorkingModeService.selectByProjectId(projectId);
						if (projectWorkingMode != null) {

							Method method = SoProjectWorkingMode.class.getMethod("getH_" + n);
							if (method != null) {
								Object v = method.invoke(projectWorkingMode);
								short f = (Short) v;
								if (f == 0) {
									// 按照设备启停设置走
									// Short sw0 = soDevices.getSw0();
									// Short sw1 = soDevices.getSw1();
									// Short sw2 = soDevices.getSw2();
									// Short sw3 = soDevices.getSw3();
									// Short sw4 = soDevices.getSw4();
									// Short sw5 = soDevices.getSw5();
									// Short sw6 = soDevices.getSw6();
									// Short sw7 = soDevices.getSw7();
									// 直接发送设备配置,如果是1就继续,如果是0就是停止
								} else {
									// 全部开始
									soDevices.setSw0((short) 1);
									soDevices.setSw1((short) 1);
									soDevices.setSw2((short) 1);
									soDevices.setSw3((short) 1);
									soDevices.setSw4((short) 1);
									soDevices.setSw5((short) 1);
									soDevices.setSw6((short) 1);
									soDevices.setSw7((short) 1);
								}
							}
						}
					}

					logger.info("start send:" + Arrays.toString(t.toArray(new SoDevices[0])));
					send(ConnectAPI.DEVICES_SCHEDULE_COMMAND, JsonUtilTool.toJson(t));
					logger.info("end send");
					start++;
				} else {
					logger.info("not more deivces");
				}
			} while (t != null && !t.isEmpty());
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			logger.info("client closed");
			synchronized (this) {
				try {
					this.wait(10000);
				} catch (InterruptedException e) {
				}
			}
			hostClient.close();
			System.out.println("----------------------");
		}
		logger.info("end schedule devices");
	}

}
