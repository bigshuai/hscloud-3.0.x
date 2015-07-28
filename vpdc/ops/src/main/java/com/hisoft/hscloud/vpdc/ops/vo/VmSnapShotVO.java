package com.hisoft.hscloud.vpdc.ops.vo;
import java.util.Date;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;

/**
 * 
 * @description VM备份---数据库层面
 * @version 1.0
 * @author dinghb
 * @update 2012-8-17 上午10:58
 */
public class VmSnapShotVO {
	private int id;
	private VpdcReference reference;
	private Date createTime;
	private String snapShot_id;
	private String snapShot_name;
	private String snapShot_comments;
	private int snapShot_type;
	private int hasExtDisk;//0:无-1：isics方式扩展盘-2：SCIS方式扩展盘-3:两种方式的扩展盘都有
	private Integer status;//备份完成标识
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public VpdcReference getReference() {
		return reference;
	}
	public void setReference(VpdcReference reference) {
		this.reference = reference;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSnapShot_id() {
		return snapShot_id;
	}
	public void setSnapShot_id(String snapShot_id) {
		this.snapShot_id = snapShot_id;
	}
	public String getSnapShot_name() {
		return snapShot_name;
	}
	public void setSnapShot_name(String snapShot_name) {
		this.snapShot_name = snapShot_name;
	}
	public String getSnapShot_comments() {
		return snapShot_comments;
	}
	public void setSnapShot_comments(String snapShot_comments) {
		this.snapShot_comments = snapShot_comments;
	}
	public int getSnapShot_type() {
		return snapShot_type;
	}
	public void setSnapShot_type(int snapShot_type) {
		this.snapShot_type = snapShot_type;
	}
	public int getHasExtDisk() {
		return hasExtDisk;
	}
	public void setHasExtDisk(int hasExtDisk) {
		this.hasExtDisk = hasExtDisk;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
