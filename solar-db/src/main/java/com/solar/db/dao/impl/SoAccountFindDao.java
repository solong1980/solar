package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountFindMapper;
import com.solar.db.dao.SoAccountMapper;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoPage;

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

	@Override
	public List<SoAccountFind> queryAccountFind(SoPage<SoAccountFind, List<SoAccountFind>> page) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountFindMapper mapper = sqlSession.getMapper(SoAccountFindMapper.class);
			List<SoAccountFind> accountFinds = mapper.queryAccountFind(page);
			return accountFinds;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Integer queryAccountFindCount(SoPage<SoAccountFind, List<SoAccountFind>> accountFindPage) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountFindMapper mapper = sqlSession.getMapper(SoAccountFindMapper.class);
			Integer total = mapper.queryAccountFindCount(accountFindPage);
			return total;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public SoAccountFind getAccountFindById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountFindMapper mapper = sqlSession.getMapper(SoAccountFindMapper.class);
			SoAccountFind accountFind = mapper.getAccountFindById(id);
			return accountFind;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void doAuditAgree(SoAccountFind accountFind, SoAccount account) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountFindMapper accountFindMapper = sqlSession.getMapper(SoAccountFindMapper.class);
			SoAccountMapper accountMapper = sqlSession.getMapper(SoAccountMapper.class);
			// update find back status
			accountFindMapper.updateStatus(accountFind);
			// update account phone and new password
			accountMapper.updateAccountPhonePwd(account);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void updateStatus(SoAccountFind accountFind) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountFindMapper accountFindMapper = sqlSession.getMapper(SoAccountFindMapper.class);
			// update find back status
			accountFindMapper.updateStatus(accountFind);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void doAuditReject(SoAccountFind accountFind) {
		updateStatus(accountFind);
	}

}
