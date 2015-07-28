/* 
* 文 件 名:  StatisLogDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.billing.dao.impl; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.dao.StatisLogDao;
import com.hisoft.hscloud.bss.billing.entity.StatisLog;

/** 
 * <统计日志> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class StatisLogDaoImpl extends HibernateDao<StatisLog, Long> implements StatisLogDao{

    @Override
    public List<StatisLog> queryStatisLog(String year, String month) {
        String hql = "from StatisLog where year = :year and month = :month";
        Map<String, String> condition = new HashMap<String, String>();
        condition.put("year", year);
        condition.put("month", month);
        return this.find(hql, condition);
    }
    
    @Override
    public void saveStatisLog(StatisLog statisLog) {
        this.save(statisLog);
    }
}
