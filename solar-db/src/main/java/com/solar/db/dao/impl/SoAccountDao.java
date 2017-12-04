package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountLocationMapper;
import com.solar.db.dao.SoAccountMapper;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;

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

	@Override
	public void addAccount(SoAccount soAccount) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			SoAccountLocationMapper locationMapper = sqlSession.getMapper(SoAccountLocationMapper.class);
			mapper.addAccount(soAccount);
			Long id = soAccount.getId();
			List<SoAccountLocation> locations = soAccount.getLocations();
			for (SoAccountLocation accountLocation : locations) {
				accountLocation.setAccountId(id);
			}
			locationMapper.addAccountLocations(locations);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
