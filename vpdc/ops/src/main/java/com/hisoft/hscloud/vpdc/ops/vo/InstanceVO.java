/**
 * @title InstanceVO.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-3-30 下午7:01:00
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hisoft.hscloud.vpdc.ops.json.bean.VmOsBean;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-3-30 下午7:01:00
 */
public class InstanceVO {
	private long referenceId;
	private String id;
	private String name;
	private String ip;
	private Date applyTime;
	private Date createTime;
	private Date expireTime;
	private String status;
	private String task;
	private String flavorId;
	private String hostName;
	private Date runTime;// 运行时间add by dinghaibin
	private Long spare;// 剩余 add by dinghaibin
	private int osId;
	private String osName;
	private int isEnable;// 0:正常；1：手动禁用；2：到期禁用
	private String VMtype;// VM类型（试用、正式）
	private String status_buss;// VM业务状态
	private String createType;// 创建人类型
	private Integer scId;
	private String zone;// 虚拟机所属域
	private String SysUser;// 操作系统用户名
	private List<VmOsBean> osList = new ArrayList<VmOsBean>();
	private int cpu_core;// CPU核数
	private int memory_size;// 内存大小
	private int disk_capacity;// 硬盘大小
	private Long instanceId;// instanceId
	private String comments;
	private Integer buyType;

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Date getRunTime() {
		return runTime;
	}

	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}

	public Long getSpare() {
		return spare;
	}

	public void setSpare(Long spare) {
		this.spare = spare;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getVMtype() {
		return VMtype;
	}

	public void setVMtype(String vMtype) {
		VMtype = vMtype;
	}

	public String getStatus_buss() {
		return status_buss;
	}

	public void setStatus_buss(String status_buss) {
		this.status_buss = status_buss;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public Integer getScId() {
		return scId;
	}

	public void setScId(Integer scId) {
		this.scId = scId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getSysUser() {
		return SysUser;
	}

	public void setSysUser(String sysUser) {
		SysUser = sysUser;
	}

	public List<VmOsBean> getOsList() {
		return osList;
	}

	public void setOsList(List<VmOsBean> osList) {
		this.osList = osList;
	}

	public int getCpu_core() {
		return cpu_core;
	}

	public void setCpu_core(int cpu_core) {
		this.cpu_core = cpu_core;
	}

	public int getMemory_size() {
		return memory_size;
	}

	public void setMemory_size(int memory_size) {
		this.memory_size = memory_size;
	}

	public int getDisk_capacity() {
		return disk_capacity;
	}

	public void setDisk_capacity(int disk_capacity) {
		this.disk_capacity = disk_capacity;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getBuyType() {
		return buyType;
	}

	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}

	@Override
	public String toString() {
		return "InstanceVO [referenceId=" + referenceId + ", id=" + id
				+ ", name=" + name + ", ip=" + ip + ", applyTime=" + applyTime
				+ ", createTime=" + createTime + ", expireTime=" + expireTime
				+ ", status=" + status + ", task=" + task + ", flavorId="
				+ flavorId + ", hostName=" + hostName + ", runTime=" + runTime
				+ ", spare=" + spare + ", osId=" + osId + ", osName=" + osName
				+ ", isEnable=" + isEnable + ", VMtype=" + VMtype
				+ ", status_buss=" + status_buss + ", createType=" + createType
				+ ", scId=" + scId + ", zone=" + zone + ", SysUser=" + SysUser
				+ ", osList=" + osList + ", cpu_core=" + cpu_core
				+ ", memory_size=" + memory_size + ", disk_capacity="
				+ disk_capacity + ", instanceId=" + instanceId + "]";
	}

}