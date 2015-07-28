/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: Administrator 2013-4-18   下午4:15:31
 * @brief: 类功能描述
 * @log: Administrator
 */


package com.hisoft.hscloud.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hisoft.hscloud.web.dao.Plans4RestDao;
import com.hisoft.hscloud.web.service.Plans4RestService;
import com.hisoft.hscloud.web.vo.PlansInfoVo;


@Service
public class Plans4RestServiceImpl implements Plans4RestService{
	/**
	 * @author: Administrator
	 * @brief: 类功能描述
	 * @log: 2013-4-18 下午4:15:31
	 **/
	@Resource
	private Plans4RestDao plans4RestDao;


	@Override
	public List<PlansInfoVo> getPlans4RestByUser(String user_id, String vmId) {
		return plans4RestDao.getPlans4RestByUser(user_id, vmId);
	}
	
}
