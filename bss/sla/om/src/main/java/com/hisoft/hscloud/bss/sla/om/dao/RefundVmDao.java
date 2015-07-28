package com.hisoft.hscloud.bss.sla.om.dao;

import com.hisoft.hscloud.bss.sla.om.entity.RefundVm;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface RefundVmDao {
	  /**
	   * 
	   * @param rv
	   * @throws HsCloudException
	   */
	  public void saveRefundVm(RefundVm rv)throws HsCloudException;
}
