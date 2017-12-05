package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoProjectMapper;
import com.solar.entity.SoProject;

public class SoProjectDao implements SoProjectMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoProjectDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoProject selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			SoProject selectById = mapper.selectById(id);
			return selectById;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void addProject(SoProject project) {
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			mapper.addProject(project);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void updateProject(SoProject project) {
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			mapper.updateProject(project);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void deleteProject(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			mapper.deleteProject(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoProject> queryProjectByLocationId(String locationId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			List<SoProject> projects = mapper.queryProjectByLocationId(locationId);
			return projects;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoProject> queryProjectByLocationIds(List<String> locationIds) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			List<SoProject> projects = mapper.queryProjectByLocationIds(locationIds);
			return projects;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}