package com.solar.db.dao;

import java.util.List;

import com.solar.entity.SoDevices;

public interface SoDevicesMapper {

	SoDevices selectById(Long id);

	List<SoDevices> selectByCustId(Long custId);

	List<SoDevices> allDevs();

}