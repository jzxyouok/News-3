package com.wangyi.news.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.wangyi.news.R;
import com.wangyi.news.constants.NetCons;
import com.wangyi.news.main.IndexActivity;
import com.wangyi.news.splash.bean.ActionInfo;
import com.wangyi.news.splash.bean.AdInfo;
import com.wangyi.news.splash.bean.AdsInfo;
import com.wangyi.news.splash.listener.OnRingClickListener;
import com.wangyi.news.splash.service.DownLoadService;
import com.wangyi.news.utils.HttpResponse;
import com.wangyi.news.utils.HttpUtil;
import com.wangyi.news.utils.ImageUtils;
import com.wangyi.news.utils.IntentUtils;
import com.wangyi.news.utils.JsonUtil;
import com.wangyi.news.utils.Md5Helper;
import com.wangyi.news.utils.SharePreferenceUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends Activity implements OnRingClickListener {

    @Bind(R.id.iv_splash)
    ImageView mIvSplash;
    @Bind(R.id.ring)
    com.wangyi.news.splash.widget.RingTextView mRing;
    //传递给DownLoadService的数据
    public static final String ADS_NAME = "ads";
    //保存至sp的json
    public static final String SPLASH_JSON = "splash_json";
    //超时长
    public static final String TIME_OUT = "time_out";
    //上一次下载的时间
    public static final String LAST_TIME = "last_time";
    //之前保存图片的索引
    public static final String IMAGE_INDEX = "index";
    //传递到WebView的参数
    public static final String ACTION_NAME = "action_info";
    private AdInfo mAdInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //获取缓存好的json
        String json = SharePreferenceUtil.getString(this, SPLASH_JSON);
        //获取网络数据
        getDatas(json);
        //加载图片
        loadImage(json);
        mRing.start(3000);
        mRing.setOnRingClickListener(this);
    }

    private void getDatas(String json) {
        //为空表示之前没有缓存
        if (TextUtils.isEmpty(json)) {
            //重新获取数据
            getAdasFromInternet();
        } else {
            //比较当前时间和上次请求网络的时间,如果超时就重新请求网络
            long newTime = System.currentTimeMillis();
            long lastTme = SharePreferenceUtil.getLong(this, LAST_TIME);
            long timeOut = SharePreferenceUtil.getLong(this, TIME_OUT);
            //如果超时就重新请求网络
            if ((newTime - lastTme) * 1000 * 60 > timeOut) {
                getAdasFromInternet();
            }
        }
    }

    private void getAdasFromInternet() {
        HttpUtil.getInstance().doGet(NetCons.SPLASH_URL, new HttpResponse<String>(String.class) {
            @Override
            public void onSuccess(String json) {
                //将json缓存在本地
                SharePreferenceUtil.putString(SplashActivity.this, SPLASH_JSON, json);
                //将json解析成Ads对象
                AdsInfo ads = JsonUtil.parse(json, AdsInfo.class);
                //保存当前获取图片资源的时间,还有超时的时间
                long time = System.currentTimeMillis();
                SharePreferenceUtil.putLong(SplashActivity.this, LAST_TIME, time);
                SharePreferenceUtil.putLong(SplashActivity.this, TIME_OUT, ads.getNext_req());

                //开启服务,在服务中进行下载以及缓存操作
                Intent intent = new Intent(SplashActivity.this, DownLoadService.class);
                intent.putExtra(ADS_NAME, ads);
                startService(intent);
            }

            @Override
            public void onFailure(String failed) {

            }
        });
    }

    /**
     * 加载ImageView
     *
     * @param json 从SharePreference中取出的json
     */
    private void loadImage(String json) {
        if (TextUtils.isEmpty(json)) {
            //handler.sendEmptyMessageDelayed(GOTO_INDEX, 2000);
        } else {
            //解析json拿到图片的url
            AdsInfo temp = JsonUtil.parse(json, AdsInfo.class);
            List<AdInfo> ads = temp.ads;
            //获取之前保存的照片的索引
            int index = SharePreferenceUtil.getInt(SplashActivity.this, IMAGE_INDEX);
            //获取之前保存的图片的url
            mAdInfo = ads.get(index);
            String url = mAdInfo.getRes_url().get(0);
            //获取保存在本地的路径
            File imageDir = ImageUtils.getImageDir();
            //获取图片的路径
            File image = new File(imageDir, Md5Helper.toMD5(url) + ".jpg");
            Logger.d(image.getAbsolutePath());
            //判断是否存在
            if (image.exists() && image.length() > 0) {
                Bitmap bitmap = ImageUtils.getImageBitmap(image.getAbsolutePath());
                if (bitmap != null) {
                    mIvSplash.setImageBitmap(bitmap);
                    //保存新的index
                    index = index + 1 >= ads.size() ? 0 : index + 1;
                    //保存到SharePreference中
                    SharePreferenceUtil.putInt(SplashActivity.this, IMAGE_INDEX, index);
                    //通过tag将数据传进去
                    mIvSplash.setTag(mAdInfo.action_params);
                    mIvSplash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                            ActionInfo action = (ActionInfo) mIvSplash.getTag();
                            if (action != null && !TextUtils.isEmpty(action.getLink_url())) {
                                intent.putExtra(ACTION_NAME, action);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }
                        }
                    });
                }

            } else {
                //如果图片不存在则重新获取数据
                getAdasFromInternet();
            }
        }
    }

    @Override
    public void onClick(View view) {
        IntentUtils.startActivityAndFinish(this,IndexActivity.class);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
