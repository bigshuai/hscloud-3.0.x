package com.pactera.hscloud.common4j.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common4jDateFormat {
	
    public static  String getTimestamp(Date date,String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

}
