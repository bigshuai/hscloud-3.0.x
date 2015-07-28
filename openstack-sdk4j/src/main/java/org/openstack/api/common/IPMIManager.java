package org.openstack.api.common;

import ipmi.command.ctrl.BmcCommander;
import ipmi.command.struct.NmInfo;
import ipmi.command.struct.NmStats;

import org.openstack.model.common.HostIpmiInfo;

public class IPMIManager {
	
	private int timeout = 2000;
	
	BmcCommander commander = null;

	public IPMIManager(String ip, String username, String passwd) {
		this.ip = ip;
		this.username = username;
		this.password = passwd;
		commander = new BmcCommander();
		commander.configConnect(ip, username, passwd);
	}

	private String ip;

	private String username;

	private String password;

	public HostIpmiInfo getHostImpiInfo() {
		HostIpmiInfo ipmiInfo2 = new HostIpmiInfo();
		ipmiInfo2.setCpuPower(0);
		ipmiInfo2.setCpuTemp(0);
		ipmiInfo2.setHostPower(75);
		ipmiInfo2.setHostTemp(24);
		ipmiInfo2.setMemPower(0);
		ipmiInfo2.setMemTemp(0);
		/**
		if (commander != null && commander.connect(timeout)) {
			NmInfo nmInfo = commander.getNmInfo();
			//System.out.println("is NM available: " + nmInfo.isAvailable());
			if (nmInfo.isAvailable()) {
				try {
					HostIpmiInfo ipmiInfo = new HostIpmiInfo();
					NmStats hostStats = commander.getNMStats();
					NmStats HostStatsTemp = commander.getNMStatsForTemp();
					NmStats hostCPUStats = commander.getNMStats(1);
					NmStats hostCPUStatsTemp = commander.getNMStatsForTemp(1);
					NmStats hostMemStats = commander.getNMStats(2);
					NmStats hostMemStatsTemp = commander.getNMStatsForTemp(2);
					ipmiInfo.setHostPower(hostStats.getAverage());
					ipmiInfo.setHostTemp(HostStatsTemp.getAverage());
					ipmiInfo.setCpuPower(hostCPUStats.getAverage());
					ipmiInfo.setCpuTemp(hostCPUStatsTemp.getAverage());
					ipmiInfo.setMemPower(hostMemStats.getAverage());
					ipmiInfo.setMemTemp(hostMemStatsTemp.getAverage());
					return ipmiInfo;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} finally {
					commander.disConnect();
				}
			} else {
				commander.disConnect();
				return null;
			}
		} else {
			return null;
		}
		**/
		return ipmiInfo2;
	}

	public void enablePowerLimit(int limit) {
		/**
		 * 
		 
		if (commander != null && commander.connect()) {
			NmInfo nmInfo = commander.getNmInfo();
			if (nmInfo.isAvailable() && limit > 0) {
				try {
					commander.setPowerPolicy(1, limit, true);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					commander.disConnect();
				}
			} else {
				commander.disConnect();
			}
		} 
		*/
	}

	public void disablePowerLimit() {
		/**
		if (commander != null && commander.connect()) {
			NmInfo nmInfo = commander.getNmInfo();
			if (nmInfo.isAvailable()) {
				try {
					commander.setPowerPolicy(1, 100, false);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					commander.disConnect();
				}
			} else {
				commander.disConnect();
			}
		} 
		**/
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
