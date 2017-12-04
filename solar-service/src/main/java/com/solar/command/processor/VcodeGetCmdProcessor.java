package com.solar.command.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.VCodeResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.Consts.GenVCodeType;
import com.solar.common.context.ErrorCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoAccountFindService;
import com.solar.db.services.SoAccountService;
import com.solar.entity.SoVCode;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.VCODE_GET_COMMAND)
public class VcodeGetCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(VcodeGetCmdProcessor.class);
	private SoAccountFindService accountFindService;
	private SoAccountService accountService;

	public VcodeGetCmdProcessor() {
		accountFindService = SoAccountFindService.getInstance();
		accountService = SoAccountService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("login : {}", json);
		}
		SoVCode soVCode = JsonUtilTool.fromJson(json, SoVCode.class);
		if (null == soVCode) {
			soVCode = new SoVCode();
			soVCode.setMsg(ErrorCode.Error_000007);
			soVCode.setRetCode(SoVCode.FAILURE);
			appSession.sendMsg(new VCodeResponse(JsonUtilTool.toJson(soVCode)));
		} else {
			int type = soVCode.getType();
			Map<String, Object> sessionContext = SolarCache.getInstance().getSessionContext(appSession.getSessionID());
			String genVcode = "8888888";

			GenVCodeType genVCodeType = GenVCodeType.type(type);
			switch (genVCodeType) {
			case REGIEST:
				genVcode = accountService.genVcode();
				sessionContext.put(Consts.REGIEST_VCODE_KEY, genVcode);

				break;
			default:
				genVcode = accountFindService.genVcode();
				sessionContext.put(Consts.ACCOUNT_FIND_VCODE_KEY, genVcode);
				break;
			}
			soVCode.setVcode(genVcode);
			// 发送短信
			logger.info("send vcode:" + genVcode);
			appSession.sendMsg(new VCodeResponse(JsonUtilTool.toJson(soVCode)));
		}
	}
}
