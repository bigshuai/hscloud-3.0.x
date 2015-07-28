package org.openstack.api.compute.ext;

import java.util.Properties;

import javax.ws.rs.client.Target;

import org.openstack.api.common.Resource;

/**
 * Simple tenant usage extension
 * 
 * @author sp
 *
 */
public class SimpleTenantUsageResource extends Resource {
	
	public SimpleTenantUsageResource(Target target, Properties properties) {
		super(target, properties);
	}

}
