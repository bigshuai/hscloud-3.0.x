package com.hisoft.hscloud.common.vo;

import java.util.List;
import java.util.Map;
import com.hisoft.hscloud.common.util.Sort;
public class QueryCondition {
	private int pageSize;
	private int pageNo;
	private Map<String, OperatorValue> params;
	private List<Sort> sorts;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Map<String, OperatorValue> getParams() {
		return params;
	}

	public void setParams(Map<String, OperatorValue> params) {
		this.params = params;
	}

	public List<Sort> getSorts() {
		return sorts;
	}

	public void setSorts(List<Sort> sorts) {
		this.sorts = sorts;
	}

}
