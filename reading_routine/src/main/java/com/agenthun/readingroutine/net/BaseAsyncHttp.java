package com.agenthun.readingroutine.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/30 上午8:45.
 */
public class BaseAsyncHttp extends AsyncHttpClient {
    public static final String HOST = "https://api.douban.com";

    public static void postReq(String host, String url, RequestParams params, JsonHttpResponseHandler handler) {
        new AsyncHttpClient().post(host + url, params, handler);
    }

    public static void postReq(String url, RequestParams params, JsonHttpResponseHandler handler) {
        new AsyncHttpClient().post(HOST + url, params, handler);
    }

    public static void getReq(String host, String url, RequestParams params, JsonHttpResponseHandler handler) {
        new AsyncHttpClient().get(host + url, params, handler);
    }

    public static void getReq(String url, RequestParams params, JsonHttpResponseHandler handler) {
        new AsyncHttpClient().get(HOST + url, params, handler);
    }

    public static void downloadFile(String url, FileDownloadHandler handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);
    }
}
