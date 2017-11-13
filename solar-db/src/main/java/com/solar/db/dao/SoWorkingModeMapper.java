package com.solar.db.dao;

import com.solar.entity.SoWorkingMode;

public interface SoWorkingModeMapper {

	SoWorkingMode selectById(Long id);

	SoWorkingMode selectLastOne();

	Integer insert(SoWorkingMode runningData);

}