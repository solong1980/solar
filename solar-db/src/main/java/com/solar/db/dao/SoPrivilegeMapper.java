package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoPrivilege;
import com.solar.entity.SoProject;

public interface SoPrivilegeMapper {

	List<SoPrivilege> selectByAccountId(Long accountId);

	void addPrivilege(@Param("list") List<SoPrivilege> privileges);

	List<String> selectOwnerLocations(Long accountId);

	List<SoProject> queryOwnerProjects(Long accountId);

	void deleteByAccountId(Long accountId);

	void deleteBy(Long projectId);

}