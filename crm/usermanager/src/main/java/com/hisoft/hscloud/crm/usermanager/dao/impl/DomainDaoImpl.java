/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserBrandVO;
import com.hisoft.hscloud.crm.usermanager.dao.DomainDao;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.util.Constant;
import com.hisoft.hscloud.crm.usermanager.vo.DomainVO;

/**
 * @author lihonglei
 *
 */
@Repository
public class DomainDaoImpl extends HibernateDao<Domain, Long> implements DomainDao{
	
	private Logger logger = Logger.getLogger(this.getClass());

    /**
     *  编辑分平台
     * @see com.hisoft.hscloud.crm.usermanager.dao.DomainDao#edit(com.hisoft.hscloud.crm.usermanager.entity.Domain)
     */
    @Override
    public long edit(Domain domain) {
        this.save(domain);
        return domain.getId();
    }
    
    /**
     * 获取所有分平台
     * @return
     */
    @Override
    public List<Domain> getAllDomain() {
        String hql = "from Domain where status = :status";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("status", Constant.DOMAIN_STATUS_VALID);
        return this.find(hql, condition);
    }

    /**
     *  根据id查询分平台
     * @see com.hisoft.hscloud.crm.usermanager.dao.DomainDao#getDomainById(long)
     */
    @Override
    public Domain getDomainById(long domainId) {
        Domain domain = this.findUniqueBy("id", domainId);
        return domain;
    }
    
    /**
     * 查询有效分平台页
     * @param page
     * @return
     */
    @Override
    public Page<Domain> findValidDomainPage(Page<Domain> page, String query) {
        StringBuilder hql = new StringBuilder("from Domain where status = :status ");
        
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("status", Constant.DOMAIN_STATUS_VALID);
        if(StringUtils.isNotBlank(query)) {
            hql.append(" and (name like :query or code like :query) ");
            condition.put("query", "%" + query + "%");
        }
        hql.append(" order by id desc ");
        
        return this.findPage(page, hql.toString(), condition);
    }
    
    /**
     * 查询分平台页
     * @param page
     * @return
     */
    @Override
    public Page<Domain> findDomainPage(Page<Domain> page, String query) {
        StringBuilder hql = new StringBuilder("from Domain ");
        
        Map<String, Object> condition = new HashMap<String, Object>();
     //   condition.put("status", Constant.DOMAIN_STATUS_VALID);
        if(StringUtils.isNotBlank(query)) {
            hql.append(" where (name like :query or code like :query) ");
            condition.put("query", "%" + query + "%");
        }
        hql.append(" order by id desc ");
        
        return this.findPage(page, hql.toString(), condition);
    }

    /**
     * 根据编码查询分平台
     * 查询不到code对应分平台返回空
     * @see com.hisoft.hscloud.crm.usermanager.dao.DomainDao#getDomainByCode(java.lang.String)
     */
    @Override
    public Domain getDomainByCode(String code) {
        List<Domain> list = this.findBy("code", code);
        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 查询分平台是否已存在相同的编码，名称，简称
     */
    @Override
    public Domain getDomainByCondition(DomainVO domain) {
        String hql = "from Domain where (name = :name or code = :code or abbreviation = :abbreviation) and id != :id ";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", domain.getName());
        condition.put("code", domain.getCode());
        condition.put("abbreviation", domain.getAbbreviation());
        condition.put("id", domain.getId());
        List<Domain> list = this.find(hql, condition);
        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 根据全名查分平台
     * @param name
     * @return
     */
    @Override
    public List<Domain> findDomainByName(String name) {
        String hql = "from Domain where name like :name and status = :status";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", "%" + name + "%");
        condition.put("status", Constant.DOMAIN_STATUS_VALID);
        return this.find(hql, condition);
    }

    @Override
	@SuppressWarnings("unchecked")
	public List<Domain> findBySQL(String sql, Map<String, ?> map) {
		try{
			
			  if(logger.isDebugEnabled()){
				logger.debug("enter DomainDaoImpl findBySQL method.");
				logger.debug("sql:"+sql);
				logger.debug("map:"+map);
			  }
		      SQLQuery query = getSession().createSQLQuery(sql);
		      query.addEntity(Domain.class);
		      Set<String> keys = map.keySet();
		      for (String key : keys) {
		    	  if(map.get(key) instanceof Collection){
		    		  query.setParameterList(key, (Collection<?>)map.get(key));
		    	  }else{
		    		  query.setParameter(key, map.get(key));
		    	  }
			  }
		      List<Domain> domains =   query.list();
			  if(logger.isDebugEnabled()){
				logger.debug("enter DomainDaoImpl findBySQL method.");
			  }
			  return domains;
			  
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<UserBrandVO> getRelatedBrand(Page<UserBrandVO> page, long domainId,
			String domainName) throws HsCloudException {
		int totalCount = 0;
		StringBuffer sql=new StringBuffer("SELECT brand.id AS brandId,brand.name AS brandName FROM  hc_user_brand AS brand");
		sql.append(" INNER JOIN hc_domain_userbrand AS du ON (brand.id = du.userbrand_id)");
		sql.append(" WHERE brand.status = 1 AND du.domain_id =:domainId");
		if(domainName != null && !"".equals(domainName)){
			sql.append(" and brand.name like :name ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("domainId", domainId);
		if(domainName != null && !"".equals(domainName)){
			sqlQuery.setParameter("name", "%"+domainName+"%");
		}
		sqlQuery.addScalar("brandId",Hibernate.LONG);
		sqlQuery.addScalar("brandName",Hibernate.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(UserBrandVO.class));		
		totalCount = sqlQuery.list().size();
		sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		sqlQuery.setMaxResults(page.getPageSize());
		List<UserBrandVO> brandList = sqlQuery.list();
		page.setTotalCount(totalCount);
		page.setResult(brandList);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<UserBrandVO> getUnRelatedBrand(Page<UserBrandVO> page,
			long domainId, String domainName) throws HsCloudException {
		int totalCount = 0;
		StringBuffer sql=new StringBuffer("SELECT brand.id AS brandId,brand.name AS brandName FROM  hc_user_brand AS brand WHERE brand.status = 1");
		sql.append(" AND brand.id NOT IN ( SELECT du.userbrand_id FROM hc_domain_userbrand AS du WHERE du.domain_id=:domainId)");
		if(domainName != null && !"".equals(domainName)){
			sql.append(" and brand.name like :name ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("domainId", domainId);
		if(domainName != null && !"".equals(domainName)){
			sqlQuery.setParameter("name", "%"+domainName+"%");
		}
		sqlQuery.addScalar("brandId",Hibernate.LONG);
		sqlQuery.addScalar("brandName",Hibernate.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(UserBrandVO.class));		
		totalCount = sqlQuery.list().size();
		sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		sqlQuery.setMaxResults(page.getPageSize());
		List<UserBrandVO> brandList = sqlQuery.list();
		page.setTotalCount(totalCount);
		page.setResult(brandList);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBrandVO> getRelatedBrandByDomainId(long domainId)
			throws HsCloudException {
		StringBuffer sql=new StringBuffer("SELECT brand.id AS brandId,brand.code brandCode,brand.name AS brandName FROM  hc_user_brand AS brand");
		sql.append(" INNER JOIN hc_domain_userbrand AS du ON (brand.id = du.userbrand_id)");
		sql.append(" WHERE brand.status = 1 AND du.domain_id =:domainId");		
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("domainId", domainId);		
		sqlQuery.addScalar("brandId",Hibernate.LONG);
		sqlQuery.addScalar("brandName",Hibernate.STRING);
		sqlQuery.addScalar("brandCode",Hibernate.STRING);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(UserBrandVO.class));		
		List<UserBrandVO> brandList = sqlQuery.list();		
		return brandList;
	}

	@Override
	public Long getBrandIdByBrandAbbreviation(String abbreviation)
			throws HsCloudException {
		String hql = "from Domain where abbreviation = :abbreviation";
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("abbreviation", abbreviation);
		List<Domain> domain_list = this.find(hql, condition);
		return domain_list.get(0).getId();
	}

}