package org.openstack.api.compute;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.hscloud.impl.SecurityLan;
import org.openstack.model.hscloud.impl.SecurityLanList;
/**
 * VPDC-Lan
 * @author dinghb
 *
 */
public class VpdcLanResource extends Resource {

	public VpdcLanResource(Target target, Properties properties) {
		super(target, properties);
	}

	/**
	 * 创建Routed LAN
	 * @return
	 */
	public SecurityLan createRoutedLan() {
		SecurityLan sl = new SecurityLan();
		sl.setType("routed");
		return target.path("")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(sl, MediaType.APPLICATION_JSON),
						SecurityLan.class);
	}
	
	/**
	 * 创建Non-Routed LAN
	 * @return
	 */
	public SecurityLan createNoRoutedLan() {
		SecurityLan sl = new SecurityLan();
		sl.setType("nonrouted");
		return target.path("")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(sl, MediaType.APPLICATION_JSON),
						SecurityLan.class);
	}
	
	/**
	 * 删除Lan
	 * @param lanId
	 */
	public void deleteLan(Integer lanId){
		target.path("/"+lanId)
				.request(MediaType.APPLICATION_JSON).delete();
	}
	
	/**
	 * 根据LanID获取Lan
	 * @param lanId
	 * @return
	 */
	public SecurityLan getLan(Integer lanId){
		return target.path("/"+lanId)
		.request(MediaType.APPLICATION_JSON)
		.get(SecurityLan.class);
	}
	
	/**
	 * 获取所有Lan列表
	 * @return
	 */
	public SecurityLanList getAllLans(){
		return target.path("")
				.request(MediaType.APPLICATION_JSON)
				.get(SecurityLanList.class);
	}
}
