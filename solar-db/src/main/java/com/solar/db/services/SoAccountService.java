package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.entity.SoAccount;

/**
 * @author long liang hua
 */
public class SoAccountService {
	private static SoAccountService accountService = new SoAccountService();

	public SoAccountService() {
		super();
	}

	public static SoAccountService getInstance() {
		return accountService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
	}

	public SoAccount select(SoAccount soAccount) {
		return soAccount;
	}

}
