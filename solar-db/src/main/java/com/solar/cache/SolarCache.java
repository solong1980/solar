package com.solar.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.solar.common.context.Consts;
import com.solar.db.services.SoWorkingModeService;
import com.solar.entity.SoWorkingMode;

public class SolarCache {

	private static SolarCache solarCache = new SolarCache();

	private Cache<String, SoWorkingMode> guavaCache;
	private SoWorkingModeService workingModeService;

	public static SolarCache getInstance() {
		return solarCache;
	}

	private SolarCache() {
		guavaCache = CacheBuilder.newBuilder().initialCapacity(1).build();
		workingModeService = SoWorkingModeService.getInstance();
	}

	public SoWorkingMode getWorkingMode() throws ExecutionException {
		SoWorkingMode value = guavaCache.get(Consts.WORKING_MODE_KEY, new Callable<SoWorkingMode>() {
			@Override
			public SoWorkingMode call() throws Exception {
				return workingModeService.selectLastOne();
			}
		});
		return value;
	}

	public void updateWorkingMode() {
		SoWorkingMode workingMode = workingModeService.selectLastOne();
		guavaCache.put(Consts.WORKING_MODE_KEY, workingMode);
	}
}
