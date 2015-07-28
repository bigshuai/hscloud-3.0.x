/**
 * @title MonitorMsgBean.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 下午2:38:58
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 下午2:38:58
 */
public class MonitorMsgBean extends BasicMsgBean {

	private HostMsgBean host;
	private VMMsgBean[] virt_machine;

	public HostMsgBean getHost() {
		return host;
	}

	public void setHost(HostMsgBean host) {
		this.host = host;
	}


	public VMMsgBean[] getVirt_machine() {
		return virt_machine;
	}

	public void setVirt_machine(VMMsgBean[] virt_machine) {
		this.virt_machine = virt_machine;
	}

	@Override
	public String toString() {
		return "MonitorMsgBean [host=" + host + ", virt_machine="
				+ virt_machine + "]";
	}
	
	

}
