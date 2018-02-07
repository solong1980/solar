package com.solar.db.dao.impl;

import java.util.ArrayList;
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
import com.solar.entity.SoProject;

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
	
	/**
	 * update account all info include address and privilege
	 * @param account
	 */
	@Override
	public void updateAccount(SoAccount account) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper accountMapper = sqlSession.getMapper(SoAccountMapper.class);
			SoAccountLocationMapper accountLocationMapper = sqlSession.getMapper(SoAccountLocationMapper.class);
			SoPrivilegeMapper privilegeMapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			accountMapper.updateAccount(account);
			List<SoAccountLocation> locations = account.getLocations();
			Long accountId = account.getId();
			if (!locations.isEmpty()) {
				// do delete and insert
				accountLocationMapper.deleteByAccountId(accountId);
				accountLocationMapper.addAccountLocations(locations);
			}
			List<SoProject> projects = account.getProjects();
			if (!projects.isEmpty()) {
				List<SoPrivilege> privileges = new ArrayList<>();
				for (SoProject project : projects) {
					SoPrivilege privilege = new SoPrivilege();
					privilege.setAccountId(accountId);
					privilege.setLocationId(project.getLocationId());
					privilege.setProjectId(project.getId());
					privileges.add(privilege);
				}
				// do delete and update
				privilegeMapper.deleteByAccountId(accountId);
				privilegeMapper.addPrivilege(privileges);
			}

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
			if(!privileges.isEmpty())
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

	@Override
	public void updateAccountPhonePwd(SoAccount account) {
		
	}

	@Override
	public Integer queryAccountCount(SoPage<SoAccount, List<SoAccount>> accountPage) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoAccountMapper mapper = sqlSession.getMapper(SoAccountMapper.class);
			Integer total = mapper.queryAccountCount(accountPage);
			return total;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
