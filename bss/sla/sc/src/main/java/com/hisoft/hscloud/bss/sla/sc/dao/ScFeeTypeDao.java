package com.hisoft.hscloud.bss.sla.sc.dao; 

import java.util.List;

import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface ScFeeTypeDao {
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param sc
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteFeeTypeByScId(ServiceCatalog sc)throws HsCloudException;
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param id
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public ScFeeType getFeeTypeById(Long id,int scId)throws HsCloudException;
	/**
	 * <根据套餐id获取套餐对应的计费规则> 
	* <功能详细描述> 
	* @param scId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ScFeeType> getScFeeTypeByScId(int scId)throws HsCloudException;
	/**
	 * <根据orderItemId获取套餐的计费规则信息> 
	* <功能详细描述> 
	* @param orderItemId
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public List<ScFeeTypeVo> getScFeeTypeByOrderItemId(long orderItemId)throws HsCloudException;
}
