package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.vo.ProfileVo;

public interface ProfileVoDao {

	public List<ProfileVo> findProfileVo(String sql,Map<String,?> params);
	
}
