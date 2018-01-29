package com.shallowan.milutv.hostlive;

import com.shallowan.milutv.utils.request.BaseRequest;

import java.io.IOException;

/**
 * Created by Administrator.
 */

public class HeartBeatRequest extends BaseRequest {
    private static final String Action = "http://imoocbearlive.butterfly.mopaasapp.com/roomServlet?action=heartBeat";

    private static final String RequestParamKey_RoomId = "roomId";
    private static final String RequestParamKey_UserId = "userId";

    public String getUrl(String roomId, String userId) {
        return Action
                + "&" + RequestParamKey_RoomId + "=" + roomId
                + "&" + RequestParamKey_UserId + "=" + userId
                ;
    }

    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100, e.getMessage());
    }


    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code, "服务出现异常");
    }


    @Override
    protected void onResponseSuccess(String body) {

    }
}
