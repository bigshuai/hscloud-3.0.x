package com.hisoft.hscloud.bss.sla.om.service;

import com.hisoft.hscloud.bss.sla.om.entity.RefundVm;
import com.hisoft.hscloud.common.util.HsCloudException;
/**
 * 
* <退款单与退掉的VM之间的关联关系管理> 
* <功能详细描述> 
* 
* @author  houyh 
* @version  [版本号, 2012-11-26] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public interface RefundVmService {
	/**
	 * <记录退款单与退掉的VM之间的关联关系> 
	* <功能详细描述> 
	* @param uv
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void saveRefundVm(RefundVm uv)throws HsCloudException;
}
