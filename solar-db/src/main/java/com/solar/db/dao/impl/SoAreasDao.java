package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAreasMapper;
import com.solar.entity.SoAreas;

public class SoAreasDao implements SoAreasMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoAreasDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoAreas selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAreasMapper mapper = sqlSession.getMapper(SoAreasMapper.class);
			SoAreas areas = mapper.selectById(id);
			return areas;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoAreas> selectByCityId(String cityId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAreasMapper mapper = sqlSession.getMapper(SoAreasMapper.class);
			List<SoAreas> areasList = mapper.selectByCityId(cityId);
			return areasList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
