package com.hisoft.hscloud.bss.sla.om.service;

import java.util.List;

import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * 
 * <订单项业务操作> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2012-11-26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface OrderItemService {
	/**
	 * <根据订单项物理Id获取订单项数据> <功能详细描述>
	 * 
	 * @param orderItemid
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItem getOrderItemById(Long orderItemid) throws HsCloudException;

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param orderItemId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean tryOrderOrNo(Long orderItemId) throws HsCloudException;
}
