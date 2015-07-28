//$Id: MarketingPromotionServiceImpl.java Sep 22, 2013 9:39:17 AM liyunhui  $begin:~
package com.hisoft.hscloud.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.dao.MarketingPromotionDao;
import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.service.MarketingPromotionService;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * @className: MarketingPromotionServiceImpl
 * @package: com.hisoft.hscloud.common.service.impl
 * @description: TODO
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 22, 2013 9:39:17 AM
 * @company: Pactera Technology International Ltd
 */
@Service
public class MarketingPromotionServiceImpl implements MarketingPromotionService {

	@Autowired
	private MarketingPromotionDao marketingPromotionDao;

	@Override
	public Page<MarketingPromotion> getMarketingPromotionByPage(String condition, 
			Page<MarketingPromotion> paging) throws HsCloudException {
		return marketingPromotionDao.getMarketingPromotionByPage(condition, paging);
	}

	@Override
	public MarketingPromotion getMarketingPromotionById(Long id)
			throws HsCloudException {
		return marketingPromotionDao.getMarketingPromotionById(id);
	}

	/**
	 * <1.校验推广名称是否重复> <功能详细描述>
	 * <2.校验推广代码是否重复> <功能详细描述>
	 * <3.校验推广地址是否重复> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean checkMarketingPromotionParameterDup(String name, String code, 
			String address) throws HsCloudException {
		return marketingPromotionDao.checkMarketingPromotionParameterDup(name,code,address);
	}

	/**
	 * <添加市场推广> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void addMarketingPromotion(MarketingPromotion mp) throws HsCloudException {
		marketingPromotionDao.addMarketingPromotion(mp);
	}

	@Override
	public MarketingPromotion findMarketingPromotionByCode(String code) {
		MarketingPromotion m = marketingPromotionDao.findUnique("from MarketingPromotion where code=?", code);
		return m;
	}

}
//$Id: MarketingPromotionServiceImpl.java Sep 22, 2013 9:39:17 AM liyunhui  $end:~