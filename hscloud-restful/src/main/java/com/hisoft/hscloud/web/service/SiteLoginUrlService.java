/* 
* 文 件 名:  SiteLoginUrlService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service; 

import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface SiteLoginUrlService {

    public User getUserByEmail(String email);
    public Domain getDomainByCode(String code);
}
