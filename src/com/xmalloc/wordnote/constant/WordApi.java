package com.xmalloc.wordnote.constant;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class WordApi extends Api{
	private static final String prefix = "http://192.168.199.220:6024/";
//	private static final String prefix = "http://api.xy.pintimes.com/";
	public WordApi(String url, int method, boolean needLogin){
		super(prefix + url, method, needLogin);
	}
	
	/** 背单词记录 */
	public static Api PLAN_LIST = new WordApi("temp/view", POST, false);
}