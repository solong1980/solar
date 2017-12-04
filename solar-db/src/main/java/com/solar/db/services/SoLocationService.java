package com.solar.db.services;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
	
	public void createLocationFile() {
		
		JSONObject locationsObject = new JSONObject();
		JSONArray provinceArray = new JSONArray();
		locationsObject.put("Province", provinceArray);
		
		List<SoProvinces> allProvinces = getAllProvinces();
		for (SoProvinces soProvinces : allProvinces) {
			JSONObject provinceObject = new JSONObject();
			JSONArray citiesArray = new JSONArray();

			provinceObject.put("Id", soProvinces.getProvinceid());
			provinceObject.put("Name", soProvinces.getProvince());
			provinceObject.put("City", citiesArray);
			provinceArray.add(provinceObject);

			String provinceid = soProvinces.getProvinceid();
			List<SoCities> citiesInProvice = getCitiesInProvice(provinceid);
			for (SoCities soCities : citiesInProvice) {
				JSONObject cityObject = new JSONObject();
				JSONArray areasArray = new JSONArray();
				cityObject.put("Id", soCities.getCityid());
				cityObject.put("Name", soCities.getCity());
				cityObject.put("Area", areasArray);
				citiesArray.add(cityObject);
				
				String cityid = soCities.getCityid();
				List<SoAreas> areasInCity = getAreasInCity(cityid);
				for (SoAreas soAreas : areasInCity) {
					JSONObject areaObject = new JSONObject();
					areaObject.put("Id", soAreas.getAreaid());
					areaObject.put("Name", soAreas.getArea());
					areasArray.add(areaObject);
				}
			}
		}
		
		//System.out.println(JsonUtilTool.toJson(locationsObject));
	}
}
