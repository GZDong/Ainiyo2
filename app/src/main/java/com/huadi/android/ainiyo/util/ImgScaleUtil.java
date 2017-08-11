package com.huadi.android.ainiyo.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by zhidong on 2017/8/8.
 */

public class ImgScaleUtil {
    //比例压缩
    public static Bitmap decodeBitmapFromResource(Resources res, int resID, int reqWidth, int reqHeight) {
        //创建位图工厂的配置类
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //设置配置：只加载位图的大小
        options.inJustDecodeBounds = true;
        //让工厂带着这样的配置去加载图片
        BitmapFactory.decodeResource(res, resID, options);
        //根据加载到的大小去计算配置里应该配置的采样率
        options.inSampleSize = computeInSampleSize(options, reqWidth, reqHeight);
        //设置配置：关闭只加载位图的大小
        options.inJustDecodeBounds = false;
        //让工厂带着修改后的配置重新加载图片
        return BitmapFactory.decodeResource(res, resID, options);
    }

    public static int computeInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获得配置的外部属性，也就是图片的原始大小
        final int oldWidth = options.outWidth;
        final int oldHeight = options.outHeight;
        //初始化采样率为1，先从原图和需求的大小开始比较
        int inSampleSize = 1;
        /*//长宽里，只要有一边以上是超出需求大小的，就尝试缩放图片
        while(oldHeight > reqHeight || oldWidth > reqWidth){
            //先缩放一次
            final int halfHeight = oldHeight/2;
            final int halfWidth = oldWidth/2;
            //如果缩放一次后发现长宽都还是比需求的大，那就继续缩放，同时得到临时的采样率
            while ((halfHeight/inSampleSize) >= reqHeight && (halfWidth/inSampleSize) >= reqWidth){
                inSampleSize = inSampleSize *2;
            }
        }*/

        int inSampleSizeW = oldWidth / reqWidth;
        int inSampleSizeH = oldHeight / reqHeight;

        if (inSampleSizeW > inSampleSizeH) {
            inSampleSize = inSampleSizeW;
        } else {
            inSampleSize = inSampleSizeH;
        }
        //得到最终的采样率
        return inSampleSize;

        //说明：结合2个while知道，只有当图片宽和长都大于需求时才会压缩
    }

    //裁剪
    public static Bitmap ScaleBitmap(Bitmap bm, int reqWidth, int reqHeight) {

        // 得到图片原始的高宽
        int rawHeight = bm.getHeight();
        int rawWidth = bm.getWidth();

        // 设定图片新的高宽
        int newHeight = reqHeight;
        int newWidth = reqWidth;

        // 计算缩放因子
        float heightScale = ((float) newHeight) / rawHeight;
        float widthScale = ((float) newWidth) / rawWidth;

        // 新建立矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(heightScale, widthScale);


        //将图片大小压缩
        //压缩后图片的宽和高以及kB大小均会变化
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, rawWidth, rawHeight, matrix, true);

        return newBitmap;
    }
}
