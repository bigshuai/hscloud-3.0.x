/* 
 * 文 件 名:  DemandRestService.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  ljg 
 * 修改时间:  2014-3-19 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.service;

import java.util.Map;

import com.hisoft.hscloud.bss.sla.om.util.SubmitOrderData;
import com.hisoft.hscloud.web.vo.DemandItemsVo;
import com.hisoft.hscloud.web.vo.OrderPlanVo;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author ljg
 * @version [版本号, 2014-3-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface DemandRestService {

	/**
	 * <按需购买> 
	* <功能详细描述> 
	* @param submitData
	* @param orderPlanVos
	* @param id
	* @param demandItemsVo
	* @param patmentFlag（true：直接支付；false：只生成订单）
	* @return
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public Map<String, Object> demandBuyDirect(SubmitOrderData submitData,
			OrderPlanVo orderPlanVos, String id, DemandItemsVo demandItemsVo,
			boolean patmentFlag) throws Exception;
}
