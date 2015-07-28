//$Id: MarketingPromotion.java Sep 22, 2013 9:34:33 AM liyunhui  $begin:~
package com.hisoft.hscloud.common.service;

import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * @className: MarketingPromotion
 * @package: com.hisoft.hscloud.common.service
 * @description: TODO
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 22, 2013 9:34:33 AM
 * @company: Pactera Technology International Ltd
 */
@Service
public interface MarketingPromotionService {
	
	public MarketingPromotion findMarketingPromotionByCode(String code);
	
	/**
	 * <分页获取市场推广数据> 
	* <功能详细描述> 
	* @param condition
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public Page<MarketingPromotion> getMarketingPromotionByPage(String condition,Page<MarketingPromotion> paging) 
			throws HsCloudException;
	/**
	 * <根据id获取市场推广的信息> 
	* <功能详细描述> 
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */		
	public MarketingPromotion getMarketingPromotionById(Long id) throws HsCloudException;
	/**
	 * <1.校验推广名称是否重复> <功能详细描述>
	 * <2.校验推广代码是否重复> <功能详细描述>
	 * <3.校验推广地址是否重复> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public boolean checkMarketingPromotionParameterDup(String name, String code, 
			String address) throws HsCloudException;
	/**
	 * <添加市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void addMarketingPromotion(MarketingPromotion mp) throws HsCloudException;

}
//$Id: MarketingPromotion.java Sep 22, 2013 9:34:33 AM liyunhui  $end:~