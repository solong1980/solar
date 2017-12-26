package com.solar.db.dao;

import java.util.List;

import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;

public interface SoDevicesMapper {

	SoDevices selectById(Long id);

	List<SoDevices> selectByProjectId(Long projectId);

	List<SoDevices> allDevs();

	SoDevices selectByDevNo(String devNo);

	void update(SoDevices devices);

	void insert(SoDevices devices);
	
	List<SoDevices> queryDevices(SoPage<SoDevices, List<SoDevices>> page);
}