package com.example.vollryhttpdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vollryhttpdemo.fragment.FirstFragment;
import com.example.vollryhttpdemo.fragment.SecondFragment;
import com.example.vollryhttpdemo.model.GroupImage;
import com.example.vollryhttpdemo.utils.Contants;
import com.example.vollryhttpdemo.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,HttpUtils.ResponseSuccess{
    //声明相关变量
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private ArrayAdapter arrayAdapter;
    private  FragmentTransaction ft;

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_main);
        findViews(); //获取控件
        initToolbar();

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                mAnimationDrawable.stop();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                mAnimationDrawable.start();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getBelleImageslist();
    }
    /**
     * 设置菜单适配器
     */
    private void setMenuDate(List<String> lvs) {
        //设置菜单列表

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
        lvLeftMenu.setOnItemClickListener(this);
        checkFragment(0);
    }
    /**
     * 配置视图ID
     */
    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lvLeftMenu.setItemChecked(position, true);
        checkFragment(position);
    }
    /**
     * 切换Fragment
     */
    private void checkFragment(int position){
        //根据item点击行号判断启用哪个Fragment
        ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = FirstFragment.newInstance(position);
        ft.replace(R.id.fragment_layout, fragment);
        ft.commit();
        mDrawerLayout.closeDrawers();
    }
    /**
     * 初始化Toolbar
     */
    private void initToolbar(){
        toolbar.setTitle("美女图库");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 拿到开放api的美女图片列表分类
     */
    private void getBelleImageslist(){
        Map<String,String> map=new HashMap<>();
        VolleyApplication.getInstance().getHttpUtils(this).get(Contants.IMAGE_CLASSIFY, 1, map);
    }
    @Override
    public void Success(String s, Integer mode) throws JSONException {
        if(mode==1){
            initDate(s);
        }
    }
    /**
     * 处理返回数据
     * @param s
     * @throws JSONException
     */
    private void initDate(final String s) throws JSONException {
        JSONArray objectArry = new JSONArray(s);
        List<String> lvs = new ArrayList<>();
        for (int i = 0; i < objectArry.length(); i++) {
            lvs.add(objectArry.getJSONObject(i).getString("title"));
        }
        setMenuDate(lvs);
    }
    @Override
    public void Error() {

    }
}
