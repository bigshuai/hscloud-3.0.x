package com.hisoft.hscloud.common.vo; 

public class OperatorValue {
	private Enum operator;
	private Object value;
	public Enum getOperator() {
		return operator;
	}
	public void setOperator(Enum operator) {
		this.operator = operator;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
