package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.impl.SoAccountDao;
import com.solar.entity.SoAccount;

/**
 * @author long liang hua
 */
public class SoAccountService {
	private static SoAccountService accountService = new SoAccountService();
	private SoAccountMapper accountDao;

	public SoAccountService() {
		super();
	}

	public static SoAccountService getInstance() {
		return accountService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		accountDao = new SoAccountDao(sqlSessionFactory);
	}

	public SoAccount selectById(Long id) {
		return accountDao.selectById(id);
	}

	public SoAccount selectByAccount(String acc) {
		return accountDao.selectByAccount(acc);
	}
}
