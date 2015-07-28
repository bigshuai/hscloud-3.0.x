/**
 * @title Dispatcher.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午10:09:37
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.BasicMsgBean;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午10:09:37
 */
public class MsgDispatcher {

	private static Gson gson = new  Gson();
	
	public static void dispatche(String msg,MsgDispatchPolicy policy){
		if(msg != null){
			BasicMsgBean basicMsgBean = gson.fromJson(msg, BasicMsgBean.class);
			String format = basicMsgBean.getFormat();
			String type = basicMsgBean.getType();
			Date date = basicMsgBean.getTimestamp();
			Map<String,MsgProcesser> policies = policy.getPolicies();
			if(policies.containsKey(format)){
				MsgProcesser processer = policies.get(format);
				processer.process(msg);
			}
		}
	}
}
