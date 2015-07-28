package com.hisoft.hscloud.bss.billing.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.billing.dao.CapitalSourceDao;
import com.hisoft.hscloud.bss.billing.vo.CapitalSource;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;

@Repository
public class CapitalSourceDaoImpl extends HibernateDao<CapitalSource, Long> implements CapitalSourceDao{

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CapitalSource> findBySQL(String sql) {
		try {
	     	SQLQuery query = getSession().createSQLQuery(sql);
	     	query.addScalar("yearMonth", Hibernate.STRING).addScalar("oldPlatform", Hibernate.BIG_DECIMAL).addScalar("alipay", Hibernate.BIG_DECIMAL).addScalar("easyMoney", Hibernate.BIG_DECIMAL).addScalar("eBank", Hibernate.BIG_DECIMAL).addScalar("cash", Hibernate.BIG_DECIMAL).addScalar("cheque", Hibernate.BIG_DECIMAL);
		    query.setResultTransformer(Transformers.aliasToBean(CapitalSource.class));
		    return query.list();
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
		}
		
	}

}
