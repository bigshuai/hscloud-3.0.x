package com.hisoft.hscloud.common.vo;

/**
 * 
 * <查询用户信息及用户品牌信息> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, Oct 8, 2013]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UserUserBrandVO {
	private String userName;
	private String userEmail;
	private String userBrandName;
	private String userBrandCode;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserBrandName() {
		return userBrandName;
	}

	public void setUserBrandName(String userBrandName) {
		this.userBrandName = userBrandName;
	}

	public String getUserBrandCode() {
		return userBrandCode;
	}

	public void setUserBrandCode(String userBrandCode) {
		this.userBrandCode = userBrandCode;
	}
}
