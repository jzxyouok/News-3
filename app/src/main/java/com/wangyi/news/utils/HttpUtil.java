package com.wangyi.news.utils;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 熊亦涛
 * @time 16/7/4  09:28
 * @desc ${TODD}
 */
public class HttpUtil {
    private static HttpUtil mHttpUtil;
    private OkHttpClient client;

    /**
     * 传递get参数对应的map集合,返回拼接之后的字符串信息
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 私有构造器
     */
    private HttpUtil() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static HttpUtil getInstance() {
        if (mHttpUtil == null) {
            synchronized (HttpUtil.class) {
                if (mHttpUtil == null) {
                    mHttpUtil = new HttpUtil();
                }
            }
        }
        return mHttpUtil;
    }

    public void doGet(String url, final HttpResponse httpResponse) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            private String content;

            @Override
            public void onFailure(Call call, IOException e) {
                //连接服务器异常
                httpResponse.onFailure("连接服务器异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //数据请求失败
                if (!response.isSuccessful()) {
                    httpResponse.onFailure("连接服务器异常");
                } else {
                    //响应内容
                    content = response.body().string();
                    //解析response
                    httpResponse.parseResponse(content);
                }
            }
        });
    }
}
