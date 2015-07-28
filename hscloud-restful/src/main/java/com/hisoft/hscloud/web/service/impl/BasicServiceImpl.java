/* 
* 文 件 名:  BasicServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.web.dao.BasicDao;
import com.hisoft.hscloud.web.service.BasicService;
import com.hisoft.hscloud.web.util.BasicUtil;
import com.hisoft.hscloud.web.util.RestConstant;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class BasicServiceImpl implements BasicService {
	private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private BasicDao basicDao;
    
    @Autowired
    private UserService userService;
    
    @Override
    public Map<String, Object> queryAccessByAccessId(String accessId) {
        return basicDao.queryAccessByAccessId(accessId);
    }
    
    @Override
    public User getUser(long userId) {
        return userService.getUser(userId);
    }
    
    @Override
    public long queryReferenceId(String vmId, String userId) {
        String referenceId = basicDao.queryReferenceIdByVmId(vmId, userId);
        if(referenceId == null) {
            return -1l;
        }
        return Long.valueOf(referenceId);
    }
    
    /**
     * <ip验证> 
    * <功能详细描述> 
    * @param accessIp
    * @param accessId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    @Override
    public boolean checkIP(String accessIp, String accessId) {
        List<String> ipList=getIpByAccesskey(accessId);
        boolean ipIsValid=false;
        for(int i=0;i<ipList.size();i++){
         //   String ip = ipList.get(i);
            String[] arrayIp = ipList.get(i).split(";");
            for(String ip : arrayIp) {
                if(accessIp.equals(ip) || "0.0.0.0".equals(ip)){
                    ipIsValid=true;
                    break;
                }
            }
        }
        return ipIsValid;
    }
    
    /**
     * <查询ip> 
    * <功能详细描述> 
    * @param accessid
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<String> getIpByAccesskey(String accessid) {
        return basicDao.getIpByAccesskey(accessid);
    }
    
    /**
     * 创建任务记录
    * @param taskMap
    * @return
     */
    @Override
    public long saveTask(Map<String, Object> taskMap) {
        return basicDao.saveTask(taskMap);
    }
    
    @Override
    public boolean checkParameter_domain(HttpServletResponse response, String accessId,
            String accessKey, String accessIp, Logger logger, String email) {
        
        if(accessId == null) {
            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_IS_NULL, response);
            return false;
        }
        Map<String, Object> accessMap = queryAccessByAccessId(accessId);
        if(accessMap.isEmpty()) {
            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_INVALID, response);
            return false;
        }
        
        String queryIp = (String)accessMap.get("ip");
        if(!BasicUtil.checkIP(accessIp, queryIp)) {
            BasicUtil.fillResultVoFalse(RestConstant.IP_INVALID, response);
            return false;
        }
        
     //   String emailaddr = (String)accessMap.get("emailaddr");
        String queryAccessKey = (String)accessMap.get("accesskey");
        String key = queryAccessKey + accessId + email;
        try {
            String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
            if(accessKey == null || !accessKey.equalsIgnoreCase(encryptedAccessKey)) {
                BasicUtil.fillResultVoFalse(RestConstant.ACCESSKEY_INVALID, response);
                return false;
            }
        } catch (Exception e) {
            logger.info("UserRegisterController.registerUser", e);
            BasicUtil.fillResultVoFalse(e.getMessage(), response);
            return false;
        }
        return true;
    }

	@Override
	public boolean checkParameterOfDomain(HttpServletResponse response,
			String accessId, String accessKey, String accessIp, String email) {
		 if(accessId == null) {
	            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_IS_NULL, response);
	            return false;
	        }
	        Map<String, Object> accessMap = this.queryAccessByAccessId(accessId, email);
	        if(accessMap.isEmpty()) {
	            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_INVALID, response);
	            return false;
	        }
	        
	        String queryIp = (String)accessMap.get("ip");
	        if(!BasicUtil.checkIP(accessIp, queryIp)) {
	            BasicUtil.fillResultVoFalse(RestConstant.IP_INVALID, response);
	            return false;
	        }
	        
	        if(StringUtils.isBlank(email)) {
	            BasicUtil.fillResultVoFalse(RestConstant.PARAMETER_INVALID, response);
	            return false;
	        }        
	        if(BasicUtil.checkEmail(email) == false) {
	            BasicUtil.fillResultVoFalse("email is invalid", response);
	            return false;
	        }
	        
	     //   String emailaddr = (String)accessMap.get("emailaddr");
	        String queryAccessKey = (String)accessMap.get("accesskey");
	        String key = queryAccessKey + email;
	        try {
	            String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
	            if(accessKey == null || !accessKey.equalsIgnoreCase(encryptedAccessKey)) {
	                BasicUtil.fillResultVoFalse(RestConstant.ACCESSKEY_INVALID, response);
	                return false;
	            }
	        } catch (Exception e) {
	            logger.error("UserRegisterController.registerUser", e);
	            BasicUtil.fillResultVoFalse(e.getMessage(), response);
	            return false;
	        }
	        return true;
	}

	@Override
	public User checkUser(HttpServletResponse response,String email) {
		User user = userService.getUserByEmail(email);
		 if (user == null) {
			 BasicUtil.fillResultVoFalse("user is not exist", response);
		 }
		 if (user.getEnable() != 3) {
			 BasicUtil.fillResultVoFalse("user status is not approved", response);
		 }
		 return user;
	}

	@Override
	public boolean checkDomainOfUser(HttpServletResponse response, User user,
			String accessId) {
		if(user == null){
			return false;
		}
		Domain domain = user.getDomain();
        if(!accessId.equals(domain.getCode())) {
            BasicUtil.fillResultVoFalse("the user is a illegal one", response);
            return false;
        }
		return true;
	}

	@Override
	public Map<String, Object> queryAccessByAccessId(String accessId,
			String emailaddr) {
		return basicDao.queryAccessByAccessId(accessId,emailaddr);
	}

	@Override
	public boolean checkParameterOfDomainWithCode(HttpServletResponse response,
			String accessId, String accessKey, String accessIp, String code) {
		 if(accessId == null) {
	            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_IS_NULL, response);
	            return false;
	        }
	        Map<String, Object> accessMap = this.queryAccessByAccessId(accessId);
	        if(accessMap.isEmpty()) {
	            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_INVALID, response);
	            return false;
	        }
	        
	        String queryIp = (String)accessMap.get("ip");
	        if(!BasicUtil.checkIP(accessIp, queryIp)) {
	            BasicUtil.fillResultVoFalse(RestConstant.IP_INVALID, response);
	            return false;
	        }
	        	        
	     //   String emailaddr = (String)accessMap.get("emailaddr");
	        String queryAccessKey = (String)accessMap.get("accesskey");
	        String key = queryAccessKey + code;
	        try {
	            String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
	            if(accessKey == null || !accessKey.equalsIgnoreCase(encryptedAccessKey)) {
	                BasicUtil.fillResultVoFalse(RestConstant.ACCESSKEY_INVALID, response);
	                return false;
	            }
	        } catch (Exception e) {
	            logger.error("checkParameterOfDomainWithCode Error", e);
	            BasicUtil.fillResultVoFalse(e.getMessage(), response);
	            return false;
	        }
	        return true;
	}

	@Override
	public boolean checkParameterOfDomainWidthID(HttpServletResponse response,
			String accessId, String accessKey, String accessIp, String userId) {
		if(accessId == null) {
            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_IS_NULL, response);
            return false;
        }
        Map<String, Object> accessMap = this.queryAccessByAccessId(accessId);
        if(accessMap.isEmpty()) {
            BasicUtil.fillResultVoFalse(RestConstant.ACCESSID_INVALID, response);
            return false;
        }
        
        String queryIp = (String)accessMap.get("ip");
        if(!BasicUtil.checkIP(accessIp, queryIp)) {
            BasicUtil.fillResultVoFalse(RestConstant.IP_INVALID, response);
            return false;
        }
        	
        String queryAccessKey = (String)accessMap.get("accesskey");
        String key = queryAccessKey + accessId+userId;
        try {
            String encryptedAccessKey = PasswordUtil.getEncyptedPasswd(key);
            if(accessKey == null || !accessKey.equalsIgnoreCase(encryptedAccessKey)) {
                BasicUtil.fillResultVoFalse(RestConstant.ACCESSKEY_INVALID, response);
                return false;
            }
        } catch (Exception e) {
            logger.error("checkParameterOfDomainWidthID Error", e);
            BasicUtil.fillResultVoFalse(e.getMessage(), response);
            return false;
        }
        return true;
	}
}
