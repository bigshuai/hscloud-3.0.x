package com.hisoft.hscloud.bss.billing.constant;

/**
 * CONSUME:消费 1
 * DEPOSIT:存款 2
 * REFUND:退款 3
 * DRAW:提款 4
 * CANCEL:撤消 5
 * REFUND_ALL:全额退款 6
 * ROLL_IN:转入11
 * ROLL_OUT:转出12
 * @author Minggang
 *
 */
public enum TranscationType {
	
	UNKNOW_TRANSCATION((short)0,"UNKNOW_TRANSCATION","transcation.unknow"),
	TRANSCATION_CONSUME((short)1,"TRANSCATION_CONSUME","transcation.consume"),
	TRANSCATION_DEPOSIT((short)2,"TRANSCATION_DEPOSIT","transcation.deposit"),
	TRANSCATION_REFUND((short)3,"TRANSCATION_REFUND","transcation.refund"),
	TRANSCATION_DRAW((short)4,"TRANSCATION_DRAW","transcation.draw"),
	TRANSCATION_CANCEL((short)5,"TRANSCATION_CANCEL","transcation.cancel"),
	TRANSCATION_REFUND_ALL((short)6,"TRANSCATION_REFUND_ALL","transcation.refundAll"),
	TRANSCATION_COUPONS_DEPOSIT((short)7,"TRANSCATION_COUPONS_DEPOSIT","transcation.coupons.deposit"),
	TRANSCATION_COUPONS_DRAW((short)8,"TRANSCATION_COUPONS_DRAW","transcation.coupons.draw"),
	TRANSCATION_GIFTS_DEPOSIT((short)9,"TRANSCATION_GIFTS_DEPOSIT","transcation.gifts.deposit"),
	TRANSCATION_GIFTS_DRAW((short)10,"TRANSCATION_GIFTS_DRAW","transcation.gifts.draw"),
	TRANSCATION_ROLLIN((short)11,"TRANSCATION_ROLLIN","transcation.rollin"),
	TRANSCATION_ROLLOUT((short)12,"TRANSCATION_ROLLOUT","transcation.rollout");
	
	private short index;
	
	private String type;
	
	private String i18n;
	
	
	TranscationType(short index,String type,String i18n){
		
		this.index = index;
		this.type = type;
		this.i18n = i18n;
		
	}


	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	
	

}
