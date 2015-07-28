/* 
* 文 件 名:  IPMIConfigServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.service.impl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.sla.sc.dao.IPMIConfigDao;
import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.bss.sla.sc.service.IIPMIConfigService;
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
@Service
public class IPMIConfigServiceImpl implements IIPMIConfigService {

	@Autowired
	private IPMIConfigDao ipmiConfigDao;
	@Override
	public IPMIConfig getIPMIConfigById(long id) throws HsCloudException {
		return ipmiConfigDao.getIPMIConfigById(id);
	}

	
}
