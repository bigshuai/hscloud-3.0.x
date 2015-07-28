package com.hisoft.hscloud.common.entity;

//1：订单创建
//2：试用创建
//3：退款删除
//4：到期删除
//5：管理员创建
//6：管理员删除
//7：外网IP删除
//8：虚机Flavour调整
//9：手动迁移
//10：自动迁移
//11：新增扩展盘
//12：删除扩展盘
//13：调整扩展盘（暂不支持）
//14：发布创建
//15：回收站恢复VM
//16：回收站彻底删除VM
//17：外网IP添加
public enum LogBizType {
	UNKNOWN((short) 0, "UNKNOWN_BIZ_TYPE", "bizType.unknown"),
	ORDER_CREATE((short) 1, "ORDER_CREATE", "bizType.orderCreate"),
	TRIAL_CREATE((short) 2, "TRIAL_CREATE","bizType.trialCreate"),
	REFUND_DELETE((short) 3, "REFUND_DELETE", "bizType.refundDelete"),
	EXPIRE_DELETE((short) 4, "EXPIRE_DELETE", "bizType.expireDelete"),
	ADMIN_CREATE((short) 5,"ADMIN_CREATE", "bizType.adminCreate"),
	ADMIN_DELETE((short) 6,"ADMIN_DELETE", "bizType.adminDelete"),
	IP_DELETE((short) 7,"IP_DELETE", "bizType.ipDelete"),
	FLAVOR_ADJUST((short) 8,"FLAVOR_ADJUST", "bizType.flavorAdjust"),
	MANUAL_MIGRATE((short) 9,"MANUAL_MIGRATE", "bizType.manualMigrate"),
	AUTOMATIC_MIGRATE((short) 10,"AUTOMATIC_MIGRATE", "bizType.automaticMigrate"),
	ADD_EXTDISK((short) 11,"ADD_EXTDISK", "bizType.addExtDisk"),
	DELETE_EXTDISK((short) 12,"DELETE_EXTDISK", "bizType.deleteExtDisk"),
	ADJUST_EXTDISK((short) 13,"ADJUST_EXTDISK", "bizType.adjustExtDisk"),
	PUBLISH_CREATE((short) 14,"PUBLISH_CREATE", "bizType.publishCreate"),
	RECYCLE_RESTORE((short) 15,"RECYCLE_RESTORE", "bizType.recycleRestore"),
	RECYCLE_DELETE((short) 16,"RECYCLE_DELETE", "bizType.recycleDelete"),
	IP_ADD((short) 17,"IP_ADD", "bizType.ipAdd");

	private short index;
	private String name;
	private String i18n;

	LogBizType(short index, String name, String i18n) {
		this.index = index;
		this.name = name;
		this.i18n = i18n;
	}

	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}

	public static LogBizType getItem(short index) {
		for (LogBizType item : values()) {
			if (item.getIndex() == index) {
				return item;
			}
		}
		return LogBizType.UNKNOWN;
	}

}
