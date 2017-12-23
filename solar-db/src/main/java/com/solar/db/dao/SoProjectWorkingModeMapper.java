package com.solar.db.dao;

import com.solar.entity.SoProjectWorkingMode;

public interface SoProjectWorkingModeMapper {

	SoProjectWorkingMode selectById(Long id);

	Integer insert(SoProjectWorkingMode projectWorkingMode);

	Integer update(SoProjectWorkingMode projectWorkingMode);

	SoProjectWorkingMode selectByProjectId(Long projectId);

}