/* 
* 文 件 名:  DailyIterator.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.systemmanagement.service.vo; 

import java.util.Calendar;
import java.util.Date;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class DailyIterator {
	
//	private final int hourOfDay,minute,second;
	private final Calendar calendar = Calendar.getInstance();
	public DailyIterator(int hourOfDay,int minute,int second){
		this(hourOfDay, minute, second, new Date());
	}
	public DailyIterator(){
		
	}
	public DailyIterator(int hourOfDay,int minute,int second,Date date){
//		this.hourOfDay = hourOfDay;
//		this.minute = minute;
//		this.second = second;
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
		calendar.set(Calendar.MINUTE,minute);
		calendar.set(Calendar.SECOND,second);
		calendar.set(Calendar.MILLISECOND,0);
		if(!calendar.getTime().before(date)){
			calendar.add(Calendar.DATE,-1);
		}
	}
	public Date nextDate(int day){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,day);
		return calendar.getTime();
	}
	public boolean isOverTime(Date nextstartTime){
		boolean resultFlag = false;
		if(nextstartTime == null){
			return true;
		}
		if(calendar.getTime().after(nextstartTime)){
			resultFlag = true;
		}
		return resultFlag;		
	}

	/** <一句话功能简述> 
	 * <功能详细描述> 
	 * @param args 
	 * @see [类、类#方法、类#成员] 
	 */
	public static void main(String[] args) {
		DailyIterator dailyIterator = new DailyIterator(0,0,0);
		//System.out.println(dailyIterator.next());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-1);
		System.out.println(calendar.getTime());
		System.out.println(dailyIterator.calendar.getTime().before(calendar.getTime()));
		System.out.println(dailyIterator.isOverTime(null));
	}

}
