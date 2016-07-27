package com.wangyi.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.wangyi.news.constants.NetCons;

import java.io.File;

/**
 * @author 熊亦涛
 * @time 16/6/23  16:06
 * @desc 图片工具类, 提供文件保存的目录的方法以及校验某图片是否已经存在并且是有效的
 */
public class ImageUtils {
    /**
     * 检查图片是否存在,而且是有效的
     *
     * @param ImageName
     * @return
     */
    public static boolean checkIsDownloaded(String ImageName) {
        //获取图片保存的路径
        File imageDir = getImageDir();
        if (!imageDir.exists()) {
            return false;
        }
        //获取图片的路径,如果路径不存在,表明文件不存在
        File image = new File(imageDir.getAbsolutePath(), ImageName + ".jpg");
        if (!image.exists() || image.length() <= 0) {
            return false;
        }
        //图片存在,校验是否能正常解析
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        if (bitmap == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取图片文件的目录即   /mnt/sdcard/xiongyitao
     *
     * @return
     */
    public static File getImageDir() {
        File sdDir = Environment.getExternalStorageDirectory();
        return new File(sdDir.getAbsolutePath(), NetCons.IMAGE_DIR_NAME);
    }

    /**
     * 根据图片的路径生成Bitmap
     * @param path
     * @return
     */
    public static Bitmap getImageBitmap(String path) {
        //生成一张bitmap的时候的配置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path, options);
    }
}
