package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.LoginLog;

public interface LoginLogDao {
	
	public void save(LoginLog loginLog);
	
	public LoginLog findBySQL(String sql,Map<String, ?> map);
	
	public LoginLog findLastLoginLog(Map<String, ?> map);

}
