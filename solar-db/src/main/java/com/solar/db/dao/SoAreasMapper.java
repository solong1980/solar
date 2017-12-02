package com.solar.db.dao;

import java.util.List;

import com.solar.entity.SoAreas;

public interface SoAreasMapper {

	SoAreas selectById(Long id);

	List<SoAreas> selectByCityId(String cityId);

}