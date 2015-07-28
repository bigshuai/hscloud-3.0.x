package org.openstack.api.hscloud;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.impl.CPUHistoryList;
import org.openstack.model.hscloud.impl.CPUMonitor;
import org.openstack.model.hscloud.impl.DISKMonitorList;
import org.openstack.model.hscloud.impl.HistoryMonitor;
import org.openstack.model.hscloud.impl.NETMonitorList;
import org.openstack.model.hscloud.impl.RAMMonitor;
import org.openstack.model.hscloud.impl.ServerMonitor;

public class ServerMonitorResource extends Resource {

	public ServerMonitorResource(Target target, Properties properties) {
		super(target, properties);
	}
	
	/**
	 * 
	 * @param resource , vm uuid
	 * @return
	 */
	public CPUMonitor getCPU(String resource){
		return target.path(resource + "/cpu").request(MediaType.APPLICATION_JSON).get(CPUMonitor.class);
	}
	
	/**
	 * 
	 * @param resource , vm uuid
	 * @return
	 */
	public RAMMonitor getRam(String resource){
		return target.path(resource + "/ram").request(MediaType.APPLICATION_JSON).get(RAMMonitor.class);
	}
	
	/**
	 * 
	 * @param resource , vm uuid
	 * @return
	 */
	public DISKMonitorList getDisk(String resource){
		return target.path(resource + "/disk").request(MediaType.APPLICATION_JSON).get(DISKMonitorList.class);
	}
	
	/**
	 * 
	 * @param resource , vm uuid
	 * @return
	 */
	public NETMonitorList getNET(String resource){
		return target.path(resource + "/net").request(MediaType.APPLICATION_JSON).get(NETMonitorList.class);
	}
	
	/**
	 * 
	 * @param resource , vm uuid
	 * @return cpu, ram, disk, net monitor info
	 */
	public ServerMonitor get(String resource){
		return target.path(resource).request(MediaType.APPLICATION_JSON).get(ServerMonitor.class);
	}
	
}
