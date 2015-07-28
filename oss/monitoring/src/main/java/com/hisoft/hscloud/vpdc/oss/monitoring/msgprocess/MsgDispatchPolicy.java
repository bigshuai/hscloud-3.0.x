/**
 * @title MsgDispatchPolicy.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午10:31:17
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午10:31:17
 */
public class MsgDispatchPolicy {

	Map policies = new HashMap<String, MsgProcesser>();

	public Map<String, MsgProcesser> getPolicies() {
		return policies;
	}

	public void setPolicies(Map policies) {
		this.policies = policies;
	}

}
