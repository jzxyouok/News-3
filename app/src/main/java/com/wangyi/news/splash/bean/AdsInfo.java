package com.wangyi.news.splash.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 熊亦涛
 * @time 16/7/27  15:03
 * @desc ${TODD}
 */
public class AdsInfo implements Serializable{
    public int result;
    public List<AdInfo> ads;
    public int next_req;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<AdInfo> getAds() {
        return ads;
    }

    public void setAds(List<AdInfo> ads) {
        this.ads = ads;
    }

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    @Override
    public String toString() {
        return "AdsInfo{" +
                "result=" + result +
                ", ads=" + ads +
                ", next_req=" + next_req +
                '}';
    }
}
