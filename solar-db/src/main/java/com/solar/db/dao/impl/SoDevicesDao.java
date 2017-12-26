package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesMapper;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;

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
	public List<SoDevices> selectByProjectId(Long projectId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> soDevicesList = mapper.selectByProjectId(projectId);
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

	@Override
	public SoDevices selectByDevNo(String devNo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			SoDevices account = mapper.selectByDevNo(devNo);
			return account;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void update(SoDevices devices) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			mapper.update(devices);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void insert(SoDevices devices) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			mapper.insert(devices);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoDevices> queryDevices(SoPage<SoDevices, List<SoDevices>> page) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> devices = mapper.queryDevices(page);
			return devices;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
