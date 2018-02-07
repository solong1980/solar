package com.solar.db.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.solar.common.context.RoleType;
import com.solar.common.util.VCodeUtil;
import com.solar.db.dao.SoAccountLocationMapper;
import com.solar.db.dao.SoAccountMapper;
import com.solar.db.dao.impl.SoAccountDao;
import com.solar.db.dao.impl.SoAccountLocationDao;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoPage;
import com.solar.entity.SoPrivilege;
import com.solar.entity.SoProject;

/**
 * @author long liang hua
 */
public class SoAccountService {
	private static final Logger logger = LoggerFactory.getLogger(SoAccountService.class);

	private static SoAccountService accountService = new SoAccountService();
	private SoAccountMapper accountDao;
	private SoAccountLocationMapper accountLocationDao;

	private AtomicLong SEED_LINE = new AtomicLong(System.currentTimeMillis());

	public SoAccountService() {
		super();
	}

	public static SoAccountService getInstance() {
		return accountService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		accountDao = new SoAccountDao(sqlSessionFactory);
		accountLocationDao = new SoAccountLocationDao(sqlSessionFactory);
	}

	public SoAccount selectById(Long id) {
		return accountDao.selectById(id);
	}

	public SoAccount selectByAccount(String acc) {
		return accountDao.selectByAccount(acc);
	}

	/**
	 * 根据帐号,手机号,email查询
	 * 
	 * @param account
	 * @return
	 */
	public List<SoAccount> selectByAccount(SoAccount account) {
		return accountDao.selectBySoAccount(account);
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

	public List<SoAccountLocation> queryGovernmentLocation(Long accountId) {
		return accountLocationDao.selectByAccountId(accountId);
	}

	public List<String> queryGovernmentLocationIds(Long accountId) {
		List<SoAccountLocation> governmentLocation = queryGovernmentLocation(accountId);
		List<String> locationIds = new ArrayList<>();
		for (SoAccountLocation soAccountLocation : governmentLocation) {
			locationIds.add(soAccountLocation.getLocationId());
		}
		return locationIds;
	}

	public SoPage<SoAccount, List<SoAccount>> queryAccount(SoPage<SoAccount, List<SoAccount>> accountPage) {
		Integer total = accountDao.queryAccountCount(accountPage);
		List<SoAccount> accounts = accountDao.queryAccount(accountPage);
		accountPage.setT(accounts);
		accountPage.setTotal(total);
		return accountPage;
	}

	public String genVcode() {
		String serialCode = VCodeUtil.toSerialCode(SEED_LINE.getAndIncrement());
		return serialCode;
	}

	public void auditAgree(SoAccount account) {
		Long id = account.getId();
		SoAccount dbAccount = accountDao.selectById(account.getId());

		int type = dbAccount.getType();
		RoleType roleType = RoleType.roleType(type);
		switch (roleType) {
		case OPERATOR:
			// query locations
			// List<SoAccountLocation> accountLocations =
			// accountLocationDao.selectByAccountId(id);
			// List<String> locationIds = new ArrayList<>();
			// for (SoAccountLocation soAccountLocation : accountLocations) {
			// locationIds.add(soAccountLocation.getLocationId());
			// }

			// store projects chooses by administrator,this time no use
			List<SoProject> projectsInLocations = account.getProjects();
			// query projects in the locations
			// List<SoProject> projectsInLocations =
			// projectService.queryProjectByLocationIds(locationIds);

			List<SoPrivilege> privileges = new ArrayList<>();
			if (projectsInLocations != null && !projectsInLocations.isEmpty()) {
				// if operator add relation to privilege and update status
				for (SoProject soProject : projectsInLocations) {
					SoPrivilege privilege = new SoPrivilege();
					privilege.setAccountId(id);
					privilege.setLocationId(soProject.getLocationId());
					privilege.setProjectId(soProject.getId());
					privileges.add(privilege);
				}
			}
			accountDao.agreeOperatorAccount(account, privileges);
			break;
		default:
			// update status
			accountDao.updateStatus(account);
			break;
		}
	}

	public void auditReject(SoAccount account) {
		// update status
		accountDao.updateStatus(account);
	}

	public void updateAccount(SoAccount account) {
		accountDao.updateAccount(account);
	}
}
