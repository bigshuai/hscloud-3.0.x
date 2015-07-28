package com.hisoft.hscloud.crm.usermanager.constant;

/**
 * 邀请状态
 * UNTREATED   未读（未处理）
 * ACCEPTED    接受
 * REJECTED    拒绝
 * TERMINATE   解除
 * @author Minggang
 *
 */
public enum InviteState {
	
	UNKNOW_STATE((short)0,"UNKNOW_STATE","invitestate.unknow"),
	INVITE_UNTREATED((short)1,"UNTREATED_STATE","invitestate.untreated"),
	INVITE_ACCEPTED((short)2,"ACCEPTED_STATE","invitestate.accepted"),
    INVITE_REJECTED((short)3,"REJECTED_STATE","invitestate.rejected"),
    INVITE_TERMINATE((short)4,"TERMINATE_STATE","invitestate.terminate");
	
	private short index;
	private String type;
	private String i18n;
	
	
	InviteState(short index,String type,String i18n){
		
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
