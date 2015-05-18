package com.xmalloc.wordnote.util;

import android.widget.TextView;

public class ViewTextUtil {
	/**
	 * 给 TextView 设置内容
	 * 
	 * @param view
	 * @param text
	 */
	public static void setText(TextView view, String text) {
		if (view == null) {
			return;
		}
		if (text == null) {
			view.setText("");
		} else {
			view.setText(text);
		}
	}
}
