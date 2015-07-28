package com.hisoft.hscloud.common.entity; 
//1：启动
//2：重启
//3：关闭
//4：启用
//5：禁用
//6：备份
//7：还原
//8：重置系统
//9:重置密码
//10:系统修复
//11:路由绑定LanNetwork
public enum LogOPSType {
	UNKNOWN((short) 0, "UNKNOWN_OPS_TYPE", "opsType.unknown"),
	START((short) 1, "START", "opsType.start"),
	REBOOT((short) 2, "REBOOT","opsType.reboot"),
	SHUTDOWN((short) 3, "SHUTDOWN", "opsType.shutdown"),
	ENABLE((short) 4, "ENABLE", "opsType.enable"),
	DISABLE((short) 5,"DISABLE", "opsType.disable"),
	BACKUP((short) 6,"BACKUP", "opsType.backup"),
	RESTORE((short) 7,"RESTORE", "opsType.restore"),
	REBUILD((short) 8,"REBUILD", "opsType.rebuild"),
	RESETPASSWD((short) 9,"RESETPASSWD", "opsType.resetpasswd"),
	REPAIR((short) 10,"REPAIR", "opsType.repair"),
	BINDLAN((short) 11,"BINDLAN", "opsType.repair");
	private short index;
	private String name;
	private String i18n;

	LogOPSType(short index, String name, String i18n) {
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

	public static LogOPSType getItem(short index) {
		for (LogOPSType item : values()) {
			if (item.getIndex() == index) {
				return item;
			}
		}
		return LogOPSType.UNKNOWN;
	}

}
