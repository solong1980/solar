package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.SoProvincesMapper;
import com.solar.entity.SoAccount;
import com.solar.entity.SoProvinces;

public class SoProvincesDao implements SoProvincesMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoProvincesDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoProvinces selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProvincesMapper mapper = sqlSession.getMapper(SoProvincesMapper.class);
			SoProvinces provinces = mapper.selectById(id);
			return provinces;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoProvinces> selectALL() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProvincesMapper mapper = sqlSession.getMapper(SoProvincesMapper.class);
			List<SoProvinces> provincesList = mapper.selectALL();
			return provincesList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
