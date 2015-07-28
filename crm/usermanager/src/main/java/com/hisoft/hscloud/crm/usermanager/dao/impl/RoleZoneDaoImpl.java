/* 
* 文 件 名:  RoleZoneDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-10-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.dao.impl; 

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.RoleZoneDao;
import com.hisoft.hscloud.crm.usermanager.entity.RoleZone;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-10-21] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class RoleZoneDaoImpl  extends HibernateDao<RoleZone, Long> implements RoleZoneDao {

	/** 
	 * @param roleId
	 * @param zoneIds
	 * @return 
	 */
	@Override
	public boolean addZoneOfRole(long roleId, List<Object> zoneIds) {
		boolean result = false;
		RoleZone roleZone = null;
		try{
			for(Object zoneId : zoneIds) {
	        	roleZone = new RoleZone();
	        	roleZone.setRoleId(roleId);
	        	roleZone.setZoneId(Long.valueOf(zoneId.toString()));
	            this.save(roleZone);
	        }
			result = true;
		}catch (Exception e) {
			logger.error("addZoneOfRole Exception:", e);
		}        
		return result;
	}

	/** 
	 * @param roleId
	 * @param zoneIds
	 * @return 
	 */
	@Override
	public boolean deleteZoneOfRole(long roleId, List<Object> zoneIds) {
		boolean result = false;
		String sql = "delete from hc_role_zone where role_id = :roleId and zone_id in (:ids)";
		try{			
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setParameter("roleId", roleId);
			query.setParameterList("ids", zoneIds);
			query.executeUpdate();
			result = true;
		}catch (Exception e) {
			logger.error("deleteZoneOfRole Exception:", e);
		}		
		return result;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasZoneOfRolePermission(long roleId, long zoneId) {
		boolean result = false;
		List<RoleZone> list = null;
		StringBuffer sql = new StringBuffer("select rz.id from hc_role_zone as rz where rz.role_Id=:roleId and rz.zone_Id=:zoneId");
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("roleId", roleId);
		sqlQuery.setParameter("zoneId", zoneId);
		sqlQuery.addScalar("id",Hibernate.LONG);
		list= sqlQuery.list();
		if(list != null && list.size()>0){
			result = true;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getZoneIdsByAdminId(long adminId) {	
		StringBuffer sql = new StringBuffer("select distinct rz.zone_id as id from hc_role_zone as rz ");
		sql.append("LEFT JOIN hc_admin_role as ar ON (rz.role_id = ar.roleId) where ar.adminId=:adminId");
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		sqlQuery.setParameter("adminId", adminId);
		sqlQuery.addScalar("id",Hibernate.LONG);
		return sqlQuery.list();
	}

	@Override
	public boolean deleteZoneOfRole(long zoneId) {
		boolean result = false;
		String sql = "delete from hc_role_zone where zone_id =:zoneId";
		try{			
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setParameter("zoneId", zoneId);
			query.executeUpdate();
			result = true;
		}catch (Exception e) {
			logger.error("deleteZoneOfRole Exception:", e);
		}		
		return result;
	}
}
