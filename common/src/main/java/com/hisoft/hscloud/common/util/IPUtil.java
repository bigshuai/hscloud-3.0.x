package com.hisoft.hscloud.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {
	
	private final static String p = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	
    public static boolean isIP(String ipAddress){
    	
        Pattern pattern = Pattern.compile(p);   
        Matcher matcher = pattern.matcher(ipAddress);   
        return matcher.matches();
        
    }

}
