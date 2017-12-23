package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoProjectWorkingModeMapper;
import com.solar.entity.SoProjectWorkingMode;

public class SoProjectWorkingModeDao implements SoProjectWorkingModeMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoProjectWorkingModeDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoProjectWorkingMode selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectWorkingModeMapper mapper = sqlSession.getMapper(SoProjectWorkingModeMapper.class);
			SoProjectWorkingMode workingMode = mapper.selectById(id);
			return workingMode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Integer insert(SoProjectWorkingMode soProjectWorkingMode) {
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectWorkingModeMapper mapper = sqlSession.getMapper(SoProjectWorkingModeMapper.class);
			result = mapper.insert(soProjectWorkingMode);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public Integer update(SoProjectWorkingMode projectWorkingMode) {
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Long id = projectWorkingMode.getId();
			if (id == null) {
				return 0;
			}
			SoProjectWorkingModeMapper mapper = sqlSession.getMapper(SoProjectWorkingModeMapper.class);
			result = mapper.update(projectWorkingMode);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public SoProjectWorkingMode selectByProjectId(Long projectId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectWorkingModeMapper mapper = sqlSession.getMapper(SoProjectWorkingModeMapper.class);
			SoProjectWorkingMode workingMode = mapper.selectByProjectId(projectId);
			return workingMode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
