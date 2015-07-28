package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.RoleDao;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.entity.Role;

@Repository
public class RoleDaoImpl extends HibernateDao<Role, Long> implements RoleDao {
	
	@Override
	public void save(Role role){
		super.save(role);
	}
	
	@Override
	public List<Role> findByIds(List<Long> ids){
		return (List<Role>)super.findByIds(ids);
	}

	@Override
	public void delete(long roleId) {
		super.delete(roleId);
	}
	
	@Override
	public Page<Role> findPage(Page<Role> page, String hql, Object... values){
		
		return (Page<Role> )super.findPage(page, hql, values);
	}

	@Override
	public Page<Role> findPage(Page<Role> page, String hql) {
		return (Page<Role> )super.findPage(page, hql);
	}
	
	@Override
	public List<Role> getAll(){
		return (List<Role>)super.getAll();
	}
	
	@Override
	public List<Role> findRole(String hql, Map<String,?> values){
		return super.find(hql, values);
	}

	@Override
	public Role get(long roleId) {
		
		//return super.get(roleId);
		return findUniqueBy("id", roleId);
	}
	
	@Override
	public Role findUniqueBy(String propertyName, String value) {
		
		return super.findUniqueBy(propertyName, value);
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> findBySQL(String sql,Map<String, ?> map) {

      SQLQuery query = getSession().createSQLQuery(sql);
      query.addEntity(Role.class);
      Set<String> keys = map.keySet();
      for (String key : keys) {
    	  if(map.get(key) instanceof Collection){
    		  query.setParameterList(key, (Collection)map.get(key));
    	  }else{
    		  query.setParameter(key, map.get(key));
    	  }
	  }
      return query.list();
		
	}

    /** 
    * @param roleId 
    */
    @SuppressWarnings("unchecked")
    @Override
    public List<Resource> getResourceForRoleId(long roleId) {
        String sql = "select a.* from hc_resource a, hc_permission b, hc_role_permission c, hc_role d where a.id = b.resource_id and b.id = c.permission_id and c.role_id = d.id and d.id = ? ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter(0, roleId);
        query.addEntity(Resource.class);
        return query.list();
    }


	


}
