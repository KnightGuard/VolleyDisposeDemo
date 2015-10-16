package com.example.vollryhttpdemo;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.vollryhttpdemo.custom.TouchImageView;

import java.util.ArrayList;

public class ImageViewActivity extends BaseActivity {

	private ArrayList<String> imgList = new ArrayList<>();
	private ViewPager imgPager;
    private int index=0;
    private ProgressBar bar;
	private static final int SCALE_DELAY = 30;
	private RelativeLayout mRowContainer;

	@Override
	protected void loadView() {
		index=getIntent().getIntExtra("index",0);
		setContentView(R.layout.image_view);
		toolbar = (Toolbar) findViewById(R.id.tl_custom);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Handle Back Navigation :D
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageViewActivity.this.onBackPressed();
			}
		});

		Intent intent = super.getIntent();
		imgList = new ArrayList<>();
		imgList = intent.getStringArrayListExtra("imgList");
		imgPager = (ViewPager) findViewById(R.id.imgPager);
		imgPager.setAdapter(new ImageAdapter());
		imgPager.setCurrentItem(index);

		bar= (ProgressBar) findViewById(R.id.progressBar);
		mRowContainer= (RelativeLayout) findViewById(R.id.row_container);
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
			TouchImageView img = new TouchImageView(ImageViewActivity.this);
			VolleyApplication.getInstance().settingImg(url, img,bar);
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageViewActivity.this.onBackPressed();
				}
			});
			 container.addView(img, 0);
			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	/**
	 * animate the views if we close the activity
	 */
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
