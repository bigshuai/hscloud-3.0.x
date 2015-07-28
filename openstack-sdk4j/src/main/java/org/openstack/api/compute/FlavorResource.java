package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.Flavor;
import org.openstack.model.compute.nova.NovaFlavor;
import org.openstack.model.images.glance.GlanceImage;

public class FlavorResource extends Resource {

	public FlavorResource(Target target, Properties properties) {
		super(target, properties);
	}

	public Flavor get() {
		return target.request(MediaType.APPLICATION_JSON).get(NovaFlavor.class);
	}

	public void delete() {
		target.request().delete();
	}
	
}
