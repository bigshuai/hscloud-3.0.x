package com.hisoft.hscloud.web.service;

import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.crm.usermanager.entity.User;

public interface ControlpanelLoginService {
	public ControlPanelUser getCPUserByVmId(String vmId);
	public User getUserByUserId(long userId);
	
}
