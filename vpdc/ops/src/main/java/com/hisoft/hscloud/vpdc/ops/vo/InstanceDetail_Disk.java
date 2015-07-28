/**
 * @title InstanceDetailVo.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-5 下午3:00:13
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author haibin.ding
 * @update 2012-9-2 下午2:43:13
 */
public class InstanceDetail_Disk {
	private int diskSize;
	private String diskName;
	
	/**
	 * @return diskSize : return the property diskSize.
	 */
	public int getDiskSize() {
		return diskSize;
	}
	/**
	 * @param diskSize : set the property diskSize.
	 */
	public void setDiskSize(int diskSize) {
		this.diskSize = diskSize;
	}
	/**
	 * @return diskName : return the property diskName.
	 */
	public String getDiskName() {
		return diskName;
	}
	/**
	 * @param diskName : set the property diskName.
	 */
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}

}
