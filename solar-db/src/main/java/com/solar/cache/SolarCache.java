package com.solar.cache;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

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

	private SoWorkingModeService workingModeService;
	private SoDevicesService devicesService;

	public static SolarCache getInstance() {
		return solarCache;
	}

	private SolarCache() {
		guavaWorkingModeCache = CacheBuilder.newBuilder().initialCapacity(1000).build();
		guavaDevicesCache = CacheBuilder.newBuilder().initialCapacity(1000).build();
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

}
