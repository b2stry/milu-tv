package com.shallowan.milutv.watcher;

import com.shallowan.milutv.utils.response.ResponseObject;
import com.shallowan.milutv.utils.request.BaseRequest;

import java.io.IOException;

/**
 * Created by ShallowAn.
 */

public class GetWatcherRequest extends BaseRequest {
    private static final String HOST = "http://47.93.242.254:8080/getWatcher";



    public String getUrl(String roomId) {
        return HOST + "?roomId=" + roomId;
    }

    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100,e.toString());
    }

    @Override
    protected void onResponseSuccess(String body) {
        WatcherResponseObj watcherResponseObj = gson.fromJson(body, WatcherResponseObj.class);
        if (watcherResponseObj == null) {
            sendFailMsg(-101, "数据格式错误");
            return;
        }

        if (watcherResponseObj.code.equals(ResponseObject.CODE_SUCCESS)) {
            sendSuccMsg(watcherResponseObj.data);
        } else if (watcherResponseObj.code.equals(ResponseObject.CODE_FAIL)) {
            sendFailMsg(Integer.valueOf(watcherResponseObj.errCode), watcherResponseObj.errMsg);
        }
    }

    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code,"服务器异常");
    }

}
