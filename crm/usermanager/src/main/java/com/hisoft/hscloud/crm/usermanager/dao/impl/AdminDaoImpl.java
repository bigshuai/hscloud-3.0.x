package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.dao.AdminDao;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.vo.AdminVO;

@Repository
public class AdminDaoImpl extends HibernateDao<Admin, Long> implements AdminDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public Admin LoginByEmail(String email, String password) {
		
		try{
			
			String hql = "from Admin where email=? and password=?";
			if(logger.isDebugEnabled()){
				logger.debug("enter AdminDaoImpl LoginByEmail method.");
				logger.debug("email:"+email);
				logger.debug("password:"+password);
				logger.debug("hql:"+hql);
			}
			
			Admin admin = (Admin)findUnique(hql, email,password);
			
			if(logger.isDebugEnabled()){
				logger.debug("exit AdminDaoImpl LoginByEmail method");
				logger.debug("admin:"+admin);
			}
			return admin;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public Page<Admin> findPage(Page<Admin> page, String hql, Map<String, ?> map) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter AdminDaoImpl findPage method.");
				logger.debug("hql:"+hql);
				logger.debug("map:"+map);
			}
			
			Page<Admin> admins = super.findPage(page, hql, map);
			
			if(logger.isDebugEnabled()){
				logger.debug("exit AdminDaoImpl LoginByEmail method");
				logger.debug("admin:"+admins);
			}
			return admins;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	}

	@Override
	public void save(Admin admin) {
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter AdminDaoImpl save method.");
				logger.debug("admin:"+admin);
			}
			super.save(admin);
			if(logger.isDebugEnabled()){
				logger.debug("exit AdminDaoImpl save method");
			}
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	@Transactional(readOnly = true)
	public Admin get(long adminId) {
		return (Admin) super.get(adminId);
	}

	@Override
	public void delete(long adminId) {
		super.delete(adminId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AdminVO> getAllAdminUser(List<Sort> sorts,String queryStr,int start,int limit,int page,String type) {
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id,a.name,a.type,a.email,a.is_enable as enable,")
		.append("a.is_super as isSuper,a.telephone,")
		.append("a.create_date as createDate,a.last_login_date as lastLoginDate,")
		.append("r.name as roleName,r.id as roleId, ")
		.append("b.id as adminRoleId ")
		.append("from hc_admin a ")
		.append("LEFT OUTER JOIN hc_admin_role b ON a.id = b.adminId ")
		.append("LEFT OUTER JOIN hc_role r ON b.roleId = r.id ")
		.append(" where 1=1 and a.is_super!=1 ");
		if(StringUtils.isNotBlank(queryStr)&&StringUtils.isNotBlank(type)){
			if("adminName".equals(type)){
				sql.append(" and a.name like '%").append(queryStr).append("%' ");
			}else if("adminEmail".equals(type)){
				sql.append(" and a.email like '%").append(queryStr).append("%' ");
			}
		}
		for (int i = 0; i < sorts.size(); i++) {
			Sort sort = sorts.get(i);
			if (i == 0) {
				sql.append(" order by ");
			}
			sql.append(sort.getProperty() + " " + sort.getDirection());

			if (i < sorts.size() - 1) {
				sql.append(",");
			}
		}
		logger.info("sql:"+ sql.toString());
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id",Hibernate.LONG);
		query.addScalar("name",Hibernate.STRING);
		query.addScalar("email",Hibernate.STRING);
		query.addScalar("enable",Hibernate.SHORT);
		query.addScalar("isSuper",Hibernate.BOOLEAN);
		query.addScalar("telephone",Hibernate.STRING);
		query.addScalar("createDate",Hibernate.TIMESTAMP);
		query.addScalar("lastLoginDate",Hibernate.TIMESTAMP);
		query.addScalar("roleName",Hibernate.STRING);
		query.addScalar("roleId",Hibernate.LONG);
		query.addScalar("adminRoleId",Hibernate.LONG);
		query.addScalar("type",Hibernate.INTEGER);
		query.setResultTransformer(Transformers.aliasToBean(AdminVO.class));
		List<AdminVO> result=query.setFirstResult(page*limit-limit).setMaxResults(limit).list();
		return result;
	}

	@Override
	public long getAdminCount(String queryStr,String type) {
		logger.info("enter getAdminCount method.");
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id,a.name,a.email,a.is_enable as enable,")
		.append("a.is_super as isSuper,a.password,a.telephone,")
		.append("a.create_date as createDate,a.last_login_date as lastLoginDate,")
		.append("r.name as roleName,r.id as roleId, ")
		.append("b.id as adminRoleId ")
		.append("from hc_admin a ")
		.append("LEFT OUTER JOIN hc_admin_role b ON a.id = b.adminId ")
		.append("LEFT OUTER JOIN hc_role r ON b.roleId = r.id ")
		.append(" where 1=1 and a.is_super!=1 ");
		if(StringUtils.isNotBlank(queryStr)&&StringUtils.isNotBlank(type)){
			if("adminName".equals(type)){
				sql.append(" and a.name like '%").append(queryStr).append("%' ");
			}else if("adminEmail".equals(type)){
				sql.append(" and a.email like '%").append(queryStr).append("%' ");
			}
		}
		String count = "select COUNT(1) count  from ("+sql.toString() +") t";
		logger.info("countsql:"+ count);
		SQLQuery query = getSession().createSQLQuery(count);
		logger.info("exit getAdminCount method.");
		query.addScalar("count", Hibernate.LONG);
		List<Long> list = query.list();
		return list.get(0);
		//return super.countHqlResult("from Admin a where a.isSuper!=1 ",new HashMap());
	}
	@Override
	public Admin getAdminByEmail(String email){
		
		try{
			
			String hql = "from Admin where email=? ";
			if(logger.isDebugEnabled()){
				logger.debug("enter AdminDaoImpl getAdminByEmail method.");
				logger.debug("email:"+email);
			}
			Admin admin =  findUnique(hql, email);
			if(logger.isDebugEnabled()){
				logger.debug("enter AdminDaoImpl getAdminByEmail method.");
			}
			return admin;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	

}
