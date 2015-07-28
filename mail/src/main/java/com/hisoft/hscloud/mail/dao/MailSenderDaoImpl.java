/* 
* 文 件 名:  MailSenderDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.mail.dao; 

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
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
@Repository
public class MailSenderDaoImpl extends HibernateDao<MailSender,Long> implements  MailSenderDao {
    
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * <创建分平台邮件发送者> 
    * <功能详细描述> 
    * @param domainId 
    * @see [类、类#方法、类#成员]
     */
    @Override
    public void createMailSender(long domainId) throws HsCloudException{
        String sql = "insert into `hc_mail_sender`(`domain_id`,`address`,`username`,`password`,`smtp`,`templete`,`type`)" +
        		" (select :domainId, `address`,`username`,`password`,`smtp`,`templete`,`type` " +
        		"from hc_mail_sender where domain_id = 0)";
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("domainId", domainId);
        int result = query.executeUpdate();
        if(result < 1){
            throw new HsCloudException(Constants.MAIL_SENDER_ADD_EXCEPTION, "创建分平台邮件发送者", logger);
        }
    }
    @Override
    @Transactional(readOnly = false)
    public List<MailSender> findMailSender() {
        String hql = "from MailSender";
        return this.find(hql, new HashMap<String, Object>());
    }
}
