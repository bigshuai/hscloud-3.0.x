/* 
* 文 件 名:  ScFeeTypeDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-12-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.dao.impl; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.dao.ScFeeTypeDao;
import com.hisoft.hscloud.bss.sla.sc.entity.ScFeeType;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.vo.ScFeeTypeVo;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-12-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class ScFeeTypeDaoImpl extends HibernateDao<ScFeeType,Long> implements
		ScFeeTypeDao {
	private Logger logger=Logger.getLogger(ScFeeTypeDaoImpl.class);
	/** 
	 * @param scId
	 * @throws HsCloudException 
	 */
	@Override
	public void deleteFeeTypeByScId(ServiceCatalog sc) throws HsCloudException {
		try{
			super.batchExecute("delete from ScFeeType where sc=?",sc);
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}
	@Override
	public ScFeeType getFeeTypeById(Long id,int scId) throws HsCloudException {
		try{
			String hql="from ScFeeType sf where sf.id = :sfId and sf.sc.id= :scId";
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("sfId",id);
			params.put("scId", scId);
			List<ScFeeType> feeTypeList=super.find(hql, params);
			if(feeTypeList!=null&&feeTypeList.size()==1){
				return feeTypeList.get(0);
			}else{
				return null;
			}
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}
	@Override
	public List<ScFeeType> getScFeeTypeByScId(int scId) throws HsCloudException {
		List<ScFeeType> result=new ArrayList<ScFeeType>();
		try{
			String hql="from ScFeeType sf where sf.sc.id= :scId ";
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("scId", scId);
			List<ScFeeType> feeTypeList=super.find(hql, params);
			if(feeTypeList!=null&&feeTypeList.size()>0){
				result=feeTypeList;
			}
			return result;
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}
	@Override
	public List<ScFeeTypeVo> getScFeeTypeByOrderItemId(long orderItemId)
			throws HsCloudException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select t.id,t.period,t.price, t2.use_point_or_not as usePointOrNot, t2.use_gift_or_not as useGiftOrNot ")
					.append(" from hc_sc_feetype t ,hc_order_item t1, hc_service_catalog t2 ")
					.append(" where t.sc_id=t1.serviceCatalogId and t1.serviceCatalogId=t2.id ")
					.append(" and t1.id = :orderItemId");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("id", Hibernate.LONG);
			query.addScalar("period", Hibernate.INTEGER);
			query.addScalar("price", Hibernate.BIG_DECIMAL);
			query.addScalar("usePointOrNot", Hibernate.BOOLEAN);
			query.addScalar("useGiftOrNot", Hibernate.BOOLEAN);
			query.setResultTransformer(Transformers
					.aliasToBean(ScFeeTypeVo.class));
			query.setParameter("orderItemId", orderItemId);
			return query.list();
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
	
	
}
