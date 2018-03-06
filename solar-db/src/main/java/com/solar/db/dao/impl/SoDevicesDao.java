package com.solar.db.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoDevicesMapper;
import com.solar.entity.SoDevices;
import com.solar.entity.SoDevicesBatch;
import com.solar.entity.SoPage;

public class SoDevicesDao implements SoDevicesMapper {
	private SqlSessionFactory sqlSessionFactory;

	public SoDevicesDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SoDevices selectById(Long id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			SoDevices account = mapper.selectById(id);
			return account;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoDevices> selectByProjectId(Long projectId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> soDevicesList = mapper.selectByProjectId(projectId);
			return soDevicesList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoDevices> allDevs() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> soDevicesList = mapper.allDevs();
			return soDevicesList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public SoDevices selectByDevNo(String devNo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			SoDevices account = mapper.selectByDevNo(devNo);
			return account;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void update(SoDevices devices) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			mapper.update(devices);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void insert(SoDevices devices) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			SoDevices existDevNo = mapper.selectByDevNo(devices.getDevNo());
			if (existDevNo != null) {
				devices.setId(existDevNo.getId());
				mapper.update(devices);
			} else {
				mapper.insert(devices);
			}
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<SoDevices> queryDevices(SoPage<SoDevices, List<SoDevices>> page) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			List<SoDevices> devices = mapper.queryDevices(page);
			return devices;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void delete(SoDevices devices) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			SoDevicesMapper mapper = sqlSession.getMapper(SoDevicesMapper.class);
			mapper.delete(devices);
			sqlSession.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void batch(SoDevicesBatch devicesBatch) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			List<SoDevices> batchAdds = devicesBatch.getBatchAdds();
			if (batchAdds != null) {
				for (SoDevices devices : batchAdds) {
					insert(devices);
				}
			}
			List<SoDevices> batchDels = devicesBatch.getBatchDels();
			if (batchDels != null) {
				for (SoDevices devices : batchDels) {
					delete(devices);
				}
			}
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw new RuntimeException(e);
		} finally {
			sqlSession.close();
		}
	}

}
