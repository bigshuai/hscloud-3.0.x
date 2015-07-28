package com.hisoft.hscloud.web.service;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.web.vo.VMInfoVo;


public interface VMInfoService {

	public List<VMInfoVo> getVMInfo(Map<String,String> args);
}
