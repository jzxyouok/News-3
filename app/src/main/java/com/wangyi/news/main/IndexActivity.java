package com.wangyi.news.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.wangyi.news.R;

/**
 * @author 熊亦涛
 * @time 16/6/25  09:43
 * @desc 主页面
 */
public class IndexActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }
}
