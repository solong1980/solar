package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoProject;

public interface SoProjectMapper {

	SoProject selectById(Long id);

	void addProject(SoProject project);

	void updateProject(SoProject project);

	void deleteProject(Long id);

	/**
	 * 通过位置id的级别做模糊查询
	 * 
	 * @param locationId
	 * @return
	 */
	List<SoProject> queryProjectByLocationId(@Param("locationId") String locationId);

	/**
	 * 通过级别union模糊查询
	 * 
	 * @param querys
	 * @return
	 */
	List<SoProject> queryProjectByLocationIds(@Param("locationIds") List<String> locationIds);

}