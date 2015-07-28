/* 
 * 文 件 名:  DemandRestServiceImpl.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  ljg 
 * 修改时间:  2014-3-19 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hisoft.hscloud.bss.sla.om.entity.OrderProduct;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.om.util.SubmitOrderData;
import com.hisoft.hscloud.bss.sla.sc.entity.Cpu;
import com.hisoft.hscloud.bss.sla.sc.entity.Disk;
import com.hisoft.hscloud.bss.sla.sc.entity.ExtDisk;
import com.hisoft.hscloud.bss.sla.sc.entity.Network;
import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.Ram;
import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.bss.sla.sc.service.IServerZoneService;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceItemService;
import com.hisoft.hscloud.bss.sla.sc.utils.CloudCache;
import com.hisoft.hscloud.common.entity.DefaultIsolationConfig;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.service.DefaultIsolationConfigService;
import com.hisoft.hscloud.common.service.UserBrandService;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.DomainService;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.hisoft.hscloud.mail.service.MailService;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.service.ImageService;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.ImageVO;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
import com.hisoft.hscloud.web.service.DemandRestService;
import com.hisoft.hscloud.web.util.CalendarUtil;
import com.hisoft.hscloud.web.vo.DemandItemsVo;
import com.hisoft.hscloud.web.vo.OrderPlanVo;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author ljg
 * @version [版本号, 2014-3-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class DemandRestServiceImpl implements DemandRestService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private UserBrandService userBrandService;
	@Resource
	private AccountService accountService;
	@Resource
	private OrderService orderService;
	@Resource
	private MessageService messageService;
	@Resource
	private MailService mailService;
	@Resource
	private IServerZoneService serverZoneService;
	@Resource
	private Operation operation;
	@Resource
	private IncomingLogService incomingLogService;
	@Resource
	private DomainService domainService;
	@Resource
	private DefaultIsolationConfigService defaultIsolationConfigService;
	@Resource//@Autowired
	private ImageService imageService;
	@Resource
	private IServiceItemService serviceItemService;

	/**
	 * @param submitData
	 * @param id
	 * @param accessId
	 * @param doUseCoupon
	 * @param doUseGift
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public Map<String, Object> demandBuyDirect(SubmitOrderData submitData,
			OrderPlanVo orderPlanVos, String id, DemandItemsVo demandItemsVo,
			boolean patmentFlag) throws Exception {
		Order order = null;
		logger.info(" order buy is begaining......");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = submitData.getUser();
			if (user == null) {
				map.put("log", "userId is not exist!");
				return map;
			}
			int vmNum = submitData.getVmNum();
			OrderItem orderItem = generateOrder4Need(submitData, demandItemsVo);
			OrderUtils.getPriceForOrderItemFrom3P(orderItem, demandItemsVo.getAccessId(),submitData.getUser().getLevel(),demandItemsVo.getDatacenter());
			List<OrderItem> items = new ArrayList<OrderItem>();
			items.add(orderItem);
			for (int i = 0; i < vmNum - 1; i++) {
				OrderItem itemNew = new OrderItem();
				itemNew.copyItem(orderItem, 2);
				items.add(itemNew);
			}

			// 根据套餐信息生成订单
			// Order order = orderService.submitOrder(orderItems, user,
			// couponsRebateRate, giftsRebateRate);
			order = new Order();
			order.submit(items, user, "Unpaid", getRebate(user),
					getGiftRebate(user));
			order.setOrderType((short) 2);
			long orderId = orderService.saveOrder(order);
			order = orderService.findOrderById(orderId);
			if (order == null) {
				map.put("log", "order is not correct!");
				return map;
			}
			orderPlanVos.setOrderno(order.getOrderNo());
			orderPlanVos.setTotalnum(submitData.getVmNum());
			orderPlanVos.setCreateon(CalendarUtil.fomartDate(order
					.getCreateDate()));
			//是否付费
			if(patmentFlag){
				map = this.paymentOrder(user, order, demandItemsVo, id,
						orderPlanVos, map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.info(" order buy is ending......");
		return map;
	}

	private Integer setValueForNull(Integer rebateRate) {
		if (rebateRate == null) {
			return 0;
		}
		return rebateRate;
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
		map.put("webSiteUrl", domain.getPublishingAddress());

		map.put("webAddress", domain.getPublishingAddress());

		mailService.saveEmail(user.getEmail(), null, type, domain.getId(), map);
		logger.info("save email for rest is ending......");
	}

	private OrderItem generateOrder4Need(SubmitOrderData submitData,
			DemandItemsVo demandItemsVo) throws Exception {
		List<OrderProduct> products = new ArrayList<OrderProduct>();
		FlavorVO flavorVO = new FlavorVO();
		String imageId = "";
		this.setCPU(products, flavorVO, demandItemsVo.getVcpu());
		this.setRam(products, flavorVO, demandItemsVo.getMemSize());
		this.setOS(products, flavorVO, demandItemsVo.getOsId(), imageId);
		this.setNetwork(products, demandItemsVo.getBandwidth());
		this.setExtDisk(products, demandItemsVo.getExtDisk());
		this.setIP(products, submitData);
		OrderItem item = new OrderItem();
		item.setUsePointOrNot(true);
		item.setPlanId(submitData.getPlanId());
		item.setPricePeriod(String.valueOf(submitData.getBuyPeriod()));
		item.setPricePeriodType("1");
		item.setPriceType("1");
		item.setQuantity(1);
		// List<ServerZone>
		// zoneList=serverZoneService.getCustomZonesByGroupId(submitData.getZoneGroupId());
		List<ServerZone> zoneList = serverZoneService
				.getCustomZonesByGroupCode(demandItemsVo.getDatacenter());
		if (zoneList != null && zoneList.size() > 0) {
			StringBuilder zonesStr = new StringBuilder(zoneList.get(0)
					.getCode());
			for (int i = 1; i < zoneList.size(); i++) {
				zonesStr.append(",").append(zoneList.get(i).getCode());
			}
			item.setNodeName(zonesStr.toString());
		}
		item.setImageId(imageId);
		String flavorId = operation.createFlavor(flavorVO);
		item.setFlavorId(flavorId);
		item.setOrderProducts(products);
		return item;
	}

	private void setCPU(List<OrderProduct> products, FlavorVO flavorVO,
			String vcpu) {
		OrderProduct product = new OrderProduct();
		Cpu cpu = (Cpu) serviceItemService.getSIBySize(Integer.valueOf(vcpu),
				"cpu");
		if (cpu != null) {
			product.setModel(cpu.getModel());
//			product.setPrice(cpu.getPrice());
			product.setExtColumn("");
			product.setProductName(cpu.getName());
			product.setSpec(vcpu);
			product.setType(cpu.getServiceType());
			product.setUnit("core");
			products.add(product);
			flavorVO.setVcpus(cpu.getCoreNum());
		}
	}

	private void setRam(List<OrderProduct> products, FlavorVO flavorVO,
			String memSize) {
		OrderProduct product = new OrderProduct();
		Ram ram = (Ram) serviceItemService.getSIBySize(
				Integer.valueOf(memSize)*1024, "ram");
		if (ram != null) {
			product.setModel(ram.getModel());
//			product.setPrice(ram.getPrice());
			product.setExtColumn("");
			product.setProductName(ram.getName());
			product.setSpec(String.valueOf(ram.getSize()));
			product.setType(ram.getServiceType());
			product.setUnit("M");
			products.add(product);
			flavorVO.setRam(ram.getSize());
		}
	}

	private void setOS(List<OrderProduct> products, FlavorVO flavorVO,
			String osId, String imageId) {
		OrderProduct product = new OrderProduct();
		Os os = (Os) serviceItemService.getSIById(Integer.valueOf(osId));
		if (os != null) {
			product.setModel(os.getArch());
//			product.setPrice(os.getPrice());
			product.setExtColumn(osId);
			product.setProductName(os.getName());
			product.setSpec(os.getImageId());
			product.setType(os.getServiceType());
			product.setUnit("");
			products.add(product);
			imageId = os.getImageId();
		}
		List<ImageVO> images = imageService.showImageList();
		for (ImageVO imageVO : images) {
			if (imageVO.getId().equals(os.getImageId())) {
				String defauleSize = imageVO.getSize();
				flavorVO.setDisk(Integer.valueOf(defauleSize));
				this.setDisk(products, flavorVO, defauleSize);
			}
		}
	}

	private void setDisk(List<OrderProduct> products, FlavorVO flavorVO,
			String diskSize) {
		OrderProduct product = new OrderProduct();
		Disk disk = (Disk) serviceItemService.getSIBySize(
				Integer.valueOf(diskSize), "disk");
		if (disk != null) {
			product.setModel(disk.getModel());
//			product.setPrice(disk.getPrice());
			product.setExtColumn("");
			product.setProductName(disk.getName());
			product.setSpec(diskSize);
			product.setType(disk.getServiceType());
			product.setUnit("G");
			products.add(product);
			flavorVO.setDisk(disk.getCapacity());
		} else {
			product.setProductName("systemDisk");
			product.setModel("");
			product.setExtColumn("");
			product.setType(3);
			product.setSpec(diskSize);
			product.setUnit("G");
			products.add(product);
			flavorVO.setDisk(Integer.parseInt(diskSize));
		}
	}

	private void setNetwork(List<OrderProduct> products, String bandwidth) {
		OrderProduct product = new OrderProduct();
		Network network = (Network) serviceItemService.getSIBySize(
				Integer.valueOf(bandwidth), "network");
		if (network != null) {
			product.setModel("");
//			product.setPrice(network.getPrice());
			product.setExtColumn("");
			product.setProductName(network.getName());
			product.setSpec(bandwidth);
			product.setType(network.getServiceType());
			product.setUnit("M");
			products.add(product);
		}
	}

	private void setExtDisk(List<OrderProduct> products, String extDiskSize) {
		OrderProduct product = new OrderProduct();
		if (!"".equals(extDiskSize)) {
			String[] extDiskArr = extDiskSize.split(",");
			for (String disk : extDiskArr) {
				ExtDisk extDisk = (ExtDisk) serviceItemService.getSIBySize(
						Integer.valueOf(disk), "extdisk");
				if (extDisk != null) {
					product.setModel(extDisk.getModel());
//					product.setPrice(extDisk.getPrice());
					product.setExtColumn("");
					product.setProductName(extDisk.getName());
					product.setSpec(extDiskSize);
					product.setType(extDisk.getServiceType());
					product.setUnit("G");
					products.add(product);
				}
			}
		}
	}

	private void setIP(List<OrderProduct> products, SubmitOrderData submitData) {
		OrderProduct product = new OrderProduct();
		product.setProductName("IP");
		product.setSpec(String.valueOf(submitData.getIpNum()));
		product.setType(9);
		products.add(product);
	}

	private Integer getRebate(User user) {
		Account account = accountService.getAccountByUserId(user.getId());
		if (account.getCouponsRebateRate() == null
				|| account.getCouponsRebateRate().intValue() == 0) {
			UserBrand ub = userBrandService.getBrandByCode(user.getLevel());
			if (ub.getRebateRate() != null
					&& ub.getRebateRate().intValue() != 0) {
				return ub.getRebateRate();
			} else {
				return 0;
			}
		} else {
			return account.getCouponsRebateRate();
		}
	}

	private Integer getGiftRebate(User user) {
		Account account = accountService.getAccountByUserId(user.getId());
		if (account.getGiftsRebateRate() == null
				|| account.getGiftsRebateRate().intValue() == 0) {
			UserBrand ub = userBrandService.getBrandByCode(user.getLevel());
			if (ub.getGiftsDiscountRate() != null
					&& ub.getGiftsDiscountRate().intValue() != 0) {
				return ub.getGiftsDiscountRate();
			} else {
				return 0;
			}
		} else {
			return account.getGiftsRebateRate();
		}
	}

	private Map<String, Object> paymentOrder(User user, Order order,
			DemandItemsVo demandItemsVo, String id, OrderPlanVo orderPlanVos,
			Map<String, Object> map) throws Exception {
		// 根据企业用户Id获取其账户信息
		Account account = accountService.getAccountByUserId(user.getId());
		Integer couponsRebateRate = this.setValueForNull(account
				.getCouponsRebateRate());
		Integer giftsRebateRate = this.setValueForNull(account
				.getGiftsRebateRate());

		// 如果是企业用户则返回自己ID如果是子用户返回其关联的企业用户ID
		Long accountId = account.getId();
		// 判断余额是否大于支付金额
		boolean checkResult = accountService.checkBalance(accountId,
				order.getTotalPrice());

		String result = "";
		Map<NovaServerForCreate, HcEventResource> rabbitMQVM = null;
		if (checkResult) {// 如果余额大于支付金额进行订单支付业务操作
			BigDecimal customerPoints = new BigDecimal(0);
			if ("true".equalsIgnoreCase(demandItemsVo.getUseCoupon())) { // 使用返点
				BigDecimal rebateRate = new BigDecimal(couponsRebateRate);
				BigDecimal orderValue = new BigDecimal(order.getTotalPrice()
						.intValue());
				customerPoints = rebateRate.multiply(orderValue).divide(
						new BigDecimal(100));
			}
			BigDecimal accountPoints = account.getCoupons();
			if (customerPoints.compareTo(BigDecimal.ZERO) >= 0
					&& accountPoints.compareTo(customerPoints) >= 0) {
				order = OrderUtils.afterPoint(order, customerPoints);
			} else {
				order = OrderUtils.afterPoint(order, accountPoints);
			}

			BigDecimal customerGift = new BigDecimal(0);
			if ("true".equalsIgnoreCase(demandItemsVo.getUseGift())) { // 使用返点
				BigDecimal rebateRate = new BigDecimal(giftsRebateRate);
				BigDecimal orderValue = new BigDecimal(order.getTotalPrice()
						.intValue());
				customerGift = rebateRate.multiply(orderValue).divide(
						new BigDecimal(100));
			}
			BigDecimal accountGift = account.getGiftsBalance();
			if (customerGift.compareTo(BigDecimal.ZERO) >= 0
					&& accountGift.compareTo(customerGift) >= 0) {
				order = OrderUtils.afterPoint(order, customerGift);
			} else {
				order = OrderUtils.afterPoint(order, accountGift);
			}

			Message message = OrderUtils.orderGenMessageForOrderPay(order,
					(short) 5);
			messageService.saveMessage(message);
			// 对账户进行扣款操作
			long transactionId = accountService.accountConsume(user.getId(),
					(short) 1, PaymentType.PAYMENT_ONLINE.getIndex(),
					ServiceType.SERVICE_CLOUD_COMPUTER.getIndex(), accountId,
					message.getMessage(), order.getId(),
					ConsumeType.CONSUME_BUY.getIndex(), order.getTotalAmount(),
					order.getTotalPointAmount(), order.getTotalGiftAmount());

			// 对订单及订单项进行支付的业务操作
			order = orderService.pay(order);
			// 订单支付完成发送邮件提醒
			// 绑定邮件模版中需要的参数
			Map<String, String> orderMailTemplate = OrderUtils
					.generateMailVariable(order);

			saveEmail(user, orderMailTemplate,
					MailTemplateType.ORDER_PAID_TEMPLATE.getType());

			DefaultIsolationConfig isolationConfig = defaultIsolationConfigService
					.getIsolationConfigById(1L);

			List<CreateVmBean> createVmBeans = OrderUtils.getCreateVMBeans(
					order, isolationConfig);
			// 创建虚拟机
			ServerZone sz = null;
			sz = serverZoneService.getDefaultServerZone();
			rabbitMQVM = operation.createVmByAPI(createVmBeans,
					Long.toString(user.getId()), sz.getCode(), id,
					demandItemsVo.getAccessId());
			for (CreateVmBean cvb : createVmBeans) {
				VpdcReference_OrderItem vroi = operation
						.getOrderItemByOrderItemId(cvb.getOrder_item_id());
				String orderItemIdStr = cvb.getOrder_item_id();
				long referenceId = vroi.getVpdcRenferenceId();
				String orderNo = order.getOrderNo();
				long orderItemId = Long.parseLong(orderItemIdStr);
				for (OrderItem item : order.getItems()) {
					if (item.getId().longValue() == orderItemId) {
						IncomingLog incomingLog = OrderUtils.getIncomingLog(
								item, referenceId, accountId, transactionId,
								user.getEmail(), order.getId(), orderNo, user
										.getDomain().getId());
						incomingLogService.saveIncomingLog(incomingLog);
					}
				}
			}
			orderPlanVos.setConsume(order.getTotalPrice().toString());
			orderPlanVos.setCouponConsume(order.getTotalPointAmount()
					.toString());

			account = accountService.getAccountByUserId(user.getId());
			orderPlanVos.setBalance(account.getAccountBalance());
			orderPlanVos.setCouponBalance(account.getCouponsBalance());
			result = "success";
			map.put(result, rabbitMQVM);
		} else {
			result = "balance is not enough";
			map.put("log", " account balance don't have sufficient funds ");
		}
		return map;
	}
}
