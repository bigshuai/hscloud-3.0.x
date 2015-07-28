/**
 * @title OrderService.java
 * @package com.hisoft.hscloud.bss.sla.om.service.impl
 * @description 订单实体
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.dao.OrderDao;
import com.hisoft.hscloud.bss.sla.om.dao.OrderItemDao;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.Order.Status;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.entity.OrderProduct;
import com.hisoft.hscloud.bss.sla.om.entity.RefundResult;
import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.bss.sla.sc.dao.ServiceCatalogDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.utils.ProductType;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcDao;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.service.Operation;

/**
 * @description 订单业务类，查看，新增，取消，支付订单
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
@Service
public class OrderServiceImpl implements OrderService {
	private Logger logger = Logger.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private ServiceCatalogDao serviceCatalogDao;
	@Autowired
	private Operation operation;
	
	@Override
	public long saveOrder(Order order) throws HsCloudException {
		if(StringUtils.isNotEmpty(order.getOrderNo())){
			String orberMantissa=orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID);
			String orderNoNew = order.getOrderNo() + orberMantissa;
			order.setOrderNo(orderNoNew);
		}
		orderDao.saveOrder(order);
		return order.getId();
	}

	@Override
	@Transactional
	public boolean cancel(Long id) throws HsCloudException {
		boolean result = false;
		try {
			Order order = orderDao.getOrderById(id);
			if (order.getStatus().ordinal() == 0
					|| order.getStatus().ordinal() == 3) {
				orderDao.saveOrder(changeOrderStatus(order, "Cancelled"));
				result = true;
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Transactional(readOnly = true)
	public Page<Order> pageOrder(Page<Order> page, Order order,
			List<Sort> sorts, String query, List<Long> useList,
			boolean isSuperAdmin, Long domainId) throws HsCloudException {
		try {
			StringBuilder sb = new StringBuilder();
			Map<String, Object> values = new HashMap<String, Object>();

			pageOrderHql(order, sorts, useList, sb, values, query,
					isSuperAdmin, domainId);

			page = orderDao.findOrderByPage(page, sb.toString(), values);

			for (Order o : page.getResult()) {
				setOrderNullOnUser(o.getUser());
				// setOrderNullOnItems(o.getItems());
			}
			return page;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Page<Order> pageOrderWebSite(Page<Order> page, Order order,
			User user, List<Sort> sorts, String query) throws HsCloudException {
		try {
			StringBuilder sb = new StringBuilder();
			Map<String, Object> values = new HashMap<String, Object>();

			pageOrderHqlWebSite(order, user, sorts, sb, values, query);

			page = orderDao.findOrderByPage(page, sb.toString(), values);

			for (Order o : page.getResult()) {
				setOrderNullOnUser(o.getUser());
				// setOrderNullOnItems(o.getItems());
			}
			return page;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Order pay(Order order) throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("pay start");
		}
		try {
			List<OrderItem> items = order.getItems();
			Date now = new Date();
			for (OrderItem item : items) {
				item.setEffectiveDate(now);
				// 根据订单关联的套餐的购买时长得到失效日期
				item.setExpirationDate(OrderUtils.getExpDateForOrderItem(item,
						now));
			}
			order.setPayDate(now);// 支付完成更新支付时间
			orderDao.saveOrder(changeOrderStatus(order, "Paid"));
			if (logger.isDebugEnabled()) {
				logger.debug("pay end");
			}
			return order;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

	@Override
	public Order payV2(Order order, String vmState, Date effectiveDate,
			Date expirationDate) throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("pay start");
		}
		try {
			List<OrderItem> items = order.getItems();
			Date now = new Date();
			Date expirationDateResult = null;
			Date effectiveDateResult = null;
			for (OrderItem item : items) {
				if (vmState.equals("NotExist")) {
					effectiveDateResult = now;
					expirationDateResult = OrderUtils.getExpDateForOrderItem(
							item, effectiveDateResult);
				} else if (vmState.equals("Normal")) {
					expirationDateResult = expirationDate;
					effectiveDateResult = DateUtils.addDays(
							expirationDateResult, 1);
					expirationDateResult = OrderUtils.getExpDateForOrderItem(
							item, expirationDateResult);
				} else if (vmState.equals("Try")) {
					effectiveDateResult = effectiveDate;
					expirationDateResult = OrderUtils.getExpDateForOrderItem(
							item, effectiveDateResult);
				}
				item.setEffectiveDate(effectiveDateResult);
				// 根据订单关联的套餐的购买时长得到失效日期
				item.setExpirationDate(expirationDateResult);
			}
			order.setPayDate(now);// 支付完成更新支付时间
			orderDao.saveOrder(changeOrderStatus(order, "Paid"));
			if (logger.isDebugEnabled()) {
				logger.debug("pay end");
			}
			return order;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@Transactional
	public Order submitOrder(List<OrderItemVo> orderItems, User user,Integer rebateRate,Integer giftRebateRate)
			throws HsCloudException {
		try {
			Order order = prepareOrder(orderItems, user, "Unpaid",rebateRate,giftRebateRate);
			String orderNo = order.getOrderNo();
			String cur = orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID);
			String orderNoNew = orderNo+cur;
			order.setOrderNo(orderNoNew);
			order.setOrderType((short)1);
			orderDao.saveOrder(order);
			return order;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Transactional(readOnly = true)
	public List<OrderItemVo> getAllOrderItemsByOrder(Long id)
			throws HsCloudException {
		try {
			List<OrderItemVo> result = orderDao.getOrderItemVoByOrderId(id);
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	@Override
	public List<OrderItemVo> orderDetail(long orderId, short orderType)
			throws HsCloudException {
		try {
			List<OrderItemVo> result =new ArrayList<OrderItemVo>();
			if(orderType==1){
				result = orderDao.getOrderItemVoByOrderId(orderId);
			}else if(orderType==2|| orderType==3){
				Order order=orderDao.getOrderById(orderId);
				for(OrderItem item:order.getItems()){
					List<OrderProduct> products=item.getOrderProducts();
					String os="";
					String cpu="";
					String mem="";
					String disk="";
					String addDisk="";
					String network="";
					String ipNum="";
					for(OrderProduct op:products){
						int type=op.getType();
						switch(ProductType.getProductTypeByIndex(type)){
							case CPU:{
								cpu=op.getSpec();
								break;
							}
							case DISK:
								disk=op.getSpec();
								break;
							case EXTDISK:
								addDisk+=op.getSpec()+",";
								break;
							case IP:
								ipNum=op.getSpec();
								break;
							case MEM:
								mem=op.getSpec();
								break;
							case NETWORK:
								network=op.getSpec();
								break;
							case OS:
								os=op.getProductName();
								break;
							default:
								break;
						}
					}
					
					if(StringUtils.isNotBlank(addDisk)){
						addDisk=addDisk.substring(0, addDisk.length()-1);
					}
					OrderItemVo itemVo=new OrderItemVo(item.getId(),order.getCreateDate(),
							item.getPrice(),order.getOrderNo(),
							order.getTotalPrice(), os,cpu, mem,
							disk,addDisk, network, 
							item.getExpirationDate(),item.getEffectiveDate(),
							item.getAmount(),
							item.getPricePeriod(),order.getRebateRate(),
							item.getPointAmount(),item.getUsePointOrNot(),
							order.getTotalPointAmount(),order.getTotalAmount(),ipNum,order.getOrderType());
					result.add(itemVo);		
				}
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Order getOrderByOrderNo(String orderNo) throws HsCloudException {
		try {
			Order order = orderDao.getOrderByOrderNo(orderNo);
			return order;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	/**
	 * 
	 * @title: queryOrder
	 * @description 订单查询
	 * @return
	 * @return List<Order>
	 * @throws
	 * @version 1.0
	 * @author YuezhouLi
	 * @update 2012-8-17 上午10:59:30
	 */
	@Transactional(readOnly = true)
	public void queryOrder(QueryCondition condition) throws HsCloudException {
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			StringBuilder sb = new StringBuilder();
			Order order = condition.getOrder();
			User user = condition.getUser();
			List<Sort> sorts = condition.getSort();
			Date fromDate = condition.getFromDate();
			Date toDate = condition.getToDate();
			Page<Order> pages = condition.getPages();
			sb.append("from Order where 1=1 and user!=null ");
			if (order.getStatus() != Status.All) {
				sb.append(" and status = :status");
				values.put("status", order.getStatus());
			}
			if (order.getStatus().ordinal() == 0) {
				if (fromDate != null) {
					sb.append(" and createDate >= :fromDate");
					values.put("fromDate", fromDate);
				}

				if (toDate != null) {
					sb.append(" and createDate <= :toDate");
					values.put("toDate", toDate);
				}

			} else if (order.getStatus().ordinal() == 1) {
				if (fromDate != null) {
					sb.append(" and payDate >= :fromDate");
					values.put("fromDate", fromDate);
				}

				if (toDate != null) {
					sb.append(" and payDate <= :toDate");
					values.put("toDate", toDate);
				}
			} else if (order.getStatus().ordinal() == 2) {
				if (fromDate != null) {
					sb.append(" and updateDate >= :fromDate");
					values.put("fromDate", fromDate);
				}

				if (toDate != null) {
					sb.append(" and updateDate <= :toDate");
					values.put("toDate", toDate);
				}

			}
			if (StringUtils.isNotBlank(order.getOrderNo())) {
				if (order.getStatus().ordinal() == 2) {
					sb.append(" and ( orderNo like :orderNo");
					values.put("orderNo", "%" + order.getOrderNo() + "%");
					sb.append(" or remark like :remark )");
					values.put("remark", "%" + order.getOrderNo() + "%");
				} else {
					sb.append(" and orderNo like :orderNo");
					values.put("orderNo", "%" + order.getOrderNo() + "%");
				}
			}

			if (user != null) {
				sb.append(" and user = :user");
				values.put("user", user);
			}

			for (int i = 0; i < sorts.size(); i++) {
				Sort sort = sorts.get(i);

				if (i == 0) {
					sb.append(" order by ");
				}

				sb.append(sort.getProperty() + " " + sort.getDirection());

				if (i < sorts.size() - 1) {
					sb.append(",");
				}
			}
			pages = orderDao.findOrderByPage(pages, sb.toString(), values);

			for (Order o : pages.getResult()) {
				setOrderNullOnUser(o.getUser());
				// setOrderNullOnItems(o.getItems());
			}
			condition.setPages(pages);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	/**
	 * @title: prepareOrder
	 * @description 用一句话说明这个方法做什么
	 * @param orderItems
	 * @param user
	 * @return
	 * @throws Exception
	 *             设定文件
	 * @return Order 返回类型
	 * @throws
	 * @version 1.1
	 * @author MaDai
	 * @update 2012-5-18 下午5:58:13
	 */
	private Order prepareOrder(List<OrderItemVo> orderItems, User user,
			String orderStatus,Integer rebateRate,Integer giftRebateRate) throws Exception {
		List<OrderItem> items = new ArrayList<OrderItem>();
		for (OrderItemVo vo : orderItems) {
			OrderItem item = new OrderItem();
			item.submitItem(vo);
			items.add(item);
		}
		Order order = new Order();
		order.submit(items, user, orderStatus,rebateRate,giftRebateRate);
		return order;
	}

	/**
	 * @title: pageOrderHql
	 * @description 准备订单分页hql
	 * @param order
	 * @param user
	 * @param sorts
	 * @param sb
	 * @param values
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-16 下午3:08:16
	 */
	private void pageOrderHql(Order order, List<Sort> sorts,
			List<Long> userList, StringBuilder sb, Map<String, Object> values,
			String query, boolean isSuperAdmin, Long domainId) throws Exception {
		sb.append("from Order o where 1=1 and o.user != null");

		if (order.getStatus() != Status.All) {
			sb.append(" and o.status = :status");
			values.put("status", order.getStatus());
		}

		if (StringUtils.isNotBlank(query)) {
			sb.append(" and (o.orderNo like '%").append(query).append("%' or ");
			sb.append(" o.remark like '%").append(query).append("%' )");
		}

		if (domainId != null && domainId.longValue() != 0L) {
			sb.append(" and o.user.domain.id = :domainId ");
			values.put("domainId", domainId);
		}

		if (!isSuperAdmin) {
			sb.append(" and o.user.id in ( :users ) ");
			values.put("users", userList);
		}

		for (int i = 0; i < sorts.size(); i++) {
			Sort sort = sorts.get(i);

			if (i == 0) {
				sb.append(" order by ");
			}

			sb.append(sort.getProperty() + " " + sort.getDirection());

			if (i < sorts.size() - 1) {
				sb.append(",");
			}
		}
	}

	/**
	 * @title: pageOrderHql
	 * @description 准备订单分页hql
	 * @param order
	 * @param user
	 * @param sorts
	 * @param sb
	 * @param values
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-16 下午3:08:16
	 */
	private void pageOrderHqlWebSite(Order order, User user, List<Sort> sorts,
			StringBuilder sb, Map<String, Object> values, String query)
			throws Exception {
		sb.append("from Order o where 1=1 and o.user != null");

		if (order.getStatus() != Status.All) {
			sb.append(" and o.status = :status");
			values.put("status", order.getStatus());
		}

		if (StringUtils.isNotBlank(query)) {
			sb.append(" and o.orderNo like '%").append(query).append("%' ");
		}

		if (user != null) {
			sb.append(" and o.user = :user");
			values.put("user", user);
		}

		for (int i = 0; i < sorts.size(); i++) {
			Sort sort = sorts.get(i);

			if (i == 0) {
				sb.append(" order by ");
			}

			sb.append(sort.getProperty() + " " + sort.getDirection());

			if (i < sorts.size() - 1) {
				sb.append(",");
			}
		}
	}

	/**
	 * @title: setOrderNullOnUser
	 * @description 用一句话说明这个方法做什么
	 * @param o
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.1
	 * @author MaDai
	 * @update 2012-6-29 上午9:35:07
	 */
	private void setOrderNullOnUser(User user) throws Exception {
		// user.setGroups(null);
		user.setUserProfile(null);
	}

	/**
	 * 
	 * @title: setOrderNullOnItems
	 * @description 设置所有订单项为空
	 * @param items
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-20 下午2:28:43
	 */
	// private void setOrderNullOnItems(List<OrderItem> items) throws Exception
	// {
	// for (OrderItem orderItem : items) {
	// orderItem.setOrder(null);
	// }
	// }

	@Override
	public Order findOrderById(Long id) throws HsCloudException {
		return orderDao.getOrderById(id);
	}

	@Override
	public Order toTry(Long orderId, String orderStatus)
			throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("toTry enter");
		}
		Order result = null;
		try {
			Order order = orderDao.getOrderById(orderId);
			// 试用前验证订单状态是否已为试用订单
			if (!orderStatus.equals(order.getStatus().name())) {
				OrderItem item = order.getItems().get(0);
				Date now = new Date();
				item.setEffectiveDate(now);
				item.setExpirationDate(DateUtils.addDays(now,
						item.getTryDuration()));//
				orderDao.saveOrder(changeOrderStatus(order, orderStatus));
				result = order;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("toTry end");
		}

		return result;
	}

	@Override
	public OrderItem toDelay(Long orderId, String orderStatus, int days,
			String configDelayDays, String configDelayBeginDate)
			throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("toDelay enter");
		}
		OrderItem result = null;
		try {
			Order order = orderDao.getOrderById(orderId);
			OrderItem item = order.getItems().get(0);
			Date nowDate = new Date();
			Date expirationDateOriginal = item.getExpirationDate();
			int allowAuditDays = 0;
			if (StringUtils.isNotBlank(configDelayDays)) {
				allowAuditDays = Integer.parseInt(configDelayDays);
			}
			Date allowAuditDate = DateUtils.addDays(expirationDateOriginal,
					allowAuditDays);
			Date newExpirationDate = DateUtils.addDays(expirationDateOriginal,
					days);
			if (allowAuditDate.after(nowDate)) {
				if (expirationDateOriginal.before(nowDate)
						&& "now".equals(configDelayBeginDate)) {
					newExpirationDate = DateUtils.addDays(nowDate, days);
				}
				item.setExpirationDate(newExpirationDate);
				orderDao.saveOrder(changeOrderStatus(order, orderStatus));
				result = item;
			}

		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("toDelay end");
		}

		return result;
	}

	@Override
	public boolean applayToDelay(Long orderId, String orderStatus)
			throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("applayToDelay enter");
		}
		boolean result = true;
		try {
			Order order = orderDao.getOrderById(orderId);
			orderDao.saveOrder(changeOrderStatus(order, orderStatus));
		} catch (Exception e) {
			result = false;
			throw new HsCloudException("", logger, e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("applayToDelay end");
		}

		return result;
	}

	@Override
	public boolean appleyToNormal(Long orderId, String orderStatus)
			throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("appleyToNormal enter");
		}
		boolean result = true;
		try {
			Order order = orderDao.getOrderById(orderId);
			orderDao.saveOrder(changeOrderStatus(order, orderStatus));
		} catch (Exception e) {
			result = false;
			throw new HsCloudException("", logger, e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("appleyToNormal end");
		}

		return result;
	}

	@Override
	public OrderItem toNormal(Order order) throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("toDelay enter");
		}
		OrderItem result = null;
		try {
			OrderItem item = order.getItems().get(0);
			Date now = new Date();
			Date effectiveDate = item.getEffectiveDate();
			Date newExpirationDate = OrderUtils.getExpDateForOrderItem(item,
					effectiveDate);
			item.setExpirationDate(newExpirationDate);
			item.setTryOrNo(false);
			item.setTryDuration(0);
			order.setPayDate(now);
			orderDao.saveOrder(changeOrderStatus(order, "Paid"));
			result = item;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("toDelay end");
		}

		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public OrderItem toNormalV2(Order order, Date VmExpirationDate)
			throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("toNormalV2 enter");
		}
		OrderItem result = null;
		try {
			OrderItem item = order.getItems().get(0);
			Date now = new Date();
			Date effectiveDate=new Date();
			int VmVmExpirationTime=VmExpirationDate.getHours()*3600*1000+VmExpirationDate.getMinutes()*60*1000+VmExpirationDate.getSeconds()*1000;
			if(order.getOrderType()==1){
				ServiceCatalog sc=serviceCatalogDao.findScById(Integer.parseInt(item.getServiceCatalogId()));
				if(sc.isTryTimeAddOrNo()){
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String VmExpirationDateString = formatter.format(VmExpirationDate);
					String nowString = formatter.format(now);
					Date tempVmExpirationDate=formatter.parse(VmExpirationDateString);
					Date tempnow=formatter.parse(nowString);
					long sumDay=(tempVmExpirationDate.getTime()-tempnow.getTime())/(1000*3600*24);
					effectiveDate=DateUtils.addDays(new Date(tempnow.getTime()+VmVmExpirationTime),(int)sumDay);
				}
			}
			Date newExpirationDate = OrderUtils.getExpDateForOrderItem(item,effectiveDate);
			item.setEffectiveDate(effectiveDate);
			item.setExpirationDate(newExpirationDate);
			order.setPayDate(now);
			orderDao.saveOrder(changeOrderStatus(order, "Paid"));
			result = item;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("toNormalV2 end");
		}

		return result;
	}

	@Override
	public void toUnpaid(Order order) throws HsCloudException {
		if (logger.isDebugEnabled()) {
			logger.debug("toUnpaid enter");
		}
		try {
			OrderItem item = order.getItems().get(0);
			item.setTryOrNo(false);
			// item.setTryDuration(0);
			orderDao.saveOrder(changeOrderStatus(order, "Unpaid"));
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("toUnpaid end");
		}
	}

	private String getOrderRemark(Order order) throws Exception {
		StringBuilder remark = new StringBuilder();
		String remarkOld = order.getRemark();
		if (StringUtils.isNotBlank(remarkOld)) {
			remark.append(remarkOld).append("||");
		}
		remark.append("订单").append(order.getOrderNo()).append("，在")
				.append(HsCloudDateUtil.getNowStr()).append("，状态变更为")
				.append(getOrderStatus(order.getStatus().ordinal()))
				.append("。");

		return remark.toString();
	}

	private String getOrderStatus(int ordinal) {
		String result = "Unknow";
		if (ordinal == 1) {
			result = Constants.ORDER_STATUS_PAID;
		} else if (ordinal == 2) {
			result = Constants.ORDER_STATUS_CANCEL;
		}
		return result;
	}

	private Order changeOrderStatus(Order order, String orderStatus)
			throws Exception {
		order.setStatus(Status.getOrderStatusByStr(orderStatus));
		order.setUpdateDate(new Date());
		order.setRemark(getOrderRemark(order));
		return order;
	}

	@Override
	public OrderItem orderRenew(OrderItem orderItem, User user, boolean payFlag)
			throws HsCloudException {
		OrderItem result = null;
		try {
			List<OrderItem> items = new ArrayList<OrderItem>();
			OrderItem itemNew = new OrderItem();
			Order orderOriginal = orderItem.getOrder();
			itemNew.copyItem(orderItem,orderOriginal.getOrderType());
			itemNew.setTryDuration(0);
			Order order = new Order();
			String orderStatus = "";
			if (payFlag) {
				Date expirationDateOriginal = orderItem.getExpirationDate();
				Date effectiveDateNew = DateUtils.addDays(
						expirationDateOriginal, 1);
				Date expirationDateNew = OrderUtils.getExpDateForOrderItem(
						orderItem, effectiveDateNew);
				itemNew.setEffectiveDate(effectiveDateNew);
				itemNew.setExpirationDate(expirationDateNew);
				orderStatus = "Paid";
			} else {
				orderStatus = "Unpaid";
			}
			items.add(itemNew);
			order.submit(items, user, orderStatus,orderOriginal.getRebateRate(),orderOriginal.getGiftsRebateRate());
			StringBuilder remark = new StringBuilder();
			remark.append(HsCloudDateUtil.getNowStr()).append("由订单:订单号为")
					.append(orderOriginal.getOrderNo())
					.append(",订单项为(order_item_id):").append(orderItem.getId())
					.append(" 续费产生.");
			order.setRemark(remark.toString());
			order.setRemark(getOrderRemark(order));
			orderDao.saveOrder(order);
			result = order.getItems().get(0);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
		return result;
	}

	@Override
	public OrderItem renewOrderV2(OrderItem item,BigDecimal actuallyPoint,BigDecimal actuallyGift, BigDecimal price,
			String priceType, String pricePeriodType, String period, User user,
			Date expirationDate,Integer rebateRate,Integer giftsRebateRate) throws HsCloudException {
		try {
			List<OrderItem> items = new ArrayList<OrderItem>();
			OrderItem itemNew = new OrderItem();
			String[] ignoreProperties = { "effectiveDate", "id",
					"expirationDate","orderProducts" };
			BeanUtils.copyProperties(item, itemNew, ignoreProperties);
			Order orderOriginal = item.getOrder();
			Order order = new Order();
			int monthes = Integer.parseInt(period);
			// int days=monthes*30;
			Date effectiveDate = DateUtils.addDays(expirationDate, 1);//改为立即生效，否则立即退款无法查询订单
			if(actuallyPoint.compareTo(BigDecimal.ZERO)<=0){
				itemNew.setUsePointOrNot(false);
			}else{
				itemNew.setUsePointOrNot(true);
			}
			if(actuallyGift.compareTo(BigDecimal.ZERO)<=0){
				itemNew.setUseGiftOrNot(false);
			}else{
				itemNew.setUseGiftOrNot(true);
			}
			itemNew.setPointAmount(null);
			itemNew.setPrice(price);
			itemNew.setPricePeriod(period);
			itemNew.setPricePeriodType(pricePeriodType);
			itemNew.setPriceType(priceType);
			itemNew.setAmount(price);
			itemNew.setEffectiveDate(effectiveDate);
			Date expirationDateNew = OrderUtils.getExpDateForOrderItem(itemNew,
					expirationDate);
			itemNew.setExpirationDate(expirationDateNew);
			items.add(itemNew);
			order.submit(items, user, "Paid",rebateRate,giftsRebateRate);
			order.setOrderType((short)1);
			StringBuilder remark = new StringBuilder();
			remark.append("此订单，由订单").append(orderOriginal.getOrderNo())
					.append("[订单项").append(item.getId()).append("]，在")
					.append(HsCloudDateUtil.getNowStr()).append("，续费产生。");
			order.setRemark(remark.toString());
			order.setRemark(getOrderRemark(order));
			order.setOrderNo(order.getOrderNo() + orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID));
			orderDao.saveOrder(order);
			return itemNew;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@Override
	public OrderItem renewOrder4Need(String uuid,OrderItem itemNew,
			User user,Date expirationDate,Integer rebateRate,Integer giftRebateRate) throws HsCloudException {
		try {
			Date effectiveDate = DateUtils.addDays(expirationDate, 1);
			itemNew.setPointAmount(null);
			itemNew.setEffectiveDate(effectiveDate);
			Date expirationDateNew = OrderUtils.getExpDateForOrderItem(itemNew,
					expirationDate);
			itemNew.setExpirationDate(expirationDateNew);
			List<OrderItem> items = new ArrayList<OrderItem>();
			items.add(itemNew);
			Order order = new Order();
			order.submit(items, user, "Paid", rebateRate,giftRebateRate);
			StringBuilder remark = new StringBuilder();
			remark.append("此订单，由云主机[").append(uuid).append("]，在")
					.append(HsCloudDateUtil.getNowStr()).append("，续费产生。");
			order.setRemark(remark.toString());
			order.setRemark(getOrderRemark(order));
			order.setOrderType((short)2);
			order.setOrderNo(order.getOrderNo() + orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID));
			orderDao.saveOrder(order);
			return itemNew;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@Transactional
	public List<OrderItemVMVo> getOrderItemVMByOrderId(long orderId)
			throws HsCloudException {
		return orderDao.getOrderItemVMByOrderId(orderId);
	}

	@Override
	public void refundGenerateNewOrder(Order order,
			Long[] orderItemIds,RefundResult result,Date dayBeginToFeeLocal) throws HsCloudException {
		try {
			List<OrderItem> itemAll = order.getItems();
			List<OrderItem> itemRefunds = new ArrayList<OrderItem>();
			User user = order.getUser();
			Date now = new Date();
			for (OrderItem itemTemp : itemAll) {
				for (Long itemId : orderItemIds) {
					if (itemTemp.getId().longValue() == itemId.longValue()) {
						itemRefunds.add(itemTemp);
					}
				}
			}
			Map<Long, OrderItem> remainItemMap = null;
			itemAll.removeAll(itemRefunds);
			if (itemAll.size() > 0) {
				Order remainOrder = new Order();
				remainOrder.setOrderNo(getOrderNo(order.getUser()));
				remainItemMap = new HashMap<Long, OrderItem>();
				List<OrderItem> remainRemainsNew = new ArrayList<OrderItem>();
				for (OrderItem item : itemAll) {
					OrderItem itemRemainNew = new OrderItem();
					itemRemainNew.copyItem(item,order.getOrderType());
					remainItemMap.put(item.getId(), itemRemainNew);
					remainRemainsNew.add(itemRemainNew);
				}
				remainOrder.submitWithoutOrderNo(remainRemainsNew, user, "Paid",order.getRebateRate(),order.getGiftsRebateRate());
				//String serivalNum = orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID);
				//remainOrder.setOrderNo(remainOrder.getOrderNo()+serivalNum);
				remainOrder.setPayDate(order.getPayDate());// 新生成订单支付日期仍为原订单支付日期
				remainOrder.setRemark("该订单由未退款项产生,父订单为:" + order.getOrderNo());
				remainOrder.setOrderType(order.getOrderType());
				orderDao.saveOrder(remainOrder);
				result.setUnRefundOrderItem(remainItemMap);
			}

			Order refundOrder = new Order();
			refundOrder.setOrderNo(getOrderNo(order.getUser()));
			List<OrderItem> itemRefundsNew = new ArrayList<OrderItem>();
			for (OrderItem item : itemRefunds) {
				OrderItem itemRefundNew = new OrderItem();
				itemRefundNew.copyItem(item,order.getOrderType());
				itemRefundNew.setExpirationDate(now);
				itemRefundNew.setEffectiveDate(now);
				BigDecimal consum = OrderUtils.getRefund(item,dayBeginToFeeLocal);
				if(item.getPointAmount()!=null&&item.getUsePointOrNot()){
					BigDecimal consumPoint=OrderUtils.getRefundPointOrGift(item,item.getPointAmount(), dayBeginToFeeLocal);
					itemRefundNew.setPointAmount(consumPoint);
				}
				if(item.getGiftAmount()!=null&&item.getUseGiftOrNot()){
					BigDecimal consumGift=OrderUtils.getRefundPointOrGift(item,item.getGiftAmount(), dayBeginToFeeLocal);
					itemRefundNew.setGiftAmount(consumGift);
				}
				itemRefundNew.setAmount(consum);
				itemRefundsNew.add(itemRefundNew);
			}
			refundOrder.submitWithoutOrderNo(itemRefundsNew, user, "Paid",order.getRebateRate(),order.getGiftsRebateRate());
			//String.valueOf(new Date().getTime());
			//String serivalNum = orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID);
			//refundOrder.setOrderNo(refundOrder.getOrderNo()+serivalNum);
			refundOrder.setPayDate(order.getPayDate());// 新生成订单支付日期仍为原订单支付日期
			refundOrder.setOrderType(order.getOrderType());
			refundOrder.setRemark("该订单由退款项产生,父订单为:" + order.getOrderNo());
			orderDao.saveOrder(refundOrder);
			result.setRefundItems(itemRefundsNew);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<VmRefundOrderItemVo> getVmRefundOrderItemVo(long referenceId,Date dayBeginToFeeLocal)
			throws HsCloudException {
		try {
			List<Long> orderItemIdList = orderDao
					.getVmRelatedPaidOrderItemId(referenceId,dayBeginToFeeLocal);
			List<VmRefundOrderItemVo> vmRefundOrderItemList = new ArrayList<VmRefundOrderItemVo>();
			for (Long orderItemId : orderItemIdList) {
				VmRefundOrderItemVo vmRefundOrderItemTemp = new VmRefundOrderItemVo();
				OrderItem item = orderItemDao.getOrderItemById(orderItemId);
				BigDecimal used = OrderUtils.getRefund(item,dayBeginToFeeLocal);
				BigDecimal unUsed = item.getAmount().add(used.abs().negate());
				Order order = item.getOrder();
				StringBuilder totalPriceDesc = new StringBuilder();
				if (order.getItems().size() > 1) {
					for (OrderItem itemTemp : order.getItems()) {
						if (order.getItems().lastIndexOf(itemTemp) != order
								.getItems().size() - 1) {
							totalPriceDesc.append(itemTemp.getAmount())
									.append(Constants.YUAN).append("+");
						} else {
							totalPriceDesc.append(itemTemp.getAmount())
									.append(Constants.YUAN).append("=");
						}
					}
				}
				if(item.getUsePointOrNot()&&item.getPointAmount()!=null){
					BigDecimal pointUsed = OrderUtils.getRefundPointOrGift(item,item.getPointAmount(),dayBeginToFeeLocal);
					BigDecimal pointUnUsed = item.getPointAmount().add(pointUsed.abs().negate());
					vmRefundOrderItemTemp.setUsedPointAmount(pointUsed);
					vmRefundOrderItemTemp.setUnUsedPointAmount(pointUnUsed);
					vmRefundOrderItemTemp.setPointAmount(item.getPointAmount());
				}
				
				if(item.getUseGiftOrNot()&&item.getGiftAmount()!=null){
					BigDecimal pointUsed = OrderUtils.getRefundPointOrGift(item,item.getGiftAmount(),dayBeginToFeeLocal);
					BigDecimal pointUnUsed = item.getGiftAmount().add(pointUsed.abs().negate());
					vmRefundOrderItemTemp.setUsedGiftAmount(pointUsed);
					vmRefundOrderItemTemp.setUnUsedGiftAmount(pointUnUsed);
					vmRefundOrderItemTemp.setGiftAmount(item.getGiftAmount());
				}
				
				if(order.getTotalPointAmount()!=null){
					vmRefundOrderItemTemp.setTotalPointAmount(order.getTotalPointAmount());
				}
				if(order.getTotalAmount()!=null){
					totalPriceDesc.append(order.getTotalAmount()).append(
							Constants.YUAN);
				}else{
					totalPriceDesc.append(order.getTotalPrice()).append(
							Constants.YUAN);
				}
				
				Date now = new Date();
				String refundMarking = Constants.VM_REFUND_PART_FLAG;
				if (item.getEffectiveDate().after(now)) {
					refundMarking = Constants.VM_REFUND_PART_FLAG;
				}
				vmRefundOrderItemTemp.setTotalPriceDetail(totalPriceDesc
						.toString());
				vmRefundOrderItemTemp.setTotalPrice(order.getTotalPrice());
				vmRefundOrderItemTemp.setTotalAmount(order.getTotalAmount());
				vmRefundOrderItemTemp.setAmount(item.getAmount());
				vmRefundOrderItemTemp.setEffectiveDate(item.getEffectiveDate());
				vmRefundOrderItemTemp.setExpirationDate(item
						.getExpirationDate());
				vmRefundOrderItemTemp.setOrderNo(order.getOrderNo());
				vmRefundOrderItemTemp.setPeriod(item.getPricePeriod());
				vmRefundOrderItemTemp.setRefundMarking(refundMarking);
				vmRefundOrderItemTemp.setUnUsedAmount(unUsed);
				vmRefundOrderItemTemp.setUsedAmount(used);
				vmRefundOrderItemList.add(vmRefundOrderItemTemp);
			}
			return vmRefundOrderItemList;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public RefundResult vmRefund(long referenceId, String vmId,Date dayBeginToFeeArg)
			throws HsCloudException {
		try {
			// 获取与vm绑定的已经支付的订单项Id
			Date dayBeginToFeeLocal=null;
			if(dayBeginToFeeArg==null){
				dayBeginToFeeLocal=new Date();
			}else{
				dayBeginToFeeLocal=dayBeginToFeeArg;
			}
			List<Long> orderItemIdList = orderDao
					.getVmRelatedPaidOrderItemId(referenceId,dayBeginToFeeLocal);
			RefundResult result = new RefundResult();
			// vm的未发生的金额
			BigDecimal refundAll = BigDecimal.ZERO;
			BigDecimal refundPointAll=BigDecimal.ZERO;
			BigDecimal refundGiftAll=BigDecimal.ZERO;
			// 订单关联的用户Id
			User user = null;
			// 退款操作的备注，便于以后追踪
			StringBuilder refundMarker = new StringBuilder();
			Date now=new Date();
			
			refundMarker.append("云主机[" + vmId + "]，在")
					.append(HsCloudDateUtil.transferDate2Str(null, now))
					.append("，执行部分退款操作");
			StringBuilder renewOrderNo = new StringBuilder();
			StringBuilder usingOrderNo=new StringBuilder();
			for (Long orderItemId : orderItemIdList) {
				OrderItem item = orderItemDao.getOrderItemById(orderItemId);
				// 正在使用订单退款
				if (item.getEffectiveDate().before(dayBeginToFeeLocal)
						&& item.getExpirationDate().after(dayBeginToFeeLocal)) {
					Order usingOrder = item.getOrder();
					result.setCurrentOrderId(usingOrder.getId());
					result.setCurrentOrderItemId(orderItemId);
					user = usingOrder.getUser();
					// 只能对已经支付的订单进行退款
					if (usingOrder.getStatus().ordinal() != 1) {
						throw new HsCloudException(referenceId + "对未支付订单退款异常",
								logger);
					}
					// vm已经使用的金额
					BigDecimal consume = OrderUtils.getRefund(item,dayBeginToFeeLocal);
					// vm未使用的金额也就是要退的金额
					BigDecimal refund = item.getAmount().add(
							consume.abs().negate());
					// 累加订单中未使用的金额
					refundAll = refundAll.add(refund);
					StringBuilder refundPointRemark=new StringBuilder("");
					if(item.getUsePointOrNot()&&item.getPointAmount()!=null&&item.getPointAmount().compareTo(BigDecimal.ZERO)>0){
						BigDecimal pointConsume=OrderUtils.getRefundPointOrGift(item,item.getPointAmount(),dayBeginToFeeLocal);
						BigDecimal pointRefund=item.getPointAmount().subtract(pointConsume);
						refundPointAll=refundPointAll.add(pointRefund);
						refundPointRemark.append("，返点退回").append(pointRefund).append("点");
					}
					
					if(item.getUseGiftOrNot()&&item.getGiftAmount()!=null&&item.getGiftAmount().compareTo(BigDecimal.ZERO)>0){
						BigDecimal giftConsume=OrderUtils.getRefundPointOrGift(item,item.getGiftAmount(), dayBeginToFeeLocal);
						BigDecimal giftRefund=item.getGiftAmount().subtract(giftConsume);
						refundGiftAll=refundGiftAll.add(giftRefund);
						refundPointRemark.append("，礼金退回").append(giftRefund).append("元");
					}
					Long[] orderItemIds = new Long[1];
					orderItemIds[0] = orderItemId;
					// 如果一个订单中有若干个虚拟机，则在vm退款的时候拆分订单，退掉的虚拟机一个订单，订单总额为已经使用的金额
					// 未退的订单生成一个订单，新生成的订单项需要绑定到原vm机上，
					// 所以返回结果为，原orderItemId与新生成的OrderItem对应的键值对数据结构，以便将vm机绑定到新的订单项上
					refundGenerateNewOrder(usingOrder, orderItemIds,result,dayBeginToFeeLocal);
					// 原订单取消
					usingOrder.setStatus(Status.Cancelled);
					String oldRemark = usingOrder.getRemark();
					StringBuilder remark = new StringBuilder();
					if (StringUtils.isNotBlank(oldRemark)) {
						remark.append(oldRemark).append("||");
					}
					remark.append("云主机[" + vmId + "]，在")
							.append(HsCloudDateUtil.transferDate2Str(null, now))
							.append("，执行部分退款操作").append("，退款金额为")
							.append(refund).append(Constants.YUAN).append(refundPointRemark)
							.append("，该订单变为已取消状态。");
					usingOrderNo.append(usingOrder.getOrderNo()).append("；");
					usingOrder.setRemark(remark.toString());
					usingOrder.setUpdateDate(now);
					//result.setUnRefundOrderItem(unRefundOrderItem);
					orderDao.saveOrder(usingOrder);
					// 未使用的续费订单退款
				} else if (item.getEffectiveDate().after(dayBeginToFeeLocal)) {
					Order unUsedOrder = item.getOrder();
					// 只能对已经支付的订单进行退款
					if (unUsedOrder.getStatus().ordinal() != 1) {
						throw new HsCloudException(referenceId + "对未支付订单退款异常",
								logger);
					}
					user = unUsedOrder.getUser();
					// 未使用的续费订单全额退款
					refundAll = refundAll
							.add(item.getAmount().abs());
					String refundPointRemark="";
					if(item.getUsePointOrNot()&&item.getPointAmount()!=null){
						refundPointAll=refundPointAll.add(item.getPointAmount().abs());
						refundPointRemark="，返点退回"+item.getPointAmount()+"点";
					}
					if(item.getUseGiftOrNot()&&item.getGiftAmount()!=null){
						refundGiftAll=refundGiftAll.add(item.getGiftAmount().abs());
						refundPointRemark="，礼金退回"+item.getGiftAmount()+"元";
					}
					// 未使用的续费订单取消
					unUsedOrder.setStatus(Status.Cancelled);
					unUsedOrder.setUpdateDate(now);
					StringBuilder unUsedOrderRemark = new StringBuilder();
					String oldRemark = unUsedOrder.getRemark();
					if (StringUtils.isNotBlank(oldRemark)) {
						unUsedOrderRemark.append(oldRemark).append("||");
					}
					unUsedOrderRemark
							.append("云主机[" + vmId + "]，在")
							.append(HsCloudDateUtil.transferDate2Str(null, now))
							.append("，执行部分退款操作").append("，退款金额为")
							.append(unUsedOrder.getTotalPrice()).append(Constants.YUAN)
						    .append(refundPointRemark).append("，该订单变为已取消状态。");
					unUsedOrder.setRemark(unUsedOrderRemark.toString());
					renewOrderNo.append(unUsedOrder.getOrderNo()).append("；");
					orderDao.saveOrder(unUsedOrder);
				}
			}
			if(usingOrderNo.length()>0){
				refundMarker.append("，执行订单号为[")
				.append(usingOrderNo).append("]，");
			}
			if (renewOrderNo.length() > 0) {
				refundMarker.append("续费订单号为[").append(renewOrderNo).append("]，");
			}
			result.setRefund(refundAll);
			result.setRefundPoint(refundPointAll);
			result.setRefundGift(refundGiftAll);
			result.setUser(user);
			refundMarker.append("退款金额为").append(refundAll).append(Constants.YUAN);
			if(refundPointAll.compareTo(BigDecimal.ZERO)>0){
				refundMarker.append("，返点退回").append(refundPointAll).append("点");
			}
			if(refundGiftAll.compareTo(BigDecimal.ZERO)>0){
				refundMarker.append("，礼金退回").append(refundGiftAll).append("元");
			}
			refundMarker.append("；");
			result.setRemark(refundMarker.toString());
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	// 全额退款时拆分一个订单包含若干个订单项
	// 未退项生成新的订单,已经退掉的项，不做任何操作
	public void refundGenerateNewOrder4RefundAll(Order order,
			Long[] orderItemIds,RefundResult result) throws HsCloudException {
		try {
			List<OrderItem> itemAll = order.getItems();
			List<OrderItem> itemRefunds = new ArrayList<OrderItem>();
			User user = order.getUser();
			Date now = new Date();
			for (OrderItem itemTemp : itemAll) {
				for (Long itemId : orderItemIds) {
					if (itemTemp.getId().longValue() == itemId.longValue()) {
						itemRefunds.add(itemTemp);
					}
				}
			}
			Map<Long, OrderItem> remainItemMap = null;
			itemAll.removeAll(itemRefunds);
			if (itemAll.size() > 0) {
				Order remainOrder = new Order();
				remainOrder.setOrderNo(getOrderNo(order.getUser()));
				remainItemMap = new HashMap<Long, OrderItem>();
				List<OrderItem> remainRemainsNew = new ArrayList<OrderItem>();
				for (OrderItem item : itemAll) {
					OrderItem itemRemainNew = new OrderItem();
					itemRemainNew.copyItem(item,order.getOrderType());
					remainItemMap.put(item.getId(), itemRemainNew);
					remainRemainsNew.add(itemRemainNew);
				}
				remainOrder.submitWithoutOrderNo(remainRemainsNew, user, "Paid",order.getRebateRate(),order.getGiftsRebateRate());
				remainOrder.setPayDate(order.getPayDate());// 新生成订单支付日期仍为原订单支付日期
				remainOrder.setRemark("此订单由未退款项产生，父订单为" + order.getOrderNo()+"。");
				remainOrder.setOrderType(order.getOrderType());
				orderDao.saveOrder(remainOrder);
				result.setUnRefundOrderItem(remainItemMap);
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public RefundResult vmRefundAll(long referenceId, String vmId,Date dayBeginToFeeArg)
			throws HsCloudException {
		try {
			// 获取与vm绑定的已经支付的订单项Id
			Date dayBeginToFeeLocal=null;
			if(dayBeginToFeeArg==null){
				dayBeginToFeeLocal=new Date();
			}else{
				dayBeginToFeeLocal=dayBeginToFeeArg;
			}
			List<Long> orderItemIdList = orderDao
					.getVmRelatedPaidOrderItemId(referenceId,dayBeginToFeeLocal);
			// 退款返回的自定义数据结构
			RefundResult result = new RefundResult();
			// vm的未发生的金额即要退款的金额
			BigDecimal refundAll = BigDecimal.ZERO;
			BigDecimal refundPointAll=BigDecimal.ZERO;
			BigDecimal refundGiftAll=BigDecimal.ZERO;
			// 订单关联的用户
			User user = null;
			// 退款操作的备注，便于以后追踪
			StringBuilder refundMarker = new StringBuilder();
			Date now = new Date();
			refundMarker.append("云主机[" + vmId + "]，在")
					.append(HsCloudDateUtil.transferDate2Str(null, now))
					.append("，执行全额退款操作，订单号为[");
			for (Long orderItemId : orderItemIdList) {
				OrderItem item = orderItemDao.getOrderItemById(orderItemId);
				
				// 正在使用订单退款
				Order orginalOrder = item.getOrder();
				if (item.getEffectiveDate().before(dayBeginToFeeLocal)
						&& item.getExpirationDate().after(dayBeginToFeeLocal)) {
					result.setCurrentOrderId(orginalOrder.getId());
					result.setCurrentOrderItemId(orderItemId);
				}
				// 只能对已经支付的订单进行退款
				if (orginalOrder.getStatus().ordinal() != 1) {
					throw new HsCloudException(referenceId + "对未支付订单退款异常",
							logger);
				}
				user = orginalOrder.getUser();
				// 原订单取消
				orginalOrder.setStatus(Status.Cancelled);
				// 订单项全额退款
				refundAll = refundAll.add(item.getAmount().abs());
				StringBuilder refundPointRemark=new StringBuilder("");
				if(item.getUsePointOrNot()&&item.getPointAmount()!=null&&item.getPointAmount().compareTo(BigDecimal.ZERO)>0){
					refundPointAll=refundPointAll.add(item.getPointAmount().abs());
					refundPointRemark.append("，返点退回").append(item.getPointAmount().abs()).append("点");
				}
				
				if(item.getUseGiftOrNot()&&item.getGiftAmount()!=null&&item.getGiftAmount().compareTo(BigDecimal.ZERO)>0){
					refundGiftAll=refundGiftAll.add(item.getGiftAmount().abs());
					refundPointRemark.append("，礼金退回").append(item.getGiftAmount().abs()).append("元");
				}
				String oldRemark = orginalOrder.getRemark();
				StringBuilder remark = new StringBuilder();
				if (StringUtils.isNotBlank(oldRemark)) {
					remark.append(oldRemark).append("||");
				}
				remark.append("云主机[" + vmId + "]，在")
						.append(HsCloudDateUtil.transferDate2Str(null, now))
						.append("，执行全额退款操作").append("，退款金额为")
						.append(item.getAmount()).append(Constants.YUAN).append(refundPointRemark).append("，该订单变为已取消状态。");
				// 如果订单包含多个订单项则拆分订单，若只有一个直接取消订单
				if (orginalOrder.getItems().size() > 1) {
					Long[] orderItemIds = new Long[1];
					orderItemIds[0] = orderItemId;
				refundGenerateNewOrder4RefundAll(orginalOrder, orderItemIds,result);
					//result.setUnRefundOrderItem(unRefundOrderItem);
				}
				refundMarker.append(orginalOrder.getOrderNo()).append("；");
				orginalOrder.setRemark(remark.toString());
				orginalOrder.setUpdateDate(now);
				orginalOrder.setOrderNo(orginalOrder.getOrderNo() + orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID));
				orderDao.saveOrder(orginalOrder);
			}
			result.setRefund(refundAll);
			result.setRefundGift(refundGiftAll);
			result.setRefundPoint(refundPointAll);
			result.setUser(user);
			refundMarker.append("]，退款金额为").append(refundAll).append(Constants.YUAN);
			if(refundPointAll.compareTo(BigDecimal.ZERO)>0){
				refundMarker.append("，返点退回").append(refundPointAll).append("点");
			}
			if(refundGiftAll.compareTo(BigDecimal.ZERO)>0){
				refundMarker.append("，礼金退回").append(refundGiftAll).append("元");
			}
			refundMarker.append("；");
			result.setRemark(refundMarker.toString());
			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<OrderVo> getVmRelatedPaidOrder(long referenceId)
			throws HsCloudException {
		return orderDao.getVmRelatedPaidOrder(referenceId);
	}

	@Override
	public String getVmCurrentOrderNo(String uuid) throws HsCloudException {
		
		return orderDao.getVmCurrentOrderNo(uuid);
	}
	@Override
	public String getVmNoteffectiveOrderNo(String uuid) throws HsCloudException {
		
		return orderDao.getVmNoteffectiveOrderNo(uuid);
	}


	@Override
	public long submitOrder4Need(List<OrderProduct> products, int vmNum)
			throws HsCloudException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public void cancelUnPaidOrderByUser(long userId) throws HsCloudException {
		try {
			List<Order> orders = orderDao.getOrderByUserId(userId, (short) 0);
			if (orders != null && orders.size() > 0) {
				for (Order o : orders) {
					o.setStatus(Order.Status.Cancelled);
					o.setUpdateDate(new Date());
					o.setRemark(getOrderRemark(o));
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

	@Override
	public String getOrderSerialNumber(String tableName) throws HsCloudException {
		
		return orderDao.getOrderSerialNumber(tableName);
	}
	
	private String getOrderNo(User user){
		OrderUtils orderUtils  = new OrderUtils();
		String orderNo=orderUtils.generateOrderNo(user);
		String serivalNum = orderDao.getOrderSerialNumber(Constants.T_ORDER_ARRANGING_ID);
		return orderNo+serivalNum;
	}

	@Override
	public Order getOrderById(Long orderId) {
	
		return orderDao.getOrderById(orderId);
	}
	
	
}
