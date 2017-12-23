package com.solar.command.processor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.ServerResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.entity.SoDataServerInfo;
import com.solar.server.commons.session.AppSession;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.DATA_SERVER_QUERY_COMMAND)
public class DataServerInfoCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DataServerInfoCmdProcessor.class);
	private SolarCache solarCache;

	public DataServerInfoCmdProcessor() {
		solarCache = SolarCache.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("Data update : {}", json);
		}
		SoDataServerInfo dataServerInfo = JsonUtilTool.fromJson(json, SoDataServerInfo.class);
		dataServerInfo.setServerIP("123.56.76.77");
		dataServerInfo.setPort(10122);
		appSession.sendMsg(
				ServerResponse.build(0, ConnectAPI.DATA_SERVER_QUERY_RESPONSE, JsonUtilTool.toJson(dataServerInfo)));
	}
}
