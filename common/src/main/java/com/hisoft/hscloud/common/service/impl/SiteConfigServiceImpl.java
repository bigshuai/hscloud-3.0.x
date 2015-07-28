package com.hisoft.hscloud.common.service.impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.common.dao.SiteConfigDao;
import com.hisoft.hscloud.common.entity.SiteConfig;
import com.hisoft.hscloud.common.service.SiteConfigService;
import com.hisoft.hscloud.common.util.HsCloudException;
@Service
public class SiteConfigServiceImpl implements SiteConfigService {
	@Autowired
	private SiteConfigDao siteCofingDao;
	public SiteConfig getSiteConfigByProperty(String property, Object value)
			throws HsCloudException {
		
		return siteCofingDao.getSiteConfigByProperty(property, value);
	}
	@Transactional(readOnly = true)
	public String getConfigValue(String config, String belongTo)
			throws HsCloudException {
		return siteCofingDao.getConfigValue(config, belongTo);
	}

}
