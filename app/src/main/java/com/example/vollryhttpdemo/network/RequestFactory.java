package com.example.vollryhttpdemo.network;

import android.content.Context;
import android.util.Log;
import com.example.vollryhttpdemo.utils.Contants;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 斌斌 on 2015/11/30.
 * 请求发生器工厂（作用是重复请求数据模板并缓存数据，volley有自己的默认缓存机制这里就不用去做缓存，但可以规范模板）
 * 作用于项目比较大的，需要批量处理，不使用用本工厂在Application将RequestFactory.getInstance().init(this);改为RequestManager.getInstance().init(this);
 */
public class RequestFactory{
    private static RequestFactory requestFactory;
    /**
     * RequestFactory
     */
    public static synchronized RequestFactory getInstance() {
        if(requestFactory==null){
            requestFactory=new RequestFactory();
        }
        return requestFactory;
    }

    public void init(Context context) {
        RequestManager.getInstance().init(context);
    }
    /**
     * 拿到开放api的美女图片列表分类
     */
    public void getBelleImageslist(RequestManager.ResponseSuccess responseSuccess){
        RequestManager.getInstance().get(Contants.IMAGE_CLASSIFY, null,responseSuccess, 1);
    }
    /**
     * 拿到开放api的美女图片集
     */
    public void getBelleImages(RequestManager.ResponseSuccess responseSuccess, int page, int id, int rows){
        Map<String,String> map=new HashMap<>();
        map.put("page",""+page);
        map.put("id",""+id);
        map.put("rows", "" + rows);


        RequestManager.getInstance().get( Contants.IMAGE_LIST, map,responseSuccess, 1);
    }
    /**
     * 拿到开放api的美女图片详情
     */
    public void getBelleImagesDetails(RequestManager.ResponseSuccess responseSuccess,String id){
        Map<String,String> map=new HashMap<>();
        map.put("id", id);
        Log.i("main", "请求参数" + Contants.IMAGE_NEW_SHOW + map.toString());
        RequestManager.getInstance().get(Contants.IMAGE_NEW_SHOW, map,responseSuccess, 1);
    }

}
