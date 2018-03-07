package com.solar.db.services;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoRunningDataMapper;
import com.solar.db.dao.impl.SoRunningDataDao;
import com.solar.entity.SoRunningData;

/**
 * @author long liang hua
 */
public class SoRunningDataService {
	private static SoRunningDataService soRunningDataService = new SoRunningDataService();

	private SoRunningDataMapper runningDataDao;

	public SoRunningDataService() {
		super();
	}

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
	 * 
	 * @param runningData
	 * @return 插入数量
	 */
	public Integer insertRunningData(SoRunningData runningData) {
		return runningDataDao.insert(runningData);
	}

	public List<SoRunningData> getLastRunningData(String devNo, int count) {
		return runningDataDao.selectLastRunntionData(devNo, count);
	}
	
	public SoRunningData selectLastMonitorData(String devNo) {
		return runningDataDao.selectLastMonitorData(devNo);
	}

}
