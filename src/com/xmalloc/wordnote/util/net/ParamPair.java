package com.xmalloc.wordnote.util.net;

import java.io.Serializable;

import org.apache.http.NameValuePair;

/**
 * 网络的API请求的参数：key - value。
 * 
 * @author lifeix
 */
public class ParamPair<V> implements NameValuePair, Serializable {
	private static final long serialVersionUID = -7020916025837496333L;
	public final String name;
	public final V value;

	public ParamPair(String k, V v) {
		name = k;
		value = v;
	}

	@Override
	public String toString() {
		return new StringBuilder(name).append("=").append(value.toString()).toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		if (value != null) {
			return value.toString();
		} else {
			return null;
		}
	}
}
