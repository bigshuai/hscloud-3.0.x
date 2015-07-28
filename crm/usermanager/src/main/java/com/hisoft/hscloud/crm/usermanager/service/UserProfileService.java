package com.hisoft.hscloud.crm.usermanager.service;

import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.vo.ProfileVo;

public interface UserProfileService {
	
	public ProfileVo findProfileVo(long id);

	public void modifyProfile(UserProfile userProfile);
	
	public UserProfile findUserProfile(long id);
}
