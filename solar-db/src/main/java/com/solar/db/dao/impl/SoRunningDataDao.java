package com.solar.db.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoRunningDataMapper;
import com.solar.entity.SoRunningData;

public class SoRunningDataDao implements SoRunningDataMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoRunningDataDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoRunningData selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoRunningDataMapper mapper = sqlSession.getMapper(SoRunningDataMapper.class);
			SoRunningData selectById = mapper.selectById(id);
			return selectById;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Integer insert(SoRunningData runningData) {
		int result = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		SoRunningDataMapper mapper = null;
		try {
			mapper = sqlSession.getMapper(SoRunningDataMapper.class);
			result = mapper.insert(runningData);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public Long selectMaxId() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoRunningDataMapper mapper = sqlSession.getMapper(SoRunningDataMapper.class);
			return mapper.selectMaxId();
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoRunningData> selectLastRunntionData(String devNo, int limit) {
		List<SoRunningData> runningDatas = new ArrayList<>(0);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoRunningDataMapper mapper = sqlSession.getMapper(SoRunningDataMapper.class);
			runningDatas = mapper.selectLastRunntionData(devNo, limit);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
		return runningDatas;
	}

}
