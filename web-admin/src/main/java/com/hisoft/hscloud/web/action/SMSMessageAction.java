package com.hisoft.hscloud.web.action; 

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.message.entity.SMSMessage;
import com.hisoft.hscloud.message.vo.AnnouncementVO;
import com.hisoft.hscloud.web.facade.Facade;


/**
 * @author yubenjie
 * 短信信息发送管理
 *
 */
public class SMSMessageAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = 7444751807265744225L;
    
    @Autowired
    private Facade facade;
    
    private Page<SMSMessage> smsMessagePage = new Page<SMSMessage>();
    private Logger logger = Logger.getLogger(this.getClass());
    private int page;
    private int limit;
    private String query;
    private long smsId;
    
    private SMSMessage smsMessage;
    private SMSMessage[] smsMessageArr;
    /**
     * 发送信息
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void saveSmsMessage() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveSmsMessage method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	smsMessage.setCreateDate(new Date());
            	smsMessage.setCreater(admin.getName());
                facade.saveSmsMessage(smsMessage);
                if(smsMessage.getType()==0){
                	facade.insertOperationLog(admin,"发送个人消息","发送个人消息",Constants.RESULT_SUCESS);
                }else{
                	facade.insertOperationLog(admin,"群发消息","群发消息",Constants.RESULT_SUCESS);
                }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"发送消息:"+e.getMessage(),"发送消息",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.SMSMESSAGE_SAVE_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveSmsMessage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 显示发送信息 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findSmsMessagePage() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findSmsMessagePage method.");			
		}
		smsMessagePage.setPageNo(page);
		smsMessagePage.setPageSize(limit);        
        if (StringUtils.isNotBlank(query)) {
            try {
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                this.dealThrow(Constants.SMSMESSAGE_SHOW_EXCEPTION, e, logger);
            }
        }
        try {
        	smsMessagePage = facade.findSmsMessagePage(smsMessagePage, query);
		} catch (Exception e) {
			this.dealThrow(Constants.SMSMESSAGE_SHOW_EXCEPTION, e, logger);
		}
        
        fillActionResult(smsMessagePage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findSmsMessagePage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 删除信息
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void delSmsMessage() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delSmsMessage method.");			
		}
		Admin admin=null;
        try{
            admin = (Admin) super.getCurrentLoginUser();
            facade.delSmsMessage(smsId,admin.getName());
            facade.insertOperationLog(admin,"删除短信信息","删除短信信息",Constants.RESULT_SUCESS);
        } catch(Exception e) {
            facade.insertOperationLog(admin,"删除短信信息错误:"+e.getMessage(),"删除短信信息",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.SMSMESSAGE_DEL_EXCEPTION,e, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delSmsMessage method.takeTime:" + takeTime + "ms");
		}
    }

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public SMSMessage getSmsMessage() {
		return smsMessage;
	}

	public void setSmsMessage(SMSMessage smsMessage) {
		this.smsMessage = smsMessage;
	}

	public SMSMessage[] getSmsMessageArr() {
		return smsMessageArr;
	}

	public void setSmsMessageArr(SMSMessage[] smsMessageArr) {
		this.smsMessageArr = smsMessageArr;
	}

	public Page<SMSMessage> getSmsMessagePage() {
		return smsMessagePage;
	}

	public void setSmsMessagePage(Page<SMSMessage> smsMessagePage) {
		this.smsMessagePage = smsMessagePage;
	}

	public long getSmsId() {
		return smsId;
	}

	public void setSmsId(long smsId) {
		this.smsId = smsId;
	}
}
