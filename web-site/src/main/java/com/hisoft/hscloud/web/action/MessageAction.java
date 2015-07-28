/**
 * 
 */
package com.hisoft.hscloud.web.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * @author lihonglei
 *
 */
public class MessageAction extends HSCloudAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3264610970565590361L;
	
	private Logger logger = Logger.getLogger(MessageAction.class);
	
	@Autowired
	private Facade facade;
	
	private String dateFrom;
	private String dateTo;
	private int pageNo;
	private Long id;
	private String order;
	private int limit;
	private Long userId;
	
	private Page<Message> pageMessage = new Page<Message>(Constants.PAGE_NUM);
	/**
	 * 查询未读消息数 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void findUnreadCount() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnreadCount method.");			
		}
		try{
			User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			long count = facade.findUnreadCount(user.getId());
			fillActionResult(count);
		} catch(Exception ex) {
			dealThrow(new HsCloudException(Constants.MESSAGE_FIND_EXCEPTION,
					"findUnreadCount exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnreadCount method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 查询未读消息 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void findUnreadMessage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findUnreadMessage method.");			
		}
		try{
			User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			pageMessage.setPageNo(pageNo);
			pageMessage.setOrder(order);
			pageMessage.setOrderBy("id");
			pageMessage.setPageSize(limit);
			facade.findUnreadMessage(pageMessage, user.getId(), getCondition());
			fillActionResult(pageMessage);
		} catch(Exception ex) {
			dealThrow(new HsCloudException(Constants.MESSAGE_FIND_EXCEPTION,
					"findUnreadMessage exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findUnreadMessage method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 查询已读消息 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void findReadedMessage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findReadedMessage method.");			
		}
		try{
			User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			pageMessage.setPageNo(pageNo);
			pageMessage.setOrder(order);
			pageMessage.setOrderBy("id");
			pageMessage.setPageSize(limit);
			facade.findReadedMessage(pageMessage, user.getId(), getCondition());
			fillActionResult(pageMessage);
		} catch(Exception ex) {
			dealThrow(new HsCloudException(Constants.MESSAGE_FIND_EXCEPTION,
					"findReadedMessage exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findReadedMessage method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 修改消息状态 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void modifyMessageStatus() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyMessageStatus method.");			
		}
		User user=null;
		String operateObject = "Message[messageId:" + id + "]";
		try{
		    user = (User) super.getCurrentLoginUser();
		    facade.modifyMessageStatus(id);
		    facade.insertOperationLog(user,"修改消息状态","修改消息状态",Constants.RESULT_SUCESS,operateObject);
		} catch(Exception ex) {
		    facade.insertOperationLog(user,"修改消息状态错误:"+ex.getMessage(),"修改消息状态",Constants.RESULT_FAILURE,operateObject);
		    dealThrow(new HsCloudException(Constants.MESSAGE_EDIT_STATUS_EXCEPTION,
					"modifyMessageStatus exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyMessageStatus method.takeTime:" + takeTime + "ms");
		}
	}
	
	public void modifyAllMessageStatus(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
		}
		User user = (User)super.getCurrentLoginUser();
		String operateObject = "Message[全部消息]";
		try{
			facade.modifyAllMessageStatus(user.getId());
			facade.insertOperationLog(user, "修改全部消息状态", "修改全部消息状态", Constants.RESULT_SUCESS,operateObject);
			
		} catch(Exception ex){
			facade.insertOperationLog(user, "修改全部消息状态错误", "修改全部消息状态错误", Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException(Constants.MESSAGE_EDIT_STATUS_EXCEPTION,
					"modifyMessageStatus exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit modifyMessageStatus method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 删除消息
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void deleteMessage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteMessage method.");			
		}
		try{
			facade.deleteMessage(id);
		} catch(Exception ex) {
			dealThrow(new HsCloudException(Constants.MESSAGE_DELETE_EXCEPTION,
					"deleteMessage exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteMessage method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 删除全部已读消息
	 */
	public void deleteAllMessage(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteAllMessage method");
		}
		User user = (User)super.getCurrentLoginUser();
		try{
			facade.deleteAllMessage(user.getId());
			
		}catch(Exception ex){
			dealThrow(new HsCloudException(Constants.MESSAGE_DELETE_EXCEPTION,
					"deleteAllMessage exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteMessage method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 查询时间处理 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	private Map<String, Object> getCondition() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getCondition method.");			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		DateFormat df = null;
		try{
			if (StringUtils.isNotBlank(dateFrom)) {
				if (this.dateFrom.matches("\\d{2}/\\d{2}/\\d{4}")) {
					df = new SimpleDateFormat("MM/dd/yyyy");
				} else if (this.dateFrom.matches("\\d{4}/\\d{2}/\\d{2}")) {
					df = new SimpleDateFormat("yyyy/MM/dd");
				}
				map.put("dateFrom", df.parse(dateFrom));
			}
			if (StringUtils.isNotBlank(dateTo)) {
				if (this.dateTo.matches("\\d{2}/\\d{2}/\\d{4}")) {
					df = new SimpleDateFormat("MM/dd/yyyy");
				} else if (this.dateTo.matches("\\d{4}/\\d{2}/\\d{2}")) {
					df = new SimpleDateFormat("yyyy/MM/dd");
				}
				Date tempDate = df.parse(dateTo);
				tempDate.setTime(tempDate.getTime() + 1000 * 60 * 60 * 24 - 1);
				map.put("dateTo", tempDate);
			}
		} catch(Exception ex) {
			dealThrow(new HsCloudException(Constants.MESSAGE_FIND_EXCEPTION,
					"getCondition exception", logger, ex), Constants.MESSAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getCondition method.takeTime:" + takeTime + "ms");
		}
		return map;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
}
