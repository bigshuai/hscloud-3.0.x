package org.openstack.client.hscloud.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.hscloud.HostMonitorResource;
import org.openstack.api.hscloud.HscloudResource;
import org.openstack.api.hscloud.ServerMonitorResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.hscloud.impl.CPUMonitor;

public class MonitorTest  extends AbstractOpenStackTest{

	protected HscloudResource hscloud ;
	@Before
	public void init(){
		init("etc/openstack.properties");
		hscloud = client.getHscloudEndpoint();
	}
	
	public  void getVMMonitor(){
		// Assume vm uuid 
		String vmUUID = "789eab9c-9519-4319-ae84-9e0781fea097";
		
		ServerMonitorResource server = hscloud.serverMonitor();
		HostMonitorResource host = hscloud.hostMonitor();
		// get cpu info
//		System.out.println(server.getURL());
//		CPUMonitor cpu = server.getCPU(vmUUID);
//		System.out.println(cpu);
//		// get disk, ram, net info
//		System.out.println(server.getDisk(vmUUID));
//		System.out.println(server.getNET(vmUUID));
//		System.out.println(server.getRam(vmUUID));
		// get all info
		for(int i =0 ; i< 5 ; i++){
			long begin = new Date().getTime();
			System.out.println("++++++++++The start time of request :" + begin);
			server.get(vmUUID);
			host.get("HSCloud002");
			long end = new Date().getTime();
			System.out.println("++++++++++The total request cost time :" + (end - begin));
		}
	}
	@Test
	public void getHostMonitor(){
		// Assume host name is host-01
		String hostname = "HSCloud003";
		HostMonitorResource host = hscloud.hostMonitor();
		CPUMonitor c = host.getCPU(hostname);
		System.out.println(c);
		//System.out.println(host.getURL());
		//System.out.println(host.getCPU(hostname));
//		System.out.println(host.getDisk(hostname));
		//System.out.println(host.getNET(hostname));
		//System.out.println(host.getRam(hostname));
		//System.out.println(host.get(hostname));
	}
}
