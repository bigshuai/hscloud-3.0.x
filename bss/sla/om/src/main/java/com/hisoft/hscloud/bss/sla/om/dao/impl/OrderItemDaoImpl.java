/* 
* 文 件 名:  OrderItemDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-11-15 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.dao.impl; 

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.OrderItemDao;
import com.hisoft.hscloud.bss.sla.om.entity.OrderItem;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-11-15] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class OrderItemDaoImpl extends HibernateDao<OrderItem, Long> implements
		OrderItemDao {
	private static Logger logger=Logger.getLogger(OrderDaoImpl.class);
	/** 
	 * @param id
	 * @return 
	 */
	@Override
	public OrderItem getOrderItemById(Long id)throws HsCloudException {
		try{
			return super.findUniqueBy("id", id);
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}

	/** 
	 * @param orderItem 
	 */
	@Override
	public void saveOrderItem(OrderItem orderItem)throws HsCloudException {
		
		 try{
			 super.save(orderItem);
			}catch(Exception e){
				throw new HsCloudException("",logger,e);
			}
	}

}
