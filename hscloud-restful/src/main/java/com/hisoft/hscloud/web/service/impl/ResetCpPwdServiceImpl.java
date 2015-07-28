/* 
* 文 件 名:  ResetCpPwdServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-26 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.controlpanel.service.ControlPanelService;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.web.dao.ResetCpPwdDao;
import com.hisoft.hscloud.web.service.ResetCpPwdService;

/** 
 * <重置控制面板密码> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-26] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class ResetCpPwdServiceImpl implements ResetCpPwdService{
    @Autowired
    private ControlPanelService controlPanelService;
    
    @Resource
    private ResetCpPwdDao resetCpPwdDao;
    
    @Autowired
    private Operation operation;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public String setCpPwd(String vmId,String pwd,User user) {
        ControlPanelUser controlPanel = controlPanelService.findControlUserByVmID(vmId);
        try {
            pwd = PasswordUtil.getEncyptedPasswd(pwd);
            pwd = PasswordUtil.getEncyptedPasswd(pwd);
        } catch (Exception e) {
            throw new HsCloudException(Constants.PASSWORD_ENCRYPTION_FAILED," setCPpwd failed.",logger);
        }
        String result = "success";
        if(controlPanel!=null){
            controlPanel.setCreate_Id(user.getId());
            controlPanel.setUpdateDate(new Date());
                controlPanel.setUserPassword(pwd);
                controlPanelService.saveControlPanelUser(controlPanel);
        }else{
            String createflag = resetCpPwdDao.queryCreateflagByVmId(vmId);
            if("1".equals(createflag)) {
                String ip = operation.getVmIpByVmId(vmId);
                controlPanelService.createControlUser(ip, pwd,user.getId(), vmId);
            } else {
                result = "The VM is creating or the VM is wrong";
            }
        }
        return result;
    }
}
