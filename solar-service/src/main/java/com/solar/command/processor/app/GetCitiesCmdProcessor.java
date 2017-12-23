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
import com.solar.entity.SoCities;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ADDR_CITIES_QUERY_COMMAND)
public class GetCitiesCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private SoLocationService locationService;

	public GetCitiesCmdProcessor() {
		super();
		locationService = SoLocationService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		SoCities soCities = JsonUtilTool.fromJson(json, SoCities.class);
		List<SoCities> soCitiesList = locationService.getCitiesInProvice(soCities.getProvinceid());
		if (soCitiesList == null) {
			appSession.sendMsg(new LocationResponse(ConnectAPI.ADDR_CITIES_QUERY_RESPONSE,
					JsonUtilTool.toJson(Collections.EMPTY_LIST)));
		} else {
			json = JsonUtilTool.toJson(soCitiesList);
			appSession.sendMsg(new LocationResponse(ConnectAPI.ADDR_CITIES_QUERY_RESPONSE, json));
		}
	}

}
