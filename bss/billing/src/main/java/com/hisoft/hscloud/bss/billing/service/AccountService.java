/**
 * @title AccountService.java
 * @package com.hisoft.hscloud.crm.usermanager.service
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-3-31 上午10:17:40
 * @version V1.0
 */
package com.hisoft.hscloud.bss.billing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.vo.AccountVO;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;

/**
 * @description Account 的service层
 * @version 1.0
 * @author guole.liang
 * @update 2012-3-31 上午10:17:40
 */
public interface AccountService {
	
	/**
	 * 
	 * @title: getAccountByUserId
	 * @description 传入user的id,查询账户信息
	 * @param id
	 * @return Account    返回类型
	 * @throws
	 * @version 1.0
	 */
	public Account getAccountByUserId(Long id);
	
	/**
	 * 
	 * @title: checkBalance
	 * @description 根据user_id检查账户余额是否足够
	 * @param user
	 * @param amount
	 * @return boolean
	 * @throws
	 * @version 1.0
	 * @return boolean true 可以支付（表示余额足够）
	 */
	public boolean checkBalance(long account, BigDecimal amount);
	
	/**
	 * 
	 * @title: createAccount
	 * @description 生成一个account，往hc_account中插入记录，account中必须有user_id字段
	 * @param account
	 * @return 设定文件
	 * @return Account    返回类型
	 * @throws
	 * @update 2012-3-31 下午4:33:30
	 */
	public Account createAccount(User user);

	
	
	
	/**
	 * 用户消费接口
	 * @param operatorId 消费人（登陆用户）
	 * @param balance 消费金额 <0(传入值大于0，也会处理成负数)
	 * @param coupons 点劵金额（传入值大于0，也会处理成负数）
	 * @param rebateRate 比率（大于0）
	 * @param orderId 订单id
	 * @param accountId 消费账户
	 * @param front 操作用户（后台用户：0，前台用户：1）
	 * @param description 描叙不能为空
	 * @return logId    日志ID
	 */
	public long accountConsume(long operatorId,short front, short paymentType,short serviceType,long accountId,String description,Long orderId,short consumeType,BigDecimal balance,BigDecimal coupons,BigDecimal gifts);
	
	/**
	 * 用户充值接口
	 * @param operatorId 充值操作人（登陆用户）
	 * @param accountId  被充值账户
	 * @param balance 充值金额>0(传入值小于0，也会处理成正数)
	 * @param remark 备注
	 * @param front 操作用户（后台用户：0，前台用户：1）
	 * @param description 描叙不能为空
	 * @param flow   转入/转出
	 * @return logId    日志ID
	 * 
	 */
	public long accountDeposit(long operatorId,short front,short paymentType,long accountId,BigDecimal balance,String description,String remark,Short depositSource,Short flow);
	/**
	 * 用户充值接口
	 * @param operatorId 充值操作人（登陆用户）
	 * @param accountId  被充值账户
	 * @param balance 充值金额>0(传入值小于0，也会处理成正数)
	 * @param remark 备注
	 * @param front 操作用户（后台用户：0，前台用户：1）
	 * @param description 描叙不能为空
	 * @param flow   转入/转出
	 * @param　billno   转入/转出
	 * @return logId    日志ID
	 * 
	 */
	public long accountDepositFO(long operatorId,short front,short paymentType,long accountId,BigDecimal balance,String description,String remark,Short depositSource,Short flow,String billno);
	/**
	 * 用户充值接口
	 * @param operatorId 充值操作人（登陆用户）
	 * @param accountId  被充值账户
	 * @param coupons 充值点劵>0(传入值小于0，也会处理成正数)
	 * @param remark 备注
	 * @param front 操作用户（后台用户：0，前台用户：1）
	 * @param description 描叙不能为空
	 * @return logId    日志ID
	 * 
	 */
	public long couponsDeposit(long operatorId, short front, short paymentType,long accountId, BigDecimal coupons, String description, String remark,Short depositSource);
	
	public long giftsDeposit(long operatorId, short s, short paymentType,long accountId, BigDecimal gifts, String string, String remark,Short depositSource);
	/**
	 * 用户退款接口
	 * @param operatorId  退款操作人（登陆用户）
	 * @param accountId  退款账户id
	 * @param balance 消费金额 >0(传入值小于0，也会处理成正数)
	 * @param coupons 点劵金额（传入值小于0，也会处理成正数）
	 * @param orderId 订单id
	 * @param front 操作用户（后台用户：0，前台用户：1）
	 * @param description 描叙不能为空
	 * @return logId    日志ID
	 * 
	 */
	public long accountRefund(long operatorId,short front,short paymentType,short serviceType,long accountId,BigDecimal balance,BigDecimal coupons,BigDecimal gifts,String description,Long orderId);
	
	/**
	 * 用户提款接口
	 * @param operatorId 提款操作人（登陆用户）
	 * @param accountId  提款账户
	 * @param balance 消费金额 <0(传入值大于0，也会处理成负数)
	 * @param remark 备注
	 * @param front 操作用户（后台用户：0，前台用户：1）
	 * @param description 描叙不能为空
	 * @return logId    日志ID
	 */
	public long accountDraw(long operatorId,short front,short paymentType,long accountId,String bankAccount,BigDecimal balance,String description,String remark,Short flow);
	public long accountDrawFI(long operatorId,short front,short paymentType,long accountId,String bankAccount,BigDecimal balance,String description,String remark,Short flow,String billno);
	public long couponsDraw(long operatorId,short front,short paymentType,long accountId,BigDecimal coupons,String description,String remark);
	public long giftsDraw(long operatorId, short front, Short paymentType,long accountId, BigDecimal gifts, String description, String remark);
	
	/**
	 * 用户撤销接口
	 * @param balance
	 * @return logId    日志ID
	 */
	public long accountCancel(BigDecimal balance);

	public Page<Account> searchAccount(String type,String query,
			List<Sort> sorts, Page<Account> page);

	public Page<Account> searchAccount(String type,String query,List<Sort> sorts, Page<Account> page,
			List<Long> dids);

	public void updateAccountRate(AccountVO accountVO);
	
	public Account getAccountById(long id);

//	/**
//	 * <转入转出现金返点综合接口> 
//	* <功能详细描述> 
//	* @param account
//	* @param fee
//	* @param feeType
//	* @param transferMode
//	* @return
//	* @throws HsCloudException 
//	* @see [类、类#方法、类#成员]
//	 */
//	public String transfer(Account account, String fee, String feeType,
//			String transferMode) throws HsCloudException;
	/**
	 * <转入转出现金返点综合接口> 
	* <功能详细描述> 
	* @param account
	* @param fee
	* @param feeType
	* @param transferMode
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long transfer(Account account, String fee, String feeType,
			String transferMode) throws HsCloudException;
	/**
	 * <转入转出现金返点综合接口> 
	* <功能详细描述> 
	* @param account
	* @param fee
	* @param feeType
	* @param transferMode
	* @param billno
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long couponsDepositFO(long operatorId, short front, short paymentType,
			long accountId, BigDecimal coupons, String description,
			String remark, Short depositSource, String billno);
	/**
	 * <转入转出现金返点综合接口> 
	* <功能详细描述> 
	* @param account
	* @param fee
	* @param feeType
	* @param transferMode
	* @param billno
	* @return
	* @throws HsCloudException 
	* @see [类、类#方法、类#成员]
	 */
	public long couponsDrawFI(long operatorId, short front, short paymentType,
			long accountId, BigDecimal coupons, String description,
			String remark, String billno);

	
}
