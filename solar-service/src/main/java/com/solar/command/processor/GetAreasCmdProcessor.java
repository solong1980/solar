package com.solar.command.processor;

import java.util.Collections;
import java.util.List;

import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.LocationResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.controller.common.MsgProcessor;
import com.solar.db.services.SoLocationService;
import com.solar.entity.SoAreas;
import com.solar.server.commons.session.AppSession;

@ProcessCMD(API_CODE = ConnectAPI.ADDR_AREAS_QUERY_COMMAND)
public class GetAreasCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private SoLocationService locationService;

	public GetAreasCmdProcessor() {
		super();
		locationService = SoLocationService.getInstance();
	}

	@Override
	public void process(AppSession appSession, ClientRequest request) throws Exception {
		String json = request.getString();
		SoAreas areas = JsonUtilTool.fromJson(json, SoAreas.class);
		List<SoAreas> areaList = locationService.getAreasInCity(areas.getCityid());
		if (areaList == null) {
			appSession.sendMsg(new LocationResponse(ConnectAPI.ADDR_AREAS_QUERY_RESPONSE,
					JsonUtilTool.toJson(Collections.EMPTY_LIST)));
		} else {
			json = JsonUtilTool.toJson(areaList);
			appSession.sendMsg(new LocationResponse(ConnectAPI.ADDR_AREAS_QUERY_RESPONSE, json));
		}
	}

}
