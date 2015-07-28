package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.vo.UserVO1;

public interface UserVO1Dao {

	public List<UserVO1> findUserVO1(String sql,Map<String,?> params);
	
}
