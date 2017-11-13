package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoWorkingModeMapper;
import com.solar.entity.SoWorkingMode;

public class SoWorkingModeDao implements SoWorkingModeMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoWorkingModeDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoWorkingMode selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoWorkingModeMapper mapper = sqlSession.getMapper(SoWorkingModeMapper.class);
			SoWorkingMode workingMode = mapper.selectById(id);
			return workingMode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Integer insert(SoWorkingMode soWorkingMode) {
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoWorkingModeMapper mapper = sqlSession.getMapper(SoWorkingModeMapper.class);
			result = mapper.insert(soWorkingMode);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public SoWorkingMode selectLastOne() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoWorkingModeMapper mapper = sqlSession.getMapper(SoWorkingModeMapper.class);
			SoWorkingMode workingMode = mapper.selectLastOne();
			return workingMode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
