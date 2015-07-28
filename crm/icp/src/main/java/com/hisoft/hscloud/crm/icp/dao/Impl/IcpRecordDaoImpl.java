/* 
* 文 件 名:  IcpDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.icp.dao.Impl; 

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.icp.dao.IcpRecordDao;
import com.hisoft.hscloud.crm.icp.entity.IcpRecord;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class IcpRecordDaoImpl extends HibernateDao<IcpRecord, Long> implements IcpRecordDao {
    @Override
    public long saveIcp(IcpRecord icpRecord) {
        this.save(icpRecord);
        return icpRecord.getId();
    }
}
