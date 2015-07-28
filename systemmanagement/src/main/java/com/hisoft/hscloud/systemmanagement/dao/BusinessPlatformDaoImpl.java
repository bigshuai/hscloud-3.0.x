package com.hisoft.hscloud.systemmanagement.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;

@Repository
public class BusinessPlatformDaoImpl extends
		HibernateDao<BusinessPlatformVO, Long> implements BusinessPlatformDao {
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public List<BusinessPlatformVO> getAllBusinessPlatformByPage(int pageSize,
			int pageNo, String queryCondition, List<Long> domainIds)
			throws HsCloudException {
		// TODO Auto-generated method stub
		List<BusinessPlatformVO> result = null;
		try {
			StringBuilder sql = new StringBuilder("select ");
			sql.append("t1.id as userId,t1.name as username,")
					.append("t1.email,t2.abbreviation as domainName,")
					.append("t3.name as brandName,t1.create_date as createDate")
					.append(" from hc_user t1,hc_domain t2,hc_user_brand t3")
					.append(" where 1=1");

			Map<String, Object> params = new HashMap<String, Object>();
			sql.append(" and t1.domain_id = t2.id and t1.level = t3.code");
			if (StringUtils.isNotBlank(queryCondition)) {
				sql.append(" and (t1.name like :query or t1.email like :query) ");
				params.put("query", "%"+queryCondition+"%");
			}

			if (domainIds != null && domainIds.size() > 0) {
				sql.append(" and t2.id in (:domainIds)");
				params.put("domainIds", domainIds);
			}
			
			sql.append(" and t1.is_enable = 3 ");
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.setProperties(params);
			sqlQuery.addScalar("userId", Hibernate.LONG);
			sqlQuery.addScalar("username", Hibernate.STRING);
			sqlQuery.addScalar("email", Hibernate.STRING);
			sqlQuery.addScalar("domainName", Hibernate.STRING);
			sqlQuery.addScalar("brandName", Hibernate.STRING);
			sqlQuery.addScalar("createDate", Hibernate.TIMESTAMP);
			sqlQuery.setResultTransformer(Transformers
					.aliasToBean(BusinessPlatformVO.class));
			result = sqlQuery.setFirstResult(pageNo * pageSize - pageSize)
					.setMaxResults(pageSize).list();
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public long getAllBusinessPlatformCount(String queryCondition,
			List<Long> domainIds) throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("select ");
			sql.append("t1.id as userId,t1.name as username,")
					.append("t1.email,t2.name as domainName,")
					.append("t3.name as brandName,t1.create_date as createDate")
					.append(" from hc_user t1,hc_domain t2,hc_user_brand t3")
					.append(" where 1=1");

			Map<String, Object> params = new HashMap<String, Object>();
			sql.append(" and t1.domain_id = t2.id and t1.level = t3.code");
			if (StringUtils.isNotBlank(queryCondition)) {
				sql.append(" and (t1.name like :query or t1.email like :query) ");
				params.put("query", "%"+queryCondition+"%");
			}

			if (domainIds != null && domainIds.size() > 0) {
				sql.append(" and t2.id in (:domainIds)");
				params.put("domainIds", domainIds);
			}
			sql.append(" and t1.is_enable = 3 ");
			String count = "select COUNT(1) count  from (" + sql.toString()
					+ ") t";
			SQLQuery sqlQuery = getSession().createSQLQuery(count);
			sqlQuery.setProperties(params);
			logger.info("exit getAdminCount method.");
			sqlQuery.addScalar("count", Hibernate.LONG);
			List<Long> list = sqlQuery.list();
			return list.get(0);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

}
