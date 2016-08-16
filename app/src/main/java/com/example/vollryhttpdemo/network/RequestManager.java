package com.example.vollryhttpdemo.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestManager {
	private static RequestManager manager;//二次封装管理类
	private RequestQueue mRequestQueue;//volley请求执行器
	public String CHARSET = "UTF-8";//字符类型
	public String CONTENT_TYPE_JSON = "application/json";//请求协议类型
	public String cookies;//cookies

	/**
	 * @return Volley
	 */
	private RequestQueue getRequestQueue() {
		return this.mRequestQueue;
	}

	public void init(Context context) {
		this.mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * 创建RequestManager管理类实例
	 */
	public static synchronized RequestManager getInstance() {
		if(manager==null){
			manager=new RequestManager();
		}
		return manager;
	}

	/**
	 * Get
	 * @param url
	 * @param actionId
	 * @param params
	 * @param responseSuccess
	 */
	public void get(final String url, final Map<String, String> params,ResponseSuccess responseSuccess, int actionId) {
		sendRequest(Request.Method.GET, getUrlWithParams(url,params),null, null, actionId, true, 0, 20 * 1000, responseSuccess);
	}

	/**
	 * Post
	 * @param url
	 * @param actionId
	 * @param body
	 * @param responseSuccess
	 */
	public void postBody(final String url,final String body,ResponseSuccess responseSuccess, int actionId) {
		sendRequest( Request.Method.POST, url, null, body, actionId, true, 0, 20* 1000, responseSuccess);
	}
	/**
	* Post
	* @param url
	* @param actionId
	* @param body
	* @param responseSuccess
	*/
	public void postHeaderBody(final String url, Map<String, String> header,final String body,ResponseSuccess responseSuccess,int actionId) {
		sendRequest( Request.Method.POST, url, header, body, actionId, true, 0, 20* 1000, responseSuccess);
	}

	/**
	 * 配置参数并提交请求
	 * @param method            请求类型
	 * @param url               请求地址
	 * @param body              格式化后的参数
	 * @param actionId          请求行为标识
	 * @param shouldCache       是否缓存
	 * @param timeoutCount      请求次数
	 * @param retryTimes        请求等待超时时间
	 * @param responseSuccess   请求回调
	 */
	public void sendRequest(int method, String url, final Map<String, String> header,final String body, final int actionId,boolean shouldCache, int timeoutCount, int retryTimes, final ResponseSuccess responseSuccess) {
		getRequestQueue().add(new StringRequest(method, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				responseSuccess.Success(response, actionId);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				responseSuccess.Error(actionId);
			}
		}) {
			@Override
			public byte[] getBody() throws AuthFailureError {
				return body == null ? super.getBody() : body.getBytes();
			}
			/**   请求加入cookie  */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<>();
				if (cookies != null && cookies.length() > 0) {
					headers.put("cookie", cookies);
				}
				if(header!=null){
					headers.putAll(header);
				}
				headers.putAll(getCommonHeaderMap());
				return headers;
			}
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				Response<String> superResponse = super.parseNetworkResponse(response);
				Map<String, String> responseHeaders = response.headers;
				String rawCookies = responseHeaders.get("Set-Cookie");
				/**   保存cookie  */
				if (rawCookies != null) {
					String session = rawCookies.substring(0, rawCookies.indexOf(";"));
					if (cookies == null || !cookies.equals(session)) {
						cookies = session;
					}
				}
				String str = null;
				try {
					str = new String(response.data,"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
//				return superResponse;
			}
			@Override
			public String getBodyContentType() {
				return CONTENT_TYPE_JSON;
			}
		}).setShouldCache(shouldCache).setRetryPolicy(new DefaultRetryPolicy(retryTimes, timeoutCount, 1f));
	}

	/**
	 * 错误回调接口（统筹全局作用）
	 */
	public interface ResponseSuccess{
		/**
		 * 得到数据回调
		 * @param actionId
		 *  @param response
		 */
		void Success(String response, int actionId);
		/**
		 * 错误回调
		 * @param actionId
		 */
		void Error(int actionId);
	}

	/**
	 * 头部信息(一般头部信息固定，额外需求自己根据sendRequest()方法定义)
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
