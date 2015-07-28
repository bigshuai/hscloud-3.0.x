package com.hisoft.hscloud.bss.sla.om.dao;

import com.hisoft.hscloud.bss.sla.om.entity.UserRefund;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface UserRefundDao {
	  /**
	   * 
	   * @param ur
	   * @throws HsCloudException
	   */
	  public Long saveUserRefund(UserRefund ur)throws HsCloudException;
}
