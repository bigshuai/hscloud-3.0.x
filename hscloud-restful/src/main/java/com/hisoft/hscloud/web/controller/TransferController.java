/* 
* 文 件 名:  TransferController.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-4-14 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.controller; 

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.PlatformRelationService;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.TransferService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.vo.ResultVo;

/** 
 * <转账> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-4-14] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Controller
public class TransferController {
	private Logger logger = Logger.getLogger(this.getClass());
    
    @Resource
    private BasicService basicService;
    @Resource
    private TransferService transferService;
    @Resource//SpringMVC的注解，@Autowired是SSH的注解
	private PlatformRelationService platformRelationService;
	private ResultVo resultVo = null;
	
	@RequestMapping(value = "/transfer_accounts", method = RequestMethod.GET)
    @ResponseBody
    public void transferAccounts(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String accessId = request.getParameter("accessid");//鉴权用户
        String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
        String userId = request.getParameter("userid");//hscloud平台用户email       
        String accessIp=request.getRemoteHost();//获取请求Ip
        String fee = request.getParameter("fee");//转账金额   
        String feeType = request.getParameter("feetype");//资金类型:1.现金2.返点     
        String transferMode = request.getParameter("transfermode");//转账方式：1.转入2.转出
        String billno = request.getParameter("billno");//转账方式：1.转入2.转出
        
        boolean flag = basicService.checkParameterOfDomainWidthID(response, accessId, accessKey, accessIp, userId);
        if(flag == false) {
            return;
        }
        User user = basicService.checkUser(response, userId);
        if(user == null){
        	return;
        }
        if(!user.getDomain().getCode().equals(accessId)){
        	return;
        }
        if(user.getEnable()!=3){
        	return;
        }
        
        try {   
        	PlatformRelation platformRelation =platformRelationService.getPlatformRelationByLocalUser(String.valueOf(user.getId()));
    		if(platformRelation != null && platformRelation.getExternalUserId()!=null && platformRelation.getUserId()!=null){
        		resultVo = transferService.transfer(user, fee, feeType, transferMode,billno);
                BasicUtil.printResult(response, resultVo); 
    		}else{
    			throw new HsCloudException("","relationship is not built.",logger); 
    		}
        } 
        catch (Exception e) {
            logger.info("transferAccounts Error:", e);
            BasicUtil.fillResultVoFalse(e.getMessage(), response);
            return;
        }
        
    }

}
