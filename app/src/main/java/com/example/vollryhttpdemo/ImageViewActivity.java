package com.example.vollryhttpdemo;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.vollryhttpdemo.custom.TouchImageView;
import com.example.vollryhttpdemo.network.RequestFactory;
import com.example.vollryhttpdemo.utils.Contants;
import com.example.vollryhttpdemo.network.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageViewActivity extends BaseActivity implements RequestManager.ResponseSuccess {

	private ArrayList<String> imgList = new ArrayList<>();
	private ViewPager imgPager;
    private int index=0;
//    private ProgressBar bar;
	private static final int SCALE_DELAY = 30;
	private RelativeLayout mRowContainer;
	private ImageAdapter adapter;

	@Override
	protected void loadView() {
		index=getIntent().getIntExtra("index",0);
		setContentView(R.layout.image_view);
		initToolbar();
		imgPager = (ViewPager) findViewById(R.id.imgPager);
		adapter=new ImageAdapter();
		imgPager.setAdapter(adapter);
		imgPager.setCurrentItem(index);
		mRowContainer= (RelativeLayout) findViewById(R.id.row_container);
		getBelleImagesDetails();
	}

	@Override
	public void Success(String response, int actionId) {
		switch (actionId){
			case 1:
				try {
					initDate(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	@Override
	public void Error(int actionId) {

	}

	/**
	 * 处理返回数据
	 * @param s
	 * @throws JSONException
	 */
	private void initDate(final String s) throws JSONException {
		Log.i("main","图片详情"+s);
		JSONObject objectString = new JSONObject(s);
		JSONArray array=objectString.getJSONArray("list");
		imgList = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			imgList.add(array.getJSONObject(i).getString("src"));
		}
		adapter.notifyDataSetChanged();

	}
	/**
	 * 拿到开放api的美女图片详情
	*/
	private void getBelleImagesDetails(){
		RequestFactory.getInstance().getBelleImagesDetails(this, getIntent().getStringExtra("id"));
	}

	private class ImageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return imgList == null ? 0 : imgList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == (View) obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			String url = imgList.get(position);
			View view= LayoutInflater.from(ImageViewActivity.this).inflate(R.layout.details_item, null);
			VolleyApplication.getInstance().settingImg(Contants.PREFIX+url, (TouchImageView)view.findViewById(R.id.image), (ProgressBar) view.findViewById(R.id.progressBar));
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageViewActivity.this.onBackPressed();
				}
			});
			 container.addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	/**
	 * 初始化Toolbar
	 */
	private void initToolbar(){
		toolbar = (Toolbar) findViewById(R.id.tl_custom);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageViewActivity.this.onBackPressed();
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onBackPressed() {
		for (int i = mRowContainer.getChildCount() - 1; i > 0; i--) {
			View rowView = mRowContainer.getChildAt(i);
			ViewPropertyAnimator propertyAnimator = rowView.animate().setStartDelay((mRowContainer.getChildCount() - 1 - i) * SCALE_DELAY)
					.scaleX(0).scaleY(0);

			propertyAnimator.setListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						finishAfterTransition();
					} else {
						finish();
					}
				}

				@Override
				public void onAnimationCancel(Animator animator) {
				}

				@Override
				public void onAnimationRepeat(Animator animator) {
				}
			});
		}
	}
}
