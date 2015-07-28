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
import com.hisoft.hscloud.bss.billing.dao.IPMessageVODao;
import com.hisoft.hscloud.bss.billing.dao.MonthStatisVODao;
import com.hisoft.hscloud.bss.billing.vo.IpMessageVO;
import com.hisoft.hscloud.bss.billing.vo.MonthStatisVO;
import com.hisoft.hscloud.common.util.HsCloudException;

@Repository
public class IPMessageVODaoImpl  extends HibernateDao<IpMessageVO, Long> implements IPMessageVODao{

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<IpMessageVO> findBySQL(String sql, Map<String, ?> map) {		
		try {
			SQLQuery query = getSession().createSQLQuery(sql);
//			Set<String> keys = map.keySet();		
			query.addScalar("userName", Hibernate.STRING)
					.addScalar("userProperties", Hibernate.STRING)
					.addScalar("userAddress", Hibernate.STRING)
					.addScalar("contactName", Hibernate.STRING)
					.addScalar("contactPhone", Hibernate.STRING)
					.addScalar("contactDocumentNum", Hibernate.STRING)
					.addScalar("installedAddress", Hibernate.STRING)
					.addScalar("installedDate", Hibernate.TIMESTAMP)
					.addScalar("startIp", Hibernate.STRING)
					.addScalar("endIp", Hibernate.STRING);
			query.setResultTransformer(Transformers
					.aliasToBean(IpMessageVO.class));
//			for (String key : keys) {
//				Object o = map.get(key);
//				if (o instanceof Collection) {
//					query.setParameterList(key, (Collection) map.get(key));
//				} else if(o instanceof Page){
//		    		  query.setFirstResult((((Page) o).getPageNo()-1)*((Page) o).getPageSize());
//		    		  query.setMaxResults(((Page) o).getPageSize());
//		    	}else {
//					query.setParameter(key, map.get(key));
//				}				
//			}
			return query.list();

		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,
					e.getMessage(), logger, e);
		}
		
	}	
}
