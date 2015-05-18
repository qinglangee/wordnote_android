package com.xmalloc.wordnote.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	/**
	 * 显示 toast 信息
	 * @param context
	 * @param text
	 */
	public static void show(Context context, String text){
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	/**
	 * 显示 toast 信息
	 * @param context
	 * @param textRes
	 */
	public static void show(Context context, int textRes){
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, context.getString(textRes), duration);
		toast.show();
	}

}
