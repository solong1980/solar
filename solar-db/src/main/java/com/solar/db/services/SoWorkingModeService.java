package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.cache.SolarCache;
import com.solar.db.dao.SoWorkingModeMapper;
import com.solar.db.dao.impl.SoWorkingModeDao;
import com.solar.entity.SoWorkingMode;

/**
 * @author long liang hua
 */
public class SoWorkingModeService {
	private static SoWorkingModeService soWorkingModeService = new SoWorkingModeService();

	private SoWorkingModeMapper workingModeDao;
	private SolarCache solarCache;

	public SoWorkingModeService() {
		super();
	}

	public static SoWorkingModeService getInstance() {
		return soWorkingModeService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		workingModeDao = new SoWorkingModeDao(sqlSessionFactory);
		solarCache = SolarCache.getInstance();
	}

	/**
	 * 插入一条设置数据
	 * 
	 * @param runningData
	 * @return 插入数量
	 */
	public Integer insertWorkingMode(SoWorkingMode workingMode) {
		// 插入后需要更新缓存
		Integer result = workingModeDao.insert(workingMode);
		if (result > 0)
			solarCache.updateWorkingMode(1L);
		return result;
	}

	public SoWorkingMode selectLastOne() {
		SoWorkingMode workingMode = workingModeDao.selectLastOne();
		return workingMode;
	}

}
