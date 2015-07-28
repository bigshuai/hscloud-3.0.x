/**
 * @title AccountServiceImpl.java
 * @package com.hisoft.hscloud.crm.usermanager.service.impl
 * @description 实现accountService接口
 * @author guole.liang
 * @update 2012-3-31 上午10:27:56
 * @version V1.0
 */
package com.hisoft.hscloud.bss.billing.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.constant.BillConstant;
import com.hisoft.hscloud.bss.billing.constant.DepositSource;
import com.hisoft.hscloud.bss.billing.constant.FlowsType;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.constant.TranscationType;
import com.hisoft.hscloud.bss.billing.dao.AccountDao;
import com.hisoft.hscloud.bss.billing.dao.TranscationLogDao;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.entity.TranscationLog;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.entity.User;


@Service
public class AccountServiceImpl implements AccountService {
	
	private	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private TranscationLogDao transcationLogDao;

	@Transactional(readOnly=true)
	public Account getAccountByUserId(Long userId) {
		
		Account account = null;
		try {
			account = accountDao.getAccountByUserId(userId);
			
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
	    if(null == account){
	    	throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);   
		}
		return account;
	}

	/**
	 * 
	 */
	@Transactional(readOnly=true)
	public boolean checkBalance(long accountId, BigDecimal amount) {
		Account account = accountDao.findById(accountId);
		
		if(amount.compareTo(account.getBalance())<=0){
			return true;
		}
		return false;
	}

	@Transactional
	public Account createAccount(User user) {
		Account account = new Account();
//		account.setUserId(user.getId());
		account.setUser(user);
		account.setCreateId(user.getId());
		account.setBalance(new BigDecimal("0.0"));
		account.setCoupons(new BigDecimal("0.0"));
		account.setCouponsRebateRate(0);
		account.setGiftsBalance(new BigDecimal("0.0"));
		account.setGiftsRebateRate(0);
		account.setCreateDate(new Date());
		account.setUpdateDate(new Date());
		accountDao.save(account);
		return account;
	}

	@Override
	public long accountConsume(long operatorId,short front, short paymentType,short serviceType,long accountId,String description,Long orderId,short consumeType,BigDecimal balance,BigDecimal coupons,BigDecimal gifts) {

		Account account = null;
		if(null == balance || null == coupons || null == gifts){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons() || null ==account.getGiftsBalance()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal accountBalance  = account.getBalance().add(balance.abs().negate());
        if( -1 == accountBalance.signum()){
       	   throw new HsCloudException(BillConstant.ACCOUNT_MONEY_NOENOUGH,"Insufficient account balance.",logger);
        }
        BigDecimal couponsBalance  = account.getCoupons().add(coupons.abs().negate());
        if(-1== couponsBalance.signum()){
           throw new HsCloudException(BillConstant.ACCOUNT_MONEY_NOENOUGH,"Insufficient account balance.",logger);
        }
        
        BigDecimal giftsBalance = account.getGiftsBalance().add(gifts.abs().negate());
        if(-1== giftsBalance.signum()){
            throw new HsCloudException(BillConstant.ACCOUNT_MONEY_NOENOUGH,"Insufficient account balance.",logger);
         }
        account.setBalance(accountBalance);
        account.setCoupons(couponsBalance);
        account.setGiftsBalance(giftsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
        	TranscationLog transcationLog = new TranscationLog();
        	transcationLog.setDomainId(account.getUser().getDomain().getId());
        	transcationLog.setOperatorType((Short)front);
        	transcationLog.setAmount(balance.abs().negate());
        	transcationLog.setBalance(accountBalance);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setCoupons(coupons.abs().negate());
        	transcationLog.setCouponsBalance(couponsBalance);
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(gifts.abs().negate());
        	transcationLog.setGiftsBalance(giftsBalance);
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setAccountId(account.getId());
        	transcationLog.setOrderId(orderId);
        	transcationLog.setPaymentType(paymentType);
        	transcationLog.setConsumeType(consumeType);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_CONSUME.getIndex());
        	transcationLog.setServiceType(serviceType);
        	transcationLog.setDescription(description);
        	transcationLog.setRateFlag(new BigDecimal(0.0).compareTo(coupons.abs())==-1?true:false);
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
        
	}

	@Override
	public long accountDeposit(long operatorId,short front,short paymentType, long accountId,
			BigDecimal balance,String description,String remark,Short depositSource,Short flow) {
		
		Account account = null;
		if(null == balance){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal accountBalance  = account.getBalance().add(balance.abs());
        account.setBalance(accountBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setPaymentType(paymentType);
    		transcationLog.setOperatorType((Short)front);
        	accountDao.save(account);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(account.getId());
        	transcationLog.setAmount(balance.abs());
        	transcationLog.setBalance(accountBalance);
        	transcationLog.setCoupons(new BigDecimal(0.0));
        	transcationLog.setCouponsBalance(account.getCoupons());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setRateFlag(false);
        	transcationLog.setDepositSource(depositSource);
        	if(flow!=null){
        	if((depositSource==1 && flow==1)||depositSource==2){
        		transcationLog.setTranscationType(TranscationType.TRANSCATION_DEPOSIT.getIndex());
        	}
        	}else{
        		if(depositSource==1){
            		transcationLog.setTranscationType(TranscationType.TRANSCATION_ROLLIN.getIndex());
            	}
            	else if(depositSource==2){
            		transcationLog.setTranscationType(TranscationType.TRANSCATION_DEPOSIT.getIndex());
            	}
        	}
        	transcationLog.setFlow(flow);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		transcationLog.setDescription(description);
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			 throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
		
	}
	@Override
	public long accountDepositFO(long operatorId,short front,short paymentType, long accountId,
			BigDecimal balance,String description,String remark,Short depositSource,Short flow,String billno) {
		
		Account account = null;
		if(null == balance){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal accountBalance  = account.getBalance().add(balance.abs());
        account.setBalance(accountBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setPaymentType(paymentType);
    		transcationLog.setOperatorType((Short)front);
        	accountDao.save(account);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(account.getId());
        	transcationLog.setAmount(balance.abs());
        	transcationLog.setBalance(accountBalance);
        	transcationLog.setCoupons(new BigDecimal(0.0));
        	transcationLog.setCouponsBalance(account.getCoupons());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setRateFlag(false);
        	transcationLog.setDepositSource(depositSource);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_ROLLIN.getIndex());
        	transcationLog.setFlow(flow);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		if(!"".equals(billno)){
        			transcationLog.setDescription("从老平台转入到新平台,老平台的交易流水号是:"+billno);
        		}else{
        			transcationLog.setDescription(description);
        		}
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			 throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
		
	}
	
	@Override
	public long couponsDeposit(long operatorId, short front, short paymentType,long accountId, BigDecimal coupons, String description,String remark,Short depositSource) {
		Account account = null;
		if(null == coupons){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getCoupons() || null == account.getBalance()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal couponsBalance  = account.getCoupons().add(coupons.abs());
        account.setCoupons(couponsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setPaymentType(paymentType);
    		transcationLog.setOperatorType((Short)front);
        	accountDao.save(account);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAmount(new BigDecimal(0.0));
        	transcationLog.setBalance(account.getBalance());
        	transcationLog.setCoupons(coupons.abs());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setCouponsBalance(couponsBalance);
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setAccountId(account.getId());
        	transcationLog.setRateFlag(false);
        	transcationLog.setDepositSource(depositSource);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_COUPONS_DEPOSIT.getIndex());
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		transcationLog.setDescription(description);
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			 throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
		
	}
	
	@Override
	public long couponsDepositFO(long operatorId, short front, short paymentType,long accountId, BigDecimal coupons, String description,String remark,Short depositSource,String billno) {
		Account account = null;
		if(null == coupons){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getCoupons() || null == account.getBalance()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal couponsBalance  = account.getCoupons().add(coupons.abs());
        account.setCoupons(couponsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setPaymentType(paymentType);
    		transcationLog.setOperatorType((Short)front);
        	accountDao.save(account);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAmount(new BigDecimal(0.0));
        	transcationLog.setBalance(account.getBalance());
        	transcationLog.setCoupons(coupons.abs());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setCouponsBalance(couponsBalance);
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setAccountId(account.getId());
        	transcationLog.setRateFlag(false);
        	transcationLog.setDepositSource(depositSource);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_COUPONS_DEPOSIT.getIndex());
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		if(!"".equals(billno)){
        			transcationLog.setDescription("通过返点的方式，从新平台转入到老平台，交易流水号是:"+billno);
        		}else{
        			transcationLog.setDescription(description);
        		}
        	}        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			 throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
		
	}
	
	@Override
	public long giftsDeposit(long operatorId, short front, short paymentType,long accountId, BigDecimal gifts, String description, String remark,Short depositSource) {
		Account account = null;
		if(null == gifts){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getGiftsBalance()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal giftsBalance  = account.getGiftsBalance().add(gifts.abs());
        account.setGiftsBalance(giftsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setPaymentType(paymentType);
    		transcationLog.setOperatorType((Short)front);
        	accountDao.save(account);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAmount(new BigDecimal(0.0));
        	transcationLog.setBalance(account.getBalance());
        	transcationLog.setCoupons(new BigDecimal(0.0));
        	transcationLog.setCouponsBalance(account.getCoupons());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(gifts.abs());
        	transcationLog.setGiftsBalance(giftsBalance);
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setAccountId(account.getId());
        	transcationLog.setRateFlag(false);
        	transcationLog.setDepositSource(depositSource);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_GIFTS_DEPOSIT.getIndex());
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		transcationLog.setDescription(description);
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			 throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
	}

	@Override
	public long accountRefund(long operatorId,short front,short paymentType, short serviceType,long accountId,
			BigDecimal balance,BigDecimal coupons,BigDecimal gifts,String description,Long orderId) {
		
		Account account = null;
		if(null == balance || null == coupons || null == gifts){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons() || null == account.getGiftsBalance()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal accountBalance  = account.getBalance().add(balance.abs());
        BigDecimal couponsBalance  = account.getCoupons().add(coupons.abs());
        BigDecimal giftsBalance  = account.getGiftsBalance().add(gifts.abs());
        account.setBalance(accountBalance);
        account.setCoupons(couponsBalance);
        account.setGiftsBalance(giftsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setOperatorType((Short)front);
    		transcationLog.setPaymentType(paymentType);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAmount(balance.abs());
        	transcationLog.setBalance(accountBalance);
        	transcationLog.setCoupons(coupons.abs());
        	transcationLog.setCouponsBalance(couponsBalance);
        	transcationLog.setGifts(gifts.abs());
        	transcationLog.setGiftsBalance(giftsBalance);
        	transcationLog.setGiftsrebateRate(new BigDecimal(0.0));
        	transcationLog.setRebateRate(new BigDecimal(0.0));
        	transcationLog.setAccountId(accountId);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_REFUND.getIndex());
        	transcationLog.setServiceType(serviceType);
        	transcationLog.setDescription(description);
        	transcationLog.setRateFlag(new BigDecimal(0.0).compareTo(coupons.abs())==-1?true:false);
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
		
	}

	@Override
	public long accountDraw(long operatorId,short front,short paymentType,long accountId,String bankAccount,
			BigDecimal balance,String description,String remark,Short flow) {
		
		Account account = null;
		if(null == balance){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal accountBalance  = account.getBalance().add(balance.abs().negate());
        if( -1 == accountBalance.signum()){
        	throw new HsCloudException(BillConstant.ACCOUNT_MONEY_NOENOUGH,"Insufficient account balance.",logger);
        }
        account.setBalance(accountBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setOperatorType((Short)front);
    		transcationLog.setPaymentType(paymentType);
        	transcationLog.setAmount(balance.abs().negate());
        	transcationLog.setBalance(accountBalance);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(accountId);
        	transcationLog.setCoupons(new BigDecimal(0.0));
        	transcationLog.setCouponsBalance(account.getCoupons());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	if(flow==null){
        		transcationLog.setTranscationType(TranscationType.TRANSCATION_ROLLOUT.getIndex());
        	}else if(flow!=null){
        		transcationLog.setTranscationType(TranscationType.TRANSCATION_DRAW.getIndex());
        	}
        	
        	transcationLog.setFlow(flow);
        	transcationLog.setBankAccount(bankAccount);
        	transcationLog.setRateFlag(false);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		transcationLog.setDescription(description);
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
        
	}
	@Override
	public long accountDrawFI(long operatorId,short front,short paymentType,long accountId,String bankAccount,
			BigDecimal balance,String description,String remark,Short flow,String billno) {
		
		Account account = null;
		if(null == balance){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal accountBalance  = account.getBalance().add(balance.abs().negate());
        if( -1 == accountBalance.signum()){
        	throw new HsCloudException(BillConstant.ACCOUNT_MONEY_NOENOUGH,"Insufficient account balance.",logger);
        }
        account.setBalance(accountBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setOperatorType((Short)front);
    		transcationLog.setPaymentType(paymentType);
        	transcationLog.setAmount(balance.abs().negate());
        	transcationLog.setBalance(accountBalance);
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(accountId);
        	transcationLog.setCoupons(new BigDecimal(0.0));
        	transcationLog.setCouponsBalance(account.getCoupons());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_ROLLOUT.getIndex());
        	transcationLog.setFlow(flow);
        	transcationLog.setBankAccount(bankAccount);
        	transcationLog.setRateFlag(false);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		if(!"".equals(billno)){
        			transcationLog.setDescription("从新平台转入到老平台,老平台的交易流水号是:"+billno);
        		}else{
        			transcationLog.setDescription(description);
        		}
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
        
	}
	
	@Override
	public long couponsDraw(long operatorId, short front, short paymentType,long accountId,BigDecimal coupons,String description, String remark) {
		Account account = null;
		if(null == coupons){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal couponsBalance  = account.getCoupons().add(coupons.abs().negate());
        if( -1 == couponsBalance.signum()){
        	throw new HsCloudException(BillConstant.COUPON_MONEY_NOENOUGH,"Insufficient coupon balance.",logger);
        }
        account.setCoupons(couponsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setOperatorType((Short)front);
    		transcationLog.setPaymentType(paymentType);
        	transcationLog.setAmount(new BigDecimal(0.0));
        	transcationLog.setBalance(account.getBalance());
        	transcationLog.setCoupons(coupons.abs().negate());
        	transcationLog.setCouponsBalance(couponsBalance);
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(accountId);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_COUPONS_DRAW.getIndex());
        	transcationLog.setRateFlag(false);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		transcationLog.setDescription(description);
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
	}
	@Override
	public long couponsDrawFI(long operatorId, short front, short paymentType,long accountId,BigDecimal coupons,String description, String remark,String billno) {
		Account account = null;
		if(null == coupons){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getBalance() || null == account.getCoupons()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal couponsBalance  = account.getCoupons().add(coupons.abs().negate());
        if( -1 == couponsBalance.signum()){
        	throw new HsCloudException(BillConstant.COUPON_MONEY_NOENOUGH,"Insufficient coupon balance.",logger);
        }
        account.setCoupons(couponsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setOperatorType((Short)front);
    		transcationLog.setPaymentType(paymentType);
        	transcationLog.setAmount(new BigDecimal(0.0));
        	transcationLog.setBalance(account.getBalance());
        	transcationLog.setCoupons(coupons.abs().negate());
        	transcationLog.setCouponsBalance(couponsBalance);
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(new BigDecimal(0.0));
        	transcationLog.setGiftsBalance(account.getGiftsBalance());
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(accountId);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_COUPONS_DRAW.getIndex());
        	transcationLog.setRateFlag(false);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		if(!"".equals(billno)){
        			transcationLog.setDescription("通过返点的方式，从新平台转入到老平台,老平台的交易流水号是:"+billno);
        		}else{
        			transcationLog.setDescription(description);
        		}
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
	}


	@Override
	public long giftsDraw(long operatorId, short front, Short paymentType,long accountId, BigDecimal gifts, String description, String remark) {
		Account account = null;
		if(null == gifts){
			throw new HsCloudException(BillConstant.ACCOUNT_PARA_ERROR,"parameter error.",logger);
		}
		try {
			account = accountDao.findById(accountId);
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger);
		}
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null == account.getGiftsBalance()){
			throw new HsCloudException(BillConstant.ACCOUNT_ERROR, "Account is error.",logger);
		}
		BigDecimal giftsBalance  = account.getGiftsBalance().add(gifts.abs().negate());
        if( -1 == giftsBalance.signum()){
        	throw new HsCloudException(BillConstant.GIFT_MONEY_NOENOUGH,"Insufficient gift balance.",logger);
        }
        account.setGiftsBalance(giftsBalance);
        account.setUpdateId(operatorId);
        account.setUpdateDate(new Date());
        try {
        	accountDao.save(account);
    		TranscationLog transcationLog = new TranscationLog();
    		transcationLog.setDomainId(account.getUser().getDomain().getId());
    		transcationLog.setOperatorType((Short)front);
    		transcationLog.setPaymentType(paymentType);
        	transcationLog.setAmount(new BigDecimal(0.0));
        	transcationLog.setBalance(account.getBalance());
        	transcationLog.setCoupons(new BigDecimal(0.0));
        	transcationLog.setCouponsBalance(account.getCoupons());
        	transcationLog.setRebateRate(new BigDecimal(account.getCouponsRebateRate()));
        	transcationLog.setGifts(gifts.abs().negate());
        	transcationLog.setGiftsBalance(giftsBalance);
        	transcationLog.setGiftsrebateRate(new BigDecimal(account.getGiftsRebateRate()));
        	transcationLog.setCreateId(operatorId);
        	transcationLog.setAccountId(accountId);
        	transcationLog.setTranscationType(TranscationType.TRANSCATION_GIFTS_DRAW.getIndex());
        	transcationLog.setRateFlag(false);
        	if(!"".equals(remark)){
        		transcationLog.setDescription(remark);
        	}else{
        		transcationLog.setDescription(description);
        	}
        	transcationLogDao.save(transcationLog);
        	return transcationLog.getId();
		} catch (Exception e) {
			throw new HsCloudException(BillConstant.ACCOUNT_DATABASE_ERROR,"Database access error.",logger, e);
		}
	}
	
	@Override
	public long accountCancel(BigDecimal balance) {
		return 0;
	}

	@Override
	public Page<Account> searchAccount(String type,String query,
			List<Sort> sorts, Page<Account> page) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("from Account a where a.user.enable=:enable");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("enable", UserState.APPROVED.getIndex());
		if(type!=null&&!"".equals(type)&&type.equals("userName")){
			sb.append(" and a.user.name like ");
			sb.append("'%" + query + "%'");
		}else if(type!=null&&!"".equals(type)&&type.equals("userEmail")){
			sb.append("  and a.user.email like");
			sb.append("'%" + query + "%'");
		}else if(type!=null&&!"".equals(type)&&type.equals("userDomain")){
			sb.append("  and (a.user.domain.name like ");
			sb.append("'%" + query + "%'");
			sb.append(" or ");
			sb.append(" a.user.domain.abbreviation like ");
			sb.append("'%" + query + "%'");
			sb.append(")");
		}
		
		for (int i = 0; i < sorts.size(); i++) {
			Sort sort = sorts.get(i);
			if (i == 0) {
				sb.append(" order by a.");
			}
			sb.append(sort.getProperty() + " " + sort.getDirection());

			if (i < sorts.size() - 1) {
				sb.append(",");
			}
		}
		String hql = sb.toString();
		return accountDao.findPage(page, hql, map);
	}

	@Override
	public Page<Account> searchAccount(String type,String query,List<Sort> sorts, Page<Account> page,
			List<Long> dids) {
		StringBuffer sb = new StringBuffer();
		sb.append("from Account a where a.user.enable=:enable");
		sb.append(" and a.user.domain.id in (:dids)");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("enable", UserState.APPROVED.getIndex());
        map.put("dids", dids);
		if(type!=null&&!"".equals(type)&&type.equals("userName")){
			sb.append(" and a.user.name like ");
			sb.append("'%" + query + "%'");
		}else if(type!=null&&!"".equals(type)&&type.equals("userEmail")){
			sb.append("  and a.user.email like");
			sb.append("'%" + query + "%'");
		}else if(type!=null&&!"".equals(type)&&type.equals("userDomain")){
			sb.append("  and (a.user.domain.name like ");
			sb.append("'%" + query + "%'");
			sb.append(" or ");
			sb.append(" a.user.domain.abbreviation like ");
			sb.append("'%" + query + "%'");
			sb.append(")");
		}
		
		for (int i = 0; i < sorts.size(); i++) {
			Sort sort = sorts.get(i);
			if (i == 0) {
				sb.append(" order by a.");
			}
			sb.append(sort.getProperty() + " " + sort.getDirection());

			if (i < sorts.size() - 1) {
				sb.append(",");
			}
		}
		String hql = sb.toString();
		return accountDao.findPage(page, hql, map);
	}

	@Override
	@Transactional
	public void updateAccountRate(AccountVO accountVO) {
		Account account = accountDao.findById(accountVO.getId());
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		if(null != accountVO && null != accountVO.getCouponsRebateRate()){
		   account.setCouponsRebateRate(accountVO.getCouponsRebateRate());
		}
		if(null != accountVO && null != accountVO.getGiftsRebateRate()){
		   account.setGiftsRebateRate(accountVO.getGiftsRebateRate());
		}
		accountDao.save(account);
	}

	@Override
	public Account getAccountById(long id) {
		Account account = accountDao.findById(id);
		if(null == account){
			throw new HsCloudException(BillConstant.ACCOUNT_NOT_FOUND,"Account is not found.",logger);
		}
		return account;
	}

//	@Override
//	public String transfer(Account account, String fee, String feeType,
//			String transferMode) throws HsCloudException {
//		String result = "";
//		Long logId = null;
//		// 转入
//		if ("2".equals(transferMode)) {			
//			//1.	现金
//			if("1".equals(feeType)){
//				logId = this.accountDeposit(account.getUser().getId(), Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex(), FlowsType.FLOWS_IN.getIndex());
//			}
//			//2.返点
//			if("2".equals(feeType)){
//				logId = this.couponsDeposit(account.getUser().getId(), Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex());
//			}
//		} else {// 转出
//			//1.	现金
//			if("1".equals(feeType)){
//				logId = this.accountDraw(account.getUser().getId(), Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), "", new BigDecimal(fee), "", "", FlowsType.FLOWS_OUT.getIndex());
//			}
//			//2.返点
//			if("2".equals(feeType)){
//				logId = this.couponsDraw(account.getUser().getId(),Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "");
//			}
//		}
//		if(logId != null){
//			result = Constants.SUCCESS;
//		}
//		return result;
//	}	
	@Override
	public long transfer(Account account, String fee, String feeType,
			String transferMode) throws HsCloudException {
		String result = "";
		Long logId = null;
		// 转入
		if ("2".equals(transferMode)) {			
			//1.	现金
			if("1".equals(feeType)){
				logId = this.accountDeposit(account.getUser().getId(), Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex(), FlowsType.FLOWS_IN.getIndex());
			}
			//2.返点
			if("2".equals(feeType)){
				logId = this.couponsDeposit(account.getUser().getId(), Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex());
			}
		} else {// 转出
			//1.	现金
			if("1".equals(feeType)){
				logId = this.accountDraw(account.getUser().getId(), Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), "", new BigDecimal(fee), "", "", FlowsType.FLOWS_OUT.getIndex());
			}
			//2.返点
			if("2".equals(feeType)){
				logId = this.couponsDraw(account.getUser().getId(),Short.valueOf("1"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "");
			}
		}
		
		return logId;
	}


}
