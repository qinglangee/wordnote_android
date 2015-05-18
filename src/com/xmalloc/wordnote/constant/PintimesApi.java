package com.xmalloc.wordnote.constant;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
public class PintimesApi extends Api{
	private static final String prefix = "http://api.pintimes.com/";
//	private static final String prefix = "http://api.xy.pintimes.com/";
	public PintimesApi(String url, int method, boolean needLogin){
		super(prefix + url, method, needLogin);
	}
	
	/** 用户初始化 */
	public static Api USER_INIT = new PintimesApi("api/user/init", POST, false);
	/** 用户登录 */
	public static Api USER_LOGIN= new PintimesApi("api/user/login", POST, false);
	/** 用户退出登录 */
	public static Api USER_LOGOUT= new PintimesApi("api/user/logout", POST, true);
	/** 用户改昵称 */
	public static Api USER_MODIFYNAME= new PintimesApi("api/user/modifyname", POST, true);
	/** 查看品委会成员 */
	public static Api USER_EDITORS= new PintimesApi("api/user/editors", GET, true);
	
	/** 关注/订阅 */
	public static Api FOLLOW_ADDFOLLOW= new PintimesApi("api/follow/addfollow", POST, true);
	/** 取消关注/订阅 */
	public static Api FOLLOW_UNFOLLOW= new PintimesApi("api/follow/unfollow", POST, true);
	/** 用户动态(用户时间轴) */
	public static Api FEED_USERFEEDS= new PintimesApi("api/feed/userfeeds", GET, false);
	
	/** 单篇内容 */
	public static Api CONTENT_VIEW= new PintimesApi("jsp/content/view", GET, false);
	/** 时间轴内容 */
	public static Api CONTENT_TIMELINE= new PintimesApi("api/content/timeline", GET, false);
	/** 内容分类 */
	public static Api CATEGORY_LIST= new PintimesApi("api/category/list", GET, false);
	/** 分类下的内容 */
	public static Api CONTENT_TERM= new PintimesApi("api/content/term", GET, false);
	
	/** 单篇内容返回参数 */
	public static Api CONTENT_VIEW_JSON = new PintimesApi("api/content/view", GET, false);
	/** 单篇页面 */
	public static Api SINGLEVIEW = new PintimesApi("jsp/content/singleview", GET, false);

	
	//评论的API
	public static Api COMMENT_LIST= new PintimesApi("api/comment/list", GET, false);
	//发表评论
	public static Api ADDCOMMENT = new PintimesApi("api/comment/addcomment", POST, true);
	//分享api
	public static Api SHARE = new PintimesApi("api/feed/share", POST, true);
	/** 精彩评论 */
	public static Api COMMENT_HOT = new PintimesApi("api/comment/hot", GET, false);
	
	//推荐 (热点界面)
	public static Api CONTENT_RECOMMEND = new PintimesApi("api/content/recommend", GET, false);
	
	/** 关于 */
	public static Api ABOUT = new PintimesApi("jsp/about", GET, false);
	/** 版本检查 */
	public static Api CHECKVERSION = new PintimesApi("api/settings/checkversion", GET, false);
	
}
