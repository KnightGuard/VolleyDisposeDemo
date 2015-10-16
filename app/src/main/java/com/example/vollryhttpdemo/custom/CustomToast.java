package com.example.vollryhttpdemo.custom;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast 工具类，防止多次弹出
 * @author admin
 *
 */
public class CustomToast {

	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text, int duration) {

		showToast(mContext, text, duration, Gravity.BOTTOM);
	}
	
	public static void showToast(Context mContext, String text, int duration, int gravity) {
		
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mToast.setGravity(gravity, 0, 100);
		mHandler.postDelayed(r, 1000);
		
		mToast.show();
	}

	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}

}
