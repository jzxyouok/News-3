package com.wangyi.news.splash.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wangyi.news.constants.NetCons;
import com.wangyi.news.splash.SplashActivity;
import com.wangyi.news.splash.bean.AdInfo;
import com.wangyi.news.splash.bean.AdsInfo;
import com.wangyi.news.utils.ImageUtils;
import com.wangyi.news.utils.Md5Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author 熊亦涛
 * @time 16/6/23  13:11
 * @desc 在IntentService中进行下载操作
 */
public class DownLoadService extends IntentService {
    public DownLoadService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //获取Ads对象
        AdsInfo ads = (AdsInfo) intent.getSerializableExtra(SplashActivity.ADS_NAME);
        if (ads != null) {
            //每张图片的下载地址都在每个Ad对象里面
            List<AdInfo> data = ads.ads;
            if (data != null && data.size() > 0) {
                for (AdInfo ad : data) {
                    List<String> imageUrls = ad.getRes_url();
                    if (imageUrls != null) {
                        //图片地址在res_url数组的第一个元素;
                        String imageUrl = imageUrls.get(0);
                        //加密
                        String md5ImageUrl = Md5Helper.toMD5(imageUrl);
                        if (!TextUtils.isEmpty(imageUrl)) {
                            //判断是否缓存过
                            if (!ImageUtils.checkIsDownloaded(md5ImageUrl)) {
                                //缓存图片到本地
                                downloadImage(imageUrl, md5ImageUrl);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 下载图片
     *
     * @param imageUrl
     * @param imageName
     */
    private void downloadImage(String imageUrl, String imageName) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                //先解析成bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                if (bitmap != null) {
                    //将bitmap保存到sd卡
                    saveToSdCard(bitmap, imageName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param bitmap
     */
    private void saveToSdCard(Bitmap bitmap, String imageName) {
        try {
            //sd卡根路径
            File sdDir = Environment.getExternalStorageDirectory();
            //图片保存的路径
            File file = new File(sdDir.getAbsolutePath(), NetCons.IMAGE_DIR_NAME);
            //创建文件夹...不创建不行
            file.mkdir();
            //图片要保存的路径
            File image = new File(file.getAbsolutePath(), imageName + ".jpg");
            Logger.d(image.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(image);
            //保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
