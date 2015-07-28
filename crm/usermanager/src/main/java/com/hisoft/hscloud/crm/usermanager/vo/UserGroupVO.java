package com.hisoft.hscloud.crm.usermanager.vo;


import com.hisoft.hscloud.common.entity.AbstractVO;

public class UserGroupVO extends AbstractVO{
	
	
	//判断是否选中
	private boolean checked = false;
	
	private boolean flag;
	
	//组中成员数
	private long members;	


	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public long getMembers() {
		return members;
	}

	public void setMembers(long members) {
		this.members = members;
	}
	
	
	

}
