package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoPrivilegeMapper;
import com.solar.entity.SoPrivilege;
import com.solar.entity.SoProject;

public class SoPrivilegeDao implements SoPrivilegeMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoPrivilegeDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public List<SoPrivilege> selectByAccountId(Long accountId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoPrivilegeMapper mapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			List<SoPrivilege> privileges = mapper.selectByAccountId(accountId);
			return privileges;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	public void addPrivilege(List<SoPrivilege> privileges) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoPrivilegeMapper mapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			mapper.addPrivilege(privileges);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	public List<String> selectOwnerLocations(Long accountId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoPrivilegeMapper mapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			List<String> locationIds = mapper.selectOwnerLocations(accountId);
			return locationIds;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoProject> queryOwnerProjects(Long accountId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoPrivilegeMapper mapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			List<SoProject> projects = mapper.queryOwnerProjects(accountId);
			return projects;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void deleteByAccountId(Long accountId) {

	}

	@Override
	public void deleteBy(Long projectId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoPrivilegeMapper mapper = sqlSession.getMapper(SoPrivilegeMapper.class);
			mapper.deleteBy(projectId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}