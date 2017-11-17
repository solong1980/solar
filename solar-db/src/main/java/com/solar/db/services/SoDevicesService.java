package com.solar.db.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.cache.SolarCache;
import com.solar.db.dao.SoDevicesMapper;
import com.solar.db.dao.impl.SoDevicesDao;
import com.solar.entity.SoDevices;

/**
 * @author long liang hua
 */
public class SoDevicesService {
	private static SoDevicesService devicesService = new SoDevicesService();
	private SoDevicesMapper deviceDao;
	private SolarCache solarCache;

	public SoDevicesService() {
		super();
	}

	public static SoDevicesService getInstance() {
		return devicesService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		deviceDao = new SoDevicesDao(sqlSessionFactory);
	}

	public SoDevices selectById(Long id) {
		return deviceDao.selectById(id);
	}

	public List<SoDevices> selectCustDevs(Long custId) {
		List<SoDevices> devicesList = deviceDao.selectByCustId(custId);
		return devicesList;
	}

	/**
	 * 划分所有设备的客户map
	 * 
	 * @return
	 */
	public Map<Long, List<SoDevices>> buildCustDevMap() {
		Map<Long, List<SoDevices>> custDevMap = new HashMap<>();
		List<SoDevices> devices = deviceDao.allDevs();
		for (SoDevices soDevices : devices) {
			custDevMap.put(soDevices.getCustId(), devices);
		}
		return custDevMap;
	}
}
