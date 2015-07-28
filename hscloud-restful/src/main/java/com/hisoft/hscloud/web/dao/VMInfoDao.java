package com.hisoft.hscloud.web.dao;


import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.web.vo.VMInfoVo;


public interface VMInfoDao {
	public List<VMInfoVo> getVMInfo(Map<String,String> args);
}
