package com.example.vollryhttpdemo.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vollryhttpdemo.VolleyApplication;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求的类 Created by remex on 14-7-16.
 */
public class HttpUtils {

	public static final String CHARSET = "UTF-8";
	public static final String CONTENT_TYPE_JSON = "application/json";
	private ResponseSuccess responseSuccess;
	/**
	 * @param url
	 * @param params
	 */
	public void get(final String url, Integer mode, final Map<String, String> params) {

		StringRequest request = new StringRequest(getUrlWithParams(url, params), new MySuccessListener(mode), new MyErrorListener()) {

			@Override
			public String getBodyContentType() {
				return CONTENT_TYPE_JSON;
			}
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return getCommonHeaderMap();
			}
		};

		VolleyApplication.getInstance().getRequestQueue().add(request).setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 1f));
	}

	/**
	 *
	 * @param url
	 * @param mode
	 */
	public void postBody(final String url, Integer mode,final String body) {

		VolleyApplication.getInstance().getRequestQueue().add(new StringRequest(Method.POST, url, new MySuccessListener(mode), new MyErrorListener()) {
			@Override
			public byte[] getBody() throws AuthFailureError {
				return body == null ? super.getBody() : body.getBytes();
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return getCommonHeaderMap();
			}

			@Override
			public String getBodyContentType() {
				return CONTENT_TYPE_JSON;
			}
		}).setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 1f));
	}

	/**
	 * 错误回调接口（统筹全局作用）
	 */
	public interface ResponseSuccess{
		/**
		 * 得到数据回调
		 * @param mode
		 *  @param s
		 */
		void Success(String s, Integer mode) throws JSONException;
		/**
		 * 错误回调
		 */
		void Error();
	}
	class MyErrorListener implements Response.ErrorListener {
		@Override
		public void onErrorResponse(VolleyError volleyError) {
			DialogUtils.showToast(VolleyApplication.getInstance(), "网络连接失败，请检查网络后重试");
			if(responseSuccess!=null){
				responseSuccess.Error();
				Log.i("main","错误:"+volleyError.getMessage());
			}

		}
	}

	class MySuccessListener implements Response.Listener<String> {
		Integer mode=null;
		MySuccessListener(Integer mode){
			this.mode=mode;
		}
		@Override
		public void onResponse(String s) {

			if(responseSuccess!=null){
				try {
					responseSuccess.Success(s,mode);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void setResponseSuccess(ResponseSuccess l) {
		responseSuccess = l;
	}
	/**
	 * 头部信息
	 * @return
	 */
	public Map<String, String> getCommonHeaderMap() {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("apikey","337020e557227243748205369d73ac48");
		return headerMap;
	}
	/**
	 * Base url append params
	 * 
	 * @param baseUrl
	 * @param params
	 * @return
	 */
	public String getUrlWithParams(String baseUrl,
			Map<String, String> params) {
		if (params == null || params.size() == 0)
			return baseUrl;
		StringBuilder sb = new StringBuilder();
		for (String paramKey : params.keySet()) {
			try {
				sb.append("&")
						.append(paramKey)
						.append("=")
						.append(URLEncoder.encode(params.get(paramKey), CHARSET));
			} catch (UnsupportedEncodingException e) {
				return baseUrl;
			} catch (Exception e) {
				return baseUrl;
			}
		}
		return baseUrl + sb.replace(0, 1, "?").toString();
	}

	/**
	 * 格式化url
	 *
	 * @param urlFormat
	 *            url参数格式
	 * @param param
	 *            动态参数
	 * @return
	 */
	public static String getUrl(String urlFormat, Object... param) {
		return String.format(urlFormat, param);
	}
}
