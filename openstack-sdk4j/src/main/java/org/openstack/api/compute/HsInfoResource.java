package org.openstack.api.compute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.nova.NovaAddressList.Network.Ip;
import org.openstack.model.compute.nova.NovaInstanceThin;
import org.openstack.model.compute.nova.NovaInstancesThin;
import org.openstack.model.compute.nova.NovaServer;

public class HsInfoResource extends Resource {

	public HsInfoResource(Target target, Properties properties) {
		super(target, properties);
	}

	public Server getServerThin(String uuid) {
		NovaInstancesThin n = target.path("/" + uuid + "/detail")
				.request(MediaType.APPLICATION_JSON)
				.get(NovaInstancesThin.class);
		if (n.getList().size() > 0) {
			NovaInstanceThin nt = n.getList().get(0);
			NovaServer ns = new NovaServer();
			Ip ip = new Ip();
			ip.setAddr(nt.getFixed_ip());
			ip.setVersion("4");
			Ip ip2 = new Ip();
			ip2.setAddr(nt.getFloatingIp());
			ip2.setVersion("4");
			List<Ip> lip = new ArrayList<Ip>();
			lip.add(ip);
			lip.add(ip2);
			Map<String, List<Ip>> m = new HashMap<String, List<Ip>>();
			m.put("private", lip);

			ns.setAddresses(m);
			ns.setId(nt.getUuid());
			ns.setStatus(nt.getVmState());
			ns.setHostName(nt.getHost());
			ns.setName(nt.getDisplayName());
			ns.setTaskStatus(nt.getTaskState());
			return ns;
		} else {
			return null;
		}

	}

	public NovaInstancesThin getServersThin() {
		return target.request().get(NovaInstancesThin.class);
	}

}
