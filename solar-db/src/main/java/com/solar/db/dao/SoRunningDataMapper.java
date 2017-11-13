package com.solar.db.dao;

import com.solar.entity.SoRunningData;

public interface SoRunningDataMapper {

	SoRunningData selectById(Long id);

	Integer insert(SoRunningData runningData);

	Long selectMaxId();
}