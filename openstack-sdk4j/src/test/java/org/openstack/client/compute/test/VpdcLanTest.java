package org.openstack.client.compute.test;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.TenantResource;
import org.openstack.api.compute.VpdcLanResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.hscloud.impl.SecurityLan;
import org.openstack.model.hscloud.impl.SecurityLanList;

public class VpdcLanTest extends AbstractOpenStackTest{
	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint();
	}
	//创建routed Lan
	public void testCreateRoutedLan(){
		VpdcLanResource vlr = compute.vpdcLans();
		SecurityLan slrl = vlr.createRoutedLan();
	}
	
	//创建no routed Lan
	public void testCreateLan(){
		VpdcLanResource vlr = compute.vpdcLans();
		SecurityLan slnrl = vlr.createNoRoutedLan();
	}
	
	//删除Lan
	public void testDeleteLan(){
		VpdcLanResource vlr = compute.vpdcLans();
		vlr.deleteLan(2);
	}
	
	//根据LanID获取Lan
	public void getLan(){
		VpdcLanResource vlr = compute.vpdcLans();
		SecurityLan sl = vlr.getLan(3);
	}
	
	//获取所有Lan
	@Test
	public void getAllLans(){
		VpdcLanResource vlr = compute.vpdcLans();
		SecurityLanList sll = vlr.getAllLans();
	}
}
