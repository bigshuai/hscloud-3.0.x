package com.hisoft.hscloud.bss.billing.dao.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.constant.BillConstant;
import com.hisoft.hscloud.bss.billing.dao.ApplicationTranscationLogDao;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.vo.ApplicationTranscationLogVO;
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
public class ApplicationTranscationLogDaoImpl implements ApplicationTranscationLogDao{
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public Page<ApplicationTranscationLogVO> getAppTransactionByPage(
			List<Sort> sorts, Page<ApplicationTranscationLogVO> page,
			String sql, Map<String, Object> map) {
		try{
			if(logger.isDebugEnabled()){
				logger.info("enter getAppTransactionByPage method.");
				logger.info("sorts"+sorts);
				logger.info("page"+page);
				logger.info("sql:"+sql);
				logger.info("map:"+map);
			}
			Long total = this.findCountBySQL(sql, map);
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
			List<ApplicationTranscationLogVO> list = this.findBySQL(sb.toString(), map);
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
	public Long findCountBySQL(String sql, Map<String, Object> map) {
		try {
			String count = "select COUNT(1) count  from ("+sql +") t";
			SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(count);
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
	public List<ApplicationTranscationLogVO> findBySQL(String sql,
			Map<String, ?> map) {
		try {
		      SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		      Set<String> keys = map.keySet();
		      query.addScalar("id", Hibernate.INTEGER)
		      .addScalar("username", Hibernate.STRING)
		      .addScalar("useremail", Hibernate.STRING)
		      .addScalar("dealDate", Hibernate.TIMESTAMP)
		      .addScalar("transaction_account", Hibernate.BIG_DECIMAL)
		      .addScalar("balance", Hibernate.BIG_DECIMAL)
		      .addScalar("app_name", Hibernate.STRING)
		      .addScalar("supplier", Hibernate.BOOLEAN)
		      .addScalar("type", Hibernate.INTEGER)
		      .addScalar("remark", Hibernate.STRING);
		      query.setResultTransformer(Transformers.aliasToBean(ApplicationTranscationLogVO.class));
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
	
	
}
