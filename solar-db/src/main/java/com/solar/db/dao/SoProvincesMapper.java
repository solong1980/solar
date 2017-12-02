package com.solar.db.dao;

import java.util.List;

import com.solar.entity.SoProvinces;

public interface SoProvincesMapper {

	SoProvinces selectById(Long id);

	List<SoProvinces> selectALL();

}