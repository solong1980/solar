package com.solar.web.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.cache.annotation.Cacheable;

import com.solar.common.context.RoleType;
import com.solar.db.InitDBServers;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoAccount;

/**
 * 认证
 */
public class UserRealm extends AuthorizingRealm {

	SoAccountService accountService;

	public UserRealm(CacheManager cacheManager) {
		super(cacheManager);
		try {
			InitDBServers.getInstance().initServersFun();
		} catch (IOException e) {
			e.printStackTrace();
		}
		accountService = SoAccountService.getInstance();
	}

	/**
	 * 授权(验证权限时调用)
	 */
	@Override
	@Cacheable(cacheNames = "shiroCache", key = "#{T(com.solar.web.util.ShiroUtils).getUserId()}")
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SoAccount account = (SoAccount) principals.getPrimaryPrincipal();
		List<String> permsList = new ArrayList<>();
		Integer role = account.getRole();
		RoleType roleType = RoleType.roleType(role);
		switch (roleType) {
		case ADMIN:
			permsList.add("/upload");
			break;
		default:
			break;
		}

		// 用户权限列表
		Set<String> permsSet = new HashSet<String>();
		for (String perms : permsList) {
			if (StringUtils.isBlank(perms)) {
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		SoAccount account = accountService.selectByAccount(username);

		// 账号不存在
		if (account == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		// 密码错误
		if (!password.equals(account.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, password, getName());
		return info;
	}

	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		SoAccount user = (SoAccount) principals.getPrimaryPrincipal();
		Long userId = user.getId();
		return userId;
	}

}
