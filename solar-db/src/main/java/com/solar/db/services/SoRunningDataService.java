package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoRunningDataMapper;
import com.solar.db.dao.impl.SoRunningDataDao;
import com.solar.entity.SoRunningData;

/**
 * @author long liang hua
 */
public class SoRunningDataService {
	private SoRunningDataMapper runningDataDao;

	private static SoRunningDataService soRunningDataService = new SoRunningDataService();

	public static SoRunningDataService getInstance() {
		return soRunningDataService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		runningDataDao = new SoRunningDataDao(sqlSessionFactory);
	}
	
	public Long getMaxId() {
		Long selectMaxId = runningDataDao.selectMaxId();
		return selectMaxId;
	}
	
	/**
	 * 插入一条上报数据
	 * @param runningData
	 * @return 插入数量
	 */
	public Integer insertRunningData(SoRunningData runningData) {
		return runningDataDao.insert(runningData);
	}
}
