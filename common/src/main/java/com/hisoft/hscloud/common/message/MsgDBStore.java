package com.hisoft.hscloud.common.message;

/**
 * 
* @description 这里用一句话描述这个类的作用
* @version 1.0
* @author AaronFeng
* @update 2012-5-9 下午4:25:14
 */
 public interface MsgDBStore {	 
	/**
	 *  
	* @title: saveMessageOfBatch
	* @description 批量保存消息
	* @param message 设定文件
	* @return void    返回类型
	* @throws
	* @version 1.0
	* @author AaronFeng
	* @update 2012-5-10 上午10:33:49
	 */
	public void saveMessageOfBatch(String message);
	/**
	 * 
	* @title: saveSingleMessage
	* @description 逐条保存消息
	* @param message 设定文件
	* @return void    返回类型
	* @throws
	* @version 1.0
	* @author AaronFeng
	* @update 2012-5-10 上午10:33:16
	 */
	public void saveSingleMessage(String message);
	//public JSONObject getMessageObj(String message);

}
