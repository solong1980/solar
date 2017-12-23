package com.solar.command.processor.app;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.AccountAddResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.ErrorCode;
import com.solar.common.context.SuccessCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountFindService;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoAccountLocation;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.ACCOUNT_FINDBACK_COMMAND)
public class AccountFindbackCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(AccountFindbackCmdProcessor.class);
	private SoAccountFindService accountFindService;

	public AccountFindbackCmdProcessor() {
		accountFindService = SoAccountFindService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("login : {}", json);
		}
		SoAccountFind accountFind = JsonUtilTool.fromJson(json, SoAccountFind.class);
		if (null == accountFind) {
			accountFind = new SoAccountFind();
			accountFind.setMsg(ErrorCode.Error_000001);
			accountFind.setRetCode(SoAccount.FAILURE);
			appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(accountFind)));
		} else {
			String vcode = accountFind.getVcode();
			Map<String, Object> sessionContext = SolarCache.getInstance().getSessionContext(appSession.getSessionID());
			Object object = sessionContext.get(Consts.ACCOUNT_FIND_VCODE_KEY);
			if (null == object) {
				// 未生成验证码
				accountFind = new SoAccountFind();
				accountFind.setRetCode(SoAccount.FAILURE);
				accountFind.setMsg(ErrorCode.Error_000009);
				appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(accountFind)));
			} else if (vcode == null || !vcode.equals(object)) {
				// 验证码不对
				accountFind = new SoAccountFind();
				accountFind.setRetCode(SoAccount.FAILURE);
				accountFind.setMsg(ErrorCode.Error_000008);
				appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(accountFind)));
			} else {

				List<SoAccountLocation> locations = accountFind.getLocations();
				StringBuilder ser = new StringBuilder();
				for (SoAccountLocation soAccountLocation : locations) {
					ser.append(soAccountLocation.getLocationId()).append(",");
				}
				accountFind.setLocationIds(ser.substring(0, ser.length() - 1));
				accountFindService.addAccountFind(accountFind);
				accountFind = new SoAccountFind();
				accountFind.setRetCode(SoAccount.SUCCESS);
				accountFind.setMsg(SuccessCode.Sucess_000001);
				appSession.sendMsg(new AccountAddResponse(JsonUtilTool.toJson(accountFind)));
				sessionContext.remove(Consts.REGIEST_VCODE_KEY);
			}
		}
	}
}
