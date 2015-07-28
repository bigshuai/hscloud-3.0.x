package org.openstack.api.compute;

import java.util.Properties;
import javax.ws.rs.client.Target;
import org.openstack.api.common.Resource;
/**
 * 节点服务
 * @author dinghb
 *
 */
public class HostActionsResource extends Resource {

	public HostActionsResource(Target target, Properties properties) {
		super(target, properties);
	}
	
	public HostActionResource host(String hostName) {
		return new HostActionResource(target.path("/{name}").pathParam("name", hostName),
				properties);
	}
}
