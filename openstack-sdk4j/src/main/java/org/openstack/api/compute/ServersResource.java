package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.client.JobHeader;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.ServerForCreate;
import org.openstack.model.compute.ServerList;
import org.openstack.model.compute.nova.NovaInstancesThin;
import org.openstack.model.compute.nova.NovaServer;
import org.openstack.model.compute.nova.NovaServerList;

public class ServersResource extends Resource {

	public ServersResource(Target target, Properties properties) {
		super(target, properties);
	}

	/**
	 * Returns a list of server names and ids for a given user
	 * 
	 * Returns a list of servers, taking into account any search options
	 * specified.
	 * 
	 * If detail is true:
	 * 
	 * Returns a list of server details for a given user
	 * 
	 * @param detail
	 * @return
	 */
	public ServerList get() {
		return target.path("/detail").request().get(NovaServerList.class);
	}

	public NovaInstancesThin getThin() {
		return target
				.path("details_thin")
				.request()
				.post(Entity.entity("", MediaType.APPLICATION_JSON),
						NovaInstancesThin.class);
	}

	public Server post(ServerForCreate serverForCreate) {
		// OSAPI bug: Can't specify an SSH key in XML?
		return target.request(MediaType.APPLICATION_JSON).post(
				Entity.entity(serverForCreate, MediaType.APPLICATION_JSON),
				NovaServer.class);
	}
	
	public Server post(ServerForCreate serverForCreate, JobHeader jobHeader) {
		// OSAPI bug: Can't specify an SSH key in XML?
		if(jobHeader != null){
			target.configuration().register(jobHeader);
		}
		return target.request(MediaType.APPLICATION_JSON).post(
				Entity.entity(serverForCreate, MediaType.APPLICATION_JSON),
				NovaServer.class);
	}

	public ServerResource server(String id) {
		return new ServerResource(target.path("/{id}").pathParam("id", id),
				properties);
	}

}
