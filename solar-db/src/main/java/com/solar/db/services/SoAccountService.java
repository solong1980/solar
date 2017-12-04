package com.solar.db.services;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.solar.common.util.VCodeUtil;
import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.impl.SoAccountDao;
import com.solar.entity.SoAccount;

/**
 * @author long liang hua
 */
public class SoAccountService {
	private static final Logger logger = LoggerFactory.getLogger(SoAccountService.class);

	private static SoAccountService accountService = new SoAccountService();
	private SoAccountMapper accountDao;
	private AtomicLong SEED_LINE = new AtomicLong(System.currentTimeMillis());

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

	public void regiest(SoAccount account) {

		String password = account.getPassword();
		if (password == null) {
			password = "00000000";
		}
		@SuppressWarnings("deprecation")
		String md = Hashing.md5().newHasher().putString(password, Charsets.UTF_8).hash().toString();
		logger.error("trans pd " + password + " to " + md);
		account.setPassword(md);
		accountDao.addAccount(account);
	}

	public String genVcode() {
		String serialCode = VCodeUtil.toSerialCode(SEED_LINE.getAndIncrement());
		return serialCode;
	}
}
