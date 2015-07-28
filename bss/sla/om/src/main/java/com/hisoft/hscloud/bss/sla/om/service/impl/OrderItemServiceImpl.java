package com.hisoft.hscloud.bss.sla.om.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.sla.om.dao.OrderDao;
import com.hisoft.hscloud.bss.sla.om.dao.OrderItemDao;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.service.OrderItemService;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.common.util.HsCloudException;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	private Logger logger = Logger.getLogger(OrderItemServiceImpl.class);
	@Autowired
	private OrderItemDao orderItemDao;
	@Override
	public OrderItem getOrderItemById(Long orderItemid) throws HsCloudException {
		return orderItemDao.getOrderItemById(orderItemid);
	}

	@Override
	public boolean tryOrderOrNo(Long orderItemId) throws HsCloudException {
		OrderItem orderItem = orderItemDao.getOrderItemById(orderItemId);
		return orderItem.getTryOrNo();
	}

}
