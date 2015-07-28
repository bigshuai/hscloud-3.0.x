package com.hisoft.hscloud.bss.billing.dao.impl;

import java.util.Collection;
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
import com.hisoft.hscloud.bss.billing.dao.MonthIncomingVODao;
import com.hisoft.hscloud.bss.billing.vo.MonthIncomingVO;
import com.hisoft.hscloud.common.util.HsCloudException;

@Repository
public class MonthIncomingVODaoImpl extends HibernateDao<MonthIncomingVO, Long> implements MonthIncomingVODao {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public Page<MonthIncomingVO> findPageBySQL(Page<MonthIncomingVO> page,
			String sql, Map<String, Object> map) {
		

		try {

			if (logger.isDebugEnabled()) {
				logger.info("enter findPageBySQL method.");
				logger.info("page" + page);
				logger.info("sql:" + sql);
				logger.info("map:" + map);
			}
			Page<MonthIncomingVO> propage = new Page<MonthIncomingVO>();
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
			map.put("page", page);
			List<MonthIncomingVO> list = this.findBySQL(sb.toString(), map);
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
	
	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public List<MonthIncomingVO> findBySQL(String sql, Map<String, ?> map) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      Set<String> keys = map.keySet();
		      query.addScalar("id", Hibernate.LONG).addScalar("domainId", Hibernate.LONG).addScalar("month", Hibernate.STRING).addScalar("abbreviation", Hibernate.STRING);
		      query.setResultTransformer(Transformers.aliasToBean(MonthIncomingVO.class));
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

	
}
