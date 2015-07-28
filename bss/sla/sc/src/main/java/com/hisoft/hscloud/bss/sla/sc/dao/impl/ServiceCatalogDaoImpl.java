package com.hisoft.hscloud.bss.sla.sc.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.dao.ServiceCatalogDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.vo.SCVo;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

@Repository
public class ServiceCatalogDaoImpl extends
		HibernateDao<ServiceCatalog, Integer> implements ServiceCatalogDao {
	private Logger logger = Logger.getLogger(ServiceCatalogDaoImpl.class);

	@Override
	public ServiceCatalog findScById(int id) throws HsCloudException {
		ServiceCatalog result = null;
		try {
			result = findUniqueBy("id", id);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}

	@Override
	public Date getCreateTime(int id) throws HsCloudException {
		Date createDate = null;
		String hql = "select createDate from ServiceCatalog where id=?";
		try {
			createDate = super.findUnique(hql, id);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return createDate;
	}

	@Override
	public void save(ServiceCatalog sc) throws HsCloudException {
		try {
			super.save(sc);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<ServiceCatalog> getServiceCatalogList(String hql,
			Map<String, Object> params) throws HsCloudException {
		List<ServiceCatalog> result = null;
		try {
			if (params == null) {
				result = super.find(hql);
			} else {
				result = super.find(hql, params);
			}

		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}

	@Override
	public Page<ServiceCatalog> findByPage(Page<ServiceCatalog> page,
			String hql, Map<String, Object> params) throws HsCloudException {
		Page<ServiceCatalog> result = null;
		try {
			result = super.findPage(page, hql, params);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}

	@Override
	public List<SCVo> getRelatedSCByBrandId(long brandId, int limit, int page,
			String query) throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT DISTINCT t1.id,t1.`name` as scName FROM ")
					.append(" hc_service_catalog t1,hc_catalog_brand t2,hc_user_brand t3 ")
					.append(" WHERE t1.id = t2.service_catalog_id ")
					.append(" AND t2.brand_id = t3.id ")
					.append(" AND t1.`status` in (1,3) ")
					.append(" AND t1.expiration_date > NOW() ")
					.append(" AND t3.id = :brandId ");
			if (StringUtils.isNotBlank(query)) {
				sql.append(" AND t1.`name` like '%").append(query).append("%'");
			}
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.addScalar("id", Hibernate.LONG);
			sqlQuery.addScalar("scName", Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(SCVo.class));
			sqlQuery.setParameter("brandId", brandId);
			return sqlQuery.setFirstResult(page * limit - limit)
					.setMaxResults(limit).list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<SCVo> getUnRelatedScByBrandId(long brandId, int limit,
			int page, String query) throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"SELECT DISTINCT t5.id,t5.`name` as scName FROM hc_service_catalog t5 ")
					.append(" where t5.id not in ( select DISTINCT t1.id from  ")
					.append(" 	hc_service_catalog t1,hc_catalog_brand t2,hc_user_brand t3 ")
					.append(" WHERE t1.id = t2.service_catalog_id ")
					.append(" AND t2.brand_id = t3.id ")
					.append(" AND t3.id = :brandId ) ")
					.append(" AND t5.`status` in (1,3) ")
					.append(" AND t5.expiration_date > NOW() ");
			if (StringUtils.isNotBlank(query)) {
				sql.append(" AND t5.`name` like '%").append(query).append("%'");
			}
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.addScalar("id", Hibernate.LONG);
			sqlQuery.addScalar("scName", Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(SCVo.class));
			sqlQuery.setParameter("brandId", brandId);
			return sqlQuery.setFirstResult(page * limit - limit)
					.setMaxResults(limit).list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public int getRelatedSCcountByBrandId(long brandId, int limit, int pageNo,
			String query) throws HsCloudException {
		int result=0;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT DISTINCT t1.id,t1.`name` as scName FROM ")
					.append(" hc_service_catalog t1,hc_catalog_brand t2,hc_user_brand t3 ")
					.append(" WHERE t1.id = t2.service_catalog_id ")
					.append(" AND t2.brand_id = t3.id ")
					.append(" AND t1.`status` in (1,3) ")
					.append(" AND t1.expiration_date > NOW() ")
					.append(" AND t3.id = :brandId ");
			if (StringUtils.isNotBlank(query)) {
				sql.append(" AND t1.`name` like '%").append(query).append("%'");
			}
			String count = "select COUNT(1) count  from ("+sql.toString() +") t";
			SQLQuery sqlQuery = getSession().createSQLQuery(count);
			sqlQuery.setParameter("brandId", brandId);
			sqlQuery.addScalar("count", Hibernate.INTEGER);
			List<Integer> list = sqlQuery.list();
			result =list.get(0);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}

	@Override
	public int getUnRelatedScCountByBrandId(long brandId, int limit,
			int pageNo, String query) throws HsCloudException {
		int result=0;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"SELECT DISTINCT t5.id,t5.`name` as scName FROM hc_service_catalog t5 ")
					.append(" where t5.id not in ( select DISTINCT t1.id from  ")
					.append(" 	hc_service_catalog t1,hc_catalog_brand t2,hc_user_brand t3 ")
					.append(" WHERE t1.id = t2.service_catalog_id ")
					.append(" AND t2.brand_id = t3.id ")
					.append(" AND t3.id = :brandId ) ")
					.append(" AND t5.`status` in (1,3) ")
					.append(" AND t5.expiration_date > NOW() ");
			if (StringUtils.isNotBlank(query)) {
				sql.append(" AND t5.`name` like '%").append(query).append("%'");
			}
			String count = "select COUNT(1) count  from ("+sql.toString() +") t";
			SQLQuery sqlQuery = getSession().createSQLQuery(count);
			sqlQuery.setParameter("brandId", brandId);
			sqlQuery.addScalar("count", Hibernate.INTEGER);
			List<Integer> list = sqlQuery.list();
			result =list.get(0);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}

	@Override
	public List<SCVo> getRelatedScByReferenceId(long referenceId)
			throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"SELECT DISTINCT t5.id,t5.`name` as scName ")
					.append(" FROM hc_vpdc_reference t1 ")
					.append(" left join hc_user t2 on t1.vm_owner=t2.id ")
					.append(" left join hc_user_brand t3 ")
					.append(" on t2.level=t3.code ")
					.append(" left join hc_catalog_brand t4")
					.append(" on t3.id=t4.brand_id")
					.append(" left join hc_service_catalog t5")
					.append(" on t4.service_catalog_id=t5.id")
					.append(" where t5.`status` in (1,3) ")
					.append(" AND t5.expiration_date > NOW() ")
					.append(" AND t1.id = :referenceId");
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.addScalar("id", Hibernate.LONG);
			sqlQuery.addScalar("scName", Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(SCVo.class));
			sqlQuery.setParameter("referenceId",referenceId);
			return sqlQuery.list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public ScIsolationConfig getScIsolationConfigByScId(int scId)
			throws HsCloudException {
		ScIsolationConfig scIsolationConfig = null;
		try{
			StringBuffer hql = new StringBuffer("select sc.scIsolationConfig from ServiceCatalog sc where sc.id=:id");					
			Query query = getSession().createQuery(hql.toString());
			query.setParameter("id", scId);
			scIsolationConfig = (ScIsolationConfig) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getScIsolationConfigByScId Exception:", e);
        }
		return scIsolationConfig;
	}

	@Override
	public ServiceCatalog getByCode(String scCode, String domainCode,String brandCode)
			throws HsCloudException {
		ServiceCatalog serviceCatalog = null;
		try{
			StringBuffer hql = new StringBuffer("SELECT sc FROM ServiceCatalog sc,Domain dn,UserBrand ub WHERE sc.catalogCode =:catalogCode AND dn.code=:code AND ub.code=:ubcode AND dn in elements(sc.domainList) AND ub in elements(sc.userBrand)");
			Query query = getSession().createQuery(hql.toString());
			query.setParameter("catalogCode", scCode);
			query.setParameter("code", domainCode);
			query.setParameter("ubcode", brandCode);
			serviceCatalog =(ServiceCatalog) query.uniqueResult();
		}catch(Exception e){
			logger.error("getByCode exception:",e);
		}
		return serviceCatalog;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<DomainVO> getDomainCodebyId(List<Domain> domainList) {
		List<DomainVO> domainVO = null;
		List list = new ArrayList();
		for(int i=0;i<domainList.size();i++){
			list.add(domainList.get(i).getId());
		}
		try{
			StringBuffer hql = new StringBuffer("SELECT dn FROM Domain dn where dn.id IN (domainIdList :domainIdList)");
			Query query = getSession().createSQLQuery(hql.toString());
			query.setParameter("domainIdList", list);
//			domainVO =  (List<DomainVO>) query.uniqueResult();
			domainVO =  (List<DomainVO>) query.list();
		}catch(Exception e){
			logger.error("getDomainCodebyId exception:",e);
		}
		return domainVO;
	}

	@Override
	public List<ServiceCatalog> getServiceCatalogCodeByCondtion(
			ServiceCatalog catalogCode) throws HsCloudException {
		List<ServiceCatalog> sc= null;
		StringBuffer hql = new StringBuffer();
		try{
			
				hql.append("from ServiceCatalog as sc where 2>1 ");
				hql.append(" and sc.catalogCode")
				.append("= :catalogCode");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("catalogCode", catalogCode.getCatalogCode());
//				sc = this.findUnique(hql.toString(), map);
				sc = super.find(hql.toString(), map);
			
		}catch(Exception e){
			logger.info("getServiceCatalogCodeByCondtion Exception");
		}
		return sc;
	}
	
	
}
