package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountFindMapper;
import com.solar.entity.SoAccountFind;

public class SoAccountFindDao implements SoAccountFindMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoAccountFindDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public void addAccountFind(SoAccountFind accountFind) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountFindMapper mapper = sqlSession.getMapper(SoAccountFindMapper.class);
			mapper.addAccountFind(accountFind);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
