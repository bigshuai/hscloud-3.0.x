/**
 * @title CommonService.java
 * @package com.hisoft.hscloud.crm.usermanager.service
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-8 下午5:49:36
 * @version V1.0
 */
package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;


public interface OptionService {
	
	//加载国家
	public List<Country> loadCountry();
	//加载行业
	public List<Industry> loadIndustry();
	//加载
	public List<Region> loadRegion(Long countryId);
	//加载品牌
	public List<UserBrand> loadUserBrand();

}
