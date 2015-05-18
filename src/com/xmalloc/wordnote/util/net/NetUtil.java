package com.xmalloc.wordnote.util.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xmalloc.wordnote.R;
import com.xmalloc.wordnote.app.CustomApp;
import com.xmalloc.wordnote.util.ToastUtil;

public class NetUtil {

	/**
	 * 基本的网络错误处理程序
	 * 
	 * @return
	 */
	public static Response.ErrorListener getErrorListener(final Context context) {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(error != null && error.getMessage() != null){
					Log.e("NetUtil", error.getMessage());
				}else{
					Log.e("NetUtil", "VolleyError is null or error.getMessage is null.");
				}
				int errRes = R.string.common_err_connection_broken;
				if(error.networkResponse != null){
					NetworkResponse resp = error.networkResponse;
					if(resp.statusCode == 401){
						errRes = R.string.common_err_connection_401;
					}else if(resp.statusCode == 500){
						errRes = R.string.common_err_connection_500;
					}
				}
				ToastUtil.show(context, context.getString(errRes));
			}
		};
	}

	/**
	 * 参数类型转换
	 * 
	 * @param params
	 * @return
	 */
	public static List<ParamPair<String>> convertParam(Map<String, String> params) {
		List<ParamPair<String>> result = new ArrayList<ParamPair<String>>();
		for (Entry<String, String> e : params.entrySet()) {
			ParamPair<String> pair = new ParamPair<String>(e.getKey(), e.getValue());
			result.add(pair);
		}
		return result;
	}


	/**
	 * 创建参数map Map<String, String>
	 * @param key
	 * @param value
	 * @return
	 */
	public static Map<String, String> createParams(String key, String value){
		Map<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		return map;
	}
}
