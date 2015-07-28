/* 
* 文 件 名:  TransferService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-4-15 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service; 

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.vo.ResultVo;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-4-15] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface TransferService {

	/**
	 * <转出> 
	* <功能详细描述> 
	* @param userId
	* @param fee
	* @param feeType
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ResultVo turnOut(Account account,String fee,String feeType);
	/**
	 * <转出> 
	* <功能详细描述> 
	* @param userId
	* @param fee
	* @param feeType
	* @param billno 老平台交易流水号
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ResultVo turnOutFI(Account account,String fee,String feeType,String billno);
	/**
	 * <转入> 
	* <功能详细描述> 
	* @param userId
	* @param fee
	* @param feeType
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ResultVo turnInto(Account account,String fee,String feeType);
	/**
	 * <转入> 
	* <功能详细描述> 
	* @param userId
	* @param fee
	* @param feeType
	* @param billno
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ResultVo turnIntoFO(Account account,String fee,String feeType,String billno);
	/**
	 * <转账> 
	* <功能详细描述> 
	* @param userId
	* @param fee
	* @param feeType
	* @param transferMode
	* @param billno
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public ResultVo transfer(User user,String fee,String feeType,String transferMode,String billno);
}
