package com.solar.db.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoCustomerMapper;
import com.solar.entity.SoCustomer;

public class SoCustomerDao implements SoCustomerMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoCustomerDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoCustomer selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoCustomerMapper mapper = sqlSession.getMapper(SoCustomerMapper.class);
			SoCustomer customer = mapper.selectById(id);
			return customer;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
