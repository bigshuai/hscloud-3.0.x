package com.hisoft.hscloud.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hisoft.hscloud.web.dao.VMInfoDao;
import com.hisoft.hscloud.web.service.VMInfoService;
import com.hisoft.hscloud.web.vo.VMInfoVo;


@Service
public class VMInfoServiceImpl implements VMInfoService {
	
	@Resource
	private VMInfoDao vmInfoDao;

	@Override
	public List<VMInfoVo> getVMInfo(Map<String,String> args) {
		return vmInfoDao.getVMInfo(args);
	}

}
