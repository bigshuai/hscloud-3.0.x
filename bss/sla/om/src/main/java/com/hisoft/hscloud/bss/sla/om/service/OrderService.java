/**
 * @title IOrderService.java
 * @package com.hisoft.hscloud.bss.sla.om.service
 * @description 订单业务接口
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.bss.sla.om.entity.OrderProduct;
import com.hisoft.hscloud.bss.sla.om.entity.RefundResult;
import com.hisoft.hscloud.bss.sla.om.exception.OrderException;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;

/**
 * @description 订单业务接口，查看，新增，取消，支付订单
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
public interface OrderService {
	/**
	 * 根据订单id获取订单
	 * @param orderId
	 * @return
	 */
	public Order getOrderById(Long orderId);

	/**
	 * 
	 * @title: getAllOrderItemsByOrder
	 * @description 订单下所有的订单项
	 * @param orderId
	 * @return 设定文件
	 * @return List<OrderItemVo> 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-20 下午2:09:21
	 */
	public List<OrderItemVo> getAllOrderItemsByOrder(Long orderId)
			throws HsCloudException;
	
	/**
	 * @title: getOrderByOrderNo
	 * @description <通过查询hc_order表的orderNo获得Order> 
	 * @param orderNo
	 * @return id
	 * @throws
	 * @version 1.0
	 * @author liyunhui
	 * @update 2013-6-20
	 */
	public Order getOrderByOrderNo(String orderNo) throws HsCloudException;

	/**
	 * 
	 * @title: cancel
	 * @description 取消订单
	 * @param id
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-3-29 上午9:14:22
	 */
	public boolean cancel(Long id) throws HsCloudException;

	/**
	 * 
	 * @title: pay
	 * @description pay
	 * @param id
	 * @param userId
	 * @throws OrderException
	 *             设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-1 下午3:36:59
	 */
	public Order payV2(Order order, String vmState, Date effectiveDate,
			Date expirationDate) throws HsCloudException;

	/**
	 * 
	 * @title: pay
	 * @description pay
	 * @param id
	 * @param userId
	 * @throws OrderException
	 *             设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-1 下午3:36:59
	 */
	public Order pay(Order order) throws HsCloudException;

	/**
	 * 
	 * @title: pageOrder
	 * @description 订单分页
	 * @param page
	 * @param order
	 * @param user
	 * @param sorts
	 * @return 设定文件
	 * @return Page<Order> 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-16 下午1:44:02
	 */
	public Page<Order> pageOrder(Page<Order> page, Order order,
			List<Sort> sorts,String query,List<Long> useList,boolean isSuperAdmin,Long domainId) throws HsCloudException;
	/**
	 * 
	 * @title: pageOrder
	 * @description 订单分页
	 * @param page
	 * @param order
	 * @param user
	 * @param sorts
	 * @return 设定文件
	 * @return Page<Order> 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-16 下午1:44:02
	 */
	public Page<Order> pageOrderWebSite(Page<Order> page, Order order, User user,
			List<Sort> sorts,String query) throws HsCloudException;

	/**
	 * 
	 * @title: submitOrder
	 * @description 提交订单
	 * @param orderItems
	 * @param user
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-3-29 上午9:15:43
	 */
	public Order submitOrder(List<OrderItemVo> orderItems, 
			User user,Integer rebateRate,Integer giftRebateRate)
			throws HsCloudException;

	/**
	 * <根据条件查询订单数据> <功能详细描述>
	 * 
	 * @param condition
	 * @see [类、类#方法、类#成员]
	 */
	public void queryOrder(QueryCondition condition) throws HsCloudException;

	/**
	 * <根据物理Id获取订单数据> <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Order findOrderById(Long id) throws HsCloudException;

	/**
	 * <change orderstatus to Try> <功能详细描述>
	 * 
	 * @param id
	 * @param orderStatus
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public Order toTry(Long orderId, String orderStatus)
			throws HsCloudException;

	/**
	 * <change orderStauts to TryDelay> <功能详细描述>
	 * 
	 * @param id
	 * @param orderStatus
	 * @param days
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItem toDelay(Long id, String orderStatus, int days,
			String configDelayDays, String configDelayBeginDate)
			throws HsCloudException;

	/**
	 * <change orderStatus to DelayWaitApprove> <功能详细描述>
	 * 
	 * @param id
	 * @param orderStatus
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean applayToDelay(Long id, String orderStatus)
			throws HsCloudException;

	/**
	 * <change orderStatus to WaitNormalApprove> <功能详细描述>
	 * 
	 * @param id
	 * @param orderStatus
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public boolean appleyToNormal(Long id, String orderStatus)
			throws HsCloudException;

	/**
	 * <change orderStatus to Paid> <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItem toNormal(Order order) throws HsCloudException;
	/**
	 * <change orderStatus to Paid> <功能详细描述>
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItem toNormalV2(Order order,Date effectiveDate) throws HsCloudException;
	/**
	 * <账户余额不足变为未支付操作> <功能详细描述>
	 * 
	 * @param id
	 * @see [类、类#方法、类#成员]
	 */
	public void toUnpaid(Order order) throws HsCloudException;


	/**
	 * <虚拟机续费> <虚拟机续费需要生成新的订单，并将虚拟机跟新的订单项关联，更新虚拟机失效时间>
	 * 
	 * @param orderItemId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItem orderRenew(OrderItem orderItem, User user, boolean payFlag)
			throws HsCloudException;

	/**
	 * <获取订单项+VM信息用于退款时退款项的展示> <功能详细描述>
	 * 
	 * @param orderId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<OrderItemVMVo> getOrderItemVMByOrderId(long orderId)
			throws HsCloudException;

	/**
	 * <部分退款时需要生成新的订单>
	 * <部分退款时一个订单拆分生成两个订单，一个订单是未退款项，一个订单是退款项，原订单只修改状态为已取消，其余不做修改，
	 * 返回结果为未退项中老订单项的Id对应新生成订单项的实体Bean，用于把虚拟机关联到新的订单项上。>
	 * 
	 * @param order
	 * @param orderItemIds
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void  refundGenerateNewOrder(Order order,
			Long[] orderItemIds,RefundResult result,Date dayBeginToFeeLocal) throws HsCloudException;

	/**
	 * <新版本续费接口，可改变购买时长> <功能详细描述>
	 * 
	 * @param item
	 * @param price
	 * @param priceType
	 * @param pricePeriodType
	 * @param period
	 * @param user
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public OrderItem renewOrderV2(OrderItem item,BigDecimal actuallyPoint,BigDecimal actuallyGift, BigDecimal price,
			String priceType, String pricePeriodType, 
			String period, User user,Date expirationDate,
			Integer rebateRate,Integer giftsRebateRate)
			throws HsCloudException;

	/**
	 * <查询虚拟机对应的退款订单信息> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<VmRefundOrderItemVo> getVmRefundOrderItemVo(long referenceId,Date dayBeginToFeeLocal)
			throws HsCloudException;

	/**
	 * <虚拟机退款返回自定义退款结果> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	public RefundResult vmRefund(long referenceId,String vmId,Date dayBeginToFeeLocal) throws HsCloudException;

	/**
	 * <虚拟机全退款返回自定义退款结果> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public RefundResult vmRefundAll(long referenceId,String vmId,Date dayBeginToFeeLocal) throws HsCloudException;

	/**
	 * <获取vm关联的已支付的订单信息> <功能详细描述>
	 * 
	 * @param referenceId
	 * @return
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public List<OrderVo> getVmRelatedPaidOrder(long referenceId)
			throws HsCloudException;
	/**
	   * <获取vm关联的正在使用的订单> 
	  * <功能详细描述> 
	  * @param referenceId
	  * @return
	  * @throws HsCloudException 
	  * @see [类、类#方法、类#成员]
	   */
    public String getVmCurrentOrderNo(String uuid)throws HsCloudException;
    /**
     * <保存订单到数据库>
     * @param order
     * @return
     * @throws HsCloudException
     */
    public long saveOrder(Order order)throws HsCloudException;
    /**
     * <按需购买生成订单>
     * @param products
     * @param vmNum
     * @return
     * @throws HsCloudException
     */
    public long submitOrder4Need(List<OrderProduct> products,int vmNum)throws HsCloudException;
    /**
     * <3.0 订单详情>
     * @param orderId
     * @param orderType
     * @return
     * @throws HsCloudException
     */
    public List<OrderItemVo> orderDetail(long orderId,short orderType)throws HsCloudException;
    /**
     * <3.0 订单按需购买续费>
     * @param oldItem
     * @param newItem
     * @param user
     * @param period
     * @return
     * @throws HsCloudException
     */
    public OrderItem renewOrder4Need(String uuid,OrderItem newItem,
    		User user,Date expirationDate,Integer rebateRate,Integer giftRebateRate)throws HsCloudException;
    /**
     * <取消用户所有未支付订单>
     * @param userId
     * @throws HsCloudException
     */
    public void cancelUnPaidOrderByUser(long userId)throws HsCloudException;
    /**
     * <获取订单序列号>
     * @param tableName
     * @return
     * @throws HsCloudException
     */
    public String getOrderSerialNumber(String tableName) throws HsCloudException;

	public String getVmNoteffectiveOrderNo(String uuid) throws HsCloudException;
}
