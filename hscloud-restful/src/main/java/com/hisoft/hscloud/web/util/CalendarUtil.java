package com.hisoft.hscloud.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: hisoft
 * @brief: Calendar util
 * @log: 2012-12-26 2:10:50
 **/
public class CalendarUtil {
	/** fn-hd
	  * rem: 
	  * @param date
	  * @return
	  * author: hisoft 2013-1-11 下午1:13:58
	  * log: 
	  */
	public static String fomartDate(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	/** fn-hd
	  * rem: 
	  * @param date
	  * @return
	  * author: hisoft 2013-1-28 下午2:09:45
	  * log: 
	  */
	public static Date fomartDate(String date) {

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return df.parse(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
	}

}
