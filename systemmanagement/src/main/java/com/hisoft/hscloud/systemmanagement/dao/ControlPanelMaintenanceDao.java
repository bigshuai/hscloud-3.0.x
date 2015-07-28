package com.hisoft.hscloud.systemmanagement.dao;

import java.util.List;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.vo.ControlPanelVO;

public interface ControlPanelMaintenanceDao {
	/**
	 * 分页查询出所有控制面板业务数据即有效虚拟机数据
	 * @param pageSize
	 * @param pageNo
	 * @param queryCondition
	 * @param domainIds
	 * @return
	 * @throws HsCloudException
	 */
	public List<ControlPanelVO> getAllControlPanelByPage(int pageSize,
			int pageNo, String queryCondition, List<Long> domainIds)
			throws HsCloudException;
	/**
	 * 查询出所有的有效虚拟机分页的totalCount
	 * @param queryCondition
	 * @param domainIds
	 * @return
	 * @throws HsCloudException
	 */
	public long getAllControlPanelCount(String queryCondition,
			List<Long> domainIds) throws HsCloudException;
}
