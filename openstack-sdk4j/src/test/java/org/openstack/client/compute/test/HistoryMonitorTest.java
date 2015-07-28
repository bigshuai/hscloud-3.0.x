/**
 * @title ComputeIntegrationTest.java
 * @package org.openstack.client.compute.test
 * @description 
 * @author YuezhouLi
 * @update 2012-5-30 上午10:36:53
 * @version V1.0
 */
package org.openstack.client.compute.test;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.TenantResource;
import org.openstack.api.compute.WqServerResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.hscloud.impl.CPUHistoryList;
import org.openstack.model.hscloud.impl.DISKHistoryList;
import org.openstack.model.hscloud.impl.HistoryMonitor;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-30 上午10:36:53
 */
public class HistoryMonitorTest extends AbstractOpenStackTest{

	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint("HK");
	}
	
	/**
	 * 根据传参不同（cpu/disk/net/ram）使用不同方法获取，接收对象也不同
	 * sethN值可为节点名/虚拟机UUID，分别查询出节点监控信息或VM监控信息
	 */
	@Test
	public void getHistoryMonitor(){
		//CPU历史
		HistoryMonitor cpu = new HistoryMonitor();
		cpu.sethN("HSCloudNode001");//查询VM监控信息，就输入UUID即可
		cpu.setiT("cpu");
		cpu.setsT("1377672552.412417");
		cpu.seteT("1377673544.507965");
		WqServerResource wsr = compute.wqservers();
		CPUHistoryList chl = wsr.getCpuHistorys(cpu);
		System.out.println(chl.getList().size());
		System.out.println(chl.getList().get(0).getCpuNum());
		System.out.println(chl.getList().get(0).getCpuRate());
		System.out.println(chl.getList().get(0).getTimestamp());
		
		//DISK历史
		HistoryMonitor disk = new HistoryMonitor();
		disk.sethN("HSCloudNode001");
		disk.setiT("disk");
		disk.setsT("1377672552.412417");
		disk.seteT("1377673544.507965");
		DISKHistoryList chl_disk = wsr.getDiskHistorys(disk);
		System.out.println(chl_disk.getList().size());
		System.out.println(chl_disk.getList().get(0).getDiskTotal());
		System.out.println(chl_disk.getList().get(0).getReadSpeed());
		System.out.println(chl_disk.getList().get(0).getWriteSpeed());
		System.out.println(chl_disk.getList().get(0).getTimestamp());

	}
}
