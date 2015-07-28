package org.openstack.client.compute.test;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.TenantResource;
import org.openstack.api.compute.VpdcNetworkResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.hscloud.impl.Network;
import org.openstack.model.hscloud.impl.NetworkList;

public class VpdcNetworkTest extends AbstractOpenStackTest{
	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint();
	}
	
	//创建Lan Network
	public void testCreateLanNetwork(){
		VpdcNetworkResource vnr = compute.vpdcNetworks();
		String label = "lan";
		String dns1 = "114.114.114.114";
		String dns2 = "8.8.8.8";
		Integer networkSize = 64;
		Network nw = vnr.createLanNetwork(label, dns1, dns2, networkSize);
	}
	
	//创建Wan Network
	
	public void testCreateWanNetwork(){
		VpdcNetworkResource vnr = compute.vpdcNetworks();
		String label = "wan";
		String cidr = "172.16.1.1/24";
		String dns1 = "114.114.114.114";
		String gateway = "172.16.12.254";
		Network nw = vnr.createWanNetwork(label, cidr, dns1, gateway);
	}
	
	//删除Network
	@Test
	public void testDeleteNetwork(){
		VpdcNetworkResource vnr = compute.vpdcNetworks();
		String id = "e111e3c9-892a-46b8-bd39-418ef4d848c9";
		vnr.deleteNetwork(id);
	}	
	
	//通过id查询Network
	public void testGetNetwork(){
		VpdcNetworkResource vnr = compute.vpdcNetworks();
		String id = "4e9a343d-c355-4655-b1a6-29f2f58b9928";
		Network nw = vnr.getNetwork(id);
	}
	
	//查询所有Network
	
	public void testGetNetworks(){
		VpdcNetworkResource vnr = compute.vpdcNetworks();
		NetworkList nwl = vnr.getAllNetworks();
		for(Network nw :nwl.getLsl()){
			System.out.println(nw.getId()+":"+nw.getCidr());
		}
	}
}
