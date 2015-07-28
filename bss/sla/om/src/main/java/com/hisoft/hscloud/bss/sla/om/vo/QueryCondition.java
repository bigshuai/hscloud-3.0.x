/**
 * @title QueryCondition.java
 * @package com.hisoft.hscloud.bss.sla.om.vo
 * @description 
 * @author YuezhouLi
 * @update 2012-8-17 上午11:01:24
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-8-17 上午11:01:24
 */
public class QueryCondition {

	private Date fromDate;
	private Date toDate;
	private Order order;
	private String query;
	private User user;
	private Page page;
	private List<Sort> sort;
	private Page<Order> pages;

	public Page<Order> getPages() {
		return pages;
	}

	public void setPages(Page<Order> pages) {
		this.pages = pages;
	}

	public List<Sort> getSort() {
		if (sort == null) {
			sort = new ArrayList<Sort>();
		}
		return sort;
	}

	public void setSort(List<Sort> sort) {
		this.sort = sort;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
