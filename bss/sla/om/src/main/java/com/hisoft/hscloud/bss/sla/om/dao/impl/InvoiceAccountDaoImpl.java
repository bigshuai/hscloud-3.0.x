/* 
* 文 件 名:  InvoiceAccountDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.dao.impl; 

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.InvoiceAccountDao;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class InvoiceAccountDaoImpl extends HibernateDao<InvoiceAccount, Long> implements InvoiceAccountDao {
    
    /**
     * 创建发票账户
    * @param invoiceAccount
    * @return
     */
    @Override
    public long saveInvoiceAccount(InvoiceAccount invoiceAccount) {
        this.save(invoiceAccount);
        return invoiceAccount.getId();
    }
    
    /**
     * 根据userId查询发票账户
    * @param userId
    * @return
     */
    @Override
    public InvoiceAccount getInvoiceAccount(long userId) {
        return this.findUniqueBy("userId", userId);
    }

}
