package com.hisoft.hscloud.bss.sla.om.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.OrderDao;
import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.entity.Order.Status;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.TryOrderVo;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;

@Repository
public class OrderDaoImpl extends HibernateDao<Order, Long> implements OrderDao {
	private static Logger logger = Logger.getLogger(OrderDaoImpl.class);

	@Override
	public Order getOrderById(long id) throws HsCloudException {
		try {
			return super.findUniqueBy("id", id);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public void saveOrder(Order order) throws HsCloudException {
		try {
			super.save(order);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrderItemVo> getOrderItemVoByOrderId(long id)
			throws HsCloudException {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append(
					"select new com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo( ")
					.append(" oi.id, oi.serviceCatalogName,oi.price, ")
					.append(" oi.quantity,o.createDate, o.orderNo, o.totalPrice,oi.vmGoods.os, ")
					.append(" oi.vmGoods.cpu,oi.vmGoods.memory,oi.vmGoods.disk, ")
					.append(" oi.vmGoods.addDisk,oi.vmGoods.bandwidth,oi.vmGoods.software, ")
					.append(" oi.serviceDesc,oi.nodeName,oi.expirationDate,oi.effectiveDate, ")
					.append(" oi.amount,oi.priceType,oi.pricePeriodType,oi.pricePeriod,o.remark, ")
					.append(" o.rebateRate,oi.pointAmount,oi.usePointOrNot, ")
					.append(" o.totalPointAmount,o.totalAmount,o.orderType,  ")
					.append(" o.totalGiftAmount,oi.giftAmount,  ")
					.append(" o.giftsRebateRate,oi.useGiftOrNot )")
					
					.append(" from Order o,OrderItem oi where oi.order=o and o.id=? ");
			return super.createQuery(hql.toString(), id).list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@Override
	public Order getOrderByOrderNo(String orderNo) throws HsCloudException {
		try {
			String hql = "from Order where orderNo = ?";
			Order order = findUniqueOrder(hql, orderNo);
			return order;
		} catch (Exception e) {
			throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,
					e.getMessage(), logger, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TryOrderVo> getTryOrderVoByPage(QueryCondition qc)
			throws HsCloudException {
		try {
			StringBuilder hql = new StringBuilder();
			Map<String, Object> param = new HashMap<String, Object>();
			Order order=qc.getOrder();
			User user=qc.getUser();
			List<Sort> sorts=qc.getSort();
			Date dateFrom=qc.getFromDate();
			Date dateTo=qc.getToDate();
			Page page=qc.getPage();
			int pageNo=page.getPageNo();
			int pageSize=page.getPageSize();
			hql.append(
					"select new com.hisoft.hscloud.bss.sla.om.vo.TryOrderVo(o.id,o.orderNo,")
					.append("o.status,o.createDate,o.payDate,")
					.append("o.updateDate, oi.effectiveDate,")
					.append("oi.expirationDate,oi.id,oi.tryDuration,user.name,oi.serviceCatalogName,oi.price,oi.priceType,oi.pricePeriodType,oi.pricePeriod) ")
					.append("from Order o,OrderItem oi,User user where oi.order=o and o.user=user and oi.tryOrNo=true ");
			if (order != null) {
				String orderNo=order.getOrderNo();
				if (StringUtils.isNotBlank(orderNo)) {
					hql.append(" and o.orderNo like :orderNo ");
					param.put("orderNo","%"+orderNo+"%");
				}
				
				if(order.getStatus()!=null&&!order.getStatus().equals(Status.All)){
					hql.append(" and o.status = :orderStatus ");
					param.put("orderStatus",order.getStatus());
				}else{
					hql.append(" and o.status != :orderStatus ");
					param.put("orderStatus", Status.Cancelled);
				}
			}else{
				hql.append(" and o.status != :orderStatus ");
				param.put("orderStatus", Status.Cancelled);
			}
			
			if (user != null) {
				hql.append(" and o.user = :user ");
				param.put("user", user);

			}
			
			if (dateFrom != null) {
				hql.append(" and o.createDate >= :updateDateFrom ");
				param.put("updateDateFrom", dateFrom);

			}

			if (dateTo != null) {
				hql.append(" and o.createDate <= :updateDateTo ");
				param.put("updateDateTo", dateTo);

			}
			if (sorts != null) {
				for (int i = 0; i < sorts.size(); i++) {
					Sort sort = sorts.get(i);

					if (i == 0) {
						hql.append(" order by ");
					}

					hql.append(sort.getProperty() + " " + sort.getDirection());

					if (i < sorts.size() - 1) {
						hql.append(",");
					}
				}
			}
			Query query = super.createQuery(hql.toString(), param);
			List<TryOrderVo> result = query
					.setFirstResult(pageNo * pageSize - pageSize).setMaxResults(pageSize)
					.list();

			return result;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

	@Override
	public long getTryOrderCount(QueryCondition qc) throws HsCloudException {
		try {
			Date dateFrom=qc.getFromDate();
			Date dateTo=qc.getToDate();
			Order order=qc.getOrder();
			User user=qc.getUser();
			StringBuilder hql = new StringBuilder(
					"from Order o,OrderItem oi,User user where oi.order=o and o.user=user and oi.tryOrNo=true ");
			Map<String, Object> param = new HashMap<String, Object>();
			if (order != null) {
				String orderNo=order.getOrderNo();
				if (StringUtils.isNotBlank(orderNo)) {
					hql.append(" and o.orderNo like :orderNo ");
					param.put("orderNo","%"+orderNo+"%");
				}
				
				if(order.getStatus()!=null&&!order.getStatus().equals(Status.All)){
					hql.append(" and o.status = :orderStatus ");
					param.put("orderStatus",order.getStatus());
				}else{
					hql.append(" and o.status != :orderStatus ");
					param.put("orderStatus", Status.Cancelled);
				}
			}else{
				hql.append(" and o.status != :orderStatus ");
				param.put("orderStatus", Status.Cancelled);
			}		
			
			if (user != null) {
				hql.append(" and o.user = :user ");
				param.put("user", user);

			}
			
			if (dateFrom != null) {
				hql.append(" and o.createDate >= :updateDateFrom ");
				param.put("updateDateFrom", dateFrom);

			}

			if (dateTo != null) {
				hql.append(" and o.createDate <= :updateDateTo ");
				param.put("updateDateTo", dateTo);

			}
			return super.countHqlResult(hql.toString(), param);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

	@Override
	public Page<Order> findOrderByPage(Page<Order> paging, String hql,
			Map<String, Object> values) throws HsCloudException {
		try {
			return super.findPage(paging, hql, values);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public Order findUniqueOrder(String hql, Object... values)
			throws HsCloudException {
		try {
			return super.findUnique(hql, values);
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

	@Override
	public List<OrderItemVMVo> getOrderItemVMByOrderId(long orderId)
			throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" select o.id as orderId,oi.id as orderItemId, ")
					.append(" vf.id as referenceId,oi.serviceCatalogName, ")
					.append(" oi.price as orderItemPrice, ")
					.append(" o.totalPrice,vf.`name` as vmName,oi.effective_date as effectiveDate ")
					.append(" from hc_order o,hc_order_item oi,")
					.append(" hc_vpdcReference_orderItem vo,hc_vpdc_reference vf ")
					.append(" where o.id=oi.order_id and oi.id=vo.order_item_id and vo.renferenceId=vf.id")
					.append(" and o.id = :orderIdParam and oi.expiration_date > now() ");
			logger.info(sql.toString());
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("orderId", Hibernate.LONG);
			query.addScalar("orderItemId", Hibernate.LONG);
			query.addScalar("referenceId", Hibernate.LONG);
			query.addScalar("serviceCatalogName", Hibernate.STRING);
			query.addScalar("orderItemPrice", Hibernate.BIG_DECIMAL);
			query.addScalar("totalPrice", Hibernate.BIG_DECIMAL);
			query.addScalar("vmName", Hibernate.STRING);
			query.addScalar("effectiveDate", Hibernate.TIMESTAMP);
			query.setResultTransformer(Transformers
					.aliasToBean(OrderItemVMVo.class));
			query.setParameter("orderIdParam", orderId);
			return query.list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<Long> getVmRelatedPaidOrderItemId(long referenceId,Date dayBeginToFeeLocal)
			throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" SELECT t1.id as orderItemId from  ")
					.append(" hc_vpdcReference_orderItem t,hc_order_item t1,hc_order t2 ")
					.append(" where 1=1  ")
					.append(" and t.order_item_id = t1.id ")
					.append(" AND t1.order_id = t2.id ")
					.append(" AND t2.`status` = 1 ")
					.append(" and t1.expiration_date > :dayBeginToFeeLocal ")
					.append(" AND t.renferenceId = :referenceId ");
			logger.info(sql.toString());
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("orderItemId", Hibernate.LONG);
			query.setParameter("referenceId", referenceId);
			query.setParameter("dayBeginToFeeLocal", dayBeginToFeeLocal);
			return query.list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	@Override
	public String getVmCurrentOrderNo(String uuid) throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" select t3.orderNo from  ")
					.append(" hc_vpdc_instance t,hc_vpdcReference_orderItem t1,hc_order_item t2,hc_order t3 ")
					.append(" where 1=1  ")
					.append(" AND  t.VpdcRefrenceId=t1.renferenceId ")
					.append(" AND t1.order_item_id=t2.id and t2.order_id=t3.id ")
					.append(" AND t3.`status` = 1 ")
					.append(" AND t2.effective_date < NOW() and t2.expiration_date > now() ")
					.append(" AND t.vm_id= :UUID order by t3.createDate");
			logger.info(sql.toString());
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("orderNo", Hibernate.STRING);
			query.setParameter("UUID", uuid);
			List<String> result=query.list();
			if(result!=null&&result.size()>=1){
				return result.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	@Override
	public String getVmNoteffectiveOrderNo(String uuid) throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" select t3.orderNo from  ")
					.append(" hc_vpdc_instance t,hc_vpdcReference_orderItem t1,hc_order_item t2,hc_order t3 ")
					.append(" where 1=1  ")
					.append(" AND  t.VpdcRefrenceId=t1.renferenceId ")
					.append(" AND t1.order_item_id=t2.id and t2.order_id=t3.id ")
					.append(" AND t3.`status` = 1 ")
					.append(" AND t2.expiration_date > now() ")
					.append(" AND t.vm_id= :UUID order by t3.createDate");
			logger.info(sql.toString());
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("orderNo", Hibernate.STRING);
			query.setParameter("UUID", uuid);
			List<String> result=query.list();
			if(result!=null&&result.size()>=1){
				return result.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<OrderVo> getVmRelatedPaidOrder(long referenceId)
			throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" SELECT t2.id as orderId,t2.orderNo,t2.totalPrice," )
			        .append(" t2.total_point_amount as totalPointAmount,")
			        .append(" t2.total_amount as totalAmount,")
			        .append(" t2.total_gift_amount as totalGiftAmount,")
					.append(" t2.pay_date as payDate,t2.remark,t3.email as ownerEmail FROM  ")
					.append(" hc_vpdcReference_orderItem t,hc_order_item t1,hc_order t2,hc_user t3 ")
					.append(" WHERE 1=1  ")
					.append(" AND t.order_item_id = t1.id ")
					.append(" AND t1.order_id = t2.id ")
					.append(" AND t3.id=t2.user_id ")
					//.append(" AND t2.`status` = 1 ")
					.append(" AND t.renferenceId = :referenceId ")
					.append(" order by t2.pay_date desc");
			logger.info(sql.toString());
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("orderId", Hibernate.LONG);
			query.addScalar("orderNo", Hibernate.STRING);
			query.addScalar("ownerEmail", Hibernate.STRING);
			query.addScalar("totalPrice", Hibernate.BIG_DECIMAL);
			query.addScalar("totalAmount", Hibernate.BIG_DECIMAL);
			query.addScalar("totalPointAmount", Hibernate.BIG_DECIMAL);
			query.addScalar("totalGiftAmount", Hibernate.BIG_DECIMAL);
			query.addScalar("remark", Hibernate.STRING);
			query.addScalar("payDate", Hibernate.TIMESTAMP);
			query.setResultTransformer(Transformers
					.aliasToBean(OrderVo.class));
			query.setParameter("referenceId", referenceId);
			return query.list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public List<Order> getOrderByUserId(long userId,short orderStatus) throws HsCloudException {
		try{
			StringBuilder hql=new StringBuilder();
			hql.append("from Order o where o.user.id= :userId and o.status= :orderStatus ");
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("userId",userId);
			params.put("orderStatus",Order.Status.getItem(orderStatus));
			Query query=super.createQuery(hql.toString(),params);
			List<Order> orders=query.list();
			return orders;
		}catch(Exception e){
			throw new HsCloudException("", logger, e);
		}
	}

	@Override
	public synchronized String getOrderSerialNumber(String tableName)
			throws HsCloudException {

		BigInteger cacheSize = BigInteger.valueOf(1);
		BigInteger ten = BigInteger.valueOf(10);
		BigInteger hunded = BigInteger.valueOf(100);
		BigInteger thousand = BigInteger.valueOf(1000);
		BigInteger zero = BigInteger.valueOf(0);
		BigInteger cur;
		BigInteger base;
		String orberMantissa;
		try {
			String sql = ("SELECT current_max_id FROM hc_id_generator WHERE table_name = '"
					+ tableName + "' ");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			BigInteger result = (BigInteger) query.uniqueResult();

			cur = result;

			sql = ("UPDATE hc_id_generator SET current_max_id = "
					+ (cur.add(cacheSize)) + " WHERE table_name = '"
					+ tableName + "' ");
			query = getSession().createSQLQuery(sql.toString());
			query.executeUpdate();
			base = cur.add(cacheSize);
			if (base.divide(ten) == zero) {
				orberMantissa = "000" + base.toString();
			} else if (base.divide(hunded) == zero) {
				orberMantissa = "00" + base.toString();
			} else if (base.divide(thousand) == zero) {
				orberMantissa = "0" + base.toString();
			} else {
				orberMantissa = base.toString();
			}
			return orberMantissa;
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}

	}

}
