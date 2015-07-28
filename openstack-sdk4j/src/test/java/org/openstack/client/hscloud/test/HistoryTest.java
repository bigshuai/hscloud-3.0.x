package org.openstack.client.hscloud.test;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.hscloud.HostHistoryResource;
import org.openstack.api.hscloud.HscloudResource;
import org.openstack.api.hscloud.ServerHistoryResource;
import org.openstack.api.hscloud.ServerMonitorResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.hscloud.HistoryRangeForSearch;
import org.openstack.model.hscloud.impl.CPUHistoryList;
import org.openstack.model.hscloud.impl.HistoryMonitor;

public class HistoryTest  extends AbstractOpenStackTest{

	protected HscloudResource hscloud ;
	@Before
	public void init(){
		init("etc/openstack.properties");
		hscloud = client.getHscloudEndpoint();
	}
	public void getServerHistory(){
		String vmuuid = "41061c33-52dc-4e5a-a58b-a6a3afd769b6";
		ServerHistoryResource server = hscloud.serverHistory();
		Calendar cal=Calendar.getInstance();
		Date date=cal.getTime(); 
		cal.add(Calendar.DATE, -1);
		Date date2 = cal.getTime();
		HistoryRangeForSearch range = new HistoryRangeForSearch(date2.getTime(), date.getTime());
		System.out.println(server.getCpu(vmuuid, range));
		System.out.println(server.getDisk(vmuuid, range));
		System.out.println(server.getRam(vmuuid, range));
		System.out.println(server.getNet(vmuuid, range));
		
		// same as disk , ram , net
	}
	
	@Test
	public void getHostHistory(){
		String hostname = "HSCloud002";
		HostHistoryResource host = hscloud.hostHistory();
		Calendar cal=Calendar.getInstance();
		Date date=cal.getTime(); 
		cal.add(Calendar.DATE, -1);
		Date date2 = cal.getTime();
		HistoryRangeForSearch range = new HistoryRangeForSearch(date2.getTime(), date.getTime());
		System.out.println(host.getCpu(hostname, range));
		System.out.println(host.getDisk(hostname, range));
		System.out.println(host.getRam(hostname, range));
		System.out.println(host.getNet(hostname, range));
		// same as disk , ram , net
	}
	
}
