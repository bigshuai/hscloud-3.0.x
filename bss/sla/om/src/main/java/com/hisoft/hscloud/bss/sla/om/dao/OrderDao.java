package com.hisoft.hscloud.bss.sla.om.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.TryOrderVo;
import com.hisoft.hscloud.common.util.HsCloudException;

public interface OrderDao {
  /**
   * <根据订单Id获取订单实体bean>
  * <功能详细描述> 
  * @param id
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public Order getOrderById(long id)throws HsCloudException;
  /**
   * <保存订单信息> 
  * <功能详细描述> 
  * @param order 
  * @see [类、类#方法、类#成员]
   */
  public void saveOrder(Order order)throws HsCloudException;
  /**
   * <根据订单号获取订单项信息> 
  * <功能详细描述> 
  * @param id
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public List<OrderItemVo> getOrderItemVoByOrderId(long id)throws HsCloudException;
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
   * <> 
  * <功能详细描述> 
  * @param queryStr
  * @param start
  * @param limit
  * @param page
  * @param type
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public List<TryOrderVo> getTryOrderVoByPage(QueryCondition query)throws HsCloudException;
  /**
   * <一句话功能简述> 
  * <功能详细描述> 
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public long getTryOrderCount(QueryCondition qc)throws HsCloudException;
  /**
   * <分页获取订单信息> 
  * <功能详细描述> 
  * @param paging
  * @param hql
  * @param values
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public Page<Order> findOrderByPage(Page<Order> paging,String hql,Map<String,Object> values)throws HsCloudException;
  /**
   * <根据条件查询出唯一一条Order数据> 
  * <功能详细描述> 
  * @param hql
  * @param values
  * @return 
  * @see [类、类#方法、类#成员]
   */
  public Order findUniqueOrder(String hql,Object... values)throws HsCloudException;
  /**
   * 
   * @param orderId
   * @return
   * @throws HsCloudException
   */
  public List<OrderItemVMVo> getOrderItemVMByOrderId(long orderId)throws HsCloudException;
  /**
   * <查询虚拟机关联的已经支付的订单项，做退款使用> 
  * <功能详细描述> 
  * @param referenceId
  * @return
  * @throws HsCloudException 
  * @see [类、类#方法、类#成员]
   */
  public List<Long> getVmRelatedPaidOrderItemId(long referenceId,Date dayBeginToFeeLocal)throws HsCloudException;
  /**
   * <获取vm关联的已支付的订单信息> 
  * <功能详细描述> 
  * @param referenceId
  * @return
  * @throws HsCloudException 
  * @see [类、类#方法、类#成员]
   */
  public List<OrderVo> getVmRelatedPaidOrder(long referenceId)throws HsCloudException;
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
   * 获得用户的所有订单
   * @param userId
   * @return
   * @throws HsCloudException
   */
  public List<Order> getOrderByUserId(long userId,short orderStatus)throws HsCloudException;
  /**
   * 
   * @param tableName
   * @return
   * @throws HsCloudException
   */
  public String getOrderSerialNumber(String tableName) throws HsCloudException;
  public String getVmNoteffectiveOrderNo(String uuid);
}
