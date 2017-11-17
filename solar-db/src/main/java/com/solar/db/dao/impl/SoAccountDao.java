package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountMapper;
import com.solar.entity.SoAccount;

public class SoAccountDao implements SoAccountMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoAccountDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoAccount selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			SoAccount account = mapper.selectById(id);
			return account;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public SoAccount selectByAccount(String acc) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			SoAccount account = mapper.selectByAccount(acc);
			return account;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
