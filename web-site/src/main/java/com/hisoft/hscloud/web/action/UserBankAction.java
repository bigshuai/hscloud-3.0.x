/**
 * 
 */
package com.hisoft.hscloud.web.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserBank;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * @author lihonglei
 *
 */
public class UserBankAction extends HSCloudAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7343662498659526690L;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Facade facade;
	
	private UserBank userBank = new UserBank();
	
	/**
	 * 保存用户银行信息
	 */
	public void saveUserBank() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveUserBank method.");			
		}
		User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		try {
			userBank.setBank(new String(userBank.getBank().getBytes("iso8859_1"), "UTF-8"));
			userBank.setPayee(new String(userBank.getPayee().getBytes("iso8859_1"), "UTF-8"));
			userBank.setUserId(user.getId());
			facade.saveUserBank(userBank);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.USER_BANK_SAVE_EXCEPTION,
					"saveUserBank", logger, e), Constants.USER_BANK_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveUserBank method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 查询银行账号
	 */
	public void findBankByUserId() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findBankByUserId method.");			
		}
		try {
			User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			userBank = facade.findBankByUserId(user.getId());
			fillActionResult(userBank);
		} catch (Exception e) {
			dealThrow(new HsCloudException(Constants.USER_BANK_FIND_EXCEPTION,
					"findBankByUserId", logger, e), Constants.USER_BANK_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findBankByUserId method.takeTime:" + takeTime + "ms");
		}
	}

	public UserBank getUserBank() {
		return userBank;
	}

	public void setUserBank(UserBank userBank) {
		this.userBank = userBank;
	}
}
