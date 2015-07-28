package com.hisoft.hscloud.mss.report.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: TOSHIBA
 * Date: 12-5-24
 * Time: 下午8:16
 * To change this template use File | Settings | File Templates.
 */
public class Tools {

    public static int[] stringToInteger(String category, String beginDate, String endDate) {

        if (!(beginDate.indexOf("-") > 0)) {
        	String s1=beginDate.split("\\(")[0];
        	String s2=endDate.split("\\(")[0];
        	if(s1.contains("GMT")){
        		s1=s1.replace("GMT ", "GMT+").trim();
        	}else if(s1.contains("UTC")){
        		s1=s1.replace("UTC ", "UTC+").trim();
        	}
        	
        	if(s2.contains("GMT")){
        		s2=s2.replace("GMT ", "GMT+").trim();
        	}else if(s2.contains("UTC")){
        		s2=s2.replace("UTC ", "UTC+").trim();
        	}
        	
            Date bd = new Date(s1);
            Date ed = new Date(s2);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            beginDate = simpleDateFormat.format(bd);
            endDate = simpleDateFormat.format(ed);
        }
        if (category.equals("day")) {
            String[] beginDates = beginDate.split("-");
            String[] endDates = endDate.split("-");
            int beginDay = Integer.valueOf(beginDates[0] + beginDates[1] + beginDates[2].replace("T00:00:00", ""));
            int endDay = Integer.valueOf(endDates[0] + endDates[1] + endDates[2].replace("T00:00:00", ""));
            return new int[]{beginDay, endDay};
        } else if (category.equals("month")) {

            String[] beginDates = beginDate.split("-");
            String[] endDates = endDate.split("-");

            int beginMonth = Integer.valueOf(beginDates[0] + beginDates[1]);
            int endMonth = Integer.valueOf(endDates[0] + endDates[1]);
            return new int[]{beginMonth, endMonth};
        } else if (category.equals("year")) {
            String[] beginDates = beginDate.split("-");
            String[] endDates = endDate.split("-");

            int beginYear = Integer.valueOf(beginDates[0]);
            int endYear = Integer.valueOf(endDates[0]);
            return new int[]{beginYear, endYear};
        }
        return null;
    }
    
}
