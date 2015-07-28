/* 
* 文 件 名:  MailSenderDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.mail.dao; 

import java.util.List;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.mail.entity.MailSender;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface MailSenderDao {

    public void createMailSender(long domainId) throws HsCloudException;

    public List<MailSender> findMailSender();

}
