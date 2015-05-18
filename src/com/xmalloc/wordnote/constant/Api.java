package com.xmalloc.wordnote.constant;

public class Api {
	public String url;
	public int method;
	public boolean needLogin;
	
	public Api(String url, int method, boolean needLogin){
		this.url = url;
		this.method = method;
		this.needLogin = needLogin;
	}
	
}
