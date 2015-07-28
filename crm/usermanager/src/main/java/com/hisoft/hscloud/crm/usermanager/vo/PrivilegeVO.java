/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihonglei
 *
 */
public class PrivilegeVO {
	private Long resourceId;
	private String name;
	
	private List<CheckboxVO> checkboxVOList = new ArrayList<CheckboxVO>();

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CheckboxVO> getCheckboxVOList() {
		return checkboxVOList;
	}

	public void setCheckboxVOList(List<CheckboxVO> checkboxVOList) {
		this.checkboxVOList = checkboxVOList;
	}
}
