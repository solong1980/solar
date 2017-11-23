package com.solar.db.dao;

import com.solar.entity.SoAppVersion;

public interface SoAppVersionMapper {

	SoAppVersion selectById(Long id);

	SoAppVersion selectLastVersion(int type);

	void addNewVersion(SoAppVersion appVersion);

}