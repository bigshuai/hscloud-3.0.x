/**
 * @title TextMessageListener.java
 * @package com.hisoft.hscloud.common.message
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-4-27 上午10:49:30
 * @version V1.0
 */
package com.hisoft.hscloud.common.message;
/**
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
**/
/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-27 上午10:49:30
 */
public class TextMessageListener {//implements MessageListener {
	public TextMessageListener() {
	}
/**
	public void onMessage(Message m) {

		TextMessage msg = (TextMessage) m;
		try {
			System.out.println("Async reading message: " + msg.getText()
					+ " (priority=" + msg.getJMSPriority() + ")");
		} catch (JMSException e) {
			System.out.println("Exception in onMessage(): " + e.toString());
		}
	}
**/
}
