/**
 * @title MsgProcesser.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午10:35:43
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午10:35:43
 */
public interface MsgProcesser {

	public void process(String msg);
}
