package com.solar.db.services;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.common.util.VCodeUtil;
import com.solar.db.dao.SoAccountFindMapper;
import com.solar.db.dao.impl.SoAccountFindDao;
import com.solar.entity.SoAccountFind;

/**
 * @author long liang hua
 */
public class SoAccountFindService {
	private static final Logger logger = LoggerFactory.getLogger(SoAccountFindService.class);

	private static SoAccountFindService accountService = new SoAccountFindService();
	private SoAccountFindMapper accountFindDao;
	
	private AtomicLong SEED_LINE = new AtomicLong(System.currentTimeMillis());

	public SoAccountFindService() {
		super();
	}

	public static SoAccountFindService getInstance() {
		return accountService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		accountFindDao = new SoAccountFindDao(sqlSessionFactory);
	}

	public void addAccountFind(SoAccountFind accountFind) {
		logger.error("add account findback");
		accountFindDao.addAccountFind(accountFind);
	}

	public String genVcode() {
		String serialCode = VCodeUtil.toSerialCode(SEED_LINE.getAndIncrement());
		return serialCode;
	}
}
