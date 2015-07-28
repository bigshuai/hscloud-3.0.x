/**
 * @title BillVO.java
 * @package com.hisoft.hscloud.bss.billing.vo
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-3-31 下午5:17:03
 * @version V1.0
 */
package com.hisoft.hscloud.bss.billing.vo;

import java.util.Date;

/**
 * @description 用于查询用户的账单
 * @version 1.0
 * @author guole.liang
 * @update 2012-3-31 下午5:17:03
 */
public class BillVO {
	private Date beginDate;
	private Date endDate;
	private Long account_id;
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}
	
	
}
