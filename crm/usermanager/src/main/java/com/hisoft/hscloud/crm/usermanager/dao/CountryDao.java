package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Country;

public interface CountryDao{
	
	public List<Country> getAll(String orderBy, boolean isAsc);

}
