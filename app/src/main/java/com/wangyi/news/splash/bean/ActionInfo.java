package com.wangyi.news.splash.bean;

import java.io.Serializable;

/**
 * @author 熊亦涛
 * @time 16/6/23  10:50
 * @desc Splash界面图片点击之后跳转的URL
 */
public class ActionInfo implements Serializable {
    public String link_url ;

    @Override
    public String toString() {
        return "ActionInfo{" +
                "link_url='" + link_url + '\'' +
                '}';
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }
}
