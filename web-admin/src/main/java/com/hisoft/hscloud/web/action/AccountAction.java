package com.hisoft.hscloud.web.action;




import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.constant.DepositSource;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;

/**
 * @description 操作account的action，对应表hc_account
 * @version 1.0
 * @author guole.liang
 * @update 2012-3-31 上午10:52:07
 */
public class AccountAction extends HSCloudAction{


	private static final long serialVersionUID = 1L;
	
	private	Logger logger = Logger.getLogger(this.getClass());
	
	private final String resourceType="com.hisoft.hscloud.bss.billing.entity.Account";
	
	private Account account;
	
	private AccountVO accountVO;
	
	private User user;
	
	private String remark;
	
	private int page;
	
	private int start;
	
    private int limit;
	
	private String type;
	
	private String query;
	
	private Short depositSource;
	
	private Short flow;
	
	private String sort;

	private Page<Account> accountPage  = new Page<Account>(Constants.PAGE_NUM);
	
	@Autowired
	private Facade facade;
	
	
	public void getAccountByUserId(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAccountByUserId method.");			
		}
		account = facade.getAccountByUserId(user.getId());
		if(null != account && null != account.getUser()){
			account.getUser().setPassword(null);
		}
		fillActionResult(account);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAccountByUserId method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	
	public void getAccountByPage(){
		
		accountPage = new Page<Account>(limit);
		accountPage.setPageNo(page);
		List<Sort> sorts = null;
		try {
			sorts = Utils.json2Object(sort, new TypeReference<List<Sort>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != query){
			try {
				query = new String(query.getBytes("iso8859_1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				dealThrow(new HsCloudException(Constants.GET_USER_BY_PAGE_ERROR, "获取用户日志异常",
						logger, e), "000");
			}
		}
		Admin admin=(Admin)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		Page<Account> account = facade.getAccountByPage(admin,sorts,accountPage, type,query);
		fillActionResult(account);
	}
	
	
	public void updateAccountRate(){
		Admin admin = (Admin)super.getCurrentLoginUser();
		Boolean flag = false;
		try {
			AccountVO accountVO = Utils.strutsJson2Object(AccountVO.class);
			facade.updateAccountRate(accountVO);
			flag = true;
			facade.insertOperationLog(admin,accountVO.getDescription(),"修改账户返点率",(true==flag)?Constants.RESULT_SUCESS:Constants.RESULT_FAILURE);
			fillActionResult(true);
		} catch (HsCloudException hce) {
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	
	/**
	 * 账户充值
	 */
	public void accountDeposit(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter accountDeposit method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			facade.accountDeposit(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(),user.getId(), account.getId(), account.getBalance(),remark,depositSource,flow);
			fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit accountDeposit method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 点劵充值。
	 */
	public void couponsDeposit(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter accountDeposit method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			facade.couponsDeposit(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(),user.getId(), account.getId(), account.getCoupons(),remark,DepositSource.OLDPLAT.getIndex());
			fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit accountDeposit method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 礼金充值。
	 */
	public void giftsDeposit(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter accountDeposit method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			facade.giftsDeposit(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(),user.getId(), account.getId(), account.getGiftsBalance(),remark,DepositSource.OLDPLAT.getIndex());
			fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit accountDeposit method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 点劵提现
	 */
	public void couponsDraw(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter accountDeposit method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			facade.couponsDraw(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(),user.getId(), account.getId(), account.getCoupons(),remark);
			fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit accountDeposit method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 礼金提现
	 */
	public void giftsDraw(){

		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter accountDeposit method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			facade.giftsDraw(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(),user.getId(), account.getId(), account.getGiftsBalance(),remark);
			fillActionResult(true);
		} catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit accountDeposit method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 账户提现
	 */
	public void accountDraw(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter accountDraw method.");			
		}
		try {
			
			Admin admin = (Admin)super.getCurrentLoginUser();
			facade.accountDraw(admin.getId(),PaymentType.PAYMENT_OFFLINE.getIndex(),user.getId(), account.getId(), account.getBalance(),remark,flow);
		    //fillActionResult(null,invoiceAmount.toString());
			fillActionResult(true);
			
		}catch(HsCloudException hce){
			dealThrow(hce,hce.getCode());
		}catch (Exception e) {
			logger.error("", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit accountDraw method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResourceType() {
		return resourceType;
	}


	public int getLimit() {
		return limit;
	}


	public void setLimit(int limit) {
		this.limit = limit;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public Page<Account> getAccountPage() {
		return accountPage;
	}


	public void setAccountPage(Page<Account> accountPage) {
		this.accountPage = accountPage;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}

	public Short getFlow() {
		return flow;
	}

	public void setFlow(Short flow) {
		this.flow = flow;
	}

	
	public AccountVO getAccountVO() {
		return accountVO;
	}

	
	public void setAccountVO(AccountVO accountVO) {
		this.accountVO = accountVO;
	}

	
	public Short getDepositSource() {
		return depositSource;
	}

	
	public void setDepositSource(Short depositSource) {
		this.depositSource = depositSource;
	}

	
	
	
}
