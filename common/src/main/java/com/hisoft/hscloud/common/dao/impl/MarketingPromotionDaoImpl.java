//$Id: MarketingPromotionDaoImpl.java Sep 22, 2013 9:42:06 AM liyunhui  $begin:~
package com.hisoft.hscloud.common.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.dao.MarketingPromotionDao;
import com.hisoft.hscloud.common.entity.MarketingPromotion;
import com.hisoft.hscloud.common.util.HsCloudException;

/**
 * @className: MarketingPromotionDaoImpl
 * @package: com.hisoft.hscloud.common.dao.impl
 * @description: TODO
 * @author: liyunhui
 * @since: 1.0
 * @createTime: Sep 22, 2013 9:42:06 AM
 * @company: Pactera Technology International Ltd
 */
@Repository
public class MarketingPromotionDaoImpl extends HibernateDao<MarketingPromotion, Long> implements MarketingPromotionDao {

	private static Logger logger=Logger.getLogger(MarketingPromotionDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public MarketingPromotion findUnique(String hql,Object ... values){
		return (MarketingPromotion)super.findUnique(hql, values);
	}
	
	@Override
	public Page<MarketingPromotion> getMarketingPromotionByPage(String condition, 
			Page<MarketingPromotion> paging) throws HsCloudException {
		try {
			StringBuilder hql=new StringBuilder("from MarketingPromotion where 1=1 ");
			Map<String,String> params=new HashMap<String,String>();
			if(StringUtils.isNotBlank(condition)){
				hql.append(" and name like :condition or code like :condition ");
				params.put("condition", "%"+condition+"%");
			}
			hql.append(" order by createDate desc");
			Page<MarketingPromotion> result=super.findPage(paging, hql.toString(), params);
             return result;
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

	@Override
	public MarketingPromotion getMarketingPromotionById(Long id)
			throws HsCloudException {
		try {
			return super.findUniqueBy("id", id);  
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

	/**
	 * <1.校验推广名称是否重复> <功能详细描述>
	 * <2.校验推广代码是否重复> <功能详细描述>
	 * <3.校验推广地址是否重复> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean checkMarketingPromotionParameterDup(String name,
			String code, String address) throws HsCloudException {
		try {
			StringBuilder hql=new StringBuilder("from MarketingPromotion where ");
			Map<String,String> params=new HashMap<String,String>();
			if (name != null){
				hql.append(" name= :name ");
				params.put("name",name);
			}
			if (code != null){
				hql.append(" code= :code ");
				params.put("code",code);
			}
			if (address != null){
				hql.append(" address= :address ");
				params.put("address",address);
			}
			List<MarketingPromotion> result=super.find(hql.toString(),params);
			if(result==null||result.size()==0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

	@Override
	public void addMarketingPromotion(MarketingPromotion mp) throws HsCloudException {
		try {
			super.save(mp);
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

}
//$Id: MarketingPromotionDaoImpl.java Sep 22, 2013 9:42:06 AM liyunhui  $end:~