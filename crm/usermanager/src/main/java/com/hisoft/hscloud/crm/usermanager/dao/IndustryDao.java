package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Industry;

public interface IndustryDao {
	
	public List<Industry> getAll(String orderBy,boolean isAsc);

}
