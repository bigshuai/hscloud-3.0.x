package com.hisoft.hscloud.systemmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.dao.ControlPanelMaintenanceDao;
import com.hisoft.hscloud.systemmanagement.vo.ControlPanelVO;
@Service
public class ControlPanelMaintenanceServiceImpl implements ControlPanelMaintenanceService {
	@Autowired
	private ControlPanelMaintenanceDao controlPanelDao;

	@Override
	public Page<ControlPanelVO> getAllControlPanelByPage(
			Page<ControlPanelVO> paging, String queryCondition,
			List<Long> domainIds) throws HsCloudException {
		List<ControlPanelVO> result = null;
		int pageSize = paging.getPageSize();
		int pageNo = paging.getPageNo();
		result = controlPanelDao.getAllControlPanelByPage(pageSize,
				pageNo, queryCondition, domainIds);
		long totalNum = controlPanelDao.getAllControlPanelCount(
				queryCondition, domainIds);
		paging.setResult(result);
		paging.setTotalCount(totalNum);
		return paging;
	}

}
