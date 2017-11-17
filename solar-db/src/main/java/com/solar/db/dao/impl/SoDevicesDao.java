package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesMapper;
import com.solar.entity.SoDevices;

public class SoDevicesDao implements SoDevicesMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoDevicesDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoDevices selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			SoDevices account = mapper.selectById(id);
			return account;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoDevices> selectByCustId(Long custId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> soDevicesList = mapper.selectByCustId(custId);
			return soDevicesList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoDevices> allDevs() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> soDevicesList = mapper.allDevs();
			return soDevicesList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
