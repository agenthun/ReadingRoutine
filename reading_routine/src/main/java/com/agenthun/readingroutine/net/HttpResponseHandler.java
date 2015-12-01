package com.agenthun.readingroutine.net;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/30 下午12:12.
 */
public abstract class HttpResponseHandler extends JsonHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        onJsonHttpSuccess(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onJsonHttpFailure(errorResponse);
    }

    public abstract void onJsonHttpSuccess(JSONObject response);

    public abstract void onJsonHttpFailure(JSONObject response);
}
