package com.example.administrator.mimovie.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;


import com.example.administrator.mimovie.util.okHttpUtils.CallBackUtil;
import com.example.administrator.mimovie.util.okHttpUtils.OkhttpUtil;

import okhttp3.Call;

/**
 * Created by YangYi on 2018/1/9.
 * 图片处理工具类，通过图片的URL从网络上加载图片
 */

public class SmartImageView extends android.support.v7.widget.AppCompatImageView {

    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 获取图片
     * @param imgPath：图片URL地址
     * @param width：宽
     * @param height：高
     */
    public void showImgFromNet(final String imgPath, final int width, final int height ){
        String url = imgPath;
        OkhttpUtil.okHttpGetBitmap(url, new CallBackUtil.CallBackBitmap(width,height) {
            @Override
            public void onFailure(Call call, Exception e) {//请求失败时调用，执行在UI线程
                //图片加载失败
            }

            @Override
            public void onResponse(Bitmap response) {
                setImageBitmap(response);
            }
        });
    }
}
