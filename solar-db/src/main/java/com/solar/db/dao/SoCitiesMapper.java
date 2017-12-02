package com.solar.db.dao;

import java.util.List;

import com.solar.entity.SoCities;

public interface SoCitiesMapper {

	SoCities selectById(Long id);

	List<SoCities> selectByProvinceId(String proviceId);

}