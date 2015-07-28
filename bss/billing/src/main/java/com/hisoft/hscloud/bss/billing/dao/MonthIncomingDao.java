/* 
* 文 件 名:  MonthIncomingDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.billing.dao; 

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.MonthIncoming;

/** 
 * <统计月收入> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface MonthIncomingDao {
    /**
     * <统计月收入> 
    * <功能详细描述> 
    * @param year
    * @param month
    * @param effectiveDate
    * @param expirationDate 
    * @see [类、类#方法、类#成员]
     */
    public void statisMonthIncoming(String year, String month, Date effectiveDate,
            Date expirationDate);
    
    /**
	 * 通过hql查询
	 * @return
	 */
	public Page<MonthIncoming> findPage(Page<MonthIncoming> page, String hql,Map<String,?> map);
	
	public Page<MonthIncoming> findPageBySQL(Page<MonthIncoming> page,String sql,Map<String,Object> map);
	
	public List<MonthIncoming> findBySQL(String sql, Map<String, ?> map);

}
