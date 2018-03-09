package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesBreakdownMapper;
import com.solar.db.dao.impl.SoDevicesBreakdownDao;
import com.solar.entity.SoDevicesBreakdown;

/**
 * @author long liang hua
 */
public class SoDevicesBreakdownService {
	private static SoDevicesBreakdownService devicesService = new SoDevicesBreakdownService();

	private SoDevicesBreakdownMapper devicesBreakdownDao;

	public SoDevicesBreakdownService() {
		super();
	}

	public static SoDevicesBreakdownService getInstance() {
		return devicesService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		devicesBreakdownDao = new SoDevicesBreakdownDao(sqlSessionFactory);
	}

	public void insert(SoDevicesBreakdown devicesBreakdown) {
		devicesBreakdownDao.insert(devicesBreakdown);
	}
}
