package com.example.vollryhttpdemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.example.vollryhttpdemo.network.RequestFactory;
import com.example.vollryhttpdemo.network.RequestManager;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by AndyOn on 2015/9/12.
 */
public class VolleyApplication extends Application{

    private static VolleyApplication sInstance;//app入口本身

    private  DisplayImageOptions options;//tImageLoader图片加载配置
//    private RequestManager httpUtils;//基于volley二次封装的请求工具类
    private  Gson gson;//格式化转换数据第三方工具类
    private ImageLoader mImageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ApiStoreSDK.init(this, "337020e557227243748205369d73ac48");
        /**  初始化请求发生器工厂  */
        RequestFactory.getInstance().init(this);
        initImageLoader(getApplicationContext());
    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)// 加载图片的线程数
                .denyCacheImageMultipleSizesInMemory() // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置磁盘缓存文件名称\
                .memoryCacheExtraOptions(400, 480) // max width, max height，即保存的每个缓存文件的最大长宽   //设置图片的大小
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .build();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .build();// 构建完成

        ImageLoader.getInstance().init(config);//全局初始化此配置
    }
    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized VolleyApplication getInstance() {
        return sInstance;
    }
    /**
     * 设置图片
     *
     * @param url
     * @param imageView
     */
    public void settingImg(String url, final ImageView imageView, final ProgressBar bar) {
        getImageLoader().displayImage(url,imageView, options,new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                //开始加载
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                //加载失败
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1,Bitmap arg2) {
                if(bar!=null){
                    bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                //加载取消
            }
        });
    }

    /**
     * 获取ImageLoader
     * @return
     */
    public ImageLoader getImageLoader() {
        if(mImageLoader==null){
            mImageLoader = ImageLoader.getInstance();
        }
        return this.mImageLoader;
    }


    /**
     * 获取GSON
     * @return
     */
    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
