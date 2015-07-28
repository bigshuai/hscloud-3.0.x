package com.hisoft.hscloud.vpdc.oss.monitoring.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name = "hc_vm_monitor_history")
public class VmMonitorHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "name", length = 128)
	private String vmName;
	
	@Column(name = "cpu_rate" )
	private double cpu_rate;
	
	@Column(name = "memory_rate")
	private double memory_rate;
	
	@Column(name = "disk_rate")
	private double disk_rate;
	
	@Column(name = "networkIn")
	private double networkIn;
	
	
	@Column(name = "networkOut")
	private double networkOut;
	
	@Column(name = "cpu_total")
	private double cpu_total;
	
	@Column(name = "memory_total")
	private double memory_total;
	
	@Column(name = "disk_total")
	private double disk_total;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "vmId")
	private String vmId;
	
	@Column(name = "order_item_id")
	private Long order_item_id;
	
	
	@Column(name = "monitorTime")
	private Date monitorTime; 

	/**    
	 * order_item_id    
	 *    
	 * @return  the order_item_id    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public Long getOrder_item_id() {
		return order_item_id;
	}

	/**    
	 * @param order_item_id the order_item_id to set    
	 */
	
	public void setOrder_item_id(Long order_item_id) {
		this.order_item_id = order_item_id;
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

	/**
	 * @return the id
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
	 * @return the vmName
	 */
	public String getVmName() {
		return vmName;
	}

	/**
	 * @param vmName the vmName to set
	 */
	public void setVmName(String vmName) {
		this.vmName = vmName;
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
	 * @return the networkIn
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
	 * @return the networkOut
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
	 * @return the disk_total
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
	 * @return the vmId
	 */
	public String getVmId() {
		return vmId;
	}

	/**
	 * @param vmId the vmId to set
	 */
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	   
	       /* (non-Javadoc)    
	        * @see java.lang.Object#toString()    
	        */    
	       
	@Override
	public String toString() {
		return "VmMonitorHistory [id=" + id + ", vmName=" + vmName
				+ ", cpu_rate=" + cpu_rate + ", memory_rate=" + memory_rate
				+ ", disk_rate=" + disk_rate + ", networkIn=" + networkIn
				+ ", networkOut=" + networkOut + ", cpu_total=" + cpu_total
				+ ", memory_total=" + memory_total + ", disk_total="
				+ disk_total + ", status=" + status + ", vmId=" + vmId
				+ ", Order_item_id=" + order_item_id + ", monitorTime="
				+ monitorTime + "]";
	}
	 
}
