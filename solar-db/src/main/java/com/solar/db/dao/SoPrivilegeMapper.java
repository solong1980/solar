package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoPrivilege;

public interface SoPrivilegeMapper {

	List<SoPrivilege> selectByAccountId(Long accountId);

	void addPrivilege(@Param("list") List<SoPrivilege> privileges);

	List<String> selectOwnerLocations(Long accountId);

}