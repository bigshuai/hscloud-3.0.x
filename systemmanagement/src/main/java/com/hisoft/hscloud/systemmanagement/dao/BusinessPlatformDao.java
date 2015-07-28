package com.hisoft.hscloud.systemmanagement.dao;

import java.util.List;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.BusinessPlatformVO;

public interface BusinessPlatformDao {
	/**
	 * 分页查询出所有业务平台数据即有效的用户数据
	 * @param pageSize
	 * @param pageNo
	 * @param queryCondition
	 * @param domainIds
	 * @return
	 * @throws HsCloudException
	 */
	public List<BusinessPlatformVO> getAllBusinessPlatformByPage(int pageSize,
			int pageNo, String queryCondition, List<Long> domainIds)
			throws HsCloudException;
	/**
	 * 查询出所有的有效用户个数用户分页的totalN
	 * @param queryCondition
	 * @param domainIds
	 * @return
	 * @throws HsCloudException
	 */
	public long getAllBusinessPlatformCount(String queryCondition,
			List<Long> domainIds) throws HsCloudException;
}
