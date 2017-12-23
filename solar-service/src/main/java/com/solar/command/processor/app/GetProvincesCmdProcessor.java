package com.solar.command.processor.app;

import java.util.Collections;
import java.util.List;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.LocationResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoLocationService;
import com.solar.entity.SoProvinces;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ADDR_PROVINCES_QUERY_COMMAND)
public class GetProvincesCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private SoLocationService locationService;

	public GetProvincesCmdProcessor() {
		super();
		locationService = SoLocationService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		List<SoProvinces> allProvinces = locationService.getAllProvinces();

		if (allProvinces == null) {
			appSession.sendMsg(new LocationResponse(ConnectAPI.ADDR_PROVINCES_QUERY_RESPONSE,
					JsonUtilTool.toJson(Collections.EMPTY_LIST)));
		} else {
			String json = JsonUtilTool.toJson(allProvinces);
			appSession.sendMsg(new LocationResponse(ConnectAPI.ADDR_PROVINCES_QUERY_RESPONSE, json));
		}
	}

}
