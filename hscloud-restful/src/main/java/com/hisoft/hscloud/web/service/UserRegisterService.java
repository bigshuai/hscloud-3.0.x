/* 
* 文 件 名:  UserRegisterService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface UserRegisterService {
    
    public String registerUser(String email, String name, String domainAbbreviation);
    /**
     * <添加外部关联用户信息> 
    * <功能详细描述> 
    * @param externalUserId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String registerUserFromExternal(String externalUserId);
}
