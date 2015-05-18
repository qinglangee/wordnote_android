package com.xmalloc.wordnote.util.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.xmalloc.wordnote.app.CustomApp;
import com.xmalloc.wordnote.app.CustomApp;
import com.xmalloc.wordnote.constant.Api;
import com.xmalloc.wordnote.util.GsonUtils;
import com.xmalloc.wordnote.util.LL;

public class GsonRequest<T> extends Request<T> {
	private Class<T> clazz;
	private Map<String, String> headers;
	private Map<String, String> params;
	private Listener<T> listener;
	private ErrorListener errListener;
	private Api api;

	public GsonRequest(Api api, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		this(api, clazz, null, null, listener, errorListener);
	}

	public GsonRequest(Api api, Class<T> clazz, Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener) {
		this(api, clazz, params, null, listener, errorListener);
	}

	/**
	 * Make a GET request and return a parsed object from JSON.
	 *
	 * @param api
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param headers
	 *            Map of request headers
	 */
	public GsonRequest(Api api, Class<T> clazz, Map<String, String> params, Map<String, String> headers,
			Listener<T> listener, ErrorListener errorListener) {
		super(api.method, api.url, errorListener);
		this.api = api;
		this.clazz = clazz;
		this.params = params;
		this.headers = headers;
		this.listener = listener;
		addCommonParams(api.needLogin);
	}

	private void addCommonParams(boolean needAuth) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put("device_id", CustomApp.getInstance().getDeviceId());
		if (needAuth) {
			if (headers == null) {
				headers = new HashMap<String, String>();
			}
//			String auth = NetUtil.createUserAuth();
//			headers.put("authorization", auth);
		}
	}

	// use new GsonRequest()
	@Deprecated
	public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener,
			ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
	}
	
	public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener,
			ErrorListener errorListener, int post) {
		super(Method.POST, url, errorListener);
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	public String getUrl() {
		// GET 有参数的请求
		if (null != params && !params.isEmpty() && api.method == Method.GET) { 
			StringBuilder buffer = new StringBuilder(super.getUrl());
			String url = buffer.append("?").append(URLEncodedUtils.format(NetUtil.convertParam(params), HTTP.UTF_8))
					.toString();
//			LL.d("url is:   " + url);
			return url;
		}
		return super.getUrl();
	}

	@Override
	public Map<String, String> getParams() throws AuthFailureError { // 返回参数map集合
		Map<String, String> result = params;
		return result;
	}

	@Override
	public Map<String, String> getPostParams() throws AuthFailureError { // 返回参数map集合
		Map<String, String> result = params;
		return result;
	}

	@Override
	protected void deliverResponse(T response) {
		listener.onResponse(response);
	}

	@Override
	public void deliverError(VolleyError error) {
		if (errListener != null) {
			errListener.onErrorResponse(error);
		}
		super.deliverError(error);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//			 LL.d(json);
			return Response.success(GsonUtils.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}