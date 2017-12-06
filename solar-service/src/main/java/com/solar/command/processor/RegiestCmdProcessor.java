package com.solar.command.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.AccountAddResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.common.context.SuccessCode;
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
@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_ADD_COMMAND)
public class RegiestCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RegiestCmdProcessor.class);
	private SoAccountService accountService;

	public RegiestCmdProcessor() {
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("login : {}", json);
		}
		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);
		if (null == account) {
			account = new SoAccount();
			account.setMsg(ErrorCode.Error_000001);
			account.setRetCode(SoAccount.FAILURE);
			appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(account)));
		} else {
			String vcode = account.getVcode();
			Map<String, Object> sessionContext = SolarCache.getInstance().getSessionContext(appSession.getSessionID());
			Object object = sessionContext.get(Consts.REGIEST_VCODE_KEY);
			if (null == object) {
				// 未生成验证码
				account = new SoAccount();
				account.setRetCode(SoAccount.FAILURE);
				account.setMsg(ErrorCode.Error_000009);
				appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(account)));
			} else if (vcode == null || !vcode.equals(object)) {
				// 验证码不对
				account = new SoAccount();
				account.setRetCode(SoAccount.FAILURE);
				account.setMsg(ErrorCode.Error_000008);
				appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(account)));
			} else {
				accountService.regiest(account);
				account = new SoAccount();
				account.setRetCode(SoAccount.SUCCESS);
				account.setMsg(SuccessCode.Sucess_000001);
				appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(account)));
				sessionContext.remove(Consts.REGIEST_VCODE_KEY);
			}
		}
	}
}
