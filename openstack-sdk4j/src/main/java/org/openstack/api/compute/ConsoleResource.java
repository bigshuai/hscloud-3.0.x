package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.Console;
import org.openstack.model.compute.nova.NovaConsole;

public class ConsoleResource extends Resource {

	public ConsoleResource(Target target, Properties properties) {
		super(target, properties);
	}

	public Console get() {
		return target.request(MediaType.APPLICATION_JSON).get(NovaConsole.class);
	}
	
//	public void delete() {
//		 resource().post(String.class);
//	}
	
}

