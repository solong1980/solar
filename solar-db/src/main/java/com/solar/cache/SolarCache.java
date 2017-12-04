package com.solar.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoWorkingModeService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoWorkingMode;

public class SolarCache {

	private static SolarCache solarCache = new SolarCache();

	private Cache<Long, SoWorkingMode> guavaWorkingModeCache;
	private Cache<Long, List<SoDevices>> guavaDevicesCache;
	private Cache<String, Map<String, Object>> guavaAppSersionCache;

	private SoWorkingModeService workingModeService;
	private SoDevicesService devicesService;

	public static SolarCache getInstance() {
		return solarCache;
	}

	private SolarCache() {
		guavaWorkingModeCache = CacheBuilder.newBuilder().initialCapacity(1000).build();
		guavaDevicesCache = CacheBuilder.newBuilder().initialCapacity(1000).build();

		guavaAppSersionCache = CacheBuilder.newBuilder().initialCapacity(30).expireAfterAccess(30, TimeUnit.MINUTES)
				.build();

		workingModeService = SoWorkingModeService.getInstance();
		devicesService = SoDevicesService.getInstance();
	}

	public SoWorkingMode getWorkingMode(Long custId) throws ExecutionException {
		SoWorkingMode value = guavaWorkingModeCache.get(custId, new Callable<SoWorkingMode>() {
			@Override
			public SoWorkingMode call() throws Exception {
				return workingModeService.selectLastOne();
			}
		});
		return value;
	}

	public List<SoDevices> getDevices(Long custId) throws ExecutionException {
		List<SoDevices> devs = guavaDevicesCache.get(custId, new Callable<List<SoDevices>>() {
			@Override
			public List<SoDevices> call() throws Exception {
				return devicesService.selectCustDevs(custId);
			}
		});
		return devs;
	}

	public void updateWorkingMode(Long custId) {
		guavaWorkingModeCache.invalidate(custId);
		// SoWorkingMode workingMode = workingModeService.selectLastOne();
		// guavaWorkingModeCache.put(custId, workingMode);
	}

	public Map<String, Object> getSessionContext(String sessionId) throws ExecutionException {
		return guavaAppSersionCache.get(sessionId, new Callable<HashMap<String, Object>>() {
			@Override
			public HashMap<String, Object> call() throws Exception {
				return new HashMap<String, Object>();
			}
		});
	}

	public void removeSessionContext(String sessionId) throws ExecutionException {
		guavaAppSersionCache.invalidate(sessionId);
	}
}
