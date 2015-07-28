package com.hisoft.hscloud.systemmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.dao.BusinessPlatformDao;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;

@Service
public class BusinessPlatformServiceImpl implements BusinessPlatformService {
	@Autowired
	private BusinessPlatformDao businessPlatformDao;

	@Override
	public Page<BusinessPlatformVO> getAllBusinessPlatformByPage(
			Page<BusinessPlatformVO> paging, String queryCondition,
			List<Long> domainIds) throws HsCloudException {
		List<BusinessPlatformVO> result = null;
		int pageSize = paging.getPageSize();
		int pageNo = paging.getPageNo();
		result = businessPlatformDao.getAllBusinessPlatformByPage(pageSize,
				pageNo, queryCondition, domainIds);
		long totalNum = businessPlatformDao.getAllBusinessPlatformCount(
				queryCondition, domainIds);
		paging.setResult(result);
		paging.setTotalCount(totalNum);
		return paging;
	}

}
