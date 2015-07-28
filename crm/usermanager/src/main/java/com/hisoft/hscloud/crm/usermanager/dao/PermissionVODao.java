package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.vo.PermissionVO;

public interface PermissionVODao {
	
	public List<PermissionVO> findBySQL(String sql,Map<String, ?> map);

}
