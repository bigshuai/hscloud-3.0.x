/**
 * @title CommonServiceImpl.java
 * @package com.hisoft.hscloud.crm.usermanager.service.impl
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-8 下午5:53:38
 * @version V1.0
 */
package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.common.dao.UserBrandDao;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.crm.usermanager.dao.CountryDao;
import com.hisoft.hscloud.crm.usermanager.dao.IndustryDao;
import com.hisoft.hscloud.crm.usermanager.dao.RegionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.service.OptionService;
import com.hisoft.hscloud.crm.usermanager.util.Constant;


@Service
public class OptionServiceImpl implements OptionService {
	
	@Autowired
	private CountryDao couDao;
	
	@Autowired
	private IndustryDao indDao;
	
	@Autowired
	private RegionDao regDao;
	
	@Autowired
	private UserBrandDao brandDao;
	
	public List<Country> loadCountry() {
		List<Country> countries =couDao.getAll("id", true);
		return countryCommon(countries);
	}

	public List<Industry> loadIndustry(){
		return indDao.getAll("id",true);
	}
	
	private List<Country> countryCommon(List<Country> countries){
		for(Country cou : countries){
			for(Region region: cou.getRegions()){
				region.setCountry(null);
			}
		}
		return countries;
	}

	@Transactional(readOnly=true)
	public List<Region> loadRegion(Long countryId) {
		List<Region> regions = regDao.find("from Region r where r.country.id = ? order by id", countryId);
		if(regions!=null&&regions.size()>0){
			for(Region region: regions){
				region.setCountry(null);
			}
		}
		return regions;
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserBrand> loadUserBrand() {
		
		List<UserBrand> userBrands = brandDao.getAll();
		return userBrands;
		
	}



}
