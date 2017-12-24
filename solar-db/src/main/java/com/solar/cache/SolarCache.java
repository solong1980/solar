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
import com.solar.db.services.SoProjectWorkingModeService;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProjectWorkingMode;

public class SolarCache {

	private static SolarCache solarCache = new SolarCache();

	private Cache<Long, SoProjectWorkingMode> guavaWorkingModeCache;
	private Cache<Long, List<SoDevices>> guavaDevicesCache;
	private Cache<String, Map<String, Object>> guavaAppSersionCache;

	private SoProjectWorkingModeService workingModeService;
	private SoDevicesService devicesService;

	public static SolarCache getInstance() {
		return solarCache;
	}

	private SolarCache() {
		guavaWorkingModeCache = CacheBuilder.newBuilder().initialCapacity(1000).build();
		guavaDevicesCache = CacheBuilder.newBuilder().initialCapacity(1000).build();

		guavaAppSersionCache = CacheBuilder.newBuilder().initialCapacity(30).expireAfterAccess(30, TimeUnit.MINUTES)
				.build();
		workingModeService = SoProjectWorkingModeService.getInstance();
		devicesService = SoDevicesService.getInstance();
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

	public void updateWorkingMode(Long projectId) {
		guavaWorkingModeCache.invalidate(projectId);
		// SoWorkingMode workingMode
		// guavaWorkingModeCache.put(custId, workingMode);
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

	public void removeSessionContext(String sessionId) throws ExecutionException {
		guavaAppSersionCache.invalidate(sessionId);
	}
}
