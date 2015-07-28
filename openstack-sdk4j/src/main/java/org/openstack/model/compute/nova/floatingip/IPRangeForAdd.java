/**
 * @title IPRangeForAdd.java
 * @package org.openstack.model.compute.nova.floatingip
 * @description 
 * @author YuezhouLi
 * @update 2012-8-27 下午1:32:53
 * @version V1.0
 */
package org.openstack.model.compute.nova.floatingip;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-8-27 下午1:32:53
 */
public class IPRangeForAdd {

	private String ip_range;

	private String pool;

	private String NICInterface;

	public String getIp_range() {
		return ip_range;
	}

	public void setIp_range(String ip_range) {
		this.ip_range = ip_range;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public String getNICInterface() {
		return NICInterface;
	}

	public void setNICInterface(String nICInterface) {
		NICInterface = nICInterface;
	}

}
