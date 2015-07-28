package com.hisoft.hscloud.crm.usermanager.dao.impl; 

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.ResourceTypeDao;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;

@Repository
public class ResourceTypeDaoImpl extends HibernateDao<ResourceType, Long> implements
 ResourceTypeDao {
	
	@Override
	public List<ResourceType> findAllResourceType(int status) {
		String hql = "from ResourceType where status = 0 or status = ?";
		return this.find(hql, status);
	}
}
