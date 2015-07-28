/* 
* 文 件 名:  BasicService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service; 

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hisoft.hscloud.crm.usermanager.entity.User;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface BasicService {
    
    
    public Map<String, Object> queryAccessByAccessId(String accessId);

    /**
     * <获取用户> 
    * <功能详细描述> 
    * @param userId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public User getUser(long userId);
    /**
     * <查看用户是否虚拟机拥有者> 
    * <功能详细描述> 
    * @param vmId
    * @param userId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public long queryReferenceId(String vmId, String userId);
    
    /**
     * <ip验证> 
    * <功能详细描述> 
    * @param accessIp
    * @param accessId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean checkIP(String accessIp, String accessId);
    
    /**
     * <创建任务记录> 
    * <功能详细描述> 
    * @param taskMap
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public long saveTask(Map<String, Object> taskMap);
    
    /**
     * <参数校验> 
    * <功能详细描述> 
    * @param response
    * @param accessId
    * @param accessKey
    * @param accessIp
    * @param logger
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean checkParameter_domain(HttpServletResponse response,
            String accessId, String accessKey, String accessIp, Logger logger, String email);
    /**
     * <参数校验> 
    * <accessKey校验为accessKey和email加密> 
    * @param response
    * @param accessId
    * @param accessKey
    * @param accessIp
    * @param email
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean checkParameterOfDomain(HttpServletResponse response,
            String accessId, String accessKey, String accessIp,String email);
    /**
     * <用户验证> 
    * <功能详细描述> 
    * @param user
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public User checkUser(HttpServletResponse response,String email);
    /**
     * <用户分平台验证> 
    * <功能详细描述> 
    * @param response
    * @param user
    * @param accessId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean checkDomainOfUser(HttpServletResponse response,User user,String accessId);
    /**
     * <根据AccessId和emailaddr查询AccessKey> 
    * <功能详细描述> 
    * @param accessId
    * @param emailaddr
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> queryAccessByAccessId(String accessId,String emailaddr);
    /**
     * <参数校验> 
    * <accessKey校验为accessKey加code加密> 
    * @param response
    * @param accessId
    * @param accessKey
    * @param accessIp
    * @param code
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean checkParameterOfDomainWithCode(HttpServletResponse response,
            String accessId, String accessKey, String accessIp,String code);
    /**
     * <参数校验> 
    * <accessKey校验为accessKey加accessId加userId加密> 
    * @param response
    * @param accessId
    * @param accessKey
    * @param accessIp
    * @param userId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public boolean checkParameterOfDomainWidthID(HttpServletResponse response,
            String accessId, String accessKey, String accessIp,String userId);
}
