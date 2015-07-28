package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.impl.Network;
import org.openstack.model.hscloud.impl.NetworkList;
import org.openstack.model.hscloud.impl.SecurityLan;
/**
 * VPDC-network
 * @author dinghb
 *
 */
public class VpdcNetworkResource extends Resource {

	public VpdcNetworkResource(Target target, Properties properties) {
		super(target, properties);
	}
	
	/**
	 * 创建Lan Network
	 * @param label
	 * @param dns1
	 * @param dns2
	 * @param networkSize
	 * @return
	 */
	public Network createLanNetwork(String label,String dns1,String dns2,int networkSize) {
		Network nw = new Network();
		nw.setLabel(label);
		nw.setDns1(dns1);
		nw.setDns2(dns2);
		nw.setNetworkSize(networkSize);
		return target.path("")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(nw, MediaType.APPLICATION_JSON),
						Network.class);
	}
	
	/**
	 * 创建Wan Network
	 * @param label
	 * @param cidr
	 * @param dns1
	 * @param gateway
	 * @return
	 */
	public Network createWanNetwork(String label,String cidr,String dns1,String gateway) {
		Network nw = new Network();
		nw.setLabel(label);
		nw.setCidr(cidr);
		nw.setDns1(dns1);
		nw.setGateway(gateway);
		return target.path("")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(nw, MediaType.APPLICATION_JSON),
						Network.class);
	}

	/**
	 * 删除network
	 * @param id
	 */
	public void deleteNetwork(String id) {
		target.path("/"+id)
				.request(MediaType.APPLICATION_JSON).delete();
	}
	
	/**
	 * 根据id获取network
	 * @param id
	 * @return
	 */
	public Network getNetwork(String id) {
		return target.path("/"+id)
				.request(MediaType.APPLICATION_JSON)
				.get(Network.class);
	}
	
	/**
	 * 获取所有network
	 * @return
	 */
	public NetworkList getAllNetworks(){
		return target.path("")
				.request(MediaType.APPLICATION_JSON)
				.get(NetworkList.class);
	}
}
