package com.hisoft.hscloud.bss.sla.om.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.hisoft.hscloud.bss.billing.entity.IncomingLog;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.entity.OrderProduct;
import com.hisoft.hscloud.bss.sla.om.entity.RefundResult;
import com.hisoft.hscloud.bss.sla.om.entity.RefundVm;
import com.hisoft.hscloud.bss.sla.om.entity.UserRefund;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.service.impl.OrderServiceImpl;
import com.hisoft.hscloud.bss.sla.sc.entity.ScIsolationConfig;
import com.hisoft.hscloud.bss.sla.sc.utils.ProductType;
import com.hisoft.hscloud.common.entity.DefaultIsolationConfig;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.common.util.RESTUtil;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVmBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.CreateVpdcBean.VlanNetwork;

public class OrderUtils {
	@Autowired
	private OrderService orderService;
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public static Date getExpDateForOrderItem(String priceType,
			String pricePeriod, String pricePeriodType, Date beginDate)
			throws Exception {
		Date beginDateLocal = (beginDate == null ? new Date() : beginDate);
		Date expirationDate = null;
		int period = Integer.parseInt(pricePeriod);
		int priceTypeInt = Integer.parseInt(priceType);
		switch (priceTypeInt) {
		case 1: {
			int pricePeriodTypeInt = Integer.parseInt(pricePeriodType);
			switch (pricePeriodTypeInt) {
			case 0:
				expirationDate = DateUtils.addYears(beginDateLocal, period);
				break;
			case 1:
				expirationDate = DateUtils.addMonths(beginDateLocal, period);
				break;
			case 2:
				expirationDate = DateUtils.addHours(beginDateLocal, period);
				break;

			}
			break;
		}
		case 2: {
			expirationDate = DateUtils.addHours(beginDateLocal, 1);
			break;
		}
		case 3: {
			expirationDate = DateUtils.addMonths(beginDateLocal, 1);
			break;
		}
		case 4: {
			expirationDate = DateUtils.addYears(beginDateLocal, 1);
			break;
		}
		}
		return expirationDate;
	}

	public static Date getExpDateForOrderItem(String tryDuration, Date beginDate)
			throws Exception {
		Date beginDateLocal = (beginDate == null ? new Date() : beginDate);
		int days = Integer.parseInt(tryDuration);
		Date expirationDate = DateUtils.addDays(beginDateLocal, days);
		return expirationDate;
	}

	public static Date getExpDateForOrderItem(OrderItem item, Date beginDate)
			throws Exception {
		String priceType = item.getPriceType();
		String pricePeriod = item.getPricePeriod();
		String pricePeriodType = item.getPricePeriodType();
		Date beginDateLocal = (beginDate == null ? new Date() : beginDate);
		Date expirationDate = null;
		int period = Integer.parseInt(pricePeriod);
		int priceTypeInt = Integer.parseInt(priceType);
		switch (priceTypeInt) {
		case 1: {
			int pricePeriodTypeInt = Integer.parseInt(pricePeriodType);
			switch (pricePeriodTypeInt) {
			case 0:
				expirationDate = DateUtils.addDays(beginDateLocal, period*360);
				break;
			case 1:
				if(period==12){
					expirationDate = DateUtils.addDays(beginDateLocal, 365);
				}else{
					expirationDate = DateUtils.addDays(beginDateLocal, period*31);
				}
				break;
			case 2:
				expirationDate = DateUtils.addHours(beginDateLocal, period);
				break;

			}
			break;
		}
		case 2: {
			expirationDate = DateUtils.addHours(beginDateLocal, 1);
			break;
		}
		case 3: {
			expirationDate = DateUtils.addDays(beginDateLocal, 30);
			break;
		}
		case 4: {
			expirationDate = DateUtils.addDays(beginDateLocal, 360);
			break;
		}
		}
		return expirationDate;
	}

	public static Map<String, String> generateMailVariable(Order order)
			throws Exception {
		User user = order.getUser();
		List<OrderItem> items = order.getItems();
		StringBuilder sTotalPrice = new StringBuilder("");
		sTotalPrice.append("现金").append(order.getTotalAmount()).append(Constants.YUAN);
		sTotalPrice.append("+返点").append(order.getTotalPointAmount()!=null?order.getTotalPointAmount():0).append("点");
		sTotalPrice.append("+礼金").append(order.getTotalGiftAmount()!=null?order.getTotalGiftAmount():0).append(Constants.YUAN);
		sTotalPrice.append(" = ").append(order.getTotalPrice()).append(Constants.YUAN);
		Map<String, String> result = new HashMap<String, String>();
		String orderStatus = "";
		if (order.getStatus().ordinal() == 1) {
			orderStatus = "已完成";
		} else if (order.getStatus().ordinal() == 2) {
			orderStatus = "已取消";
		}
		result.put("totalPrice", sTotalPrice.toString());
		StringBuilder detailStr = new StringBuilder();
		for (OrderItem item : items) {
			if(order.getOrderType()==1){
				detailStr.append("<tr><td>").append(item.getServiceCatalogName())
				.append("x").append(item.getPricePeriod()).append("个月");
			}else{
				detailStr.append("<tr><td>").append(item.getPricePeriod()).append("个月");
			}
			detailStr.append("（现金消费").append(item.getAmount()).append("元").append("+")
			.append(" 返点消费");
			detailStr.append(item.getPointAmount()!=null?item.getPointAmount():0);
			detailStr.append("点+");
			detailStr.append(" 礼金消费");
			detailStr.append(item.getGiftAmount()!=null?item.getGiftAmount():0).append("元");
			detailStr.append("）x 1 = ").append(item.getPrice()).append("元</td></tr>");
		}
		result.put("orderDetail", detailStr.toString());
		result.put("email", user.getEmail());
		result.put("userName", user.getName());
		result.put("orderNo", order.getOrderNo());
		result.put("orderState", orderStatus);
		result.put("today",HsCloudDateUtil.transferDate2Str("yyyy-MM-dd",new Date()));
		result.put("webSiteUrl", user.getDomain().getUrl());
		result.put("createDate",order.getCreateDate()!=null?HsCloudDateUtil.transferDate2Str(order.getCreateDate()):"");
		result.put("updateDate",order.getUpdateDate()!=null?HsCloudDateUtil.transferDate2Str(order.getUpdateDate()):"");
		result.put("scQuantity", String.valueOf(items.size()));
		return result;
	}

	public static List<CreateVmBean> getCreateVMBeans(Map<OrderItem,ScIsolationConfig> dataForCreateVmBean) throws Exception {
		Set<OrderItem> orderItemList=dataForCreateVmBean.keySet();
		List<CreateVmBean> createVmBeans = new ArrayList<CreateVmBean>();
		for (OrderItem item : orderItemList) {
			CreateVmBean createVmBean = new CreateVmBean();
			createVmBean.setImageId(item.getImageId());
			createVmBean.setVcpus(item.getVmGoods().getvCpus());
			createVmBean.setVcpusType(item.getVmGoods().getCpuModel());
			createVmBean.setStart_time(item.getEffectiveDate());
			createVmBean.setEnd_time(item.getExpirationDate());
			createVmBean.setNetwork(item.getVmGoods().getBandwidth());
			createVmBean.setNetworkType(item.getVmGoods().getNetworkType());
			createVmBean.setFlavorId(item.getFlavorId());
			createVmBean.setRam(item.getVmGoods().getMemory());
			createVmBean.setRamType(item.getVmGoods().getMemoryModel());
			createVmBean.setAddDisk(item.getVmGoods().getAddDisk());
			createVmBean.setOrder_item_id(Long.toString(item.getId()));
			createVmBean.setDisk(item.getVmGoods().getDisk());
			createVmBean.setDiskType(item.getVmGoods().getDiskModel());
			createVmBean.setOsId(item.getVmGoods().getOsId());
			createVmBean.setOwner(String.valueOf(item.getOrder().getUser().getId()));
			createVmBean.setScId(new Integer(item.getServiceCatalogId()));
			createVmBean.setVmZone(item.getNodeName());
			createVmBean.setBuyType(0);
			ScIsolationConfig isolationConfig=dataForCreateVmBean.get(item);
			if(isolationConfig!=null){
				createVmBean.setCpuLimit(isolationConfig.getCpuLimit());
				createVmBean.setDiskRead(isolationConfig.getDiskRead());
				createVmBean.setDiskWrite(isolationConfig.getDiskWrite());
				createVmBean.setBwtIn(isolationConfig.getBandWidthUp());
				createVmBean.setBwtOut(isolationConfig.getBandWidthDown());
				createVmBean.setTcpConnIn(isolationConfig.getTcpConnectionUp());
				createVmBean.setTcpConnOut(isolationConfig.getTcpConnectionDown());
				createVmBean.setUdpConnIn(isolationConfig.getUdpConnectionUp());
				createVmBean.setUdpConnOut(isolationConfig.getUdpConnectionDown());
				createVmBean.setIpConnIn(isolationConfig.getIpConnectionUp());
				createVmBean.setIpConnOut(isolationConfig.getIpConnectionDown());
			}
			createVmBeans.add(createVmBean);
		}
		return createVmBeans;
	}
	public static List<CreateVmBean> getCreateVMBeans(Order order,DefaultIsolationConfig isolationConfig)
			throws Exception {
		List<CreateVmBean> createVmBeans = new ArrayList<CreateVmBean>();
		for (OrderItem item : order.getItems()) {
			CreateVmBean createVmBean = new CreateVmBean();
			String addDisk = "";
			createVmBean.setBuyType(1);
			for (OrderProduct op : item.getOrderProducts()) {
				int type = op.getType();
				switch (ProductType.getProductTypeByIndex(type)) {
				case CPU: {
					createVmBean.setVcpus(op.getSpec());
					createVmBean.setVcpusType(op.getModel());
					break;
				}
				case DISK:
					createVmBean.setDisk(op.getSpec());
					createVmBean.setDiskType(op.getModel());
					break;
				case EXTDISK:
					addDisk += op.getSpec() + ",";
					break;
				case IP:
					createVmBean.setIpNum(Integer.parseInt(op.getSpec()));
					break;
				case MEM:
					createVmBean.setRam(op.getSpec());
					createVmBean.setRamType(op.getModel());
					break;
				case NETWORK:
					createVmBean.setNetwork(op.getSpec());
					createVmBean.setNetworkType(op.getModel());
					break;
				case OS:
					createVmBean.setOsId(op.getExtColumn());
					break;
				default:
					break;
				}
			}
			if(StringUtils.isNotBlank(addDisk)){
				createVmBean.setAddDisk(addDisk.substring(0, addDisk.length() - 1));
			}
			createVmBean.setImageId(item.getImageId());
			if (item.getVpdcId() == null) {//直接通过主机购买的VM
				createVmBean.setVpdcId(null);
			} else {// 通过vpdc创建的VM
				createVmBean.setVpdcId(item.getVpdcId());
			}
			createVmBean.setStart_time(item.getEffectiveDate());
			createVmBean.setEnd_time(item.getExpirationDate());
			createVmBean.setFlavorId(item.getFlavorId());
			createVmBean.setOrder_item_id(Long.toString(item.getId()));
			createVmBean.setOwner(String.valueOf(order.getUser().getId()));
			createVmBean.setVmZone(item.getNodeName());
			if (isolationConfig != null) {
				createVmBean.setCpuLimit(isolationConfig.getCpuLimit());
				createVmBean.setDiskRead(isolationConfig.getDiskRead());
				createVmBean.setDiskWrite(isolationConfig.getDiskWrite());
				createVmBean.setBwtIn(isolationConfig.getBandWidthUp());
				createVmBean.setBwtOut(isolationConfig.getBandWidthDown());
				createVmBean.setTcpConnIn(isolationConfig.getTcpConnectionUp());
				createVmBean.setTcpConnOut(isolationConfig
						.getTcpConnectionDown());
				createVmBean.setUdpConnIn(isolationConfig.getUdpConnectionUp());
				createVmBean.setUdpConnOut(isolationConfig
						.getUdpConnectionDown());
				createVmBean.setIpConnIn(isolationConfig.getIpConnectionUp());
				createVmBean
						.setIpConnOut(isolationConfig.getIpConnectionDown());
			}
			// createVmBean.set
			createVmBeans.add(createVmBean);
		}
		return createVmBeans;
	}

	public static BigDecimal getRefund(OrderItem item,Date dayBeginToFeeLocal) throws Exception {
		String priceType = item.getPriceType();
		String pricePeriod = item.getPricePeriod();
		String pricePeriodType = item.getPricePeriodType();
		BigDecimal price = item.getAmount();
		Date beginDateLocal = item.getEffectiveDate();
		long period = Long.parseLong(pricePeriod);
		int priceTypeInt = Integer.parseInt(priceType);
		BigDecimal pricePerDay = BigDecimal.ZERO;
		long days = 0L;
		if (beginDateLocal == null || dayBeginToFeeLocal.before(beginDateLocal)) {
			return BigDecimal.ZERO;
		} else {
			days=getDaysBetweenDateByHour(dayBeginToFeeLocal,beginDateLocal);
		}
		switch (priceTypeInt) {
		case 1: {
			int pricePeriodTypeInt = Integer.parseInt(pricePeriodType);
			switch (pricePeriodTypeInt) {
			case 0:
				BigDecimal years = BigDecimal.valueOf(360L * period);
				pricePerDay = price.divide(years, 2, RoundingMode.HALF_UP);
				break;
			case 1:
				BigDecimal monthes = null;
				if(period==12){
					monthes=BigDecimal.valueOf(365L);
				}else{
					monthes=BigDecimal.valueOf(31L * period);
				}
				pricePerDay=price.divide(monthes, 2, RoundingMode.HALF_UP);
				break;
			case 2:
				pricePerDay=price.divide(new BigDecimal(period), 2, RoundingMode.HALF_UP);
			}
			break;
		}
		case 3: {
			BigDecimal month = BigDecimal.valueOf(30L);
			pricePerDay = price.divide(month, 2, RoundingMode.HALF_UP);
			break;
		}
		case 4: {
			BigDecimal year = BigDecimal.valueOf(360L);
			pricePerDay = price.divide(year, 2, RoundingMode.HALF_UP);
			break;
		}
		}
		BigDecimal consum = pricePerDay.multiply(BigDecimal.valueOf(days));
		return consum;
	}
    
	public static BigDecimal getRefundPointOrGift(OrderItem item,BigDecimal amount,Date dayBeginToFeeLocal) throws Exception {
		String priceType = item.getPriceType();
		String pricePeriod = item.getPricePeriod();
		String pricePeriodType = item.getPricePeriodType();
		Date beginDateLocal = item.getEffectiveDate();
		long period = Long.parseLong(pricePeriod);
		int priceTypeInt = Integer.parseInt(priceType);
		BigDecimal pricePerDay = BigDecimal.ZERO;
		long days = 0L;
		if (beginDateLocal == null || dayBeginToFeeLocal.before(beginDateLocal)) {
			return BigDecimal.ZERO;
		} else {
			days=getDaysBetweenDateByHour(dayBeginToFeeLocal,beginDateLocal);
		}
		switch (priceTypeInt) {
		case 1: {
			int pricePeriodTypeInt = Integer.parseInt(pricePeriodType);
			switch (pricePeriodTypeInt) {
			case 0:
				BigDecimal years = BigDecimal.valueOf(360L * period);
				pricePerDay = amount.divide(years, 2, RoundingMode.HALF_UP);
				break;
			case 1:
				BigDecimal monthes = null;
				if(period==12){
					monthes=BigDecimal.valueOf(365L);
				}else{
					monthes=BigDecimal.valueOf(31L * period);
				}
				pricePerDay = amount.divide(monthes, 2, RoundingMode.HALF_UP);
				break;
			case 2:
				pricePerDay=amount.divide(new BigDecimal(period), 2, RoundingMode.HALF_UP);
			}
			break;
		}
		case 3: {
			BigDecimal month = BigDecimal.valueOf(30L);
			pricePerDay = amount.divide(month, 2, RoundingMode.HALF_UP);
			break;
		}
		case 4: {
			BigDecimal year = BigDecimal.valueOf(360L);
			pricePerDay = amount.divide(year, 2, RoundingMode.HALF_UP);
			break;
		}
		}
		BigDecimal consum = pricePerDay.multiply(BigDecimal.valueOf(days));
		return consum;
	}
	
	public static UserRefund getUserRefund(Long orderId, Long accountId,
			Long adminId, BigDecimal refund) throws Exception {
		UserRefund ur = new UserRefund();
		Date now = new Date();
		ur.setOrderId(orderId);
		ur.setAccountId(accountId);
		ur.setAudit(true);
		ur.setRefund(refund);
		ur.setRefundOn(now);
		ur.setFinishOn(new Date());
		ur.setRefundBy(adminId);
		StringBuilder remark = new StringBuilder();
		remark.append("orderId:")
				.append(orderId)
				.append(" at ")
				.append(HsCloudDateUtil.transferDate2Str("yyyy-MM-dd HH:mm:ss",
						now)).append(" refund:").append(refund)
				.append(" operated by").append("adminId:").append(adminId);
		ur.setRemark(remark.toString());
		return ur;
	}

	public static RefundVm getRefundVm(Long referenceId, Long userRefundId)
			throws Exception {
		RefundVm rf = new RefundVm();
		rf.setReferenceId(referenceId);
		rf.setRefundId(userRefundId);
		return rf;
	}

	public static Message orderGenMessageForOrderPay(Order order,
			short operationType) {
		Message message = new Message();
		Long userId=order.getUser().getId();
		StringBuilder messageStr = new StringBuilder();
		messageStr.append("订单：").append(order.getOrderNo()).append("执行")
				.append(OrderOperationType.getItem(operationType).getI18n())
				.append("操作").append("，现金消费：").append(order.getTotalAmount()).append("元");
		if(order.getTotalPointAmount()!=null&&order.getTotalAmount()!=null){
			messageStr.append("，返点消费").append(order.getTotalPointAmount()).append("点");
		}
		if(order.getTotalGiftAmount()!=null&&order.getTotalAmount()!=null){
			messageStr.append("，礼金消费").append(order.getTotalGiftAmount()).append("元");
		}
		message.setMessage(messageStr.toString());
		message.setMessageType(2);
		message.setUserId(userId);
		return message;
	}
	public static Message orderGenMessage(String vmName,Order order,long userId,
			short operationType) {
		Message message = new Message();
		StringBuilder messageStr = new StringBuilder();
		messageStr.append("云主机").append(vmName).append("执行")
				.append(OrderOperationType.getItem(operationType).getI18n())
				.append("操作").append("，现金消费：").append(order.getTotalAmount()).append("元");
		if(order.getTotalPointAmount()!=null&&order.getTotalAmount()!=null){
			messageStr.append("，返点消费").append(order.getTotalPointAmount()).append("点");
		}
		if(order.getTotalGiftAmount()!=null&&order.getTotalAmount()!=null){
			messageStr.append("，礼金消费").append(order.getTotalGiftAmount()).append("元");
		}
		message.setMessage(messageStr.toString());
		message.setMessageType(2);
		message.setUserId(userId);
		return message;
	}
	
	public static Message orderGenMessageForRefund(String vmName,RefundResult result,long userId,
			short operationType) {
		Message message = new Message();
		StringBuilder messageStr = new StringBuilder();
		messageStr.append("云主机").append(vmName).append("执行")
				.append(OrderOperationType.getItem(operationType).getI18n())
				.append("操作").append("，现金退款：").append(result.getRefund()).append("元");
		if(result.getRefundPoint()!=null){
			messageStr.append("，返点退回").append(result.getRefundPoint()).append("点");
		}
		if(result.getRefundGift()!=null){
			messageStr.append("，礼金退回").append(result.getRefundGift()).append("元");
		}
		message.setMessage(messageStr.toString());
		message.setMessageType(2);
		message.setUserId(userId);
		return message;
	}
	public static Message orderGenMessage(Order order,short operationType,Object... values) {
		Message message = new Message();
		Long userId=order.getUser().getId();
		StringBuilder messageStr = new StringBuilder();
		messageStr.append("订单：").append(order.getOrderNo()).append("执行")
				.append(OrderOperationType.getItem(operationType).getI18n())
				.append("操作");
		message.setMessage(messageStr.toString());
		message.setMessageType(2);
		message.setUserId(userId);
		return message;
	}
	
	public static long getDaysBetweenDateByHour(Date date1,Date date2){
		long days=0;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long diff = time1 - time2;
		long oneDayTime = 24 * 60 * 60 * 1000;
		if (diff % oneDayTime == 0) {
			days = diff / (oneDayTime);
		} else {
			days = diff / (oneDayTime) + 1;
		}
		return days;
	}
	
	public static long getDaysBetweenDateByDay(Date date1,Date date2){
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		long day1=c1.get(Calendar.DAY_OF_YEAR);
		long day2=c2.get(Calendar.DAY_OF_YEAR);
		long diffDays=day1-day2+1; 
		return diffDays;
	}
	
	
	public static int getUnusedDays(Date effectiveDate,Date expirationDate,Date now){
		long totalDays=getDaysBetweenDateByHour(expirationDate,effectiveDate);
		long usedDays=getDaysBetweenDateByHour(now,effectiveDate);
		int unusedDays=(int)(totalDays-usedDays);
		return unusedDays;
	}
	
	public static IncomingLog getIncomingLog(OrderItem item,long referenceId,long accountId,long transactionId,String email,long orderId,String orderNo,Long domainId){
		IncomingLog incomingLog=new IncomingLog();
		incomingLog.setOrderItemId(item.getId());
		incomingLog.setAccountId(accountId);
		BigDecimal amount=item.getAmount();
		BigDecimal dayPrice=null;
		if(item.getPricePeriod().equals("12")){
			dayPrice=amount.divide(BigDecimal.valueOf(365L), 2, RoundingMode.DOWN);
		}else{
			dayPrice=amount.divide(BigDecimal.valueOf(Integer.parseInt(item.getPricePeriod())*31L), 2, RoundingMode.DOWN);
		}
		incomingLog.setAmount(amount);
		incomingLog.setDayPrice(dayPrice);
		incomingLog.setEffectiveDate(item.getEffectiveDate());
		incomingLog.setEmail(email);
		if(item.getOrder().getOrderType()==1){
			incomingLog.setScId(Integer.parseInt(item.getServiceCatalogId()));
			incomingLog.setScName(item.getServiceCatalogName());
		}
		incomingLog.setExpirationDate(item.getExpirationDate());
		incomingLog.setIncomingType((short)1);
		incomingLog.setObjectId(referenceId);
		incomingLog.setOrderId(orderId);
		incomingLog.setOrderNo(orderNo);
		incomingLog.setDomainId(domainId);
		incomingLog.setProductType((short)1);
		incomingLog.setTransactionId(transactionId);
		incomingLog.setTranscationType((short)1);
		return incomingLog;
	}
	
	public static Order afterPoint(Order order, BigDecimal points) {
		if (points.compareTo(BigDecimal.ZERO)>0) {
			BigDecimal remainPoints =points;
			BigDecimal orderTotalAmount = BigDecimal.ZERO;
			BigDecimal orderTotalPointAmount = BigDecimal.ZERO;
			Integer rebateRate=order.getRebateRate();
			if(rebateRate==null){
				rebateRate=new Integer(0);
			}
			for (OrderItem item : order.getItems()) {
				if (item.getUsePointOrNot()) {
					BigDecimal price = item.getPrice();
					BigDecimal pointAmount = price.multiply(
							new BigDecimal(rebateRate)).divide(
							new BigDecimal(100),2,RoundingMode.HALF_UP);
					pointAmount=price.min(pointAmount);
					BigDecimal localRemainPoints=remainPoints;
					remainPoints = remainPoints.subtract(pointAmount);
					if (remainPoints.compareTo(BigDecimal.ZERO) > 0) {
						item.setAmount(price.subtract(pointAmount));
						item.setPointAmount(pointAmount);
					} else {
						item.setAmount(price.subtract(localRemainPoints));
						item.setPointAmount(localRemainPoints);
						break;
					}
				}
			}

			for (OrderItem item : order.getItems()) {
				orderTotalAmount = orderTotalAmount.add(item.getAmount());
				if (item.getPointAmount() != null) {
					orderTotalPointAmount = orderTotalPointAmount.add(item
							.getPointAmount());
				}

			}
			
			order.setTotalAmount(orderTotalAmount);
			order.setTotalPointAmount(orderTotalPointAmount);
		}else{
			order.setTotalPointAmount(BigDecimal.ZERO);
		}
		
		return order;
	}
	
	public static Order afterGift(Order order, BigDecimal gifts) {
		if (gifts.compareTo(BigDecimal.ZERO)>0) {
			BigDecimal remainGifts =gifts;
			BigDecimal orderTotalAmount = BigDecimal.ZERO;
			BigDecimal orderTotalGiftAmount = BigDecimal.ZERO;
			Integer rebateRate=order.getGiftsRebateRate();
			if(rebateRate==null){
				rebateRate=new Integer(0);
			}
			for (OrderItem item : order.getItems()) {
				if (item.getUseGiftOrNot()) {
					BigDecimal amount = item.getAmount();
					BigDecimal giftAmount = item.getPrice().multiply(
							new BigDecimal(rebateRate)).divide(
							new BigDecimal(100),2,RoundingMode.HALF_UP);
					giftAmount=amount.min(giftAmount);
					BigDecimal localRemainGifts=remainGifts;
					remainGifts = remainGifts.subtract(giftAmount);
					if (remainGifts.compareTo(BigDecimal.ZERO) > 0) {
						item.setAmount(amount.subtract(giftAmount));
						item.setGiftAmount(giftAmount);
					} else {
						item.setAmount(amount.subtract(localRemainGifts));
						item.setGiftAmount(localRemainGifts);
						break;
					}
				}
			}

			for (OrderItem item : order.getItems()) {
				orderTotalAmount = orderTotalAmount.add(item.getAmount());
				if (item.getGiftAmount() != null) {
					orderTotalGiftAmount = orderTotalGiftAmount.add(item
							.getGiftAmount());
				}

			}
			
			order.setTotalAmount(orderTotalAmount);
			order.setTotalGiftAmount(orderTotalGiftAmount);
		}else{
			order.setTotalGiftAmount(BigDecimal.ZERO);
		}
		
		return order;
	}
	
	public static BigDecimal getActuallyOrderAmount(BigDecimal orderPrice,BigDecimal accountPoints,BigDecimal rebateRate){
		BigDecimal actuallyRebate=BigDecimal.ZERO;
		if(rebateRate!=null){
			actuallyRebate=rebateRate;
		}
		BigDecimal pointAmount=orderPrice.multiply(actuallyRebate).divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
		BigDecimal result=null;
		if(accountPoints.compareTo(pointAmount)>=0){
			result=orderPrice.subtract(pointAmount);
		}else{
			result=orderPrice.subtract(accountPoints);
		}
		return result;
	}
	public static BigDecimal getActuallyOrderAmount(BigDecimal orderPrice,BigDecimal amount,BigDecimal accountPoints,BigDecimal rebateRate){
		BigDecimal actuallyRebate=BigDecimal.ZERO;
		if(rebateRate!=null){
			actuallyRebate=rebateRate;
		}
		BigDecimal pointAmount=orderPrice.multiply(actuallyRebate).divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
		BigDecimal result=null;
		if(accountPoints.compareTo(pointAmount)>=0){
			result=amount.subtract(pointAmount);
		}else{
			result=amount.subtract(accountPoints);
		}
		return result;
	}
	private static String getRequestPriceURLFrom3P(OrderItem item,String domainCode)throws Exception{
		String restServer=PropertiesUtils.getProperties("common.properties").get("demandBuyPriceRequestUrl");
		StringBuilder requestURL=new StringBuilder(restServer);
		String addDisk="";
		for(OrderProduct p:item.getOrderProducts()){
			switch (p.getType()) {
			case 1: {
				requestURL.append("CPU=").append(p.getSpec()).append("&");
				break;
			}
			case 2: {
				requestURL.append("memory=").append(p.getSpec()).append("&");
				break;
			}
			case 5: {
				requestURL.append("bandwidth=").append(p.getSpec()).append("&");
				break;
			}
			case 8: {
				addDisk+=(p.getSpec()+",");
				break;
			}
			case 9: {
				requestURL.append("IP=").append(p.getSpec()).append("&"); 
			}
			}
		}
		if(StringUtils.isNotBlank(addDisk)){
			requestURL.append("extDisk=").append(addDisk.substring(0,addDisk.length()-1)).append("&");
		}
		requestURL.append("planId=").append(item.getPlanId()).append("&");
		requestURL.append("payMonth=").append(item.getPricePeriod()).append("&");
		requestURL.append("upgrade=").append(0).append("&");
		requestURL.append("domainCode=").append(domainCode);
		return requestURL.toString();
	}	
	
	public static void getPriceForOrderItemFrom3P(OrderItem item,String domainCode)throws Exception{
		String price=RESTUtil.load(getRequestPriceURLFrom3P(item,domainCode));
		if(StringUtils.isBlank(price)||"0".equals(price)){
			throw new Exception("submit order for demand get price from 3p exception.");
		}
		item.setAmount(new BigDecimal(price));
		item.setPrice(new BigDecimal(price));
	}

	public static CreateVpdcBean getCreateVpdcBean(Order order, long templateId) throws Exception {
		List<OrderItem> items = order.getItems();
		OrderItem orderItem = null;
		if (items != null && items.size() > 0) {
			orderItem = items.get(0);
		}
		CreateVpdcBean createVpdcBean = new CreateVpdcBean();
		VlanNetwork vn = createVpdcBean.new VlanNetwork();
		createVpdcBean.setVpdcType(Constants.VPDC_ROUTED);// 路由模式
		createVpdcBean.setZoneGroup(Long.parseLong(orderItem.getExtColumn()));
		createVpdcBean.setName(orderItem.getVpdcName());
		createVpdcBean.setUseLong(Integer.valueOf(orderItem.getPricePeriod()));
		createVpdcBean.setStart_time(orderItem.getEffectiveDate());
		createVpdcBean.setEnd_time(orderItem.getExpirationDate());
		createVpdcBean.setOrder_item_id(orderItem.getId().toString());
		createVpdcBean.setRouterTemplateId(templateId);
		List<OrderProduct> op_list = orderItem.getOrderProducts();
		for (OrderProduct orderProduct : op_list) {
			switch (orderProduct.getType()) {
				case 1:// cpu类型
					createVpdcBean.setCpuCore(Integer.valueOf(orderProduct.getSpec()));
					break;
				case 2:// ram类型
					createVpdcBean.setMemSize(Integer.valueOf(orderProduct.getSpec()));
					break;
				case 3:// disk类型
					createVpdcBean.setDiskCapacity(Integer.valueOf(orderProduct.getSpec()));
					break;
				case 4:// os类型
					createVpdcBean.setOsId(Integer.valueOf(orderProduct.getExtColumn()));
					createVpdcBean.setRouterImage(orderProduct.getSpec());
					break;
				case 5:// network类型
					createVpdcBean.setBandWidth(Integer.valueOf(orderProduct.getSpec()));
					break;
				case 10:// vlan类型
					vn.setDns1(orderProduct.getExtColumn().split(",")[0]);
					vn.setDns2(orderProduct.getExtColumn().split(",")[1]);
					vn.setSize(Integer.parseInt(orderProduct.getSpec()));
					break;
			}
		}
		List<VlanNetwork> vlans = new ArrayList<VlanNetwork>();
		vlans.add(vn);
		createVpdcBean.setVlans(vlans);
		createVpdcBean.setZones(orderItem.getNodeName());
		return createVpdcBean;
	}

	@Autowired
	public String generateOrderNo(User user) {
		String orderHead = "P";
		String platform;
		long domainId = user.getDomain().getId();
		long domainIdMode =(int) (domainId/10);
		if(domainIdMode==0){
			String newDomainIdMode = '0' + String.valueOf(domainId);
			platform = newDomainIdMode;
		}else{
			platform = String.valueOf(domainIdMode);
		}	
		SimpleDateFormat sdf=new SimpleDateFormat("ddMMyy");
		String date = sdf.format(new java.util.Date()); 
		
		return orderHead + platform + date;
	}
	
	private static String getRequestPriceURLFrom3P(OrderItem item,String domainCode,String brandCode,String zoneGroupCode)throws Exception{
		String restServer=PropertiesUtils.getProperties("common.properties").get("demandBuyPriceRequestUrl");
		StringBuilder requestURL=new StringBuilder(restServer);
		String cpu="";
		String memory="";
		String bandwidth="";
		String addDisk="";
		String ip="";
		for(OrderProduct p:item.getOrderProducts()){
			switch (p.getType()) {
			case 1: {
				cpu=p.getSpec();
				break;
			}
			case 2: {
				memory=p.getSpec();
				break;
			}
			case 5: {
				bandwidth=p.getSpec();
				break;
			}
			case 8: {
				addDisk=p.getSpec();
				break;
			}
			case 9: {
				ip=p.getSpec(); 
			}
			}
		}		
		if("defaultBrand".equals(brandCode)){
			requestURL.append("/0");
		}else {
			requestURL.append("/1");
		}
		requestURL.append("/").append(cpu).append("/").append(Integer.valueOf(memory)/1024).append("/").append(bandwidth);
		requestURL.append("/").append(addDisk).append("/").append(ip).append("/");
		requestURL.append(item.getPricePeriod()).append("/").append("1");
		return requestURL.toString();
	}
	
	public static void getPriceForOrderItemFrom3P(OrderItem item,String domainCode,String brandCode,String zoneGroupCode)throws Exception{
		String price=RESTUtil.load(getRequestPriceURLFrom3P(item,domainCode,brandCode,zoneGroupCode));
		if(StringUtils.isBlank(price)){
			throw new Exception("submit order for demand get price from 3p exception.");
		}
		item.setAmount(new BigDecimal(price));
		item.setPrice(new BigDecimal(price));
	}
	
}