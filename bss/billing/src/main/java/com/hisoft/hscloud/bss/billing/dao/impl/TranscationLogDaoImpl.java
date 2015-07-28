package com.hisoft.hscloud.bss.billing.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.constant.BillConstant;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.bss.billing.vo.OtherResponsibility;
import com.hisoft.hscloud.bss.billing.vo.ResponsibilityIncoming;
import com.hisoft.hscloud.bss.billing.vo.Statistics;
import com.hisoft.hscloud.bss.billing.vo.TranscationLogVO;
import com.hisoft.hscloud.bss.billing.vo.VMResponsibility;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;

@Repository
public class TranscationLogDaoImpl extends HibernateDao<TranscationLog, Long> implements TranscationLogDao{
	
	private Logger logger = Logger.getLogger(this.getClass());

	
	public void save(TranscationLog transcationLog){
		
		try {
			super.save(transcationLog);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
		
	}

	@Override
	public Page<TranscationLogVO> findPageBySQL(List<Sort> sorts,Page<TranscationLogVO> page,String sql,Map<String,Object> map) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.info("enter findPageBySQL method.");
				logger.info("sorts"+sorts);
				logger.info("page"+page);
				logger.info("sql:"+sql);
				logger.info("map:"+map);
			}
//			Page<TranscationLogVO> propage = new Page<TranscationLogVO>();
//			propage.setPageNo(page.getPageNo());
//			propage.setOrder(page.getOrder());
//			propage.setOrderBy(page.getOrderBy());
			Long total = this.findCountBySQL(sql, map);
			if(logger.isDebugEnabled()){
				logger.info("propage:"+page);
				logger.info("total:"+total);
			}
			page.setTotalCount(total.intValue());
			StringBuffer sb = new StringBuffer(sql);
			for (int i = 0; i < sorts.size(); i++) {
				Sort sort = sorts.get(i);
				if (i == 0) {
					sb.append(" order by ");
				}
				sb.append(sort.getProperty() + " " + sort.getDirection());
	
				if (i < sorts.size() - 1) {
					sb.append(",");
				}
			}
			map.put("page", page);
			List<TranscationLogVO> list = this.findBySQL(sb.toString(), map);
			page.setResult(list);
			if(logger.isDebugEnabled()){
				logger.info("sql:"+sb.toString());
				logger.info("propage:"+page);
				logger.info("exit findPageBySQL method.");
			}
			return page;
		
	    } catch (Exception e) {
		    throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public List<TranscationLogVO> findBySQL(String sql, Map<String, ?> map) {
		
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      Set<String> keys = map.keySet();
		      query.addScalar("id", Hibernate.LONG)
		      .addScalar("transcationOn", Hibernate.TIMESTAMP)
		      .addScalar("transcationType", Hibernate.SHORT)
		      .addScalar("amount", Hibernate.BIG_DECIMAL)
		      .addScalar("balance", Hibernate.BIG_DECIMAL)
		      .addScalar("email",  Hibernate.STRING)
		      .addScalar("remark", Hibernate.STRING)
		      .addScalar("description", Hibernate.STRING)
		      .addScalar("username", Hibernate.STRING)
		      .addScalar("abbreviation", Hibernate.STRING)
		      .addScalar("operator", Hibernate.STRING)
		      .addScalar("orderId", Hibernate.LONG)
		      .addScalar("coupons", Hibernate.BIG_DECIMAL)
		      .addScalar("couponsBalance", Hibernate.BIG_DECIMAL)
		      .addScalar("gifts", Hibernate.BIG_DECIMAL)
		       .addScalar("giftsBalance", Hibernate.BIG_DECIMAL)
		      .addScalar("orderNo", Hibernate.STRING)
		      .addScalar("flow",Hibernate.SHORT);
		      query.setResultTransformer(Transformers.aliasToBean(TranscationLogVO.class));
		      for (String key : keys) {
		    	  Object o = map.get(key);
		    	  if( o instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else if(o instanceof Page){
		    		  query.setFirstResult((((Page) o).getPageNo()-1)*((Page) o).getPageSize());
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

	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public Long findCountBySQL(String sql, Map<String, ?> map) {
		
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
//			if(null == list || list.isEmpty()){
//				return 0l;
//			}else{
//				Map<String,Object> m = (Map<String,Object>)list.get(0);
//				return (Long)m.get("count");
//			}
			return list.get(0);
			
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CapitalSource> findCapitalSource(String sql) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
//		      Set<String> keys = map.keySet();
		      query.addScalar("month", Hibernate.STRING).addScalar("oldPlatform", Hibernate.BIG_DECIMAL)
		           .addScalar("alipay", Hibernate.BIG_DECIMAL)
		           .addScalar("easyMoney", Hibernate.BIG_DECIMAL)
		           .addScalar("eBank", Hibernate.BIG_DECIMAL)
		           .addScalar("cash", Hibernate.BIG_DECIMAL)
		           .addScalar("check", Hibernate.BIG_DECIMAL);
		      query.setResultTransformer(Transformers.aliasToBean(CapitalSource.class));
//		      for (String key : keys) {
//		    	  Object o = map.get(key);
//		    	  if( o instanceof Collection){
//		    		  query.setParameterList(key, (Collection)map.get(key));
//		    	  }else if(o instanceof Page){
//		    		  query.setFirstResult((((Page) o).getFirst()-1));
//		    		  query.setMaxResults(((Page) o).getPageSize());
//		    	  }else{
//		    		  query.setParameter(key, map.get(key));
//		    	  }
//			  }
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ResponsibilityIncoming> findResponsibilityIncoming(String sql) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("month", Hibernate.STRING)
		           .addScalar("consume", Hibernate.BIG_DECIMAL)
		           .addScalar("prepay", Hibernate.BIG_DECIMAL)
		           .addScalar("refund", Hibernate.BIG_DECIMAL)
		           .addScalar("drawCash", Hibernate.BIG_DECIMAL)
		           .addScalar("machine", Hibernate.BIG_DECIMAL)
		           .addScalar("express", Hibernate.BIG_DECIMAL)
		           .addScalar("other", Hibernate.BIG_DECIMAL)
		           .addScalar("monthResponsibilityBalance", Hibernate.BIG_DECIMAL);
		      query.setResultTransformer(Transformers.aliasToBean(ResponsibilityIncoming.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OtherResponsibility> findOtherResponsibility(String sql) {
		try {
			
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("transcationId", Hibernate.STRING)
		           .addScalar("type", Hibernate.SHORT)
		           .addScalar("transcationOn", Hibernate.TIMESTAMP)
		           .addScalar("amount", Hibernate.BIG_DECIMAL)
		           .addScalar("currentIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("noneventIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("email", Hibernate.STRING)
		           .addScalar("description", Hibernate.STRING);
		      query.setResultTransformer(Transformers.aliasToBean(OtherResponsibility.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VMResponsibility> findVMResponsibility(String sql) {
		try {
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("email", Hibernate.STRING)
		           .addScalar("transcationId", Hibernate.STRING)
		           .addScalar("orderNo", Hibernate.STRING)
		           .addScalar("vmNo", Hibernate.STRING)
		           .addScalar("type", Hibernate.SHORT)
		           .addScalar("transcationOn", Hibernate.TIMESTAMP)
		           .addScalar("startTime", Hibernate.TIMESTAMP)
		           .addScalar("endTime", Hibernate.TIMESTAMP)
		           .addScalar("orderDuration", Hibernate.LONG)
		           .addScalar("usedDuration", Hibernate.LONG)
		           .addScalar("amount", Hibernate.BIG_DECIMAL)
		           .addScalar("currentIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("finishedIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("noneventIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("description", Hibernate.STRING)
		           .addScalar("itemAmout", Hibernate.BIG_DECIMAL)
		           .addScalar("refrenceId", Hibernate.LONG);
		      query.setResultTransformer(Transformers.aliasToBean(VMResponsibility.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Statistics> findstatistics(String sql) {
		try {
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("month", Hibernate.STRING)
		           .addScalar("deposit", Hibernate.BIG_DECIMAL)
		           .addScalar("draw", Hibernate.BIG_DECIMAL)
		           .addScalar("consume", Hibernate.BIG_DECIMAL)
		           .addScalar("refund", Hibernate.BIG_DECIMAL)
		           .addScalar("responsibility", Hibernate.BIG_DECIMAL)
		           .addScalar("depositBalance", Hibernate.BIG_DECIMAL)
		           .addScalar("consumeBalance", Hibernate.BIG_DECIMAL);
		      query.setResultTransformer(Transformers.aliasToBean(Statistics.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Statistics> findNoReportStatistics(String sql) {
		try {
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addScalar("month", Hibernate.STRING)
		           .addScalar("deposit", Hibernate.BIG_DECIMAL)
		           .addScalar("draw", Hibernate.BIG_DECIMAL)
		           .addScalar("consume", Hibernate.BIG_DECIMAL)
		           .addScalar("refund", Hibernate.BIG_DECIMAL)
		           .addScalar("responsibility", Hibernate.BIG_DECIMAL);
		      query.setResultTransformer(Transformers.aliasToBean(Statistics.class));
		      return query.list();
		      
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,e.getMessage(),logger, e);
		}
	}

	@Override
	public Page<VMResponsibility> findVMResponsibilityPageBySQL(
			List<Sort> sorts, Page<VMResponsibility> page, String sql,
			Map<String, Object> map) {
		try {

			if (logger.isDebugEnabled()) {
				logger.info("enter findPageBySQL method.");
				logger.info("sorts" + sorts);
				logger.info("page" + page);
				logger.info("sql:" + sql);
				logger.info("map:" + map);
			}
			Long total = this.findCountBySQL(sql, map);
			if (logger.isDebugEnabled()) {
				logger.info("propage:" + page);
				logger.info("total:" + total);
			}
			page.setTotalCount(total.intValue());
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
			List<VMResponsibility> list = this.findResponsibilityBySQL(sb.toString(), map);
			page.setResult(list);
			if (logger.isDebugEnabled()) {
				logger.info("sql:" + sb.toString());
				logger.info("propage:" + page);
				logger.info("exit findPageBySQL method.");
			}
			return page;

		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,
					e.getMessage(), logger, e);
		}
	}

	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public List<VMResponsibility> findResponsibilityBySQL(String sql,
			Map<String, ?> map) {
		try {
		      SQLQuery query = getSession().createSQLQuery(sql);
		      Set<String> keys = map.keySet();
		      query.addScalar("email", Hibernate.STRING)
		           .addScalar("transcationId", Hibernate.STRING)
		           .addScalar("orderNo", Hibernate.STRING)
		           .addScalar("vmNo", Hibernate.STRING)
		           .addScalar("objectId", Hibernate.LONG)
		           .addScalar("orderItemId", Hibernate.LONG)
		           .addScalar("productType", Hibernate.BYTE)
		           .addScalar("type", Hibernate.SHORT)
		           .addScalar("transcationOn", Hibernate.TIMESTAMP)
		           .addScalar("startTime", Hibernate.TIMESTAMP)
		           .addScalar("endTime", Hibernate.TIMESTAMP)
		           .addScalar("orderDuration", Hibernate.LONG)
		           .addScalar("usedDuration", Hibernate.LONG)
		           .addScalar("amount", Hibernate.BIG_DECIMAL)
		           .addScalar("currentIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("finishedIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("noneventIncoming", Hibernate.BIG_DECIMAL)
		           .addScalar("description", Hibernate.STRING)
		           .addScalar("itemAmout", Hibernate.BIG_DECIMAL)
		           .addScalar("refrenceId", Hibernate.LONG);
		      query.setResultTransformer(Transformers.aliasToBean(VMResponsibility.class));
		      for (String key : keys) {
		    	  Object o = map.get(key);
		    	  if( o instanceof Collection){
		    		  query.setParameterList(key, (Collection)map.get(key));
		    	  }else if(o instanceof Page){
		    		  query.setFirstResult((((Page) o).getPageNo()-1)*((Page) o).getPageSize());
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
	
	@Override
	public List<TranscationLog> findByHQL(String hql, Map<String, ?> map) {
		
		try{
			
			  if(logger.isDebugEnabled()){
					logger.debug("enter TranscationLogDaoImpl findByHQL method.");
					logger.debug("hql:"+hql);
					logger.debug("map:"+map);
			  }
		      Query query = getSession().createQuery(hql);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<TranscationLog> resources = query.list();
			  if(logger.isDebugEnabled()){
				  logger.debug("enter TranscationLogDaoImpl findByHQL method.");
		      }
			  return resources;
		  
		 } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	     }	      
	}
	
	

	
}
