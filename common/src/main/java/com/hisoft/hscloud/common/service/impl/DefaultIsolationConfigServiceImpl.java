package com.hisoft.hscloud.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.dao.DefaultIsolationConfigDao;
import com.hisoft.hscloud.common.entity.DefaultIsolationConfig;
import com.hisoft.hscloud.common.service.DefaultIsolationConfigService;
@Service
public class DefaultIsolationConfigServiceImpl implements
		DefaultIsolationConfigService {
	@Autowired
	private DefaultIsolationConfigDao configDao;
	@Override
	public DefaultIsolationConfig getIsolationConfigById(long id) {
		// TODO Auto-generated method stub
		return configDao.getIsolationConfigById(id);
	}

}
