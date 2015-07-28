/* 
* 文 件 名:  AccessAccountDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.dao; 

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.AccessAccount;

/** 
 * <API接口用户服务> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface AccessAccountDao {

    public void saveAccessAccount(AccessAccount accessAccount);

    public Page<AccessAccount> findAccessAccountPage(
            Page<AccessAccount> accessAccountPage, String query);

    public void deleteAccessAccount(long id);

    public AccessAccount getAccessAccount(String accessId);
}
