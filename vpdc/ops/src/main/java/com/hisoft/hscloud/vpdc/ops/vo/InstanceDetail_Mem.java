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
public class InstanceDetail_Mem {
	private int memSize;
	private String memName;
	
	/**
	 * @return memSize : return the property memSize.
	 */
	public int getMemSize() {
		return memSize;
	}
	/**
	 * @param memSize : set the property memSize.
	 */
	public void setMemSize(int memSize) {
		this.memSize = memSize;
	}
	/**
	 * @return memName : return the property memName.
	 */
	public String getMemName() {
		return memName;
	}
	/**
	 * @param memName : set the property memName.
	 */
	public void setMemName(String memName) {
		this.memName = memName;
	}

}
