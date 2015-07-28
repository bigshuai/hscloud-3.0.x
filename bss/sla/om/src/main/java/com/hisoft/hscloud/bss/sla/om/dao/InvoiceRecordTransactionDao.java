/* 
* 文 件 名:  InvoiceRecordTransactionDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.dao; 

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
public interface InvoiceRecordTransactionDao {
    /**
     * 保存实体 
    * <功能详细描述> 
    * @param entity 
    * @see [类、类#方法、类#成员]
     */
    public void saveEntity(InvoiceRecordTransaction entity);

}
