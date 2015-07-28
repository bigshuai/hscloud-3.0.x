/**
 * @title Constant.java
 * @package com.hisoft.hscloud.crm.usermanager.util
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-5-8 上午11:04:40
 * @version V1.0
 */
package com.hisoft.hscloud.crm.usermanager.util;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author guole.liang
 * @update 2012-5-8 上午11:04:40
 */
public class Constant {
	public static String NOR_USER = "NorUser"; // 普通用户
	public static String ENT_USER = "EntUser"; // 企业用户
	public static String SUB_USER = "SubUser";

	public static String USER_NAME = "userName"; // 用户模糊查询时使用，按名字模糊查询
	public static String USER_EMAIL = "userEmail"; // 用户模糊查询时使用，按邮箱模糊查询

	public static String ADMIN_NAME = "adminName"; // 管理员模糊查询时使用，按名字模糊查询
	public static String ADMIN_EMAIL = "adminEmail"; // 管理员模糊查询时使用，按邮箱模糊查询

	public static String SUBUSER_NAME = "subUserName"; // 子用户模糊查询时使用，按名字模糊查询
	public static String SUBUSER_EMAIL = "subUserEmail"; // 子用户模糊查询时使用，按邮箱模糊查询

	public static String ROLE_NAME = "roleName"; // 角色模糊查询时使用，按邮箱模糊查询

	public static String Group_NAME = "groupName";
	
	public static long NOR_USER_GROUP_ID = 1;
	public static long ENT_USER_GROUP_ID = 2 ;
	
	public static String NOR_USER_GROUP = "NorUser_";
	public static String ENT_USER_GROUP = "EntUser_";
	
	public static long FOREIGN_COUNTRY_REGION_ID= 1;
	public static String FOREIGN_COUNTRY_REGION_NAME_CH ="外国";
	public static String FOREIGN_COUNTRY_REGION_CODE ="001";


	public static String PAGE_SUBUSER_BY_GROUP = "subUserInGroup"; // 子用户分页查询时使用，查询某个用户组内的子用户
	public static String PAGE_SUBUSER_BY_USER = "subUserInUser"; // 子用户分页查询时使用，查询某个用户所创建的子用户
	public static long ROOT = -1L;// means root node
	public static String GROUP = "group";
	public static String ROLE = "role";
	public static String ADMIN = "admin";
	public static String USER = "user";
	public static String MENU = "menu";
	public static String VM = "VM";
	/*******************************************************************
	 * ERROR CODE
	 * ******************************************************************/
	// 权限表中没有数据或数据库连接问题
	public static String DATABASEERROR = "HSC00000001";

	// 系统内部错误，JSON数据格式不对，丢失消息头。
	public static String MISSINGFIELDS = "HSC00000002";
	
	//资源类型-菜单
	public static String RESOURCE_TYPE_MENU = "com.hisoft.hscloud.crm.usermanager.entity.Menu";
	//资源类型-虚拟机
	public static String RESOURCE_TYPE_VM = "com.hisoft.hscloud.vpdc.ops.entity.VpdcReference";
	//资源类型-资源
	public static String RESOURCE_TYPE_RESOURCE = "com.hisoft.hscloud.crm.usermanager.entity.Resource";
	//资源类型-资源域
	public static String RESOURCE_TYPE_ZONE = "com.hisoft.hscloud.bss.sla.sc.entity.ServerZone";
	//资源父节点名称
	public static String RESOURCE_PARENT_NAME = "ALL";
	
	//前后台均有
	public static int STATUS_ALL = 0;
	//状态类型为qiant应用
	public static int STATUS_USER = 1;
	//状态类型为后台应用
	public static int STATUS_ADMIN = 2;
	//action对象的级别
	public static int ACTION_LEVEL = 1;
	
	//菜单根节点
	public static String MENU_ROOT = "0";
	//审核通过的用户
	public static short USER_STATE_APPROVED = 3;
	
	//分平台有效状态
	public static String DOMAIN_STATUS_VALID = "1";
	
	//分平台无效状态
	public static String DOMAIN_STATUS_INVALID = "0";
}
