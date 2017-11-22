package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAppVersionMapper;
import com.solar.entity.SoAppVersion;

public class SoAppVersionDao implements SoAppVersionMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoAppVersionDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoAppVersion selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAppVersionMapper mapper = sqlSession.getMapper(SoAppVersionMapper.class);
			SoAppVersion appVersion = mapper.selectById(id);
			return appVersion;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public SoAppVersion selectLastVersion(int type) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAppVersionMapper mapper = sqlSession.getMapper(SoAppVersionMapper.class);
			SoAppVersion appVersion = mapper.selectLastVersion(type);
			return appVersion;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
