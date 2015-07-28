package org.openstack.client.hscloud.test;

import org.junit.Test;
import org.openstack.api.common.IPMIManager;
import org.openstack.model.common.HostIpmiInfo;

public class IPMITest {

	@Test
	public void getIpmiInfo(){
		IPMIManager ipmiManager = new IPMIManager("192.168.7.137", "root", "root");
		HostIpmiInfo info = ipmiManager.getHostImpiInfo();
		System.out.println(info);
	}
	
	public void setIpmiInfo(){
		IPMIManager ipmiManager = new IPMIManager("192.168.7.137", "root", "root");
		ipmiManager.enablePowerLimit(200);
	}
	
	public void disablePowerLimit(){
		IPMIManager ipmiManager = new IPMIManager("192.168.7.137", "root", "root");
		ipmiManager.disablePowerLimit();
	}
}
