package com.hisoft.hscloud.systemmanagement.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;

public interface BusinessPlatformService {
	/**
	 * 分页查询出所有业务平台数据即有效的用户数据
	 * 
	 * @param pageSize
	 * @param pageNo
	 * @param queryCondition
	 * @param domainIds
	 * @return
	 * @throws HsCloudException
	 */
	public Page<BusinessPlatformVO> getAllBusinessPlatformByPage(
			Page<BusinessPlatformVO> paging, String queryCondition,
			List<Long> domainIds) throws HsCloudException;
}
