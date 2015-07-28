package org.openstack.api.hscloud;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.impl.ZoneForCreate;

public class ZoneResource extends Resource {

	public ZoneResource(Target target, Properties properties) {
		super(target, properties);
	}

	public void createZone(ZoneForCreate zone) {
		target.request(MediaType.APPLICATION_JSON).post(
				Entity.entity(zone, MediaType.APPLICATION_JSON), String.class);
	}
}
