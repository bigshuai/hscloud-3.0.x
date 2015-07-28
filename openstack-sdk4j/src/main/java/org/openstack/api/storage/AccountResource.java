package org.openstack.api.storage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openstack.api.common.Resource;
import org.openstack.model.storage.StorageContainer;
import org.openstack.model.storage.swift.SwiftContainer;

public class AccountResource extends Resource {
	
	// GET /account List containers
	// HEAD account Retrieve account metadata

	
	public AccountResource(Target target, Properties properties) {
		super(target, properties);
	}

	public List<StorageContainer> get() {
		return (List<StorageContainer>) (List<?>) target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<SwiftContainer>>() {});
	}
	
	public Response head() {
		return target.request().head();
	}

	public ContainerResource container(String id) {
		return new ContainerResource(target.path("/{id}").pathParam("id", id), properties);
	}
	
	public ContainerResource container(){
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String datestr = sdf.format(d);
		System.out.println(datestr);
		return new ContainerResource(target.path("/{id}").pathParam("id", datestr), properties);
	}

	public Response updateKey(String key){
		Builder b = target.request(MediaType.APPLICATION_JSON);
		b.header("X-Account-Meta-Temp-Url-Key", key);
		return b.post(Entity.entity("", MediaType.APPLICATION_JSON));
	}
	
	public Response post() {
		return target.request().post(null);
	}

}
