package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;



public interface TabDao {
	
	public List<Map<String,Object>> findBySQL(String sql,Map<String, ?> map);

}
