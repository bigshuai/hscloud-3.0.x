/**
 * 
 */
package com.hisoft.hscloud.vpdc.ops.json.bean;


/**
 * @author jianwei zhang
 * *@version  [版本号, 2013-10-11] 
 * @see  [相关类/方法] 
 */
public class ResourceLimit {

	private Integer bwtIn;
	
	private Integer bwtOut;

	private Integer ipConnIn;
	
	private Integer ipConnOut;
	
	private Integer tcpConnIn;
	
	private Integer tcpConnOut;
	
	private Integer udpConnIn;
	
	private Integer udpConnOut;

//	private Integer cpuLimit;
//
//	private Integer diskRead;
//
//	private Integer diskWrite;

	public Integer getBwtIn() {
		return bwtIn;
	}

	public void setBwtIn(Integer bwtIn) {
		this.bwtIn = bwtIn;
	}

	public Integer getBwtOut() {
		return bwtOut;
	}

	public void setBwtOut(Integer bwtOut) {
		this.bwtOut = bwtOut;
	}

	public Integer getIpConnIn() {
		return ipConnIn;
	}

	public void setIpConnIn(Integer ipConnIn) {
		this.ipConnIn = ipConnIn;
	}

	public Integer getIpConnOut() {
		return ipConnOut;
	}

	public void setIpConnOut(Integer ipConnOut) {
		this.ipConnOut = ipConnOut;
	}

	public Integer getTcpConnIn() {
		return tcpConnIn;
	}

	public void setTcpConnIn(Integer tcpConnIn) {
		this.tcpConnIn = tcpConnIn;
	}

	public Integer getTcpConnOut() {
		return tcpConnOut;
	}

	public void setTcpConnOut(Integer tcpConnOut) {
		this.tcpConnOut = tcpConnOut;
	}

	public Integer getUdpConnIn() {
		return udpConnIn;
	}

	public void setUdpConnIn(Integer udpConnIn) {
		this.udpConnIn = udpConnIn;
	}

	public Integer getUdpConnOut() {
		return udpConnOut;
	}

	public void setUdpConnOut(Integer udpConnOut) {
		this.udpConnOut = udpConnOut;
	}
	

}
