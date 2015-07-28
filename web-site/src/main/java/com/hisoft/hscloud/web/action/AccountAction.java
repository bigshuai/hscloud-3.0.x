package com.hisoft.hscloud.web.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

public class AccountAction extends HSCloudAction {

	private static Logger logger = Logger.getLogger(AccountAction.class);
	private static final long serialVersionUID = 1L;

	private final String resourceType = "com.hisoft.hscloud.bss.billing.entity.Account";

	@Autowired
	private Facade facade;
	private String fee;
	private String feeType;
	private String transferMode;
	private String domainCode;

	/**
	 * 通过用户id,获得账户信息。
	 */
	public void getAccountByUserId() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAccountByUserId method.");
		}
		try {
			User user = getCurrentUser();
			logger.debug("UserId:" + user.getId());
			Account account = facade.getAccountByUserId(user.getId());
			if (account != null) {
				account.setName(user.getName());
			}
			if (null != account && null != account.getUser()) {
				account.getUser().setPassword(null);
			}
			fillActionResult(account);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAccountByUserId method.takeTime:" + takeTime
					+ "ms");
		}
	}

	/**
	 * <获取账户余额> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getAccountBalance() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAccountBalance method.");
		}
		try {
			User user = getCurrentUser();
			logger.debug("UserId:" + user.getId());
			Account account = facade.getAccountByUserId(user);
			if (null != account && null != account.getUser()) {
				account.getUser().setPassword(null);
			}
			// String balance="";
			// if(account!=null){
			// balance=account.getBalance().toString();
			// }
			super.getActionResult().setResultObject(account);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAccountBalance method.takeTime:" + takeTime
					+ "ms");
		}
	}

	/**
	 * 获取账户余额和当前用户的真实返点率
	 */
	public void getAccountBalanceAndRealRebate() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAccountBalanceAndRealRebate method.");
		}
		try {
			User user = getCurrentUser();
			logger.debug("UserId:" + user.getId());
			Account account = facade.getAccountByUserId(user);
			super.getActionResult().setResultObject(account);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAccountBalanceAndRealRebate method.takeTime:"
					+ takeTime + "ms");
		}
	}

	public void transfer() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter transfer method.");
		}
		try {
			User user = getCurrentUser();
			logger.debug("UserId:" + user.getId());
			Object result = facade.transfer(user,fee,feeType,transferMode,domainCode);
			super.fillActionResult(result);
		} catch (Exception e) {
			super.getActionResult().setSuccess(false);
			super.getActionResult().setResultMsg(e.getMessage());
			dealThrow(new HsCloudException("", logger, e), "");
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit transfer method.takeTime:" + takeTime + "ms");
		}
	}

	private User getCurrentUser() {
		Object obj = super.getCurrentLoginUser();
		User user = null;
		if (obj != null) {
			if (obj instanceof User) {
				user = (User) obj;
			}
		}
		return user;
	}

	public String getResourceType() {
		return resourceType;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

}
