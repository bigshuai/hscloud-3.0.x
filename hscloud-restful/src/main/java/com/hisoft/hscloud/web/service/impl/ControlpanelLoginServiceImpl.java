package com.hisoft.hscloud.web.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.controlpanel.service.ControlPanelService;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.web.service.ControlpanelLoginService;
@Service
public class ControlpanelLoginServiceImpl implements ControlpanelLoginService {
private Logger logger = Logger.getLogger(this.getClass());
    
    private String log="";
    
    @Resource
    private ControlPanelService controlPanelService;
    @Resource
    private UserService userService;
    
    
	@Override
	public ControlPanelUser getCPUserByVmId(String vmId) {
		return controlPanelService.findControlUserByVmID(vmId);
	}
	
	@Override
	public User getUserByUserId(long userId) {
		return userService.getUser(userId);
	}


	

}
