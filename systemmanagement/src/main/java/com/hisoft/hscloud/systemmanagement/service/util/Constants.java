/**
 * @title Constant.java
 * @package com.hisoft.hscloud.systemmanagement.service.util
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-16 下午3:39:09
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.service.util;

import java.util.HashMap;
import java.util.Map;

import com.hisoft.hscloud.systemmanagement.service.HscloudThread;
import com.hisoft.hscloud.systemmanagement.service.vo.HscloudThreadVO;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-16 下午3:39:09
 */
public class Constants {
	public static Map<String, HscloudThreadVO> HSCLOUD_THREAD_VO_MAP = new HashMap<String, HscloudThreadVO>();
	public static Map<String, HscloudThread> HSCLOUD_THREAD_MAP = new HashMap<String, HscloudThread>();
	public static int HSCLOUD_THREAD_INIT = 1; //需要随系统启动
	public static int HSCLOUD_THREAD_NOTINIT = 0; //不需要随系统启动
	public static int HSCLOUD_THREAD_RUNING = 1; //正在运行
	public static int HSCLOUD_THREAD_CLOSEING = 0; //已经关闭
	public static String CRONEXPRESSION = "0 0/30 * * * ?";//每半个小时执行一次
}
