package com.hisoft.hscloud.systemmanagement.service; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.dao.HcEventVmOpsDao;
import com.hisoft.hscloud.systemmanagement.vo.LogQueryVO;
@Service
public class HcEventVmOpsServiceImpl implements HcEventVmOpsService {
	@Autowired
    private HcEventVmOpsDao vmOpsLogDao;
	@Override
	public Page<HcEventVmOps> getVmOpsLog(Page<HcEventVmOps> paing,LogQueryVO param)
			throws HsCloudException {
		return vmOpsLogDao.getVmOpsLog(paing,param);
	}
	@Override
	public String getVMOpsLogMessageById(Long id) throws HsCloudException {
		String result=null;
		HcEventVmOps vmOpsLog=vmOpsLogDao.getVMOpsLogById(id);
		if(vmOpsLog!=null){
			result=vmOpsLog.getMessage();
		}
		return result;
	}
	@Override
	public Page<HcEventVmOps> getVmOperationLog(Page<HcEventVmOps> paing,
			LogQueryVO param) throws HsCloudException {
		return vmOpsLogDao.getVmOperationLog(paing,param);
	}

}
