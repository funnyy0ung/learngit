package com.mpyf.lening.Jutil;

import android.util.Log;

/**
 * @author Administrator
 * Log����
 *
 */
public class LogUtil {

	  private static boolean isPrintLog = true;  
	  
	    private static int LOG_MAXLENGTH = 2000;  
	  
	    public static void LogShitou(String msg) {  
	        if (isPrintLog) {  
	  
	            int strLength = msg.length();  
	            int start = 0;  
	            int end = LOG_MAXLENGTH;  
	            for (int i = 0; i < 100; i++) {  
	                if (strLength > end) {  
	                    Log.e("shitou___" + i, msg.substring(start, end));  
	                    start = end;  
	                    end = end + LOG_MAXLENGTH;  
	                } else {  
	                    Log.e("shitou___" + i, msg.substring(start, strLength));  
	                    break;  
	                }  
	            }  
	        }  
	    }  
	  
	    public static void LogShitou(String type, String msg) {  
	  
	        if (isPrintLog) {  
	  
	            int strLength = msg.length();  
	            int start = 0;  
	            int end = LOG_MAXLENGTH;  
	            for (int i = 0; i < 100; i++) {  
	                if (strLength > end) {  
	                    Log.e(type + "___" + i, msg.substring(start, end));  
	                    start = end;  
	                    end = end + LOG_MAXLENGTH;  
	                } else {  
	                    Log.e(type + "___" + i, msg.substring(start, strLength));  
	                    break;  
	                }  
	            }  
	        }  
	    }  
	  
}
