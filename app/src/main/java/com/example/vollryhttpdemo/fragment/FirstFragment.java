package com.example.vollryhttpdemo.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;

import com.example.vollryhttpdemo.ImageViewActivity;
import com.example.vollryhttpdemo.R;
import com.example.vollryhttpdemo.VolleyApplication;
import com.example.vollryhttpdemo.adapter.ApplicationAdapter;
import com.example.vollryhttpdemo.custom.itemanimator.CustomItemAnimator;
import com.example.vollryhttpdemo.custom.itemanimator.ReboundItemAnimator;
import com.example.vollryhttpdemo.model.GroupImage;
import com.example.vollryhttpdemo.utils.Contants;
import com.example.vollryhttpdemo.utils.HttpUtils.ResponseSuccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 斌斌 on 2015/10/15.
 */
public class FirstFragment extends Fragment implements ResponseSuccess{
    private View view;
    private RecyclerView listView;
    private List<GroupImage> lvs=new ArrayList<>();
    private ApplicationAdapter mAdapter;
    private int num=1;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_first,null);
        listView= (RecyclerView) view.findViewById(R.id.first_list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_default_primary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                num = 1;
                getBelleImages();
            }
        });
        mAdapter = new ApplicationAdapter(new ArrayList<GroupImage>(), R.layout.first_item, this);
        listView.setAdapter(mAdapter);
        listView.setItemAnimator(new CustomItemAnimator());
        listView.setItemAnimator(new ReboundItemAnimator());
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
//        mSwipeRefreshLayout.setChildView(mListView);
        getBelleImages();
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }
    /**
     * 构造获取实例
     *
     * @param id
     */
    public static FirstFragment newInstance(int id) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void Success(String s, Integer mode) throws JSONException {
        mSwipeRefreshLayout.setRefreshing(false);
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

        Map<String,String> map=new HashMap<>();
        map.put("page",""+num);
        map.put("id",""+getArguments().getInt("id"));
        map.put("rows", "20");
        Log.i("main","请求参数"+map.toString());
        VolleyApplication.getInstance().getHttpUtils(this).get(Contants.IMAGE_LIST, 1, map);
    }


    /**
     * 处理返回数据
     * @param s
     * @throws JSONException
     */
    private void initDate(final String s) throws JSONException {
        JSONObject objectString = new JSONObject(s);
        JSONArray array=objectString.getJSONArray("tngou");
        lvs = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            GroupImage item=new GroupImage();
            item.setTitle(array.getJSONObject(i).getString("title"));
            item.setId(array.getJSONObject(i).getString("id"));
            item.setImg(array.getJSONObject(i).getString("img"));
            lvs.add(item);

        }
        if(num==1){
            mAdapter.clearApplications();

        }
        num++;
        mAdapter.addApplications(lvs);

    }

    /**
     * 执行Activity动画
     * @param appInfo
     * @param appIcon
     * @param image
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void animateActivity(GroupImage appInfo, View appIcon,String image) {
        Intent i = new Intent(getActivity(), ImageViewActivity.class);
        ArrayList<String> images=new ArrayList<>();
        images.add(image);
        i.putExtra("imgList", images);
        i.putExtra("id",appInfo.getId());
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), Pair.create(appIcon, "appIcon"));
        getActivity().startActivity(i, transitionActivityOptions.toBundle());
    }

}
