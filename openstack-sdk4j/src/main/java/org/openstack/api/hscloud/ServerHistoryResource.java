package org.openstack.api.hscloud;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.HistoryRangeForSearch;
import org.openstack.model.hscloud.impl.CPUHistoryList;
import org.openstack.model.hscloud.impl.DISKHistoryList;
import org.openstack.model.hscloud.impl.HistoryMonitor;
import org.openstack.model.hscloud.impl.NETHistoryList;
import org.openstack.model.hscloud.impl.RAMHistoryList;

public class ServerHistoryResource extends Resource {

	public ServerHistoryResource(Target target, Properties properties) {
		super(target, properties);
	}

	public CPUHistoryList getCpu(String resource, HistoryRangeForSearch range) {
		return target
				.path(resource + "/cpu")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(range, MediaType.APPLICATION_JSON),
						CPUHistoryList.class);
	}

	public RAMHistoryList getRam(String resource, HistoryRangeForSearch range) {
		return target
				.path(resource + "/ram")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(range, MediaType.APPLICATION_JSON),
						RAMHistoryList.class);
	}

	public DISKHistoryList getDisk(String resource, HistoryRangeForSearch range) {
		return target
				.path(resource + "/disk")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(range, MediaType.APPLICATION_JSON),
						DISKHistoryList.class);
	}

	public NETHistoryList getNet(String resource, HistoryRangeForSearch range) {
		return target
				.path(resource + "/net")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(range, MediaType.APPLICATION_JSON),
						NETHistoryList.class);
	}

}
