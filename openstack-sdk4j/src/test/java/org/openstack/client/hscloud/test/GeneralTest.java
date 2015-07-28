package org.openstack.client.hscloud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.ServerResource;
import org.openstack.api.compute.TenantResource;
import org.openstack.api.hscloud.HscloudResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.compute.nova.server.actions.RebuildAction;
import org.openstack.model.hscloud.impl.HostAcquisition;
import org.openstack.model.hscloud.impl.HostBasic;
import org.openstack.model.hscloud.impl.HostGlobalAcquisition;
import org.openstack.model.hscloud.impl.HostList;
import org.openstack.model.hscloud.impl.ServerAcquisition;
import org.openstack.model.hscloud.impl.ZoneForCreate;

public class GeneralTest extends AbstractOpenStackTest{

	protected HscloudResource hscloud ;
	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		hscloud = client.getHscloudEndpoint();
		compute = client.getComputeEndpoint();
	}
	public void discoveryHost(){
		HostList l = hscloud.getHosts();
		
		List<HostBasic> hs = l.getList();
		for(HostBasic hb : hs){
			System.out.println(hb);
		}
	}
	
	public void listVm(){
		compute.servers().get().getList();
	}
	
	
	public void getGlobalAcquisition(){
		HostGlobalAcquisition a = hscloud.globalAcquisition();
		List<HostAcquisition> l = a.getList();
		for(HostAcquisition li : l){
			System.out.println(li);
			List<ServerAcquisition> ls = li.getList();
			for(ServerAcquisition sa : ls){
				System.out.println(sa);
			}
		}
	}
	
	public void rebulid(){
		ServerResource serverResource = compute.servers().server("81f992b9-e94a-43b2-a44b-67b8b937b206");
		RebuildAction action = new RebuildAction();
		action.setImageRef("08936f61-2362-4795-bfc6-c532260f7a98");
		serverResource.rebuild(action);
	}
	@Test
	public void zoneCreate(){
		ZoneForCreate z = new ZoneForCreate();
		z.setIsDefault(0);
		z.setZoneCode("1112233");
		hscloud.zoneResource().createZone(z);
	}
	
}
