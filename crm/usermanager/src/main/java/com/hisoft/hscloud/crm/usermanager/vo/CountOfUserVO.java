/**
 * @title CountOfUserVO.java
 * @package com.hisoft.hscloud.crm.usermanager.vo
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-18 下午4:53:04
 * @version V1.1
 */
package com.hisoft.hscloud.crm.usermanager.vo;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.1
 * @author guole.liang
 * @update 2012-5-18 下午4:53:04
 */
public class CountOfUserVO {
	private long countOfEntUser=0L;
	
	private long countOfNorUser=0L;
	
	private long countOfOnlineEntUser=0L;
	
	private long countOfOnlineNorUser=0L;
	
    public CountOfUserVO(){
    	
    }
    public CountOfUserVO(long countOfEntUser,long countOfNorUser,long countOfOnlineEntUser,long countOfOnlineNorUser){
    	this.countOfEntUser=countOfEntUser;
    	this.countOfNorUser=countOfNorUser;
    	this.countOfOnlineEntUser=countOfOnlineEntUser;
    	this.countOfOnlineNorUser=countOfOnlineNorUser;
    }
	public long getCountOfEntUser() {
		return countOfEntUser;
	}

	@Override
	public String toString() {
		return "CountOfUserVO [countOfEntUser=" + countOfEntUser
				+ ", countOfNorUser=" + countOfNorUser + "]";
	}

	public long getCountOfOnlineEntUser() {
		return countOfOnlineEntUser;
	}

	public void setCountOfOnlineEntUser(long countOfOnlineEntUser) {
		this.countOfOnlineEntUser = countOfOnlineEntUser;
	}

	public long getCountOfOnlineNorUser() {
		return countOfOnlineNorUser;
	}

	public void setCountOfOnlineNorUser(long countOfOnlineNorUser) {
		this.countOfOnlineNorUser = countOfOnlineNorUser;
	}

	public void setCountOfEntUser(long countOfEntUser) {
		this.countOfEntUser = countOfEntUser;
	}

	public long getCountOfNorUser() {
		return countOfNorUser;
	}

	public void setCountOfNorUser(long countOfNorUser) {
		this.countOfNorUser = countOfNorUser;
	}
	
	

}
