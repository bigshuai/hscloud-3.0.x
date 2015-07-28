/* 
* 文 件 名:  IPMIConfigDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao.impl; 

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.dao.IPMIConfigDao;
import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class IPMIConfigDaoImpl extends HibernateDao<IPMIConfig, Long> implements IPMIConfigDao {

	@Override
	public IPMIConfig getIPMIConfigById(long id) throws HsCloudException {
		return this.findUniqueBy("id", id);
	}

}
