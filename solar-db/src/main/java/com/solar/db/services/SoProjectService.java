package com.solar.db.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoProjectMapper;
import com.solar.db.dao.SoProjectWorkingModeMapper;
import com.solar.db.dao.impl.SoProjectDao;
import com.solar.db.dao.impl.SoProjectWorkingModeDao;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.entity.SoProjectWorkingMode;

/**
 * @author long liang hua
 */
public class SoProjectService {
	private static SoProjectService devicesService = new SoProjectService();
	private SoProjectMapper projectDao;
	private SoProjectWorkingModeMapper projectWorkingModeDao;

	public SoProjectService() {
		super();
	}

	public static SoProjectService getInstance() {
		return devicesService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		projectDao = new SoProjectDao(sqlSessionFactory);
		projectWorkingModeDao = new SoProjectWorkingModeDao(sqlSessionFactory);
	}

	public SoProject selectById(Long id) {
		SoProject project = projectDao.selectById(id);
		Long projectId = project.getId();
		SoProjectWorkingMode mode = projectWorkingModeDao.selectByProjectId(projectId);
		project.setProjectWorkingMode(mode);
		return project;
	}

	public void addProject(SoProject project) {
		projectDao.addProject(project);
	}

	public void updataProject(SoProject project) {
		projectDao.updateProject(project);
	}

	public void deleteProject(Long id) {
		projectDao.deleteProject(id);
	}

	public List<SoProject> queryProjectByLocationId(String locationId) {
		/**
		 * 判断级别
		 */
		if (locationId.length() != 6) {
			throw new RuntimeException("");
		}
		String query = "";
		String areaCode = locationId.substring(4, 6);
		if ("00".equals(areaCode)) {
			query = locationId.substring(0, 4);
		}
		String cityCode = locationId.substring(2, 4);
		if ("00".equals(cityCode)) {
			query = locationId.substring(0, 2);
		}
		String provinceCode = locationId.substring(0, 2);
		if ("00".equals(provinceCode)) {
			query = "";
		}
		return projectDao.queryProjectByLocationId(query);
	}

	/**
	 * 根据location id list查询所有管辖的项目
	 * 
	 * @param locations
	 * @return
	 */
	public List<SoProject> queryProjectByLocationIds(List<String> locations) {
		if (locations == null || locations.isEmpty())
			return Collections.emptyList();
		List<String> querys = new ArrayList<>();
		for (String locationId : locations) {
			if (locationId.length() != 6) {
				throw new RuntimeException("");
			}
			String query = locationId;
			String areaCode = locationId.substring(4, 6);
			String cityCode = locationId.substring(2, 4);
			// String provinceCode = locationId.substring(0, 2);
			if ("00".equals(cityCode)) {
				query = locationId.substring(0, 2);
			} else if ("00".equals(areaCode)) {
				query = locationId.substring(0, 4);
			}
			querys.add(query);
		}
		return projectDao.queryProjectByLocationIds(querys);
	}

	/**
	 * 根据location id list查询所有管辖的项目
	 * 
	 * @param locations
	 * @return
	 */
	public SoPage<SoProject, List<SoProject>> queryProjects(SoPage<SoProject, List<SoProject>> page) {
		SoProject c = page.getC();

		String locationId = c.getLocationId();
		if (locationId != null && !locationId.isEmpty()) {
			if (locationId.length() != 6) {
				throw new RuntimeException("");
			}
			String areaCode = locationId.substring(4, 6);
			String cityCode = locationId.substring(2, 4);
			// String provinceCode = locationId.substring(0, 2);
			if ("00".equals(cityCode)) {
				locationId = locationId.substring(0, 2);
			} else if ("00".equals(areaCode)) {
				locationId = locationId.substring(0, 4);
			}
		}
		c.setLocationId(locationId);
		List<SoProject> projects = projectDao.queryProjects(page);
		Integer total = projectDao.queryProjectCount(page);
		page.setT(projects);
		page.setTotal(total);
		return page;
	}

	public String calcProjectPchg(List<SoDevices> projectDevs) {
		String ichg = projectDao.calcProjectPchg(projectDevs);
		return ichg;
	}
}
