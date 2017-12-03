package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountLocationMapper;
import com.solar.db.dao.SoAccountMapper;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;

public class SoAccountLocationDao implements SoAccountLocationMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoAccountLocationDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public List<SoAccountLocation> selectByAccountId(String accountId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountLocationMapper mapper = sqlSession.getMapper(SoAccountLocationMapper.class);
			List<SoAccountLocation> accountLocations = mapper.selectByAccountId(accountId);
			return accountLocations;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void addAccountLocations(List<SoAccountLocation> accountLocations) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountLocationMapper mapper = sqlSession.getMapper(SoAccountLocationMapper.class);
			mapper.addAccountLocations(accountLocations);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
