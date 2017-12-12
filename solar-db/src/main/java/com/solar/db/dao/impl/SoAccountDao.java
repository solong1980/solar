package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountLocationMapper;
import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.SoPrivilegeMapper;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoPage;
import com.solar.entity.SoPrivilege;

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

	@Override
	public SoAccount selectByPhone(String matchPhone) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			SoAccount matchedAccount = mapper.selectByPhone(matchPhone);
			return matchedAccount;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void updateAccount(SoAccount account) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			mapper.updateAccount(account);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoAccount> selectBySoAccount(SoAccount account) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			List<SoAccount> accounts = mapper.selectBySoAccount(account);
			return accounts;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoAccount> queryAccount(SoPage<SoAccount, List<SoAccount>> accountPage) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			List<SoAccount> accounts = mapper.queryAccount(accountPage);
			return accounts;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void agreeOperatorAccount(SoAccount account, List<SoPrivilege> privileges) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper accountMapper = sqlSession.getMapper(SoAccountMapper.class);
			SoPrivilegeMapper privilegeMapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			accountMapper.updateStatus(account);
			privilegeMapper.addPrivilege(privileges);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void updateStatus(SoAccount account) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper accountMapper = sqlSession.getMapper(SoAccountMapper.class);
			accountMapper.updateStatus(account);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
