/* 
 * 文 件 名:  TransferServiceImpl.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  ljg 
 * 修改时间:  2014-4-15 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.billing.constant.DepositSource;
import com.hisoft.hscloud.bss.billing.constant.FlowsType;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.service.TransferService;
import com.hisoft.hscloud.web.vo.ResultVo;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author ljg
 * @version [版本号, 2014-4-15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class TransferServiceImpl implements TransferService {

	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private AccountService accountService;
	private static Short front=0;
	private ResultVo resultVo = null;
	/**
	 * @param userId
	 * @param fee
	 * @param feeType
	 * @return
	 */
	@Override
	public ResultVo turnOut(Account account, String fee, String feeType) {
		resultVo = new ResultVo();
		Long logId = null;
		//1.	现金
		if("1".equals(feeType)){
			logId = accountService.accountDraw(account.getUser().getId(), front, PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), "", new BigDecimal(fee), "", "", FlowsType.FLOWS_OUT.getIndex());
		}
		//2.返点
		if("2".equals(feeType)){
			logId = accountService.couponsDraw(account.getUser().getId(), front, PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "");
		}
		if(logId != null){
			resultVo.setSuccess(true);	
			Map map=new HashMap();
			map.put("fee", fee);
			JSONObject json=JSONObject.fromObject(map);
			resultVo.setResultObject(json);
//			resultVo.setResultObject(JSONObject.fromObject(fee).toString());
		}
		return resultVo;
	}
	/**
	 * @param userId
	 * @param fee
	 * @param feeType
	 * @return
	 */
	@Override
	public ResultVo turnOutFI(Account account, String fee, String feeType,String billno) {
		resultVo = new ResultVo();
		Long logId = null;
		//1.	现金
		if("1".equals(feeType)){
			logId = accountService.accountDrawFI(account.getUser().getId(), front, PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), "", new BigDecimal(fee), "", "", FlowsType.FLOWS_OUT.getIndex(),billno);
		}
		//2.返点
		if("2".equals(feeType)){
			logId = accountService.couponsDrawFI(account.getUser().getId(), front, PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "",billno);
		}
		if(logId != null){
			resultVo.setSuccess(true);	
			Map map=new HashMap();
			map.put("fee", fee);
			JSONObject json=JSONObject.fromObject(map);
			resultVo.setResultObject(json);
//			resultVo.setResultObject(JSONObject.fromObject(fee).toString());
		}
		return resultVo;
	}
	/**
	 * @param userId
	 * @param fee
	 * @param feeType
	 * @return
	 */
	@Override
	public ResultVo turnInto(Account account, String fee, String feeType) {
		resultVo = new ResultVo();
		Long logId = null;
		//1.	现金
		if("1".equals(feeType)){
			logId = accountService.accountDeposit(account.getUser().getId(), Short.valueOf("0"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex(), FlowsType.FLOWS_IN.getIndex());
		}
		//2.返点
		if("2".equals(feeType)){
			logId = accountService.couponsDeposit(account.getUser().getId(), front, PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex());
		}
		if(logId != null){
			resultVo.setSuccess(true);	
			Map map=new HashMap();
			map.put("fee", fee);
			JSONObject json=JSONObject.fromObject(map);
			resultVo.setResultObject(json);
//			resultVo.setResultObject(JSONObject.fromObject(fee));
		}
		return resultVo;
	}
	/**
	 * @param userId
	 * @param fee
	 * @param feeType
	 * @return
	 */
	@Override
	public ResultVo turnIntoFO(Account account, String fee, String feeType,String billno) {
		resultVo = new ResultVo();
		Long logId = null;
		//1.	现金
		if("1".equals(feeType)){
			logId = accountService.accountDepositFO(account.getUser().getId(), Short.valueOf("0"), PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex(), FlowsType.FLOWS_IN.getIndex(),billno);//accountDepositFO:accountDepositFromOutside
		}
		//2.返点
		if("2".equals(feeType)){
			logId = accountService.couponsDepositFO(account.getUser().getId(), front, PaymentType.PAYMENT_OFFLINE.getIndex(), account.getId(), new BigDecimal(fee), "", "", DepositSource.OLDPLAT.getIndex(),billno);
		}
		if(logId != null){
			resultVo.setSuccess(true);	
			Map map=new HashMap();
			map.put("fee", fee);
			JSONObject json=JSONObject.fromObject(map);
			resultVo.setResultObject(json);
//			resultVo.setResultObject(JSONObject.fromObject(fee));
		}
		return resultVo;
	}

	/**
	 * @param userId
	 * @param fee
	 * @param feeType
	 * @param transferMode
	 * @return
	 */
	@Override
	@Transactional
	public ResultVo transfer(User user, String fee, String feeType,
			String transferMode,String billno) {
		ResultVo resultVo = null;
		Account account = accountService.getAccountByUserId(user.getId());
		// 转入
		if ("1".equals(transferMode)) {
			resultVo = this.turnIntoFO(account, fee, feeType, billno);//turnIntoFromOutside turnIntoFO
		} else {// 转出
			resultVo = this.turnOutFI(account, fee, feeType,billno);
		}
		return resultVo;
	}

}
