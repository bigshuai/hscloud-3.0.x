package com.hisoft.hscloud.bss.sla.om.dao; 

import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface OrderItemDao {
	
   public OrderItem getOrderItemById(Long id)throws HsCloudException;
   
   public void saveOrderItem(OrderItem orderItem)throws HsCloudException;
   
}
