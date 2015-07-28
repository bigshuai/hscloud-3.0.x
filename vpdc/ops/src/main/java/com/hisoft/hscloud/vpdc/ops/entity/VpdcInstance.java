package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "hc_vpdc_instance")
public class VpdcInstance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// snapshot集合
	@OneToMany(mappedBy = "instance", targetEntity = VmSnapShot.class, cascade = {
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@OrderBy("id desc")
	private Set<VmSnapShot> snapShots;
	
	// 虚拟机id
	@Column(name = "vm_id")
	private String vmId;
	
	// 虚拟机id
	@Column(name = "initVmId")
	private String initVmId;
	
	// 虚拟机节点
	@Column(name = "nodeName")
	private String nodeName;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "VpdcRefrenceId")
	private VpdcReference vpdcreference;

	@Column(name = "status")
	private int status;// 0:正常；1：删除/停用

	// 创建者id
	@Column(name = "create_id", nullable = false)
	private Long createId;
	// 创建时间
	@Column(name = "create_date", nullable = false)
	private Date createDate = new Date();
	// 修改人
	@Column(name = "update_Id", nullable = false)
	private Long updateId;
	// 修改时间
	@Column(name = "update_date", nullable = false)
	private Date updateDate = new Date();
	// 版本
	@Version
	@Column(name = "version", nullable = false)
	private long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return snapShots : return the property snapShots.
	 */
	public Set<VmSnapShot> getSnapShots() {
		return snapShots;
	}

	/**
	 * @param snapShots
	 *            : set the property snapShots.
	 */
	public void setSnapShots(Set<VmSnapShot> snapShots) {
		this.snapShots = snapShots;
	}

	/**
	 * @return vmId : return the property vmId.
	 */
	public String getVmId() {
		return vmId;
	}

	/**
	 * @param vmId
	 *            : set the property vmId.
	 */
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	
	public String getInitVmId() {
		return initVmId;
	}

	public void setInitVmId(String initVmId) {
		this.initVmId = initVmId;
	}

	/**
	 * @return nodeName : return the property nodeName.
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName
	 *            : set the property nodeName.
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return vpdcreference : return the property vpdcreference.
	 */

	public VpdcReference getVpdcreference() {
		return vpdcreference;
	}

	/**
	 * @param vpdcreference
	 *            : set the property vpdcreference.
	 */

	public void setVpdcreference(VpdcReference vpdcreference) {
		this.vpdcreference = vpdcreference;
	}

	/**
	 * @return status : return the property status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            : set the property status.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "VpdcInstance [id=\"" + this.id + "\",snapShots=" + snapShots + ", vmId=" + vmId
				+ ", nodeName=" + nodeName + ", vpdcreference=" + vpdcreference
				+ ", status=" + status + "createDate=\"" + this.createDate + "\",updateId=\""
				+ this.updateId + "\",updateDate=\"" + this.updateDate
				+ "\",version=" + this.version + "\",createId=\""
				+ this.createId+"]";
	}
}
