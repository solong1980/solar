package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAppVersionMapper;
import com.solar.db.dao.impl.SoAppVersionDao;
import com.solar.entity.SoAppVersion;

/**
 * @author long liang hua
 */
public class SoAppVersionService {
	private static SoAppVersionService appVersionService = new SoAppVersionService();
	private SoAppVersionMapper appVersionDao;

	public SoAppVersionService() {
		super();
	}

	public static SoAppVersionService getInstance() {
		return appVersionService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		appVersionDao = new SoAppVersionDao(sqlSessionFactory);
	}

	public SoAppVersion selectById(Long id) {
		return appVersionDao.selectById(id);
	}

	public SoAppVersion selectLastAppVersion(int type) {
		return appVersionDao.selectLastVersion(type);
	}

	public void addNewVersion(SoAppVersion appVersion) {
		appVersionDao.addNewVersion(appVersion);
	}
}
