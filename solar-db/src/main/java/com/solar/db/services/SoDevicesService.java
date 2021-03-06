package com.solar.db.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesMapper;
import com.solar.db.dao.impl.SoDevicesDao;
import com.solar.entity.SoDevices;
import com.solar.entity.SoDevicesBatch;
import com.solar.entity.SoPage;

/**
 * @author long liang hua
 */
public class SoDevicesService {
	private static SoDevicesService devicesService = new SoDevicesService();
	private SoDevicesMapper deviceDao;

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

	public List<SoDevices> selectProjectDevs(Long projectId) {
		return deviceDao.selectByProjectId(projectId);
	}

	/**
	 * 划分所有设备的客户map
	 * 
	 * @return
	 */
	public Map<Long, List<SoDevices>> buildProjectDevMap() {
		Map<Long, List<SoDevices>> projectDevMap = new HashMap<>();
		List<SoDevices> devices = deviceDao.allDevs();
		for (SoDevices soDevices : devices) {
			projectDevMap.put(soDevices.getProjectId(), devices);
		}
		return projectDevMap;
	}

	public SoDevices selectByDevNo(String devNo) {
		return deviceDao.selectByDevNo(devNo);
	}

	public void update(SoDevices devices) {
		deviceDao.update(devices);
	}

	public void insert(SoDevices devices) {
		deviceDao.insert(devices);
	}

	public SoPage<SoDevices, List<SoDevices>> queryDevices(SoPage<SoDevices, List<SoDevices>> page) {
		List<SoDevices> devices = deviceDao.queryDevices(page);
		page.setT(devices);
		return page;
	}

	public void delete(SoDevices devices) {
		deviceDao.delete(devices);
	}

	public void batch(SoDevicesBatch devicesBatch) {
		deviceDao.batch(devicesBatch);
	}
}
