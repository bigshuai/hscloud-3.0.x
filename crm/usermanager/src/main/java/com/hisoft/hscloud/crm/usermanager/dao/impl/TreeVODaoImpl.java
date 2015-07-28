package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.TreeVODao;
import com.hisoft.hscloud.crm.usermanager.vo.AdminMenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.CheckboxVO;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;
import com.hisoft.hscloud.crm.usermanager.vo.PrivilegeVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeQueryVO;
import com.hisoft.hscloud.crm.usermanager.vo.TreeVO;
import com.hisoft.hscloud.crm.usermanager.vo.UniformDefQueryVO;
@Repository
public class TreeVODaoImpl  extends HibernateDao<TreeVO, Long> implements TreeVODao{

	
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeVO> findBySQL(String sql, Map<String, ?> map) {

	      SQLQuery query = getSession().createSQLQuery(sql);
	      query.addScalar("primKey", Hibernate.STRING).addScalar("permissionId", Hibernate.LONG).addScalar("actionId", Hibernate.LONG).addScalar("level", Hibernate.LONG).addScalar("resourceId", Hibernate.LONG)
	      .addScalar("resourceName", Hibernate.STRING).addScalar("acitonName", Hibernate.STRING).addScalar("resourceType", Hibernate.STRING).addScalar("id", Hibernate.STRING);
	      query.setResultTransformer(Transformers.aliasToBean(TreeVO.class));
	      Set<String> keys = map.keySet();
	      for (String key : keys) {
	    	  if(map.get(key) instanceof Collection){
	    		  query.setParameterList(key, (Collection<?>)map.get(key));
	    	  }else{
	    		  query.setParameter(key, map.get(key));
	    	  }
		  }
	      return query.list();
	      
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeQueryVO> findTreeByHibernateSQL(String sql,Map<String, Object> map){
		
		SQLQuery query = getSession().createSQLQuery(sql);
		for (Entry<String, Object> entry : map.entrySet()) {
		    query.setParameter(entry.getKey(), entry.getValue());
		}
		
	    query.addScalar("id", Hibernate.STRING).addScalar("name", Hibernate.STRING)
	    .addScalar("type", Hibernate.STRING).addScalar("permissionId", Hibernate.LONG)
	    .addScalar("resourceId", Hibernate.LONG).addScalar("actionId", Hibernate.LONG)
	    .setResultTransformer(Transformers.aliasToBean(TreeQueryVO.class));

        return query.list();
       /* List<TreeVO>  lists = new ArrayList<TreeVO>();
        lists = query.list();
        return lists;*/
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AdminMenuVO> findMenuBySQL(String sql, Map<String, Object> map) {
		SQLQuery query = getSession().createSQLQuery(sql);
		for (Entry<String, Object> entry : map.entrySet()) {
	    	  if(entry.getValue() instanceof Collection){
	    		  query.setParameterList(entry.getKey(), (Collection<?>)entry.getValue());
	    	  }else{
	    		  query.setParameter(entry.getKey(), entry.getValue());
	    	  }
		  }
		query.addScalar("id", Hibernate.STRING);
		query.addScalar("name", Hibernate.STRING);
		query.addScalar("url", Hibernate.STRING);
		query.addScalar("icon", Hibernate.STRING);
		query.addScalar("parentId", Hibernate.STRING);
		query.setResultTransformer(Transformers.aliasToBean(AdminMenuVO.class));
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findCount(String sql, Map<String, Object> map) {
		SQLQuery query = getSession().createSQLQuery(sql);
		for (Entry<String, Object> entry : map.entrySet()) {
	    	  if(entry.getValue() instanceof Collection){
	    		  query.setParameterList(entry.getKey(), (Collection<?>)entry.getValue());
	    	  }else{
	    		  query.setParameter(entry.getKey(), entry.getValue());
	    	  }
		  }
		query.addScalar("count", Hibernate.LONG);
		//query.setResultTransformer(Transformers.aliasToBean(AdminMenuVO.class));
		//query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeQueryVO> findPageBySQL(String sql, Map<String, Object> map, int firstResult, int maxResult) {
		SQLQuery query = getSession().createSQLQuery(sql);
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				query.setParameterList(entry.getKey(),
						(Collection<?>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		query.addScalar("id", Hibernate.STRING)
				.addScalar("name", Hibernate.STRING)
				.addScalar("type", Hibernate.STRING)
				.addScalar("permissionId", Hibernate.LONG)
				.addScalar("resourceId", Hibernate.LONG)
				.addScalar("actionId", Hibernate.LONG)
				.setResultTransformer(Transformers.aliasToBean(TreeQueryVO.class));
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeQueryVO> findUnassignedList(Map<String, Object> map, Page<PrivilegeVO> pagePrivilege) {
		String resourceCondition = (String)map.get("resourceCondition");
		String table = (String)map.get("table");
		String queryStr = (String)map.get("query");
		
		if(StringUtils.isNotBlank(queryStr)) {
			table = "(select * from " + table + " where name like '%" + queryStr + "%' )";
		}
		
		//20130325 start
		String conditionSql = (String)map.get("conditionTable");
		SQLQuery conditionQuery = getSession().createSQLQuery(conditionSql);
		conditionQuery.setParameter("conditionId", ((Map<String, Object>)map.get("conditionMap")).get("conditionId"));
		List<BigInteger> list = conditionQuery.list();
		list.add(new BigInteger("-1"));
		/*StringBuilder permissionIdsStr = new StringBuilder();
		for(BigInteger array : list) {
		    permissionIdsStr.append(array[0].toString()).append(",");
		}*/
	//	System.out.println(list.toString());
		//20130325 end
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("select x.id as id, x.name as name, :type as type, p.id as permissionId, r.id as resourceId, p.action_id as actionId ");
		sql.append("from " + table + " as x ");
		sql.append("left join hc_resource r on (x.id = r.primKey and r.resourceType = :type) ");
		sql.append("left join (select p1.* from hc_permission p1 where p1.id  in ( :list )) p ");
		sql.append("on r.id = p.resource_id where p.id is null ");
		if(resourceCondition != null) {
			sql.append(resourceCondition);
		}
		sql.append(" order by x.id desc ");
		
		String sqlStr = sql.toString();
		SQLQuery query = getSession().createSQLQuery(sqlStr);
		Map<String, Object> conditionMap = (Map<String, Object>)map.get("conditionMap");
		conditionMap.put("list", list);
		conditionMap.remove("conditionId");
		for (Entry<String, Object> entry : conditionMap.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				query.setParameterList(entry.getKey(),
						(Collection<?>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		query.addScalar("id", Hibernate.STRING)
				.addScalar("name", Hibernate.STRING)
				.addScalar("type", Hibernate.STRING)
				.addScalar("permissionId", Hibernate.LONG)
				.addScalar("resourceId", Hibernate.LONG)
				.addScalar("actionId", Hibernate.LONG)
				.setResultTransformer(Transformers.aliasToBean(TreeQueryVO.class));
		query.setFirstResult((pagePrivilege.getPageNo() - 1) * pagePrivilege.getPageSize());
		query.setMaxResults(pagePrivilege.getPageSize());
		
		
		String counterSql = "select count(1) " + sqlStr.substring(sqlStr.indexOf("from"));
		SQLQuery countQuery = getSession().createSQLQuery(counterSql.toString());
		for (Entry<String, Object> entry : conditionMap.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				countQuery.setParameterList(entry.getKey(),
						(Collection<?>) entry.getValue());
			} else {
				countQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		pagePrivilege.setTotalCount(Long.valueOf(countQuery.uniqueResult().toString()));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findAssignedList(Map<String, Object> map, Page<PrivilegeVO> pagePrivilege) {
		String resourceCondition = (String)map.get("resourceCondition");
		String table = (String)map.get("table");
		String queryStr = (String)map.get("query");
		
		if(StringUtils.isNotBlank(queryStr)) {
			table = "(select * from " + table + " where name like '%" + queryStr + "%' )";
		}
		
		//20130325 start
        String conditionSql = (String)map.get("conditionTable");
        SQLQuery conditionQuery = getSession().createSQLQuery(conditionSql);
        conditionQuery.setParameter("conditionId", ((Map<String, Object>)map.get("conditionMap")).get("conditionId"));
        List<BigInteger> list = conditionQuery.list();
        list.add(new BigInteger("-1"));
        /*StringBuilder permissionIdsStr = new StringBuilder();
        for(BigInteger array : list) {
            permissionIdsStr.append(array[0].toString()).append(",");
        }*/
    //  System.out.println(list.toString());
        //20130325 end
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("select r.id, x.name from ");
		sql.append(table +" x "); 
		sql.append("left join hc_resource r on x.id = r.primKey ");
		sql.append("where r.resourceType = :type ");
		if(resourceCondition != null) {
			sql.append(resourceCondition);
		}
		sql.append("and r.id in ( :list ) ");
		sql.append("order by x.id desc ");
		
		String sqlStr = sql.toString();
		SQLQuery query = getSession().createSQLQuery(sqlStr);
		
		Map<String, Object> conditionMap = (Map<String, Object>)map.get("conditionMap");
		conditionMap.put("list", list);
        conditionMap.remove("conditionId");
		for (Entry<String, Object> entry : conditionMap.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				query.setParameterList(entry.getKey(),
						(Collection<?>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		query.setFirstResult((pagePrivilege.getPageNo() - 1) * pagePrivilege.getPageSize());
		query.setMaxResults(pagePrivilege.getPageSize());
		
		String counterSql = "select count(1) " + sqlStr.substring(sqlStr.indexOf("from"));
		SQLQuery countQuery = getSession().createSQLQuery(counterSql.toString());
		for (Entry<String, Object> entry : conditionMap.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				countQuery.setParameterList(entry.getKey(),
						(Collection<?>) entry.getValue());
			} else {
				countQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		pagePrivilege.setTotalCount(Long.valueOf(countQuery.uniqueResult().toString()));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CheckboxVO> findPermissionByResourceIds(List<Object> list, Long roleId) {
		String sql = "select p.resource_id resourceId, p.id permissionId, p.action_id actionId, if(rp.permission_id is null, false, true) checked " +
				"from hc_permission p left join hc_role_permission rp " +
				"on p.id = rp.permission_id and rp.role_id = :roleId where p.resource_id in (:ids) ";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameterList("ids", list);
		query.setParameter("roleId", roleId);
		
		query.addScalar("permissionId", Hibernate.LONG)
			.addScalar("resourceId", Hibernate.LONG)
			.addScalar("actionId", Hibernate.LONG)
			.addScalar("checked", Hibernate.BOOLEAN);
		query.setResultTransformer(Transformers.aliasToBean(CheckboxVO.class));
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UniformDefQueryVO> findUiformDefList(String type, Long id, String tableAndCondition, String column) {
		StringBuilder sql = new StringBuilder();
		sql.append("select r.id resourceId, p.id permissionId, p.action_id actionId, " + column + " checkId ");
		sql.append("from hc_resource r left join hc_permission p on r.id = p.resource_id ");
		sql.append("left join " + tableAndCondition);
		sql.append(" where r.resourceType = :type and r.primKey = 0");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("type", type);
		query.setParameter("id", id);
		query.addScalar("resourceId", Hibernate.LONG)
				.addScalar("permissionId", Hibernate.LONG)
				.addScalar("actionId", Hibernate.LONG)
				.addScalar("checkId", Hibernate.LONG);
		query.setResultTransformer(Transformers.aliasToBean(UniformDefQueryVO.class));
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MenuVO> findMenuStore(Long roleId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select x.id, x.name, r.id as resourceId, :type as type, p.id as permissionId,  p.action_id as actionId, x.parent_id as parentId, rp.permission_id as checked ");
		sql.append(" from hc_menu x left join hc_resource r on (x.id = r.primKey and r.resourceType = :type) ");  
		sql.append("left join hc_permission p on r.id = p.resource_id left join hc_role_permission rp on rp.permission_id = p.id and rp.role_id = :roleId order by x.id ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("type", "com.hisoft.hscloud.crm.usermanager.entity.Menu");
		query.setParameter("roleId", roleId);
		
		query.addScalar("id", Hibernate.STRING)
				.addScalar("name", Hibernate.STRING)
				.addScalar("type", Hibernate.STRING)
				.addScalar("permissionId", Hibernate.LONG)
				.addScalar("resourceId", Hibernate.LONG)
				.addScalar("actionId", Hibernate.LONG)
				.addScalar("parentId", Hibernate.STRING)
				.addScalar("checked", Hibernate.STRING)
				.setResultTransformer(
						Transformers.aliasToBean(MenuVO.class));
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CheckboxVO> findPermissionForUser(List<Object> list, Long usergroupId) {
		String sql = "select p.resource_id resourceId, p.id permissionId, p.action_id actionId, if(up.permission_id is null, false, true) checked " +
				"from hc_permission p left join hc_usergroup_permission up " +
				"on p.id = up.permission_id and up.usergroup_id = :usergroupId where p.resource_id in (:ids) ";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameterList("ids", list);
		query.setParameter("usergroupId", usergroupId);
		
		query.addScalar("permissionId", Hibernate.LONG)
			.addScalar("resourceId", Hibernate.LONG)
			.addScalar("actionId", Hibernate.LONG)
			.addScalar("checked", Hibernate.BOOLEAN);
		query.setResultTransformer(Transformers.aliasToBean(CheckboxVO.class));
		return query.list();
	}
}
