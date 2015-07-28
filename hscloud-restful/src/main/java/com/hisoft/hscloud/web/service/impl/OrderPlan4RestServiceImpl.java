/* 
* 文 件 名:  OrderPlan4RestServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-29 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.service.impl; 

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.billing.constant.ConsumeType;
import com.hisoft.hscloud.bss.billing.constant.PaymentType;
import com.hisoft.hscloud.bss.billing.constant.ServiceType;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.billing.service.IncomingLogService;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.sc.entity.Cpu;
import com.hisoft.hscloud.bss.sla.sc.entity.Disk;
import com.hisoft.hscloud.bss.sla.sc.entity.ExtDisk;
import com.hisoft.hscloud.bss.sla.sc.entity.Network;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.Ram;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.Software;
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.service.UserBrandService;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.hisoft.hscloud.mail.service.MailService;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.web.dao.OrderPlan4RestDao;
import com.hisoft.hscloud.web.service.OrderPlan4RestService;
import com.hisoft.hscloud.web.util.CalendarUtil;
import com.hisoft.hscloud.web.vo.OrderPlanVo;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-29] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class OrderPlan4RestServiceImpl implements OrderPlan4RestService {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    private String log="";
    
    @Resource
    private IServiceCatalogService serviceCatalogService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private OrderService orderService;
    
    @Resource
    private IServerZoneService serverZoneService;
    
    @Resource
    private Operation operation;
    
    @Resource
    private DomainService domainService;
    
    @Resource
    private MailService mailService;
    
    @Resource
    private AccountService accountService;
    
    @Resource
    private OrderPlan4RestDao orderPlan4RestDao;
    
    @Resource
    private MessageService messageService;
    
    @Resource
    private IncomingLogService incomingLogService;
    
    @Resource
    private UserBrandService userBrandService;

    @Override
    public String getFactorySeq(String accessid) {
        return orderPlan4RestDao.getFactorySeq(accessid);
    }

    @Override
    public String getUserId(String accessid) {
        return orderPlan4RestDao.getUserId(accessid);
    }
    
    @Override
    public Map<String, Object> queryAccessByAccessId(String accessId) {
        return orderPlan4RestDao.queryAccessByAccessId(accessId);
    }

    @Override
    @Transactional
    public Map<String,Object> scBuyDirect(int scId, int osId, String period, int scNum,
            long userId, OrderPlanVo orderPlanVos, String id, String accessId, String doUseCoupon, String doUseGift) throws Exception {
        logger.info(" order buy is begaining......");
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            List<OrderItemVo> orderItems = new ArrayList<OrderItemVo>();
            User user = userService.getUser(userId);
            if(user==null){
                log="userId is not exist!";
                map.put("log", log);
                return map;
            }
            UserBrand userBrand = userBrandService.getBrandByCode(user.getLevel());
            ServiceCatalog sc=serviceCatalogService.get(scId);
            boolean userBrandFlag = false;
            if(this.validationServiceCatalog(sc)) {
                for(UserBrand scUserBrand : sc.getUserBrand()) {
                    if(scUserBrand.getId() == userBrand.getId()) {
                        userBrandFlag = true;
                    }
                }
            }else{
            	log="scId is not valid!";
                map.put("log", log);
                return map;
            }
            if(userBrandFlag == false) {
                log="scId is not exist!";
                map.put("log", log);
                return map;
            }
            
            for (int i = 0; i < scNum; i++) {
                OrderItemVo orderItemVo = addItemForRest(sc, osId, period);
                if(StringUtils.isBlank(orderItemVo.getImageId())) {
                    log="Operating system is not exist!";
                    map.put("log", log);
                    return map;
                }
                orderItems.add(orderItemVo);
            }
            
            // 根据企业用户Id获取其账户信息
            Account account = accountService.getAccountByUserId(userId);
            Integer couponsRebateRate = setValueForNull(account.getCouponsRebateRate()); 
            Integer giftsRebateRate = setValueForNull(account.getGiftsRebateRate());

            //根据套餐信息生成订单
            Order order = orderService.submitOrder(orderItems, user, couponsRebateRate, giftsRebateRate);
            if(order==null){
                log="order is not correct!";
                map.put("log", log);
                return map;
            }
            orderPlanVos.setOrderno(order.getOrderNo());
            orderPlanVos.setTotalnum(scNum);
            orderPlanVos.setCreateon(CalendarUtil.fomartDate(order.getCreateDate()));
            long orderId = order.getId();
            // 如果是企业用户则返回自己ID如果是子用户返回其关联的企业用户ID
            
            Long accountId = account.getId();
            // 判断余额是否大于支付金额
            boolean checkResult = accountService.checkBalance(accountId,
                    order.getTotalPrice());
            
            String result = "";
            Map<NovaServerForCreate,HcEventResource> rabbitMQVM = null;
            if (checkResult) {// 如果余额大于支付金额进行订单支付业务操作
                BigDecimal customerPoints = new BigDecimal(0);
                if("true".equalsIgnoreCase(doUseCoupon) ) { //使用返点
                    BigDecimal rebateRate = new BigDecimal(couponsRebateRate);
                    BigDecimal orderValue = new BigDecimal(order.getTotalPrice().intValue());
                    customerPoints = rebateRate.multiply(orderValue).divide(new BigDecimal(100));
                }
                BigDecimal accountPoints=account.getCoupons();
                if(customerPoints.compareTo(BigDecimal.ZERO)>=0&&accountPoints.compareTo(customerPoints)>=0){
                    order=OrderUtils.afterPoint(order, customerPoints);
                }else{
                    order=OrderUtils.afterPoint(order, accountPoints);
                }
                
                BigDecimal customerGift = new BigDecimal(0);
                if("true".equalsIgnoreCase(doUseGift) ) { //使用返点
                    BigDecimal rebateRate = new BigDecimal(giftsRebateRate);
                    BigDecimal orderValue = new BigDecimal(order.getTotalPrice().intValue());
                    customerGift = rebateRate.multiply(orderValue).divide(new BigDecimal(100));
                }
                BigDecimal accountGift=account.getGiftsBalance();
                if(customerGift.compareTo(BigDecimal.ZERO)>=0&&accountGift.compareTo(customerGift)>=0){
                    order=OrderUtils.afterGift(order, customerGift);
                }else{
                    order=OrderUtils.afterGift(order, accountGift);
                }
                
                Message message = OrderUtils.orderGenMessageForOrderPay(order, (short) 5);
                messageService.saveMessage(message);
                // 对账户进行扣款操作
                long transactionId=accountService.accountConsume(userId,(short)1,
                        PaymentType.PAYMENT_ONLINE.getIndex(),ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(), accountId,
                 message.getMessage(), orderId, ConsumeType.CONSUME_BUY.getIndex(),
                order.getTotalAmount(), order.getTotalPointAmount(),order.getTotalGiftAmount());
                
                
                // 对订单及订单项进行支付的业务操作
                order = orderService.pay(order);
                // 订单支付完成发送邮件提醒
                // 绑定邮件模版中需要的参数
                Map<String, String> orderMailTemplate = OrderUtils
                        .generateMailVariable(order);

                // lihonglei 20130322 saveEmail0
                saveEmail(user, orderMailTemplate, MailTemplateType.ORDER_PAID_TEMPLATE.getType());
                //****************************lihonglei start 2013-05-23 11:24
                Map<OrderItem,ScIsolationConfig> dataForCreateVmBean=new HashMap<OrderItem,ScIsolationConfig>();
				for(OrderItem item:order.getItems()){
					dataForCreateVmBean.put(item, sc.getScIsolationConfig());
				}
	            
                List<CreateVmBean> createVmBeans = OrderUtils.getCreateVMBeans(
                		dataForCreateVmBean);
                // 创建虚拟机
                ServerZone sz = null;
                sz = serverZoneService.getDefaultServerZone();
                rabbitMQVM = operation.createVmByAPI(createVmBeans, Long.toString(userId), sz.getCode(), id, accessId);
                for(CreateVmBean cvb:createVmBeans){
                    VpdcReference_OrderItem vroi = operation.getOrderItemByOrderItemId(cvb.getOrder_item_id());
                    String orderItemIdStr=cvb.getOrder_item_id();
                    long referenceId=vroi.getVpdcRenferenceId();
                    String orderNo=order.getOrderNo();
                    long orderItemId=Long.parseLong(orderItemIdStr);
                    for(OrderItem item:order.getItems()){
                        if(item.getId().longValue()==orderItemId){
                            IncomingLog incomingLog=OrderUtils.getIncomingLog(item, referenceId, accountId, transactionId, user.getEmail(), orderId, orderNo,user.getDomain().getId());
                            incomingLogService.saveIncomingLog(incomingLog);
                        }
                    }
                }
                orderPlanVos.setConsume(order.getTotalPrice().toString());
                orderPlanVos.setCouponConsume(order.getTotalPointAmount().toString());
                
                account = accountService.getAccountByUserId(userId);
                orderPlanVos.setBalance(account.getAccountBalance());
                orderPlanVos.setCouponBalance(account.getCouponsBalance());
                result = "success";
                map.put(result, rabbitMQVM);
              //****************************lihonglei end 2013-05-23 11:24
            }else{
                log= " account balance dont have sufficient funds ";
                result = "balance is not enough";
                map.put("log", log);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
         /*   log=e.getMessage();
            map.put("log", log);
            return map;*/
            throw e;
        }
        logger.info(" order buy is ending......");
        return map;
    }
    
    public Integer setValueForNull(Integer rebateRate) {
        if(rebateRate == null) {
            return 0;
        }
        return rebateRate;
    }
    
    /**
     * 判断用户品牌 
    * <功能详细描述> 
    * @param user
    * @param sc
    * @return 
    * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unused")
	private boolean isScUserBrand(User user, ServiceCatalog sc) {
        UserBrand userBrand = userBrandService.getBrandByCode(user.getLevel());
        boolean userBrandFlag = false;
        if(sc != null) {
            for(UserBrand scUserBrand : sc.getUserBrand()) {
                if(scUserBrand.getId() == userBrand.getId()) {
                    userBrandFlag = true;
                }
            }
        }
        return userBrandFlag;
    }
    
    @Transactional
    private OrderItemVo addItemForRest(ServiceCatalog sc,int osId,String period){
        logger.info("add item for rest is begaining......");
        try {
            
            List<Os> osList=new ArrayList<Os>();
            List<String> addDiskStrList=new ArrayList<String>();
            OrderItemVo item = new OrderItemVo();
            
            item.setUsePointOrNot(sc.getUsePointOrNot());
            item.setServiceCatalogName(sc.getName());
            item.setServiceCatalogId(String.valueOf(sc.getId()));
            item.setFlavorId(String.valueOf(sc.getFlavorId()));
            List<ServerZone> zones=sc.getZoneList();
			String firstZoneCode=zones.get(0).getCode();
			StringBuilder zoneListStr=new StringBuilder(firstZoneCode);
			for(int i=1;i<zones.size();i++){
				zoneListStr.append(",").append(zones.get(i).getCode());
			}
			item.setNodeName(zoneListStr.toString());
            item.setServiceDesc(sc.getDescription());
            for(ServiceItem si :sc.getItems()){
                switch(si.getServiceType()){
                //保存cpu信息
                case 1:{
                    Cpu cpu=(Cpu)si;
                    item.setCpu(cpu.getName());
                    item.setCpuModel(cpu.getModel());
                    item.setvCpus(String.valueOf(cpu.getCoreNum()));
                    break;
                }
                //内存信息
                case 2:{
                    Ram ram=(Ram)si;
                    item.setMemory(String.valueOf(ram.getSize()));
                    item.setMemoryModel(ram.getModel());
                    break;
                }
                //系统盘信息
                case 3:{
                    Disk disk=(Disk)si;
                    item.setDisk(String.valueOf(disk.getCapacity()));
                    item.setDiskModel(disk.getModel());
                    break;
                }
                //操作系统信息
                case 4:{
                    Os os = (Os)si;
                    osList.add(os);
                    break;
                }
                //带宽信息
                case 5:{
                    Network network = (Network)si;
                    item.setNetwork(String.valueOf(network.getBandWidth()));
                    item.setNetworkType(network.getType());
                    break;
                }
                case 6:{
                    Software sf=(Software)si;
                    item.setSoftware(sf.getName());
                    break;
                }
                //扩展盘信息
                case 8:{
                    ExtDisk extDisk=(ExtDisk)si;
                    addDiskStrList.add(String.valueOf(extDisk.getCapacity()));
                    break;
                }
                }
            }
            //套餐可能包括多个操作系统，但是创建虚拟机时是根据客户传递过来的osid来确定默认的os
            if(osList.size()>=1){
                StringBuilder osIdsStr = new StringBuilder();
                for(Os os:osList){
                    if (osList.lastIndexOf(os) == osList.size() - 1) {
                        osIdsStr.append(os.getId());
                    } else {
                        osIdsStr.append(os.getId()).append(",");
                    }
                    if(os.getId()==osId){
                        item.setOs(os.getName());
                        item.setOsId(String.valueOf(osId));
                        item.setImageId(os.getImageId());
                    }
                }
                item.setOsIds(osIdsStr.toString());
            }
            //多个扩展盘以形如extdisk，extdisk1，extdisk2的形式存在订单中
            if(addDiskStrList.size()>0){
                StringBuilder addDiskStrSB = new StringBuilder();
                for(int i=0;i<addDiskStrList.size();i++){
                    if(i==addDiskStrList.size()-1){
                        addDiskStrSB.append(addDiskStrList.get(i));
                    }else{
                        addDiskStrSB.append(addDiskStrList.get(i)).append(",");
                    }
                }
                item.setAddDisk(addDiskStrSB.toString());
            }
            //根据套餐的计费规则填充订单中金额信息 
            for(ScFeeType feeType :sc.getFeeTypes()){
                if(feeType.getPeriod().equals(period)){
                    item.setPrice(feeType.getPrice());
                    item.setPriceType(feeType.getPriceType());
                    item.setPricePeriodType(feeType.getPricePeriodType());
                    item.setPricePeriod(period);
                }
            }
            logger.info("add item for rest is ending......");
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void saveEmail(User user, Map<String, String> map, String type) {
        logger.info("save email for rest is begaining......");
        Domain domain = domainService.getDomainById(user.getDomain().getId());
        
        map.put("abbreviation", domain.getAbbreviation());
        map.put("name", domain.getName());
        map.put("telephone", domain.getTelephone());
        map.put("address", domain.getAddress());
        map.put("url", domain.getUrl());
        map.put("bank", domain.getBank());
        map.put("cardNo", domain.getCardNo());
        map.put("serviceHotline", domain.getServiceHotline());
        map.put("webSiteUrl",domain.getPublishingAddress());
        
        map.put("webAddress", domain.getPublishingAddress());
        
        mailService.saveEmail(user.getEmail(), null, type,
                domain.getId(), map);
        logger.info("save email for rest is ending......");
    }
    
    /**
     * <验证套餐的有效性> 
    * <如果套餐为已审核并且没有到失效日期则返回真，否则为假> 
    * @param sc
    * @return 
    * @see [类、类#方法、类#成员]
     */
    private boolean validationServiceCatalog(ServiceCatalog sc){
    	boolean flag = false;
    	if(sc == null){
    		return false;
    	}
    	if(sc.getStatus()==1 && sc.getExpirationDate().after(Calendar.getInstance().getTime())){
    		flag = true;
    	}
    	return flag;
    }

	@Override
	@Transactional
	public Map<String, Object> applyOrderPlan(String code, int osId,
			String period, int scNum, long userId, OrderPlanVo orderPlanVos,
			String id, String accessId, String doUseCoupon, String doUseGift)
			throws Exception {
		logger.info(" order buy is begaining......");
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            List<OrderItemVo> orderItems = new ArrayList<OrderItemVo>();
            User user = userService.getUser(userId);
            if(user==null){
                log="userId is not exist!";
                map.put("log", log);
                return map;
            }
            UserBrand userBrand = userBrandService.getBrandByCode(user.getLevel());
            ServiceCatalog sc=serviceCatalogService.getByCode(code,accessId,user.getLevel());
            boolean userBrandFlag = false;
            if(this.validationServiceCatalog(sc)) {
                for(UserBrand scUserBrand : sc.getUserBrand()) {
                    if(scUserBrand.getId() == userBrand.getId()) {
                        userBrandFlag = true;
                    }
                }
            }else{
            	log="ServiceCatalog is not valid!";
                map.put("log", log);
                return map;
            }
            if(userBrandFlag == false) {
                log="ServiceCatalog is not exist!";
                map.put("log", log);
                return map;
            }
            
            for (int i = 0; i < scNum; i++) {
                OrderItemVo orderItemVo = addItemForRest(sc, osId, period);
                if(StringUtils.isBlank(orderItemVo.getImageId())) {
                    log="Operating system is not exist!";
                    map.put("log", log);
                    return map;
                }
                orderItems.add(orderItemVo);
            }
            
            // 根据企业用户Id获取其账户信息
            Account account = accountService.getAccountByUserId(userId);
            Integer couponsRebateRate = setValueForNull(account.getCouponsRebateRate()); 
            Integer giftsRebateRate = setValueForNull(account.getGiftsRebateRate());

            //根据套餐信息生成订单
            Order order = orderService.submitOrder(orderItems, user, couponsRebateRate, giftsRebateRate);
            if(order==null){
                log="order is not correct!";
                map.put("log", log);
                return map;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        logger.info(" order apply is ending......");
        return map;
	}

}
