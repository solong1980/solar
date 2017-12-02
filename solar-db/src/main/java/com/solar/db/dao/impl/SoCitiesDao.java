package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.SoCitiesMapper;
import com.solar.db.dao.SoProvincesMapper;
import com.solar.entity.SoAccount;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;

public class SoCitiesDao implements SoCitiesMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoCitiesDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoCities selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoCitiesMapper mapper = sqlSession.getMapper(SoCitiesMapper.class);
			SoCities cities = mapper.selectById(id);
			return cities;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoCities> selectByProvinceId(String proviceId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoCitiesMapper mapper = sqlSession.getMapper(SoCitiesMapper.class);
			List<SoCities> citiesList = mapper.selectByProvinceId(proviceId);
			return citiesList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
