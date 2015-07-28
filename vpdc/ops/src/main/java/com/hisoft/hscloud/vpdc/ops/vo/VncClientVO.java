package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * 通过Vncviewer连接VNC时所需参数值
 * @author dinghb
 *
 */
public class VncClientVO {
	//代理IP
	private String proxyIP;
	//代理端口号
	private Integer proxyPort;
	public String getProxyIP() {
		return proxyIP;
	}
	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}
	public Integer getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
	@Override
	public String toString() {
		return "VncClientVO [proxyIP=" + proxyIP + ", proxyPort=" + proxyPort
				+ "]";
	}
	
}
