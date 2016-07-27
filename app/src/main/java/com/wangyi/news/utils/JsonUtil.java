package com.wangyi.news.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author 熊亦涛
 * @time 16/6/23  11:22
 * @desc ${TODD}
 */

public class JsonUtil {
    /**
     * 将json转成指定的对象
     *
     * @param json
     * @param clazz
     * @param <T>   要转成的类型
     * @return 转好的对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
