package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.dao.TabDao;
import com.hisoft.hscloud.crm.usermanager.service.TabResultService;

@Service
public class TabResultServiceImpl implements TabResultService {
	
	@Autowired
	TabDao resTabVODao;


	@Override
	public  List<Map<String,Object>> getTabResult(Map<String, ?> map, String sql) {

		return resTabVODao.findBySQL(sql, map);
		
	}
	
	

}
