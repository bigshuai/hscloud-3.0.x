package com.hisoft.hscloud.common.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.OrderBy;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.dao.UserBrandDao;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.UserUserBrandVO;

@Repository
public class UserBrandDaoImpl extends HibernateDao<UserBrand, Long> 
    implements UserBrandDao {
	
	private static Logger logger=Logger.getLogger(UserBrandDaoImpl.class);
	
	@OrderBy("id")
	public List<UserBrand> getAll() {
		return super.getAll();
	}

	public void addBrand(UserBrand brand)
			throws HsCloudException {
		try {
			super.save(brand);
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

	public UserBrand getBrandById(Long brandId) throws HsCloudException {
		try {
			return super.findUniqueBy("id", brandId);  
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
		
	}

	public Page<UserBrand> getBrandByPage(String condition,Page<UserBrand> paging)
			throws HsCloudException {
		try {
			StringBuilder hql=new StringBuilder("from UserBrand where 1=1 ");
			Map<String,String> params=new HashMap<String,String>();
			if(StringUtils.isNotBlank(condition)){
				hql.append(" and name like :condition or description like :condition ");
				params.put("condition", "%"+condition+"%");
			}
			hql.append(" order by createDate desc");
			Page<UserBrand> result=super.findPage(paging, hql.toString(), params);
             return result;
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

	public List<UserBrand> getAllNormalBrand() throws HsCloudException {
		try {
			StringBuilder hql=new StringBuilder("from UserBrand where status=1 ");
			List<UserBrand> result=super.find(hql.toString());
            return result;
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
	}

	public boolean checkBrandNameDup(String name) throws HsCloudException {
		try {
			StringBuilder hql=new StringBuilder("from UserBrand where name= :name ");
			Map<String,String> params=new HashMap<String,String>();
			params.put("name",name);
			List<UserBrand> result=super.find(hql.toString(),params);
			if(result==null||result.size()==0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
		
	}

	public UserBrand getBrandByCode(String code) throws HsCloudException {
		try {
			UserBrand result=super.findUniqueBy("code", code);
			return result;
		} catch (Exception e) {
			throw new HsCloudException("",logger,e);
		}
		
	}

	@Override
	public UserUserBrandVO getUserAndBrandByReferenceId(long referenceId)
			throws HsCloudException {
		StringBuilder sqlStr = new StringBuilder("select");
		sqlStr.append(" t1.name as userName,t1.email as userEmail, ").append(
				" t2.name as userBrandName,t2.code as userBrandCode");
		sqlStr.append(
				" from hc_vpdc_reference t3 left join hc_user t1 on t3.vm_owner=t1.id left join hc_user_brand t2 on t1.level=t2.code")
				.append(" where t3.id= :referenceId limit 1 ");
		Map<String, Long> params = new HashMap<String, Long>();
		try {
			params.put("referenceId", referenceId);
			SQLQuery query = this.getSession()
					.createSQLQuery(sqlStr.toString());
			query.setProperties(params);
			query.addScalar("userName", Hibernate.STRING);
			query.addScalar("userEmail", Hibernate.STRING);
			query.addScalar("userBrandName", Hibernate.STRING);
			query.addScalar("userBrandCode", Hibernate.STRING);
			query.setResultTransformer(Transformers
					.aliasToBean(UserUserBrandVO.class));
			List<UserUserBrandVO> resultList = query.list();
			if (resultList != null && resultList.size() > 0) {
				return resultList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new HsCloudException("", logger, e);
		}
	}
}