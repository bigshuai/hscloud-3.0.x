package org.openstack.model.compute.nova;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class NovaInstanceThin implements Serializable {

	@JsonProperty("vm_state")
	private String vmState;

	@JsonProperty("availability_zone")
	private String availabilityZone;

	@JsonProperty("terminated_at")
	private String terminatedAt;

	@JsonProperty("ramdisk_id")
	private String ramdiskId;

	@JsonProperty("instance_type_id")
	private String instanceTypeId;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("vm_mode")
	private String vmMode;

	@JsonProperty("deleted_at")
	private String deletedAt;

	@JsonProperty("reservation_id")
	private String reservationId;

	@JsonProperty("id")
	private int id;

	@JsonProperty("disable_terminate")
	private boolean disable_terminate;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("server_name")
	private String serverName;

	@JsonProperty("default_swap_device")
	private String defaultSwapDevice;

	@JsonProperty("fixed_ip")
	private String fixed_ip;

	@JsonProperty("hostname")
	private String hostname;

	@JsonProperty("launched_on")
	private String launchedOn;

	@JsonProperty("display_description")
	private String displayDescription;

	@JsonProperty("key_data")
	private String keyData;

	@JsonProperty("deleted")
	private boolean deleted;

	@JsonProperty("power_state")
	private int powerState;

	@JsonProperty("default_ephemeral_device")
	private String defaultEphemeralDevice;

	@JsonProperty("progress")
	private int progress;

	@JsonProperty("project_id")
	private String projectId;

	@JsonProperty("launched_at")
	private String launchedAt;

	@JsonProperty("scheduled_at")
	private String scheduledAt;

	@JsonProperty("ephemeral_gb")
	private String ephemeralGb;

	@JsonProperty("access_ip_v6")
	private String accessIpV6;

	@JsonProperty("access_ip_v4")
	private String accessIpV4;

	@JsonProperty("kernel_id")
	private String kernel_id;

	@JsonProperty("key_name")
	private String keyname;

	@JsonProperty("floating_ip")
	private String floatingIp;

	@JsonProperty("user_data")
	private String userData;

	@JsonProperty("host")
	private String host;

	@JsonProperty("architecture")
	private String architecture;

	@JsonProperty("display_name")
	private String displayName;

	@JsonProperty("task_state")
	private String taskState;

	@JsonProperty("shutdown_terminate")
	private boolean shutdownTerminate;

	@JsonProperty("cell_name")
	private String cellName;

	@JsonProperty("root_gb")
	private int rootGb;

	@JsonProperty("locked")
	private boolean locked;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("launch_index")
	private String launchIndex;

	@JsonProperty("memory_mb")
	private int memoryMb;

	@JsonProperty("vcpus")
	private int vcpus;

	@JsonProperty("image_ref")
	private String image_ref;

	@JsonProperty("root_device_name")
	private String rootDeviceName;

	@JsonProperty("auto_disk_config")
	private String autoDiskConfig;

	@JsonProperty("os_type")
	private String osType;

	@JsonProperty("config_drive")
	private String configDrive;

	public String getVmState() {
		return vmState;
	}

	public void setVmState(String vmState) {
		this.vmState = vmState;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String getTerminatedAt() {
		return terminatedAt;
	}

	public void setTerminatedAt(String terminatedAt) {
		this.terminatedAt = terminatedAt;
	}

	public String getRamdiskId() {
		return ramdiskId;
	}

	public void setRamdiskId(String ramdiskId) {
		this.ramdiskId = ramdiskId;
	}

	public String getInstanceTypeId() {
		return instanceTypeId;
	}

	public void setInstanceTypeId(String instanceTypeId) {
		this.instanceTypeId = instanceTypeId;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getVmMode() {
		return vmMode;
	}

	public void setVmMode(String vmMode) {
		this.vmMode = vmMode;
	}

	public String getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDisable_terminate() {
		return disable_terminate;
	}

	public void setDisable_terminate(boolean disable_terminate) {
		this.disable_terminate = disable_terminate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDefaultSwapDevice() {
		return defaultSwapDevice;
	}

	public void setDefaultSwapDevice(String defaultSwapDevice) {
		this.defaultSwapDevice = defaultSwapDevice;
	}

	public String getFixed_ip() {
		return fixed_ip;
	}

	public void setFixed_ip(String fixed_ip) {
		this.fixed_ip = fixed_ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getLaunchedOn() {
		return launchedOn;
	}

	public void setLaunchedOn(String launchedOn) {
		this.launchedOn = launchedOn;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public String getKeyData() {
		return keyData;
	}

	public void setKeyData(String keyData) {
		this.keyData = keyData;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getPowerState() {
		return powerState;
	}

	public void setPowerState(int powerState) {
		this.powerState = powerState;
	}

	public String getDefaultEphemeralDevice() {
		return defaultEphemeralDevice;
	}

	public void setDefaultEphemeralDevice(String defaultEphemeralDevice) {
		this.defaultEphemeralDevice = defaultEphemeralDevice;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getLaunchedAt() {
		return launchedAt;
	}

	public void setLaunchedAt(String launchedAt) {
		this.launchedAt = launchedAt;
	}

	public String getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(String scheduledAt) {
		this.scheduledAt = scheduledAt;
	}

	public String getEphemeralGb() {
		return ephemeralGb;
	}

	public void setEphemeralGb(String ephemeralGb) {
		this.ephemeralGb = ephemeralGb;
	}

	public String getAccessIpV6() {
		return accessIpV6;
	}

	public void setAccessIpV6(String accessIpV6) {
		this.accessIpV6 = accessIpV6;
	}

	public String getAccessIpV4() {
		return accessIpV4;
	}

	public void setAccessIpV4(String accessIpV4) {
		this.accessIpV4 = accessIpV4;
	}

	public String getKernel_id() {
		return kernel_id;
	}

	public void setKernel_id(String kernel_id) {
		this.kernel_id = kernel_id;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	public String getFloatingIp() {
		return floatingIp;
	}

	public void setFloatingIp(String floatingIp) {
		this.floatingIp = floatingIp;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public boolean isShutdownTerminate() {
		return shutdownTerminate;
	}

	public void setShutdownTerminate(boolean shutdownTerminate) {
		this.shutdownTerminate = shutdownTerminate;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public int getRootGb() {
		return rootGb;
	}

	public void setRootGb(int rootGb) {
		this.rootGb = rootGb;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLaunchIndex() {
		return launchIndex;
	}

	public void setLaunchIndex(String launchIndex) {
		this.launchIndex = launchIndex;
	}

	public int getMemoryMb() {
		return memoryMb;
	}

	public void setMemoryMb(int memoryMb) {
		this.memoryMb = memoryMb;
	}

	public int getVcpus() {
		return vcpus;
	}

	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}

	public String getImage_ref() {
		return image_ref;
	}

	public void setImage_ref(String image_ref) {
		this.image_ref = image_ref;
	}

	public String getRootDeviceName() {
		return rootDeviceName;
	}

	public void setRootDeviceName(String rootDeviceName) {
		this.rootDeviceName = rootDeviceName;
	}

	public String getAutoDiskConfig() {
		return autoDiskConfig;
	}

	public void setAutoDiskConfig(String autoDiskConfig) {
		this.autoDiskConfig = autoDiskConfig;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getConfigDrive() {
		return configDrive;
	}

	public void setConfigDrive(String configDrive) {
		this.configDrive = configDrive;
	}

	@Override
	public String toString() {
		return "NovaInstanceThin [vmState=" + vmState + ", availabilityZone="
				+ availabilityZone + ", terminatedAt=" + terminatedAt
				+ ", ramdiskId=" + ramdiskId + ", instanceTypeId="
				+ instanceTypeId + ", updatedAt=" + updatedAt + ", vmMode="
				+ vmMode + ", deletedAt=" + deletedAt + ", reservationId="
				+ reservationId + ", id=" + id + ", disable_terminate="
				+ disable_terminate + ", userId=" + userId + ", uuid=" + uuid
				+ ", serverName=" + serverName + ", defaultSwapDevice="
				+ defaultSwapDevice + ", fixed_ip=" + fixed_ip + ", hostname="
				+ hostname + ", launchedOn=" + launchedOn
				+ ", displayDescription=" + displayDescription + ", keyData="
				+ keyData + ", deleted=" + deleted + ", powerState="
				+ powerState + ", defaultEphemeralDevice="
				+ defaultEphemeralDevice + ", progress=" + progress
				+ ", projectId=" + projectId + ", launchedAt=" + launchedAt
				+ ", scheduledAt=" + scheduledAt + ", ephemeralGb="
				+ ephemeralGb + ", accessIpV6=" + accessIpV6 + ", accessIpV4="
				+ accessIpV4 + ", kernel_id=" + kernel_id + ", keyname="
				+ keyname + ", floatingIp=" + floatingIp + ", userData="
				+ userData + ", host=" + host + ", architecture="
				+ architecture + ", displayName=" + displayName
				+ ", taskState=" + taskState + ", shutdownTerminate="
				+ shutdownTerminate + ", cellName=" + cellName + ", rootGb="
				+ rootGb + ", locked=" + locked + ", createdAt=" + createdAt
				+ ", launchIndex=" + launchIndex + ", memoryMb=" + memoryMb
				+ ", vcpus=" + vcpus + ", image_ref=" + image_ref
				+ ", rootDeviceName=" + rootDeviceName + ", autoDiskConfig="
				+ autoDiskConfig + ", osType=" + osType + ", configDrive="
				+ configDrive + "]";
	}

}
