package com.solar.cache;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.solar.common.context.AppType;
import com.solar.db.services.SoAppVersionService;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoProjectWorkingModeService;
import com.solar.entity.SoAppVersion;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProjectWorkingMode;

public class SolarCache {
	private static final Logger logger = LoggerFactory.getLogger(SolarCache.class);

	private static SolarCache solarCache = new SolarCache();

	private Cache<Long, SoProjectWorkingMode> guavaWorkingModeCache;
	private Cache<Long, List<SoDevices>> guavaDevicesCache;
	private Cache<String, Map<String, Object>> guavaAppSersionCache;

	private Cache<AppType, SoAppVersion> deviceWareVersionCache;
	private Cache<Integer, byte[]> deviceWareDataBlockCache;

	private SoProjectWorkingModeService workingModeService;
	private SoDevicesService devicesService;
	private SoAppVersionService appVersionService;

	private final int k = 1024;

	public static SolarCache getInstance() {
		return solarCache;
	}

	private SolarCache() {
		guavaWorkingModeCache = CacheBuilder.newBuilder().initialCapacity(1000).build();
		guavaDevicesCache = CacheBuilder.newBuilder().initialCapacity(1000).build();

		deviceWareVersionCache = CacheBuilder.newBuilder().initialCapacity(1).expireAfterAccess(1, TimeUnit.DAYS)
				.build();
		deviceWareDataBlockCache = CacheBuilder.newBuilder().initialCapacity(1000).expireAfterAccess(1, TimeUnit.DAYS)
				.build();

		guavaAppSersionCache = CacheBuilder.newBuilder().initialCapacity(30).expireAfterAccess(10, TimeUnit.MINUTES)
				.build();

		workingModeService = SoProjectWorkingModeService.getInstance();
		devicesService = SoDevicesService.getInstance();
		appVersionService = SoAppVersionService.getInstance();
		
		devBlockValueCheck();
	}

	public SoProjectWorkingMode getWorkingMode(Long projectId) throws ExecutionException {
		SoProjectWorkingMode value = guavaWorkingModeCache.get(projectId, new Callable<SoProjectWorkingMode>() {
			@Override
			public SoProjectWorkingMode call() throws Exception {
				SoProjectWorkingMode mode = workingModeService.selectByProjectId(projectId);
				return mode;
			}
		});
		return value;
	}

	public List<SoDevices> getDevices(Long projectId) throws ExecutionException {
		List<SoDevices> devs = guavaDevicesCache.get(projectId, new Callable<List<SoDevices>>() {
			@Override
			public List<SoDevices> call() throws Exception {
				return devicesService.selectProjectDevs(projectId);
			}
		});
		return devs;
	}

	public SoAppVersion getDeviceWareVerion() throws ExecutionException {
		SoAppVersion deviceWareVersion = deviceWareVersionCache.get(AppType.RPM, new Callable<SoAppVersion>() {
			@Override
			public SoAppVersion call() throws Exception {
				SoAppVersion deviceWareVersion = appVersionService.selectLastAppVersion(AppType.RPM.type());
				if (deviceWareVersion == null) {
					SoAppVersion appVersion = new SoAppVersion();
					appVersion.setVerNo(0);
					return appVersion;
				}
				// load file to memory
				deviceWareVersion.load();
				return deviceWareVersion;
			}
		});
		return deviceWareVersion;
	}

	public byte[] getDeviceWareDataBlock(int dataBlockNo) throws ExecutionException {
		SoAppVersion appVersion = getDeviceWareVerion();
		if (appVersion != null && appVersion.getVerNo() > 0) {
			if (dataBlockNo == appVersion.getBlockCount() + 1) {
				// write end
				String fileName = appVersion.getFileName();
				String[] fileInfos = fileName.split("-");
				try {
					String crc = fileInfos[1];
					String ver = fileInfos[2];
					ByteArrayOutputStream bStream = new ByteArrayOutputStream(8);
					DataOutputStream dStream = new DataOutputStream(bStream);
					dStream.writeInt(Integer.parseInt(ver));
					dStream.writeInt(Integer.parseInt(crc));
					dStream.flush();
					return bStream.toByteArray();
				} catch (Exception e) {
					logger.error("create block data end flag data error", e);
					String crc = "1233566";
					String ver = "1";
					ByteArrayOutputStream bStream = new ByteArrayOutputStream(8);
					DataOutputStream dStream = new DataOutputStream(bStream);
					try {
						dStream.writeInt(Integer.parseInt(ver));
						dStream.writeInt(Integer.parseInt(crc));
						dStream.flush();
						return bStream.toByteArray();
					} catch (NumberFormatException | IOException e1) {
						e1.printStackTrace();
					} finally {
						try {
							bStream.close();
							dStream.close();
						} catch (IOException e1) {
						}
					}
				}
			}

			if (dataBlockNo >= appVersion.getBlockCount() + 2)
				return new byte[0];
			byte[] bs = deviceWareDataBlockCache.get(dataBlockNo, new Callable<byte[]>() {
				@Override
				public byte[] call() throws Exception {
					byte[] fileData = appVersion.getFileData();
					if (fileData == null || fileData.length == 0)
						return new byte[0];
					int dLen = fileData.length;
					int pos = k * dataBlockNo;
					if (pos <= dLen) {
						byte[] cd = new byte[k];
						System.arraycopy(fileData, k * (dataBlockNo - 1), cd, 0, k);
						return cd;
					} else if ((pos - k) < dLen && pos > dLen) {
						byte[] cd = new byte[dLen - (pos - k)];
						System.arraycopy(fileData, pos - k, cd, 0, dLen - (pos - k));
						return cd;
					} else {
						return new byte[0];
					}
				}
			});
			return bs;
		} else {
			return new byte[0];
		}
	}

	public int getDeviceWareVerionNo() throws ExecutionException {
		SoAppVersion deviceWareVersion = getDeviceWareVerion();
		return deviceWareVersion.getVerNo();
	}

	public void updateWorkingMode(Long projectId) {
		guavaWorkingModeCache.invalidate(projectId);
	}

	public void updateDeviceWareVerion() {
		deviceWareVersionCache.invalidate(AppType.RPM);
	}

	public void updateDeviceWareDataBlock() {
		deviceWareDataBlockCache.invalidateAll();
	}

	public void updateProjectDevs(Long projectId) {
		guavaDevicesCache.invalidate(projectId);
	}

	public Map<String, Object> getSessionContext(String sessionId) throws ExecutionException {
		return guavaAppSersionCache.get(sessionId, new Callable<HashMap<String, Object>>() {
			@Override
			public HashMap<String, Object> call() throws Exception {
				return new HashMap<String, Object>();
			}
		});
	}

	public void removeSessionContext(String sessionId) {
		guavaAppSersionCache.invalidate(sessionId);
	}

	private DelayQueue<DevValveFlag> devBlockDownloadValve = new DelayQueue<>();
	private Map<String, DevValveFlag> devValveMap = new ConcurrentHashMap<>();

	public void devBlockValueCheck() {
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					DevValveFlag delayedItem = devBlockDownloadValve.poll();
					if (delayedItem != null) {
						delayedItem.run();
						continue;
					}
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}

	public boolean accessDownload(String devNo) {
		if (devValveMap.size() > 30)
			return false;
		if (devNo == null)
			return false;

		boolean isDevIn = devValveMap.containsKey(devNo);
		if (isDevIn)
			return false;
		DevValveFlag devValveFlag = new DevValveFlag(devNo, 1);
		devValveMap.put(devNo, devValveFlag);
		devBlockDownloadValve.put(devValveFlag);
		return true;
	}

	private class DevValveFlag implements Delayed, Runnable {
		private String devNo;
		private long workTime;
		private long submitTime;

		public DevValveFlag(String devNo, long workTime) {
			super();
			this.devNo = devNo;
			this.workTime = workTime;
			this.submitTime = TimeUnit.NANOSECONDS.convert(workTime, TimeUnit.MINUTES) + System.nanoTime();
		}

		@Override
		public int compareTo(Delayed o) {
			if (o == null || !(o instanceof DevValveFlag))
				return 1;
			if (o == this)
				return 0;
			DevValveFlag s = (DevValveFlag) o;
			if (this.workTime > s.workTime) {
				return 1;
			} else if (this.workTime == s.workTime) {
				return 0;
			} else {
				return -1;
			}
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(submitTime - System.nanoTime(), TimeUnit.NANOSECONDS);
		}

		@Override
		public void run() {
			devValveMap.remove(this.devNo);
		}

	}
}
