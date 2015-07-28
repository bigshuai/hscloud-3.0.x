//$Id: MarketingPromotionDao.java Sep 22, 2013 9:41:53 AM liyunhui  $begin:~
package com.hisoft.hscloud.common.dao;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * @className: MarketingPromotionDao
 * @package: com.hisoft.hscloud.common.dao
 * @description: TODO
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 22, 2013 9:41:53 AM
 * @company: Pactera Technology International Ltd
 */
public interface MarketingPromotionDao {
	
	public MarketingPromotion findUnique(String hql,Object ... values);

	Page<MarketingPromotion> getMarketingPromotionByPage(String condition, Page<MarketingPromotion> paging) 
			throws HsCloudException;

	MarketingPromotion getMarketingPromotionById(Long id) throws HsCloudException;

	boolean checkMarketingPromotionParameterDup(String name, String code,
			String address) throws HsCloudException;

	void addMarketingPromotion(MarketingPromotion mp) throws HsCloudException;

}
//$Id: MarketingPromotionDao.java Sep 22, 2013 9:41:53 AM liyunhui  $end:~