package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Region;

public interface RegionDao {
	
	public List<Region> findBy(String propertyName, Object value);
	
	public List<Region> find(String hql, Object... values);


}
