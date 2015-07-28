package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.impl.HostServiceDisable;
import org.openstack.model.hscloud.impl.HostServiceEnable;
/**
 * 节点服务
 * @author dinghb
 *
 */
public class HostActionResource extends Resource {

	public HostActionResource(Target target, Properties properties) {
		super(target, properties);
	}
	
	/**
	 * 启用节点服务
	 * @param lanId
	 */
	public void enableHostService(String topic){
		HostServiceEnable hs = new HostServiceEnable();
		hs.setTopic(topic);
		target.path("/action")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(hs, MediaType.APPLICATION_JSON),
						HostServiceEnable.class);
	}
	
	/**
	 * 禁用节点服务
	 * @param lanId
	 */
	public void disableHostService(String topic){
		HostServiceDisable hsd = new HostServiceDisable();
		hsd.setTopic(topic);
		target.path("/action")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(hsd, MediaType.APPLICATION_JSON),
						HostServiceDisable.class);
	}
}
