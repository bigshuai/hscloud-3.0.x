package com.hisoft.hscloud.vpdc.ops.vo;

import com.hisoft.hscloud.crm.usermanager.entity.User;

/**
 * @className: SecurityPolicyVO
 * @package: com.hisoft.hscloud.vpdc.ops.vo
 * @description: 安全策略封装的VO
 * @author: liyunhui
 * @createTime: Aug 26, 2013 1:28:51 PM
 * @company: Pactera Technology International Ltd
 */
public class SecurityPolicyVO {

	// 内网安全提交的所有的虚拟机的instanceId
	private String submit_instanceIds;
	// 内网安全提交的所有的虚拟机的uuid
	private String submit_uuids;
	// 原来的数据库中加载的内网安全的所有的虚拟机的instanceId
	private String old_instanceIds;
	// 原来的数据库中加载的内网安全的所有的虚拟机的uuid
	private String old_uuids;
	// 提交的 protocal-portFrom-portTo,protocal-portFrom-portTo 的String数据
	private String extranet_submit_data;
	// 原来的 protocal-portFrom-portTo,protocal-portFrom-portTo 的String数据
	private String extranet_old_data;
	// 选中的这台虚拟机的uuid
	private String uuid;
	// 选中的这台虚拟机的instanceId
	private String instanceId;
	// 选中的这台虚拟机所属内网安全组的groupId
	private String groupId;
	private User user;

	public String getSubmit_instanceIds() {
		return submit_instanceIds;
	}

	public void setSubmit_instanceIds(String submit_instanceIds) {
		this.submit_instanceIds = submit_instanceIds;
	}

	public String getSubmit_uuids() {
		return submit_uuids;
	}

	public void setSubmit_uuids(String submit_uuids) {
		this.submit_uuids = submit_uuids;
	}

	public String getOld_instanceIds() {
		return old_instanceIds;
	}

	public void setOld_instanceIds(String old_instanceIds) {
		this.old_instanceIds = old_instanceIds;
	}

	public String getOld_uuids() {
		return old_uuids;
	}

	public void setOld_uuids(String old_uuids) {
		this.old_uuids = old_uuids;
	}

	public String getExtranet_submit_data() {
		return extranet_submit_data;
	}

	public void setExtranet_submit_data(String extranet_submit_data) {
		this.extranet_submit_data = extranet_submit_data;
	}

	public String getExtranet_old_data() {
		return extranet_old_data;
	}

	public void setExtranet_old_data(String extranet_old_data) {
		this.extranet_old_data = extranet_old_data;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SecurityPolicyVO(String submit_instanceIds, String submit_uuids,
			String old_instanceIds, String old_uuids,
			String extranet_submit_data, String extranet_old_data, String uuid,
			String instanceId, String groupId, User user) {
		super();
		this.submit_instanceIds = submit_instanceIds;
		this.submit_uuids = submit_uuids;
		this.old_instanceIds = old_instanceIds;
		this.old_uuids = old_uuids;
		this.extranet_submit_data = extranet_submit_data;
		this.extranet_old_data = extranet_old_data;
		this.uuid = uuid;
		this.instanceId = instanceId;
		this.groupId = groupId;
		this.user = user;
	}

	@Override
	public String toString() {
		return "SecurityPolicyVO [submit_instanceIds=" + submit_instanceIds
				+ ", submit_uuids=" + submit_uuids + ", old_instanceIds="
				+ old_instanceIds + ", old_uuids=" + old_uuids
				+ ", extranet_submit_data=" + extranet_submit_data
				+ ", extranet_old_data=" + extranet_old_data + ", uuid=" + uuid
				+ ", instanceId=" + instanceId + ", groupId=" + groupId
				+ ", user=" + user + "]";
	}

}