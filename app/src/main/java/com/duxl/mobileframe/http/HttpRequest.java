package com.duxl.mobileframe.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.duxl.mobileframe.common.Global;
import com.duxl.mobileframe.util.Log;
import com.duxl.mobileframe.util.NetworkUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author duxl
 * 
 */
public class HttpRequest {

	private final String TAG = "HttpRequest";
	private int mTimeout = 15000;
	private Map<String, String> mParam = new HashMap<String, String>();
	private Context mContext;

	private Handler mHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			OnCallbackListener listener = (OnCallbackListener) msg.obj;
			String data = msg.getData().getString("data");
			int status = msg.getData().getInt("status");
			String error = msg.getData().getString("error");
			listener.onCallback(data, status, error);
		};
	};

	public HttpRequest(Context context) {
		this.mContext = context;
	}

	/**
	 * 设置连接超时时间
	 * @param time 超时时间，单位秒
	 */
	public void setTimeout(int time) {
		mTimeout = time;
	}

	public Map<String, String> addParam(String key, Object value) {
		mParam.put(key, value.toString());
		return mParam;
	}

	/**
	 * 构造Get请求url
	 * @param url
	 * @return
	 */
	private String buildGetUrl(String url) {
		StringBuffer sb = new StringBuffer(url);
		if(!mParam.isEmpty()) {
			sb.append("?");
			Iterator<Map.Entry<String, String>> it = mParam.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
		}

		String newUrl = sb.toString();
		if(newUrl.endsWith("&")) {
			newUrl = newUrl.substring(0, newUrl.length() - 1);
		}

		return newUrl;
	}

	
	public void get(final String url, final OnCallbackListener listener) {
		if(!new NetworkUtil().isNetworkConnected(mContext)) {
			callback(listener, url, null, -2, null);
			return;
		}

		try {
			String paramUrl = buildGetUrl(url);
			Log.i(TAG, "请求接口GET:: url=" + paramUrl);

			Request request = new Request.Builder().url(paramUrl).build();
			if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
			    request.addHeader("Connection", "close");
			}
			OkHttpClient okHttpClient = new OkHttpClient();
			okHttpClient.setConnectTimeout(mTimeout, TimeUnit.SECONDS);
			okHttpClient.setReadTimeout(mTimeout, TimeUnit.SECONDS);
			Call call = okHttpClient.newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {
					callback(listener, request.urlString(), null, 0, e.getMessage());
				}

				@Override
				public void onResponse(Response response) throws IOException {
					callback(listener, response.request().urlString(), response.body().string(), response.code(), response.message());
				}
			});

		} catch (Exception e) {
			if(e instanceof SocketTimeoutException) {
				callback(listener, url, null, -1, "timeout");

			} else {
				e.printStackTrace();
				callback(listener, url, null, 0, e.getMessage());
			}
		}
	}

	private void post(final String url, Request request, final OnCallbackListener listener) {
		if(!new NetworkUtil().isNetworkConnected(mContext)) {
			callback(listener, url, null, -2, null);
			return;
		}

		try {
			OkHttpClient okHttpClient = new OkHttpClient();
			okHttpClient.setConnectTimeout(mTimeout, TimeUnit.SECONDS);
			okHttpClient.setReadTimeout(mTimeout, TimeUnit.SECONDS);
			Call call = okHttpClient.newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {
					callback(listener, request.urlString(), null, 0, e.getMessage());
				}

				@Override
				public void onResponse(Response response) throws IOException {
					callback(listener, response.request().urlString(), response.body().string(), response.code(), response.message());
				}
			});

		} catch (Exception e) {
			if(e instanceof SocketTimeoutException) {
				callback(listener, url, null, -1, "timeout");

			} else {
				e.printStackTrace();
				callback(listener, url, null, 0, e.getMessage());
			}
		}
	}

	public void post(String url, OnCallbackListener listener) {
		if(Global.DEBUG) {
			Log.i(TAG, "请求接口POST:: url=" + url + ", postData=" + buildGetUrl(""));
		}
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if(!mParam.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = mParam.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				builder.add(entry.getKey(), entry.getValue());
			}
		}
		Request request = new Request.Builder().url(url).post(builder.build()).build();
		if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
		    request.addHeader("Connection", "close");
		}
		post(url, request, listener);
	}

	public void post(final String url, byte[] data, final OnCallbackListener listener) {
		if(Global.DEBUG) {
			Log.i(TAG, "请求接口POST:: url=" + url + ", postData=" + new String(data));
		}
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
		Request request = new Request.Builder().url(url).post(requestBody).build();
		if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
		    request.addHeader("Connection", "close");
		}
		post(url, request, listener);
	}
	
	private void callback(OnCallbackListener listener, String url, String data, int status, String error) {
		Log.i(TAG, "接口响应:: url=" + url + ", data=" + data + ", status=" + status + ", error=" + error);
		if(listener != null) {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("data", data);
			bundle.putInt("status", status);
			bundle.putString("error", error);
			msg.obj = listener;
			msg.setData(bundle);

			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 从输入流里面获取数据
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private String getStringFromInputStream(InputStream in) throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	public interface OnCallbackListener {
		/**
		 * 
		 * @param json 返回的数据
		 * @param status 状态：200成功、-1超时、-2无网络、0其他失败
		 * @param error 错误描述
		 */
		void onCallback(String json, int status, String error);
	}
}
