package org.openstack.client.compute.test;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.HostActionResource;
import org.openstack.api.compute.HostActionsResource;
import org.openstack.api.compute.TenantResource;
import org.openstack.client.AbstractOpenStackTest;

public class HostServiceTest extends AbstractOpenStackTest{
	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint();
	}
	//启用节点服务
	public void testEnableHostService(){
		HostActionsResource har = compute.hostActionsResource();
		HostActionResource ha = har.host("aahyhaa");//aahyhaa为节点名称
		ha.enableHostService("compute");//compute目前为固定值
	}
	
	//禁用节点服务
	@Test
	public void testDisableHostService(){
		HostActionsResource har = compute.hostActionsResource();
		HostActionResource ha = har.host("aahyhaa");//aahyhaa为节点名称
		ha.disableHostService("compute");//compute目前为固定值
	}
	
}
