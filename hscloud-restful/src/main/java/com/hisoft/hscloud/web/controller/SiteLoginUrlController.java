/* 
* 文 件 名:  SiteLoginUrlController.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.controller; 

import java.net.URLEncoder;

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
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.service.SiteLoginUrlService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.RestConstant;

/** 
 * <前台登录url> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Controller
public class SiteLoginUrlController {
private Logger logger = Logger.getLogger(this.getClass());
    
    @Resource
    private BasicService basicService;
    
    @Resource
    private SiteLoginUrlService siteLoginUrlService;
    
    /**
     * <返回云平台用户登录URL> 
    * <功能详细描述> 
    * @param request
    * @param response 
    * @see [类、类#方法、类#成员]
     */
    //http://127.0.0.1:8080/hscloud-restful/services/site_login?accessid=xr&accesskey=40d92b734fe7a691068cbc28eedf1c0d&name=123&email=test
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/site_login", method = RequestMethod.GET)
    @ResponseBody
    public void getSiteLoginUrl(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String accessId = request.getParameter("accessid");//鉴权用户
        String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String authentication = request.getParameter("authentication"); //默认是true 需要填密码
        
        String accessIp=request.getRemoteHost();//获取请求Ip
        
        if(StringUtils.isBlank(email)) {
            BasicUtil.fillResultVoFalse(RestConstant.PARAMETER_INVALID, response);
            return;
        }        
        
        if(BasicUtil.checkEmail(email) == false) {
            BasicUtil.fillResultVoFalse("email is invalid", response);
            return;
        }
        
        if(StringUtils.isBlank(authentication)) {
        	authentication="false";
        } 
//         basicService.checkParameter_domain(response, accessId, accessKey, accessIp, logger, email);
         boolean flag = basicService.checkParameterOfDomain(response, accessId, accessKey, accessIp, email);
        if(flag == false) {
            return;
        }
        
        User user = siteLoginUrlService.getUserByEmail(email);
        if (user == null) {
            BasicUtil.fillResultVoFalse("user is not exist", response);
            return;
        }
        if (user.getEnable() != 3) {
            BasicUtil.fillResultVoFalse("user status is not approved", response);
            return;
        }
        Domain domain = user.getDomain();
        if(!accessId.equals(domain.getCode())) {
            BasicUtil.fillResultVoFalse("the user is a illegal one", response);
            return;
        }
        
        if(!"false".equalsIgnoreCase(authentication)) {
            if(StringUtils.isBlank(password)) {
                BasicUtil.fillResultVoFalse("password is null", response);
                return;
            } else {
                try {
                    password =PasswordUtil.getEncyptedPasswd(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!password.equalsIgnoreCase(user.getPassword())) {
                    BasicUtil.fillResultVoFalse("password is wrong", response);
                    return;
                }
            }
        }

        String originalKey = user.getEmail() + user.getPassword();
        String authorKey;
        try {
            authorKey = PasswordUtil.getEncyptedPasswd(originalKey);
        } catch (Exception e) {
            logger.info("UserRegisterController.registerUser", e);
            BasicUtil.fillResultVoFalse(e.getMessage(), response);
            return;
        }
        String dateKey = SecurityHelper.EncryptData(
                HsCloudDateUtil.getNowStr(), Constants.DEFAULT_SECURITY_KEY);
        dateKey = URLEncoder.encode(dateKey);
        String domainPublishUrl = domain.getPublishingAddress();
        String appUrl = domainPublishUrl + "/user_mgmt/loginByUrl!userLoginByUrl.action?";
        String result = appUrl + "authorKey=" + authorKey + "&userId="
                + user.getId() + "&dateKey=" + dateKey + "&operator=" + email;
        
        BasicUtil.printResult(response, result);
    }
    /**
     * <返回云平台用户注册URL> 
    * <功能详细描述> 
    * @param request
    * @param response 
    * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("deprecation")
	@RequestMapping(value = "/cloud_register", method = RequestMethod.GET)
    @ResponseBody
    public void getSiteRegisterUrl(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    	 String accessId = request.getParameter("accessid");//鉴权用户
         String accessKey = request.getParameter("accesskey");//鉴权密码  MD5加密
         String externalUserId = request.getParameter("userid");//客户平台用户ID        
         String accessIp=request.getRemoteHost();//获取请求Ip       
         
         boolean flag = basicService.checkParameterOfDomainWidthID(response, accessId, accessKey, accessIp, externalUserId);
         if(flag == false) {
             return;
         }
      
        Domain domain = siteLoginUrlService.getDomainByCode(accessId);
        if(domain == null) {
            BasicUtil.fillResultVoFalse("the user is a illegal one", response);
            return;
        }    
        
        String dateKey = SecurityHelper.EncryptData(
                HsCloudDateUtil.getNowStr(), Constants.DEFAULT_SECURITY_KEY);
        dateKey = URLEncoder.encode(dateKey);
        String domainPublishUrl = domain.getPublishingAddress();
        String appUrl = domainPublishUrl + "/portal/register.html?";
        String result = appUrl + "userId="+ externalUserId + "&dateKey=" + dateKey;        
        BasicUtil.printResult(response, result);
    }
}
