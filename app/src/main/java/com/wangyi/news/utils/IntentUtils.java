package com.wangyi.news.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * @author 熊亦涛
 * @time 16/6/25  23:41
 * @desc ${TODD}
 */
public class IntentUtils {

    public static void startActivity(Activity activity, Class<? extends Activity> clz) {
        activity.startActivity(new Intent(activity.getApplicationContext(), clz));
    }

    public static void startActivityAndFinish(Activity activity, Class<? extends Activity> clz) {
        activity.startActivity(new Intent(activity.getApplicationContext(), clz));
        activity.finish();
    }
}
