package com.hisoft.hscloud.vpdc.oss.monitoring.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name = "hc_host_monitor_history")
public class HostMonitorHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "name", length = 128)
	private String name;
	
	@Column(name = "cpu_rate")
	private double cpu_rate;
	
	@Column(name = "memory_rate")
	private double memory_rate;
	
	@Column(name = "disk_rate")
	private double disk_rate;
	
	@Column(name = "networkIn" )
	private double networkIn;
	
	@Column(name = "networkOut" )
	private double networkOut;
	
	@Column(name = "cpu_total" )
	private double cpu_total;
	
	@Column(name = "disk_total" )
	private double disk_total;
	
	@Column(name = "memory_total" )
	private double memory_total;
	
	@Column(name = "vm_num")
	private int vm_num;
	
	@Column(name = "vm_active_num" )
	private int vm_active_num;
	
	@Column(name = "vm_error_num" )
	private int vm_error_num;
	
	@Column(name = "status" )
	private String status;
	
	@Column(name = "host_id" )
	private String host_id;
	
	@Column(name = "monitorTime" )
	private Date monitorTime;
	 
	/**    
	 * networkIn    
	 *    
	 * @return  the networkIn    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public double getNetworkIn() {
		return networkIn;
	}

	/**    
	 * @param networkIn the networkIn to set    
	 */
	
	public void setNetworkIn(double networkIn) {
		this.networkIn = networkIn;
	}

	/**    
	 * networkOut    
	 *    
	 * @return  the networkOut    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public double getNetworkOut() {
		return networkOut;
	}

	/**    
	 * @param networkOut the networkOut to set    
	 */
	
	public void setNetworkOut(double networkOut) {
		this.networkOut = networkOut;
	}

	
	/**    
	 * id    
	 *    
	 * @return  the id    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public int getId() {
		return id;
	}

	/**    
	 * @param id the id to set    
	 */
	
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cpu_rate
	 */
	public double getCpu_rate() {
		return cpu_rate;
	}

	/**
	 * @param cpu_rate the cpu_rate to set
	 */
	public void setCpu_rate(double cpu_rate) {
		this.cpu_rate = cpu_rate;
	}

	/**
	 * @return the memory_rate
	 */
	public double getMemory_rate() {
		return memory_rate;
	}

	/**
	 * @param memory_rate the memory_rate to set
	 */
	public void setMemory_rate(double memory_rate) {
		this.memory_rate = memory_rate;
	}

	/**
	 * @return the disk_rate
	 */
	public double getDisk_rate() {
		return disk_rate;
	}

	/**
	 * @param disk_rate the disk_rate to set
	 */
	public void setDisk_rate(double disk_rate) {
		this.disk_rate = disk_rate;
	}

	 
 
	/**
	 * @return the cpu_total
	 */
	public double getCpu_total() {
		return cpu_total;
	}

	/**
	 * @param cpu_total the cpu_total to set
	 */
	public void setCpu_total(double cpu_total) {
		this.cpu_total = cpu_total;
	}

	/**
	 * @return the memory_total
	 */
	public double getMemory_total() {
		return memory_total;
	}

	/**
	 * @param memory_total the memory_total to set
	 */
	public void setMemory_total(double memory_total) {
		this.memory_total = memory_total;
	}

 

	/**
	 * @return the vm_active_num
	 */
	public int getVm_active_num() {
		return vm_active_num;
	}

	/**
	 * @param vm_active_num the vm_active_num to set
	 */
	public void setVm_active_num(int vm_active_num) {
		this.vm_active_num = vm_active_num;
	}

	/**
	 * @return the vm_error_num
	 */
	public int getVm_error_num() {
		return vm_error_num;
	}

	/**
	 * @param vm_error_num the vm_error_num to set
	 */
	public void setVm_error_num(int vm_error_num) {
		this.vm_error_num = vm_error_num;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the host_id
	 */
	public String getHost_id() {
		return host_id;
	}

	/**
	 * @param host_id the host_id to set
	 */
	public void setHost_id(String host_id) {
		this.host_id = host_id;
	}

	/**    
	 * disk_total    
	 *    
	 * @return  the disk_total    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public double getDisk_total() {
		return disk_total;
	}

	/**    
	 * @param disk_total the disk_total to set    
	 */
	
	public void setDisk_total(double disk_total) {
		this.disk_total = disk_total;
	}

	/**    
	 * vm_num    
	 *    
	 * @return  the vm_num    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public int getVm_num() {
		return vm_num;
	}

	/**    
	 * @param vm_num the vm_num to set    
	 */
	
	public void setVm_num(int vm_num) {
		this.vm_num = vm_num;
	}

	/**    
	 * monitorTime    
	 *    
	 * @return  the monitorTime    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public Date getMonitorTime() {
		return monitorTime;
	}

	/**    
	 * @param monitorTime the monitorTime to set    
	 */
	
	public void setMonitorTime(Date monitorTime) {
		this.monitorTime = monitorTime;
	}

	   
	       /* (non-Javadoc)    
	        * @see java.lang.Object#toString()    
	        */    
	       
	@Override
	public String toString() {
		return "HostMonitorHistory [id=" + id + ", name=" + name
				+ ", cpu_rate=" + cpu_rate + ", memory_rate=" + memory_rate
				+ ", disk_rate=" + disk_rate + ", networkIn=" + networkIn
				+ ", networkOut=" + networkOut + ", cpu_total=" + cpu_total
				+ ", disk_total=" + disk_total + ", memory_total="
				+ memory_total + ", vm_num=" + vm_num + ", vm_active_num="
				+ vm_active_num + ", vm_error_num=" + vm_error_num
				+ ", status=" + status + ", host_id=" + host_id
				+ ", monitorTime=" + monitorTime + "]";
	}

 
	 
}
