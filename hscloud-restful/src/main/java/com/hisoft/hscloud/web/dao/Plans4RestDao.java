/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: Administrator 2013-4-18   下午3:57:36
 * @brief: 类功能描述
 * @log: Administrator
 */

package com.hisoft.hscloud.web.dao;

import java.util.List;

import com.hisoft.hscloud.web.vo.PlansInfoVo;


public interface Plans4RestDao {
	/**
	 * @author: Administrator
	 * @brief: 类功能描述
	 * @log: 2013-4-18 下午3:57:36
	 **/
	public List<PlansInfoVo> getPlans4RestByUser(String user_id, String vmId);

}
