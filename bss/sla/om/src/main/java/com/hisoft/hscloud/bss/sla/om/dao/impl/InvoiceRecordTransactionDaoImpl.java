/* 
* 文 件 名:  InvoiceRecordTransactionDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.dao.impl; 

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.InvoiceRecordTransactionDao;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecordTransaction;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class InvoiceRecordTransactionDaoImpl extends HibernateDao<InvoiceRecordTransaction, Long>implements
        InvoiceRecordTransactionDao {
    
    @Override
    public void saveEntity(InvoiceRecordTransaction entity) {
        this.save(entity);
    }
}
