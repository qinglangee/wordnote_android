package com.xmalloc.wordnote.util;

public class ViewUtil {


	private static long lastClickTime;
	/**
	 * 判断双击
	 * @return
	 */
    public static boolean isFastDoubleClick() {
    	
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1500) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
}
