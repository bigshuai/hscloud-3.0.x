package com.hisoft.hscloud.mail.constant;


public enum MailTemplateType {
	
	UNKNOW_TEMPLATE((short)0,"UNKNOW_TEMPLATE","template.unknow"),
	ACTIVE_USER_TEMPLATE((short)1,"ACTIVE_USER_TEMPLATE","template.userregister"),
	FIND_POSSWORD_TEMPLATE((short)2,"FIND_POSSWORD_TEMPLATE","template.findpassword"),
	OPEN_VM_TEMPLATE((short)3,"OPEN_VM_TEMPLATE","template.openvmtemplate"),
	TRIALVM_EXPIRE_TEMPLATE((short)4,"TRIALVM_EXPIRE_TEMPLATE","template.trialvmexpiretemplate"),
	ORDER_PAID_TEMPLATE((short)5,"ORDER_PAID_TEMPLATE","template.orderpaidtemplate"),
	APPROVED_USER_TEMPLATE((short)6,"APPROVED_USER_TEMPLATE","template.approvedtemplate"),
	ORDER_TRY_TEMPLATE((short)7,"ORDER_TRY_TEMPLATE","template.ordertrytemplate"),
	ORDER_CANCEL_TEMPLATE((short)8,"ORDER_CANCEL_TEMPLATE","template.ordercanceltemplate"),
	ORDER_TRYDELAY_TEMPLATE((short)9,"ORDER_TRYDELAY_TEMPLATE","template.ordertrydelaytemplate");
	

	
	private short index;
	private String type;
	private String i18n;
	
	
	private MailTemplateType(short index, String type, String i18n) {
		
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
	
	public static MailTemplateType getItem(short index){
		
		for (MailTemplateType mailTemplateType:MailTemplateType.values()) {
			if(mailTemplateType.index==index){
				return mailTemplateType;
			}
		}
		
		return MailTemplateType.UNKNOW_TEMPLATE;
		
	}
	
	
	
	

}
