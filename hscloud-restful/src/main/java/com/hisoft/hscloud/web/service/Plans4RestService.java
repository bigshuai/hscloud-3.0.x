/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: Administrator 2013-4-18   下午4:14:55
 * @brief: 类功能描述
 * @log: Administrator
 */

package com.hisoft.hscloud.web.service;

import java.util.List;

import com.hisoft.hscloud.web.vo.PlansInfoVo;



public interface Plans4RestService {

	public List<PlansInfoVo> getPlans4RestByUser(String user_id, String vmId);
}
