package org.openstack.api.hscloud;

import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.filter.LoggingFilter;
import org.openstack.api.common.Resource;
import org.openstack.api.compute.TenantResource;
import org.openstack.model.hscloud.impl.HostGlobalAcquisition;
import org.openstack.model.hscloud.impl.HostList;

public class HscloudResource extends Resource {

	private LoggingFilter loggingFilter = new LoggingFilter(Logger.getLogger(TenantResource.class.getPackage().getName()),true);
	
	public HscloudResource(Target target, Properties properties) {
		super(target, properties);
		if(Boolean.parseBoolean(properties.getProperty("verbose"))) {
			target.configuration().register(loggingFilter);
		}
	}
	
	public ServerMonitorResource serverMonitor(){
		return path("/servers/monitor", ServerMonitorResource.class);
	}
	
	public HostMonitorResource hostMonitor(){
		return path("/hosts/monitor", HostMonitorResource.class);
	}
	
	public ServerHistoryResource serverHistory(){
		return path("/servers/history", ServerHistoryResource.class);
	}
	
	public HostHistoryResource hostHistory(){
		return path("/hosts/history", HostHistoryResource.class);
	}
	
	public HostList getHosts(){
		return target.path("/hosts").request(MediaType.APPLICATION_JSON).get(HostList.class);
	}
	
	public IpResource ipResource(){
		return path("/floating_ip", IpResource.class);
	}
	
	public ZoneResource zoneResource(){
		return path("/zone", ZoneResource.class);
	}
	
	public HostGlobalAcquisition globalAcquisition(){
		return target.path("/hosts/acquisition").request(MediaType.APPLICATION_JSON).get(HostGlobalAcquisition.class);
	}

}
