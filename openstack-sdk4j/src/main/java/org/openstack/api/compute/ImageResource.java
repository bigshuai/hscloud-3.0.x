package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.Image;
import org.openstack.model.compute.nova.MetadataForCreateOrUpdate;
import org.openstack.model.compute.nova.NovaImage;

public class ImageResource extends Resource {

	public ImageResource(Target target, Properties properties) {
		super(target, properties);
	}

	public Image get() {
		return target.request(MediaType.APPLICATION_JSON).get(NovaImage.class);
	}

	public Response delete() {
		return target.request().delete();
	}

	/*
	 * public Metadata metadata() { // /metadata return new NovaMetadata(); }
	 */

	public String setMetadata(MetadataForCreateOrUpdate m) {
		return target.path("/metadata").request(MediaType.APPLICATION_JSON).post(
				Entity.entity(m, MediaType.APPLICATION_JSON), String.class);
	}
}
