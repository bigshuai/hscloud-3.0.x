/**
 * @title FloatingIpTest.java
 * @package org.openstack.client.compute.test
 * @description 
 * @author YuezhouLi
 * @update 2012-8-27 下午4:53:05
 * @version V1.0
 */
package org.openstack.client.compute.test;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.TenantResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.compute.FloatingIp;
import org.openstack.model.compute.nova.floatingip.DeleteFloatingIpAction;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-8-27 下午4:53:05
 */
public class FloatingIpTest extends AbstractOpenStackTest {

	protected TenantResource compute;

	@Before
	public void init() {
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint();

	}
	

	public void createFloatingIp() throws Exception {
		System.out.println("ddddddd");
		FloatingIp floatingIp = compute.floatingIps().post(null);
		System.out.println(floatingIp.getIp());

	}
	
	public void createFloatingIpRange() throws Exception{
		String pool = "test1";
		String from = "123.1.1.20";
		String to = "123.1.1.24";
		compute.ipResource().createFloatingIps(pool,from,to);
	}
	
	@Test
	public void deleteFloatingIpRange() throws Exception{
		DeleteFloatingIpAction dfia = new DeleteFloatingIpAction();
		dfia.setFrom("123.1.1.10");
		dfia.setTo("123.1.1.14");
		compute.ipResource().deleteFloatingIps(dfia);
	}
	
}
