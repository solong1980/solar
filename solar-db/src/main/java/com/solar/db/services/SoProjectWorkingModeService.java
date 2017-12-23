package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.cache.SolarCache;
import com.solar.db.dao.SoProjectWorkingModeMapper;
import com.solar.db.dao.impl.SoProjectWorkingModeDao;
import com.solar.entity.SoProjectWorkingMode;

/**
 * @author long liang hua
 */
public class SoProjectWorkingModeService {
	private static SoProjectWorkingModeService soProjectWorkingModeService = new SoProjectWorkingModeService();

	private SoProjectWorkingModeMapper projectWorkingModeDao;
	private SolarCache solarCache;

	public SoProjectWorkingModeService() {
		super();
	}

	public static SoProjectWorkingModeService getInstance() {
		return soProjectWorkingModeService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		projectWorkingModeDao = new SoProjectWorkingModeDao(sqlSessionFactory);
		solarCache = SolarCache.getInstance();
	}

	/**
	 * 插入一条设置数据
	 * 
	 * @param runningData
	 * @return 插入数量
	 */
	public Integer insertWorkingMode(SoProjectWorkingMode workingMode) {
		// 插入后需要更新缓存
		Integer result = projectWorkingModeDao.insert(workingMode);
		if (result > 0)
			solarCache.updateWorkingMode(1L);
		return result;
	}

	/**
	 * 修改一条设置数据
	 * 
	 * @param runningData
	 * @return 插入数量
	 */
	public Integer updateWorkingMode(SoProjectWorkingMode workingMode) {
		// 插入后需要更新缓存
		Integer result = projectWorkingModeDao.update(workingMode);
		if (result > 0)
			solarCache.updateWorkingMode(workingMode.getProjectId());
		return result;
	}

	public SoProjectWorkingMode selectByProjectId(Long projectId) {
		SoProjectWorkingMode workingMode = projectWorkingModeDao.selectByProjectId(projectId);
		return workingMode;
	}

}
