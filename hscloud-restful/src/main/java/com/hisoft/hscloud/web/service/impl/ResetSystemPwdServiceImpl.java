/* 
* 文 件 名:  ResetSystemPwdServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.web.service.ResetSystemPwdService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class ResetSystemPwdServiceImpl implements ResetSystemPwdService {
    @Autowired
    public UserService userService;
    
    @Autowired
    public Operation operationImpl;
    
    @Override
    @Transactional
    public void resetSystemPwd(String userId, String vmId, String password, long taskId) {
        User user = userService.getUser(Long.valueOf(userId));
        operationImpl.resetSystemPwd(vmId, password, user, LogOperatorType.USER.getName(), taskId);
    }
}
