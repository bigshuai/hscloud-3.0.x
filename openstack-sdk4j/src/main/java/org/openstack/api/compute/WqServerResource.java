package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.impl.CPUHistoryList;
import org.openstack.model.hscloud.impl.DISKHistoryList;
import org.openstack.model.hscloud.impl.HistoryMonitor;
import org.openstack.model.hscloud.impl.NETHistoryList;
import org.openstack.model.hscloud.impl.RAMHistoryList;

public class WqServerResource extends Resource {

	public WqServerResource(Target target, Properties properties) {
		super(target, properties);
	}

	public CPUHistoryList getCpuHistorys(HistoryMonitor hm) {
		return target
				.path("/1/action")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(hm, MediaType.APPLICATION_JSON),
						CPUHistoryList.class);
		
	}
	
	public DISKHistoryList getDiskHistorys(HistoryMonitor hm) {
		return target
				.path("/1/action")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(hm, MediaType.APPLICATION_JSON),
						DISKHistoryList.class);
		
	}
	
	public NETHistoryList getNetHistorys(HistoryMonitor hm) {
		return target
				.path("/1/action")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(hm, MediaType.APPLICATION_JSON),
						NETHistoryList.class);
		
	}
	
	public RAMHistoryList getRamHistorys(HistoryMonitor hm) {
		return target
				.path("/1/action")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(hm, MediaType.APPLICATION_JSON),
						RAMHistoryList.class);
		
	}
}
