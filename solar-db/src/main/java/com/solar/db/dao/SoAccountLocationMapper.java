package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoAccountLocation;

public interface SoAccountLocationMapper {

	List<SoAccountLocation> selectByAccountId(Long accountId);

	void addAccountLocations(@Param("locations") List<SoAccountLocation> accountLocations);

	void deleteByAccountId(Long accountId);

}