package com.hisoft.hscloud.web.param;

import java.util.List;
import java.util.Map;

import org.openstack.model.compute.nova.NovaServerForCreate;

import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;

public class CreateVmForBuyParamObject {
	public Order order;
	public String defaultZone;
	public String CreateVpdcBean;
	public List<CreateVmBean> createVmBeans;
	public Map<String, Object> rabbitMQRouter;
	public CreateVmForBuyParamObject(Order order,String defaultZone){
		this.defaultZone=defaultZone;
		this.order=order;
	}
}
