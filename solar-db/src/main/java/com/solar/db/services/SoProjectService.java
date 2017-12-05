package com.solar.db.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoProjectMapper;
import com.solar.db.dao.impl.SoProjectDao;
import com.solar.entity.SoProject;

/**
 * @author long liang hua
 */
public class SoProjectService {
	private static SoProjectService devicesService = new SoProjectService();
	private SoProjectMapper projectDao;

	public SoProjectService() {
		super();
	}

	public static SoProjectService getInstance() {
		return devicesService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		projectDao = new SoProjectDao(sqlSessionFactory);
	}

	public SoProject selectById(Long id) {
		return projectDao.selectById(id);
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
			//String provinceCode = locationId.substring(0, 2);
			if ("00".equals(cityCode)) {
				query = locationId.substring(0, 2);
			} else if ("00".equals(areaCode)) {
				query = locationId.substring(0, 4);
			}
			querys.add(query);
		}
		return projectDao.queryProjectByLocationIds(querys);
	}

}
