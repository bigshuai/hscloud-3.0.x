/* 
* 文 件 名:  UserRegisterController.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.controller; 

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.UserRegisterService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.RestConstant;
import com.hisoft.hscloud.web.vo.ResultVo;

/** 
 * <分平台用户注册> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Controller
public class UserRegisterController {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Resource
    private BasicService basicService;
    
    @Resource
    private UserRegisterService userRegisterService;
    
    private ResultVo resultVo = new ResultVo();
    
    //http://127.0.0.1:8080/hscloud-restful/services/user_register?accessid=xr&accesskey=40d92b734fe7a691068cbc28eedf1c0d&name=123&email=test
    /*@RequestMapping(value = "/user_register", method = RequestMethod.GET)
    @ResponseBody
    public void registerUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String accessId = request.getParameter("accessid");//鉴权用户
        String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        String accessIp=request.getRemoteHost();//获取请求Ip
        
        if(StringUtils.isBlank(email) || StringUtils.isBlank(email)) {
            BasicUtil.fillResultVoFalse(RestConstant.PARAMETER_INVALID, response);
            return;
        }
        
        if(BasicUtil.checkEmail(email) == false) {
            BasicUtil.fillResultVoFalse("email is invalid", response);
            return;
        }
        
        boolean flag = basicService.checkParameter_domain(response, accessId, accessKey, accessIp, logger, email);
        if(flag == false) {
            return;
        }
        
        try {
            String result = userRegisterService.registerUser(email, name, accessId);
            if(Constants.SUCCESS.equals(result)) {
                resultVo = new ResultVo();
                BasicUtil.printResult(response, resultVo);
            } else {
                BasicUtil.fillResultVoFalse(result, response);
            }
        } catch (Exception e) {
            logger.info("UserRegisterController.registerUser", e);
            BasicUtil.fillResultVoFalse(e.getMessage(), response);
            return;
        }
    }*/
    
    /**
     * <老平台新用户注册> 
    * <功能详细描述> 
    * @param request
    * @param response 
    * @see [类、类#方法、类#成员]
     */
    //http://127.0.0.1:8080/hscloud-restful/services/user_register?accessid=xr&accesskey=40d92b734fe7a691068cbc28eedf1c0d&userid=123
    @RequestMapping(value = "/user_register", method = RequestMethod.GET)
    @ResponseBody
    public void registerUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String accessId = request.getParameter("accessid");//鉴权用户
        String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
        String externalUserId = request.getParameter("userid");//客户平台用户ID        
        String accessIp=request.getRemoteHost();//获取请求Ip       
        
        boolean flag = basicService.checkParameterOfDomainWidthID(response, accessId, accessKey, accessIp, externalUserId);
        if(flag == false) {
            return;
        }
        
        try {
            String result = userRegisterService.registerUserFromExternal(externalUserId);
            if(Constants.SUCCESS.equals(result)) {
                resultVo = new ResultVo();
                BasicUtil.printResult(response, resultVo);
            } else {
                BasicUtil.fillResultVoFalse(result, response);
            }
        } catch (Exception e) {
            logger.info("UserRegisterController.registerUser", e);
            BasicUtil.fillResultVoFalse(e.getMessage(), response);
            return;
        }
    }
}
