package com.solar.db.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.mail.MessagingException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.solar.common.context.ErrorCode;
import com.solar.common.util.VCodeUtil;
import com.solar.db.dao.SoAccountFindMapper;
import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.impl.SoAccountDao;
import com.solar.db.dao.impl.SoAccountFindDao;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoPage;
import com.solar.mns.email.MailServer;

/**
 * @author long liang hua
 */
public class SoAccountFindService {
	private static final Logger logger = LoggerFactory.getLogger(SoAccountFindService.class);

	private static SoAccountFindService accountService = new SoAccountFindService();
	private SoAccountFindMapper accountFindDao;
	private SoAccountMapper accountDao;

	private AtomicLong SEED_LINE = new AtomicLong(System.currentTimeMillis());

	public SoAccountFindService() {
		super();
	}

	public static SoAccountFindService getInstance() {
		return accountService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		accountFindDao = new SoAccountFindDao(sqlSessionFactory);
		accountDao = new SoAccountDao(sqlSessionFactory);
	}

	public void addAccountFind(SoAccountFind accountFind) {
		accountFindDao.addAccountFind(accountFind);
	}

	public SoPage<SoAccountFind, List<SoAccountFind>> queryAccountFind(SoPage<SoAccountFind, List<SoAccountFind>> accountFindPage) {
		List<SoAccountFind> accountFinds = accountFindDao.queryAccountFind(accountFindPage);
		Integer total = accountFindDao.queryAccountFindCount(accountFindPage);
		accountFindPage.setT(accountFinds);
		accountFindPage.setTotal(total);
		return accountFindPage;
	}

	public String genVcode() {
		String serialCode = VCodeUtil.toSerialCode(SEED_LINE.getAndIncrement());
		return serialCode;
	}

	public void auditAgree(SoAccountFind accountFind) {
		// query by id
		SoAccountFind dbAccountFind = accountFindDao.getAccountFindById(accountFind.getId());
		// find match
		String matchPhone = dbAccountFind.getOldPhone();
		if (matchPhone == null) {
			matchPhone = dbAccountFind.getPhone();
		}
		if (matchPhone == null) {
			throw new RuntimeException(ErrorCode.Error_000010);
		}
		SoAccount account = accountDao.selectByPhone(matchPhone);
		if (account == null) {
			throw new RuntimeException(ErrorCode.Error_000010);
		} else {
			String password = genVcode();// as password
			String phone = dbAccountFind.getPhone();
			logger.error("gen new password for account findback match phone is " + matchPhone + ",new password is "
					+ password);
			account.setPhone(phone);

			@SuppressWarnings("deprecation")
			String md = Hashing.md5().newHasher().putString(password, Charsets.UTF_8).hash().toString();

			account.setPassword(md);
			// update status
			// phone and new password
			accountFindDao.doAuditAgree(accountFind, account);
			String email = account.getEmail();
			// send email with account,password
			try {
				MailServer.getInstance().send(email, "率找回信息", "你的新手机号" + phone + "," + "新密码" + password);
			} catch (MessagingException e) {
				logger.error("send mail fail", e);
				throw new RuntimeException("帐号更新成功,发送邮件失败!");
			}
		}

	}

	public void auditReject(SoAccountFind accountFind) {
		accountFindDao.doAuditReject(accountFind);
	}
}
