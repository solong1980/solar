package com.solar.db.services;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAreasMapper;
import com.solar.db.dao.SoCitiesMapper;
import com.solar.db.dao.SoProvincesMapper;
import com.solar.db.dao.impl.SoAreasDao;
import com.solar.db.dao.impl.SoCitiesDao;
import com.solar.db.dao.impl.SoProvincesDao;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;

/**
 * @author long liang hua
 */
public class SoLocationService {
	private static SoLocationService locationService = new SoLocationService();

	private SoProvincesMapper provincesDao;
	private SoCitiesMapper citiesDao;
	private SoAreasMapper areasDao;

	public SoLocationService() {
		super();
	}

	public static SoLocationService getInstance() {
		return locationService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		provincesDao = new SoProvincesDao(sqlSessionFactory);
		citiesDao = new SoCitiesDao(sqlSessionFactory);
		areasDao = new SoAreasDao(sqlSessionFactory);
	}

	public List<SoProvinces> getAllProvinces() {
		return provincesDao.selectALL();
	}

	public List<SoCities> getCitiesInProvice(String proviceId) {
		return citiesDao.selectByProvinceId(proviceId);
	}

	public List<SoAreas> getAreasInCity(String cityId) {
		return areasDao.selectByCityId(cityId);
	}
}
