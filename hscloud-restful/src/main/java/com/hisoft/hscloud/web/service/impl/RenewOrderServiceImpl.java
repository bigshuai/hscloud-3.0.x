/* 
* 文 件 名:  RenewOrderService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.billing.constant.ConsumeType;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.constant.ServiceType;
import com.hisoft.hscloud.bss.billing.constant.TranscationType;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.billing.service.IncomingLogService;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.service.OrderItemService;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.util.VMTypeEnum;
import com.hisoft.hscloud.web.service.RenewOrderService;
import com.hisoft.hscloud.web.vo.RenewOrderVo;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class RenewOrderServiceImpl implements RenewOrderService {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Autowired
    private Operation operation;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private IServiceCatalogService serviceCatalogService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private IncomingLogService incomingLogService;
    
    
    /*@Override
    public long queryReferenceId(String vmId, String userId) {
        String referenceId = renewOrderDao.queryReferenceIdByVmId(vmId, userId);
        if(referenceId == null) {
            return -1l;
        }
        return Long.valueOf(referenceId);
    }*/
    
    @Override
    @Transactional
    public String renewOrderV2(long referenceId,String paymode,String feeTypeId, User user, RenewOrderVo renewOrderVo, String doUseCoupon, String doUseGift)
            throws HsCloudException {
        String result = Constants.VM_RENEW_RESULT_FAIL;
        try {
            // 根据虚拟机Id查询orderItemId与referenceId关联实体bean
            VpdcReference vr = operation.getReferenceById(referenceId);
            if (VMTypeEnum.REGULAR.getCode() == vr.getVm_type()) {
                // 根据虚拟机Id查询orderItemId与referenceId关联实体bean
                VpdcReference_OrderItem vroi = operation
                        .getOrderItemByReferenceId(referenceId);
                // 只有根据vmId查询出相应的reference和orderItem的对应关系，才能进行续费操作，否则续费失败
                if (vroi != null) {
                    long oldOrderItemId = Long.valueOf(vroi.getOrder_item_id());
                    long userId = user.getId();
                    OrderItem orderItem = orderItemService
                            .getOrderItemById(oldOrderItemId);
                    String scId = orderItem.getServiceCatalogId();
                    int scIdInt = Integer.parseInt(scId);
                    BigDecimal price = null;
                    String priceType = null;
                    String pricePeriodType = null;
                    String period = null;
                    ScFeeType resultFeeType=null;
                    if (StringUtils.isNotBlank(feeTypeId)) {
                    	Long feeTypeIdLocal=Long.parseLong(feeTypeId);
                    	resultFeeType = serviceCatalogService
                                .getfeeTypeById(feeTypeIdLocal, scIdInt);
                    }else if(StringUtils.isNotBlank(paymode)){
                    	resultFeeType = serviceCatalogService
                                .getfeeTypeByPeriod(paymode, scIdInt);
                    }
                    if (resultFeeType != null) {
                        price = resultFeeType.getPrice();
                        priceType = resultFeeType.getPriceType();
                        pricePeriodType = resultFeeType.getPricePeriodType();
                        period = resultFeeType.getPeriod();
                    }else{
                    	if(StringUtils.isNotBlank(feeTypeId)) {
                    	    return Constants.VM_RENEW_RESULT_INVALID_FEETYPE;
                        } else if(StringUtils.isNotBlank(paymode)) {
                            return Constants.VM_RENEW_RESULT_INVALID_PAYMODE;
                        } else {
                            return Constants.VM_RENEW_RESULT_INVALID_FEETYPE;
                        }
                    }
                    
                    Account account = accountService.getAccountByUserId(userId);
                    Long accountId = account.getId();
                    boolean checkResult = accountService.checkBalance(
                            accountId, price);
                    // checkResult为标志，如果余额大于需要支付金额，续费产生的新订单为已支付，否则为未支付
                    if (checkResult) {
                        String description = Constants.VM_PERIOD_LOG_RENEWCOST;
                        // 获取虚拟机失效时间
                        VpdcReference_Period vrp =  operation
                                .getReferencePeriod(referenceId);
                        Date expirationDate = vrp.getEndTime();
                        
//                        UserBrand userBrand = userBrandService.getBrandByCode(user.getLevel());
                        
                      /*  double couponAmount = 0;
                        if("true".equalsIgnoreCase(doUseCoupon) ) { //使用返点
                            couponAmount = userBrand.getRebateRate() * price.intValue() / 100.0;
                        }*/
                        ServiceCatalog serviceCatalog = serviceCatalogService.get(scIdInt);
                        BigDecimal customerPoints = new BigDecimal(0);
                        if(serviceCatalog.getUsePointOrNot() == true && "true".equalsIgnoreCase(doUseCoupon) ) { //使用返点
                            BigDecimal rebateRate = new BigDecimal(account.getCouponsRebateRate());
                            BigDecimal orderValue = new BigDecimal(price.intValue());
                            customerPoints = rebateRate.multiply(orderValue).divide(new BigDecimal(100));
                        }
                        
                        
                        BigDecimal customerGift = new BigDecimal(0);
                        if(serviceCatalog.getUseGiftOrNot() == true && "true".equalsIgnoreCase(doUseGift) ) { //使用返点
                            BigDecimal rebateRate = new BigDecimal(account.getGiftsRebateRate());
                            BigDecimal orderValue = new BigDecimal(price.intValue());
                            customerGift = rebateRate.multiply(orderValue).divide(new BigDecimal(100));
                        }
                    //    BigDecimal customerPoints=new BigDecimal(couponAmount);
                        
                        // 对订单及订单项进行支付的业务操作
                        OrderItem orderItemNew = orderService.renewOrderV2(
                                orderItem, customerPoints,customerGift, price, priceType, pricePeriodType,
                                period, user, expirationDate, account.getCouponsRebateRate(), account.getGiftsRebateRate());
                        Long newOrderItemId = orderItemNew.getId();
                        BigDecimal newAmount=orderItemNew.getAmount();
                        Order newOrder = orderItemNew.getOrder();
                        Long orderId = newOrder.getId();
                        
                        
                        BigDecimal accountPoints=account.getCoupons();
                        if(customerPoints.compareTo(BigDecimal.ZERO)>=0&&accountPoints.compareTo(customerPoints)>=0){
                            newOrder=OrderUtils.afterPoint(newOrder, customerPoints);
                        }else{
                            newOrder=OrderUtils.afterPoint(newOrder, accountPoints);
                        }
                        
                        BigDecimal accountGift=account.getGiftsBalance();
                        if(customerGift.compareTo(BigDecimal.ZERO)>=0&&accountGift.compareTo(customerGift)>=0){
                            newOrder=OrderUtils.afterGift(newOrder, customerGift);
                        }else{
                            newOrder=OrderUtils.afterGift(newOrder, accountGift);
                        }
                        
                        // 续费需要扣款，需要发送站内提醒
                        Message message = OrderUtils.orderGenMessage(vr.getName(),
                                newOrder, user.getId(),(short) 2);
                        messageService.saveMessage(message);
                        
                        if(newOrder.getRebateRate() == null) {
                            newOrder.setRebateRate(0);
                        }
                        // 对账户进行扣款操作
                        long transactionId=accountService.accountConsume(userId,(short)1,
                                PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(), accountId, 
                                message.getMessage(), orderId, ConsumeType.CONSUME_RENEW.getIndex(), 
                                 newOrder.getTotalAmount(), newOrder.getTotalPointAmount(), newOrder.getTotalGiftAmount());
                        
                        Date expirationDateNew = orderItemNew
                                .getExpirationDate();
                        Date effectiveDateNew = orderItemNew.getEffectiveDate();
                        //新添加虚拟机与订单项的关联关系
                        operation.saveReference_OrderItem(referenceId,
                                String.valueOf(newOrderItemId));
                        // 更新虚拟机的失效时间
                        operation.updateReferencePeriod(
                                String.valueOf(newOrderItemId), null,
                                expirationDateNew);
                        //启用已到期VM
                        IncomingLog incomingLog=new IncomingLog();
                        incomingLog.setAccountId(accountId);
                        incomingLog.setAmount(newAmount);
                        BigDecimal days=BigDecimal.valueOf(Integer.parseInt(period)*30L);
                        incomingLog.setDayPrice(newAmount.divide(days, 2, RoundingMode.HALF_UP));
                        incomingLog.setEffectiveDate(effectiveDateNew);
                        incomingLog.setScId(Integer.parseInt(orderItemNew.getServiceCatalogId()));
                        incomingLog.setScName(orderItemNew.getServiceCatalogName());
                        incomingLog.setExpirationDate(expirationDateNew);
                        incomingLog.setEmail(user.getEmail());
                        incomingLog.setIncomingType((short)1);
                        incomingLog.setObjectId(referenceId);
                        incomingLog.setOrderId(orderId);
                        incomingLog.setOrderItemId(newOrderItemId);
                        incomingLog.setOrderNo(newOrder.getOrderNo());
                        incomingLog.setProductType((short)1);
                        incomingLog.setTranscationType(TranscationType.TRANSCATION_CONSUME.getIndex());
                        incomingLog.setTransactionId(transactionId);
                        incomingLog.setDomainId(user.getDomain().getId());
                        incomingLogService.saveIncomingLog(incomingLog);
                        if(expirationDateNew.after(new Date())){
                            operation.activeExpireVM(vr, user.getId());
                        }
                        operation.saveVmPeriodLog(description, referenceId,
                                effectiveDateNew, expirationDateNew);
                        
                        renewOrderVo.setConsume(newOrder.getTotalAmount().toString());
                        renewOrderVo.setCouponConsume(newOrder.getTotalPointAmount().toString());
                        renewOrderVo.setGiftConsume(newOrder.getTotalGiftAmount().toString());
                        
                        account = accountService.getAccountByUserId(userId);
                        renewOrderVo.setBalance(account.getAccountBalance());
                        renewOrderVo.setCouponBalance(account.getCouponsBalance());
                        result = Constants.VM_RENEW_RESULT_SUCC;
                    } else {
                        result = Constants.VM_RENEW_RESULT_NOFEE;
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new HsCloudException("", logger, e);
        }
    }
}
