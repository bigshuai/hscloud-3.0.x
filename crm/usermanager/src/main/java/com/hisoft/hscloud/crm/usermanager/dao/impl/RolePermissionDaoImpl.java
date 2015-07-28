package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.RolePermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.RolePermission;
//import com.mysql.jdbc.Connection;

@Repository
public class RolePermissionDaoImpl extends HibernateDao<RolePermission, Long> implements RolePermissionDao{
	
	@Override
	public void save(RolePermission rolePermission){
		super.save(rolePermission);
	}
	
	@Override
	public void delete(String hql, Object... value) {
		Query query = (Query)super.createQuery(hql, value);
		query.executeUpdate();
	}

	@Override
	public void batchDelete(Map<String, Object> condition) {
		Query query = (Query)super.createQuery("delete RolePermission where roleId = :roleId and permissionId in " +
				"(select id from Permission where resourceId in (:resource))", condition);
		query.executeUpdate();
	}

	@Override
	public void deleteRPForMenu(Long roleId, List<String> list) {
		String sql = "delete from hc_role_permission where role_id = :roleId and permission_id in (:ids)";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("roleId", roleId);
		query.setParameterList("ids", list);
		query.executeUpdate();
	}
}
