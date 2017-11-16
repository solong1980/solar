package com.solar.command.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AccountResponse;
import com.solar.common.context.RoleType;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoAccount;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
public class LoginCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(LoginCmdProcessor.class);
	private SoAccountService accountService;

	public LoginCmdProcessor() {
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("login : {}", json);
		}
		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		String passwd = account.getPassword();
		SoAccount dbAccount = accountService.select(account);
		// @SuppressWarnings("deprecation")
		// String md = Hashing.md5().newHasher().putString(passwd,
		// Charsets.UTF_8).hash().toString();

		if (null == dbAccount) {
			account.setMsg("帐号不存在");
			appSession.sendMsg(new AccountResponse(JsonUtilTool.toJson(account)));
		} else if (dbAccount.getPassword().equals(passwd)) {
			dbAccount.setMsg("登陆成功");
			dbAccount.setRoleType(RoleType.OPERATOR);
			dbAccount.setPassword(null);
			appSession.sendMsg(new AccountResponse(JsonUtilTool.toJson(dbAccount)));
		} else {
			account.setMsg("密码错误");
			appSession.sendMsg(new AccountResponse(JsonUtilTool.toJson(account)));
		}

	}
}
