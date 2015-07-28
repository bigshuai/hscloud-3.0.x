/* 
* 文 件 名:  MonthIncomingDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.billing.dao.impl; 

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.constant.BillConstant;
import com.hisoft.hscloud.bss.billing.dao.MonthIncomingDao;
import com.hisoft.hscloud.bss.billing.entity.MonthIncoming;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <统计月收入> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class MonthIncomingDaoImpl extends HibernateDao<MonthIncoming, Long> implements MonthIncomingDao {
    
	private Logger logger = Logger.getLogger(this.getClass());
	
    @Override
    public void statisMonthIncoming(String year, String month, Date effectiveDate, Date expirationDate) {
        String sql = "{call p_incoming_statis(:year,:month,:effectiveDate,:expirationDate)}";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("effectiveDate", effectiveDate);
        query.setParameter("expirationDate", expirationDate);
        query.executeUpdate();
    }

	@Override
	public Page<MonthIncoming> findPageBySQL(Page<MonthIncoming> page,
			String sql, Map<String, Object> map) {
		try {

			if (logger.isDebugEnabled()) {
				logger.info("enter findPageBySQL method.");
				logger.info("page" + page);
				logger.info("sql:" + sql);
				logger.info("map:" + map);
			}
			Page<MonthIncoming> propage = new Page<MonthIncoming>();
			propage.setPageNo(page.getPageNo());
			propage.setOrder(page.getOrder());
			propage.setOrderBy(page.getOrderBy());
			Long total = this.findCountBySQL(sql, map);
			if (logger.isDebugEnabled()) {
				logger.info("propage:" + propage);
				logger.info("total:" + total);
			}
			propage.setTotalCount(total.intValue());
			StringBuffer sb = new StringBuffer(sql);
//			for (int i = 0; i < sorts.size(); i++) {
//				Sort sort = sorts.get(i);
//				if (i == 0) {
//					sb.append(" order by ");
//				}
//				sb.append(sort.getProperty() + " " + sort.getDirection());
//
//				if (i < sorts.size() - 1) {
//					sb.append(",");
//				}
//			}
			map.put("page", page);
			List<MonthIncoming> list = this.findBySQL(sb.toString(), map);
			propage.setResult(list);
			if (logger.isDebugEnabled()) {
				logger.info("sql:" + sb.toString());
				logger.info("propage:" + propage);
				logger.info("exit findPageBySQL method.");
			}
			return propage;

		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,
					e.getMessage(), logger, e);
		}
	}

	@Override
	public List<MonthIncoming> findBySQL(String sql, Map<String, ?> map) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      Set<String> keys = map.keySet();
		      query.addScalar("id", Hibernate.LONG).addScalar("domainId", Hibernate.LONG).addScalar("month", Hibernate.STRING);
		      query.setResultTransformer(Transformers.aliasToBean(MonthIncoming.class));
		      for (String key : keys) {
		    	  Object o = map.get(key);
		    	  if( o instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else if(o instanceof Page){
		    		  query.setFirstResult((((Page) o).getFirst()-1));
		    		  query.setMaxResults(((Page) o).getPageSize());
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	private Long findCountBySQL(String sql, Map<String, ?> map) {
		
		try {
			
			String count = "select COUNT(1) count  from ("+sql +") t";
			SQLQuery query = getSession().createSQLQuery(count);
			query.addScalar("count", Hibernate.LONG);
			Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  Object o = map.get(key);
		    	  if( o instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
			List<Long> list = query.list();
			return list.get(0);
			
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}

	}
}
