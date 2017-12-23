package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoRunningData;

public interface SoRunningDataMapper {

	SoRunningData selectById(Long id);

	Integer insert(SoRunningData runningData);

	List<SoRunningData> selectLastRunntionData(@Param("uuid")String devNo, @Param("count")int limit);

	Long selectMaxId();
}