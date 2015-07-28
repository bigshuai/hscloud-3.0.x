package com.hisoft.hscloud.payment.alipay.util;

public enum TradeStatus {
	TRADE_CREATE((short) 1, "TRADE_CREATE"), TRADE_FINISHED((short) 2,
			"TRADE_FINISHED"), TRADE_FAILURE((short) 3, "TRADE_FAILURE"), UNKNOWN_STATUS(
			(short) 4, "UNKNOWN_STATUS");
	private short index;
	private String status;

	private TradeStatus(short index, String status) {
		this.index = index;
		this.status = status;
	}
	public static TradeStatus getByIndex(short index){
		for(TradeStatus status:TradeStatus.values()){
			if(status.index==index){
				return status;
			}
		}
		return TradeStatus.UNKNOWN_STATUS;
	}
	
	public static TradeStatus getByStatus(String status){
		return valueOf(status);
	}
	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
