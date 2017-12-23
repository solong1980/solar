package com.solar.command.message.response.app;

import java.io.IOException;

import com.solar.command.message.response.ServerResponse;
import com.solar.common.context.ConnectAPI;

/**
 * Created by kevin on 2016/6/22.
 */
public class ErrorResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param message
     */
    public ErrorResponse(String message) throws IOException {
        super(1,ConnectAPI.ERROR_RESPONSE);
        output.writeUTF(message);
       	output.close();
       // entireMsg();
    }
}
