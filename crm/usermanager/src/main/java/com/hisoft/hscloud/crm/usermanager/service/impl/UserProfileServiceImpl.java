package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.dao.ProfileVoDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserProfileDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.service.UserProfileService;
import com.hisoft.hscloud.crm.usermanager.vo.ProfileVo;

@Service
public class UserProfileServiceImpl implements UserProfileService {
	
	@Autowired
	private ProfileVoDao profileVoDao;
	
	private UserProfileDao  userProfileDao;

	@Override
	public ProfileVo findProfileVo(long id) {
		try {
			String sql = "select hup.id,hup.id_card idCard,hup.telephone,hup.company,hup.address,hup.industry_id industryId,hi.name_code industryCode,hup.country_id countryId,hc.name_code countryCode,region_id regionId,hr.name_code regionCode  from hc_user_profile hup LEFT JOIN hc_country hc on hc.id=hup.country_id LEFT JOIN hc_region hr on hr.id=hup.region_id LEFT JOIN hc_industry hi on hi.id=hup.industry_id where hup.id=(:id)";
			Map<String,Object> params =new HashMap<String, Object>();
			params.put("id", id);
			List<ProfileVo> profileVos = profileVoDao.findProfileVo(sql, params);
			return null == profileVos || profileVos.isEmpty()?null:profileVos.get(0);
		} catch (Exception e) {
			System.out.println();
		}
		return null;
		
	}


	@Override
	public UserProfile findUserProfile(long id) {
		return userProfileDao.findUniqueBy("id", id);
	}

	@Override
	public void modifyProfile(UserProfile userProfile) {
		userProfileDao.save(userProfile);
	}
	

}
