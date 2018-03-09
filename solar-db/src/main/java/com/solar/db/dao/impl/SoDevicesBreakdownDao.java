package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesBreakdownMapper;
import com.solar.entity.SoDevicesBreakdown;

public class SoDevicesBreakdownDao implements SoDevicesBreakdownMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoDevicesBreakdownDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public Integer insert(SoDevicesBreakdown devicesBreakdown) {
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		SoDevicesBreakdownMapper mapper = null;
		try {
			mapper = sqlSession.getMapper(SoDevicesBreakdownMapper.class);
			result = mapper.insert(devicesBreakdown);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

}
