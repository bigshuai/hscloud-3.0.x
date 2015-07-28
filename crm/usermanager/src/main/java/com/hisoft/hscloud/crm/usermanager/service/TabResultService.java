package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;
import java.util.Map;


public interface TabResultService {
	
	public  List<Map<String,Object>> getTabResult(Map<String,?> map,String sql);

}
