package com.solar.db.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesMapper;
import com.solar.db.dao.impl.SoDevicesDao;
import com.solar.entity.SoDevices;

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
}
