package com.hisoft.hscloud.bss.billing.dao;

import java.util.List;

import com.hisoft.hscloud.bss.billing.vo.CapitalSource;

public interface CapitalSourceDao {
	
	public List<CapitalSource>  findBySQL(String sql);

}
