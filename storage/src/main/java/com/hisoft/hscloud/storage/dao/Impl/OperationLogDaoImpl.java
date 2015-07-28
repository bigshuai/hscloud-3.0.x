/* 
* 文 件 名:  OperationLogDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-6 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.dao.Impl; 

import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.storage.dao.OperationLogDao;
import com.hisoft.hscloud.storage.entity.OperationLog;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-6] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class OperationLogDaoImpl extends HibernateDao<OperationLog, Long> implements OperationLogDao {

    @SuppressWarnings("unchecked")
    @Override
    public Page<OperationLog> findPage(Page<OperationLog> page, Map<String, Object> condition) {
        StringBuilder hql = new StringBuilder();
        hql.append("from OperationLog where operator = :username");
        if(condition.containsKey("startDate")) {
            hql.append(" and date >= :startDate ");
        }
        if(condition.containsKey("endDate")) {
            hql.append(" and date < :endDate ");
        }
        if(condition.containsKey("query")) {
            hql.append(" and ip like :query ");
        }
        hql.append(" order by ").append(page.getOrderBy()).append(" ").append(page.getOrder());
        
        Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("username", condition.get("username"));
        
        if(condition.containsKey("startDate")) {
            query.setParameter("startDate", condition.get("startDate"));
        }
        if(condition.containsKey("endDate")) {
            query.setParameter("endDate", condition.get("endDate"));
        }
        if(condition.containsKey("query")) {
            query.setParameter("query", condition.get("query"));
        }
        
        page.setTotalCount(query.list().size());
        query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        page.setResult(query.list());
        return page;
    }

    @Override
    public void addOperationLog(OperationLog operationLog) {
        this.save(operationLog);
    }
    
}
