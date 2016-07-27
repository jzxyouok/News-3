package com.wangyi.news.utils;

import android.text.TextUtils;

/**
 * @author 熊亦涛
 * @time 16/6/23  10:59
 * @desc 网络请求回调监听器
 */
public abstract class HttpResponse<T> {
    private Class<T> t;

    /**
     * 提供给外界创建对象时需要指定类型,以方便解析成指定的对象
     */
    public HttpResponse(Class<T> t) {
        this.t = t;
    }

    /**
     * 请求成功,返回的类型是传入的类型
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败,返回失败信息
     *
     * @param failed
     */
    public abstract void onFailure(String failed);

    /**
     * 提供解析方法,将response解析成指定对象
     *
     * @param response
     * @return
     */
    public void parseResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            onFailure("解析失败");
            return;
        }
        //如果请求的是String类型,就不解析直接返回response
        if (t == String.class) {
            onSuccess((T) response);
            return;
        }
        //如果请求的是状态码也返回string
//        if (t == LoadingPager.PagerState.class) {
//
//        }
        //否则请求的是解析好的json对象
        T res = JsonUtil.parse(response, t);
        if (res == null) {
            onFailure("解析失败");
        } else {
            onSuccess(res);
        }
    }
}
