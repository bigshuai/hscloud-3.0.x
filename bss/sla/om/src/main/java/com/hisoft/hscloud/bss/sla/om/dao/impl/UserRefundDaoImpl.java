package com.hisoft.hscloud.bss.sla.om.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.UserRefundDao;
import com.hisoft.hscloud.bss.sla.om.entity.UserRefund;
import com.hisoft.hscloud.common.util.HsCloudException;
@Repository
public class UserRefundDaoImpl extends HibernateDao<UserRefund, Long> implements
		UserRefundDao {
	private static Logger logger=Logger.getLogger(UserRefundDaoImpl.class);
	@Override
	public Long saveUserRefund(UserRefund ur) throws HsCloudException {
		try{
		  super.save(ur);
		  return ur.getId();
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}

}
