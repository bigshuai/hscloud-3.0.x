package org.openstack.api.hscloud;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.nova.floatingip.DeleteFloatingIpAction;
import org.openstack.model.compute.nova.floatingip.NovaFloatingIpForAssign;
import org.openstack.model.hscloud.impl.FloatingIpBase;
import org.openstack.model.hscloud.impl.FloatingIpDetailList;

public class IpResource  extends Resource{

	public IpResource(Target target, Properties properties) {
		super(target, properties);
	}

	/**
	 * 
	 * @param nfifa
	 * @return the assign ip or null
	 */
	public FloatingIpBase assignFloatingIp(NovaFloatingIpForAssign nfifa){
		return target.path("/assign")
		.request(MediaType.APPLICATION_JSON)
		.post(Entity.entity(nfifa, MediaType.APPLICATION_JSON),
				FloatingIpBase.class);
	}
	
	public FloatingIpDetailList listFloatingIps(){
		return target.request(MediaType.APPLICATION_JSON)
				.get(FloatingIpDetailList.class);
	}
	
	public void createFloatingIps(String pool,String from,String to){
		String body = "{\"pool\":\""+pool+"\",\"from\":\""+from+"\",\"to\":\""+to+"\"}";
		target.request(MediaType.APPLICATION_JSON)
		.post(Entity.entity(body, MediaType.APPLICATION_JSON),
				String.class);
	}
	
	public void deleteFloatingIps(DeleteFloatingIpAction dfia){
		target.path("/delete")
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(dfia, MediaType.APPLICATION_JSON),
						String.class);
	}
	
}
