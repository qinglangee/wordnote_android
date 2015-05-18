package com.xmalloc.wordnote.util.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.xmalloc.wordnote.R;

public class VolleyWrapper {
	private static VolleyWrapper mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

	private VolleyWrapper(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();

		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
			private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(30);

			@Override
			public Bitmap getBitmap(String url) {
				return cache.get(url);
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				cache.put(url, bitmap);
			}
		});
	}

	public static synchronized VolleyWrapper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VolleyWrapper(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * 加载图片
	 * @param context
	 * @param url
	 * @param imageView
	 */
	public static void loadImage(Context context, String url, ImageView imageView) {
		loadImage(context, url, imageView, R.drawable.ic_launcher);
	}
	
	/**
	 * 加载图片, 指定默认资源
	 * @param context
	 * @param url
	 * @param imageView
	 */
	public static void loadImage(Context context, String url, ImageView imageView, int defaultRes) {
		if(url == null || imageView == null){
			return;
		}
		VolleyWrapper.getInstance(context).getImageLoader()
		.get(url, ImageLoader.getImageListener(imageView, defaultRes, defaultRes));
	}
}
