package com.wangyi.news.splash.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 熊亦涛
 * @time 16/6/23  09:30
 * @desc Splash界面广告的图片url
 */
public class AdInfo implements Serializable {
    public List<String> res_url;
    public ActionInfo action_params;

    public List<String> getRes_url() {
        return res_url;
    }

    public void setRes_url(List<String> res_url) {
        this.res_url = res_url;
    }

    public ActionInfo getAction_params() {
        return action_params;
    }

    public void setAction_params(ActionInfo action_params) {
        this.action_params = action_params;
    }
}
