package com.example.vollryhttpdemo.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.vollryhttpdemo.ImageViewActivity;
import com.example.vollryhttpdemo.R;
import com.example.vollryhttpdemo.VolleyApplication;
import com.example.vollryhttpdemo.adapter.ApplicationAdapter;
import com.example.vollryhttpdemo.adapter.FirestAdpater;
import com.example.vollryhttpdemo.utils.HttpUtils.ResponseSuccess;
import com.example.vollryhttpdemo.model.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 斌斌 on 2015/10/15.
 */
public class FirstFragment extends Fragment {
    private View view;
    private RecyclerView listView;
    private List<Item> lvs=new ArrayList<>();
//    private FirestAdpater adapter;
    private ApplicationAdapter mAdapter;
    private String URL="http://apis.baidu.com/txapi/mvtp/meinv";
    private int num=10;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_first,null);
        listView= (RecyclerView) view.findViewById(R.id.first_list);
//        adapter = new FirestAdpater(getActivity(), R.layout.first_item);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ApplicationAdapter(new ArrayList<Item>(), R.layout.first_item, this);
        listView.setAdapter(mAdapter);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);

        getBelleImages();
        return view;
    }

    @Override
    public void Success(String s, Integer mode) throws JSONException {
        progressBar.setVisibility(View.GONE);
        switch (mode){
            case 1:
                initDate(s);
                break;
        }
    }

    @Override
    public void Error() {
        progressBar.setVisibility(View.GONE);
    }
    /**
     * 拿到开放api的美女图片集
     */
    private void getBelleImages(){

        progressBar.setVisibility(View.VISIBLE);
        Map<String,String> map=new HashMap<>();
        map.put("num",""+num);
        VolleyApplication.getInstance().getHttpUtils(new ResponseSuccess() {
            @Override
            public void Success(String s, Integer mode) throws JSONException {

            }

            @Override
            public void Error() {

            }
        }).get(URL, 1, map);
    }


    /**
     * 处理返回数据
     * @param s
     * @throws JSONException
     */
    private void initDate(final String s) throws JSONException {
        JSONObject array = new JSONObject(s);
        lvs = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Item item=new Item();
            item.setTitle(array.getJSONObject("" + i).getString("title"));
            item.setDescription(array.getJSONObject("" + i).getString("description"));
            item.setPicUrl(array.getJSONObject("" + i).getString("picUrl"));
            item.setUrl(array.getJSONObject("" + i).getString("url"));
            lvs.add(item);

        }
        mAdapter.clearApplications();
        mAdapter.addApplications(lvs);

    }

    /**
     * 执行Activity动画
     * @param appInfo
     * @param appIcon
     * @param image
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void animateActivity(Item appInfo, View appIcon,String image) {
        Intent i = new Intent(getActivity(), ImageViewActivity.class);
        ArrayList<String> images=new ArrayList<>();
        images.add(image);
        i.putExtra("imgList",images);
//        i.putExtra("appInfo", appInfo.getComponentName());
//        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create((View) mFabButton, "fab"), Pair.create(appIcon, "appIcon"));

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), Pair.create(appIcon, "appIcon"));
        getActivity().startActivity(i, transitionActivityOptions.toBundle());
    }

}
