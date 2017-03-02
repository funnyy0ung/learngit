package com.mpyf.lening.Jutil;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapSortUtil {

    /** 
     * 使用 Map按key进行排序 
     * @param map 
     * @return 
     */  
	public static Map<String, String> sortMapByKey(Map<String, String> oriMap) {  
	    if (oriMap == null || oriMap.isEmpty()) {  
	        return null;  
	    }  
	    Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {  
	        public int compare(String key1, String key2) {  
//	            int intKey1 = 0, intKey2 = 0;  
//	            try {  
//	                intKey1 = getInt(key1);  
//	                intKey2 = getInt(key2);  
//	            } catch (Exception e) {  
//	                intKey1 = 0;   
//	                intKey2 = 0;  
//	            }  
//	            return intKey1 - intKey2;  
	        	return key1.compareTo(key2);
	        }});  
	    sortedMap.putAll(oriMap);  
	    return sortedMap;  
	}  
	  
	private static int getInt(String str) {  
	    int i = 0;  
	    try {  
	        Pattern p = Pattern.compile("^\\d+");  
	        Matcher m = p.matcher(str);  
	        if (m.find()) {  
	            i = Integer.valueOf(m.group());  
	        }  
	    } catch (NumberFormatException e) {  
	        e.printStackTrace();  
	    }  
	    return i;  
	} 
}

