package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoProjectMapper;
import com.solar.db.dao.SoProjectWorkingModeMapper;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.entity.SoProjectWorkingMode;

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
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			SoProjectWorkingModeMapper projectWorkingModeMapper = sqlSession
					.getMapper(SoProjectWorkingModeMapper.class);
			mapper.addProject(project);

			SoProjectWorkingMode mode = project.getProjectWorkingMode();
			if (mode != null) {
				mode.setProjectId(project.getId());
				projectWorkingModeMapper.insert(mode);
			}
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void updateProject(SoProject project) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			SoProjectWorkingModeMapper projectWorkingModeMapper = sqlSession
					.getMapper(SoProjectWorkingModeMapper.class);
			mapper.updateProject(project);
			SoProjectWorkingMode mode = project.getProjectWorkingMode();
			if (mode != null) {
				mode.setProjectId(project.getId());
				projectWorkingModeMapper.update(mode);
			}
			sqlSession.commit();
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

	@Override
	public List<SoProject> queryProjects(SoPage<SoProject, List<SoProject>> project) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			List<SoProject> projects = mapper.queryProjects(project);
			return projects;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Integer queryProjectCount(SoPage<SoProject, List<SoProject>> page) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			Integer total = mapper.queryProjectCount(page);
			return total;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public String calcProjectIchg(List<SoDevices> projectDevs) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoProjectMapper mapper = sqlSession.getMapper(SoProjectMapper.class);
			String ichg = mapper.calcProjectIchg(projectDevs);
			return ichg;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}