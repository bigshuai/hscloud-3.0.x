package com.hisoft.hscloud.bss.sla.om.service;

import com.hisoft.hscloud.bss.sla.om.entity.UserRefund;
import com.hisoft.hscloud.common.util.HsCloudException;
/**
 * 
* <退款业务管理> 
* <功能详细描述> 
* 
* @author  houyh 
* @version  [版本号, 2012-11-26] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public interface UserRefundService {
	/**
	 * <每一次退款都会生成一条退款记录> 
	* <功能详细描述> 
	* @param ur
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Long saveUserRefund(UserRefund ur)throws HsCloudException;
}
