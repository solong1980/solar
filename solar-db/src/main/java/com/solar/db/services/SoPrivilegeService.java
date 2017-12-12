package com.solar.db.services;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.db.dao.SoPrivilegeMapper;
import com.solar.db.dao.impl.SoPrivilegeDao;
import com.solar.entity.SoPrivilege;

/**
 * @author long liang hua
 */
public class SoPrivilegeService {
	private static final Logger logger = LoggerFactory.getLogger(SoPrivilegeService.class);

	private static SoPrivilegeService accountService = new SoPrivilegeService();
	private SoPrivilegeMapper privilegeDao;

	public SoPrivilegeService() {
		super();
	}

	public static SoPrivilegeService getInstance() {
		return accountService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		privilegeDao = new SoPrivilegeDao(sqlSessionFactory);
	}

	public void addPrivileges(List<SoPrivilege> privileges) {
		privilegeDao.addPrivilege(privileges);
	}

	public List<String> queryOwnerLocationIds(Long accountId) {
		return privilegeDao.selectOwnerLocations(accountId);
	}

}
