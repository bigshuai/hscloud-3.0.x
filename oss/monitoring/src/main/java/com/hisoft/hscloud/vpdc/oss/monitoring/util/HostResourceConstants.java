package com.hisoft.hscloud.vpdc.oss.monitoring.util;
/**
 * 
        * 此类描述的是：节点全局监控：饼图所对应的资源名称    
        * @author: hongqin.li      
        * @version: 2012-5-17 下午8:19:16
 */
public class HostResourceConstants {
	public static String CPU_USED_NAME = "used";
	public static String CPU_UNUSED_NAME = "free";
	public static String VIRTUALCPU_ASSIGN_NAME = "assigned";
	public static String VIRTUALCPU_USING_NAME = "using";
	public static String MEMORY_USED_NAME = "used";
	public static String MEMORY_UNUSED_NAME = "free";
	public static String VIRTUALMEMORY_ASSIGN_NAME = "assigned";
	public static String VIRTUALMEMORY_USING_NAME = "using";
	public static String DISK_USED_NAME = "used";
	public static String DISK_UNUSED_NAME = "free";
	public static String VIRTUALDISK_ASSIGN_NAME = "assigned";
	public static String VIRTUALDISK_USING_NAME = "using";
	public static String IP_USED_NAME = "used";
	public static String IP_UNUSED_NAME = "free";
	
	/*节点的资源名*/
	public static String HOST_CPU_NAME="cpu";
	public static String HOST_MEMORY_NAME="memory";
	
	/*存储消息的消息队列名称：hs_R_K_app_monitor*/
	
	public static String RECEIVE_MESSAGE_QUEUENAME="hs_Q_app_monitor";
	
	public static int RECEIVE_MESSAGE_TIME=1000;
	
	public static int PERIOD=5;//单位为秒，opensStack 向rabbitMQ中发送消息的周期
	
	public static long CACHE_TIMEOUT = 60*1000;
	
	public static String VM_STATUS_ACTIVE="active";
	public static String VM_STATUS_ERROR="error";
	public static String VM_STATUS_SUSPEND="suspended";
	
//	public static int VM_STATUS_RUNNING=1;
//	public static int VM_STATUS_BLOCKED=2;
//	public static int VM_STATUS_PAUSED=3;
//	public static int VM_STATUS_SHUT_DOWN=4;
//	public static int VM_STATUS_SHUT_OFF=5;
//	public static int VM_STATUS_CRASHED=6;
			
	public static String HOST_STATUS_ACTIVE="active";
	public static String HOST_STATUS_SUSPEND="suspend";
	public static String HOST_VM_STATUS_WORK="work";
	public static String HOST_VM_STATUS_UNWORK="unWork";
	
	public static String 	CPU_REMAIN="个";
	
	public static String MEMORY_REMAIN="G";
}
