package com.hisoft.hscloud.bss.sla.om.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import com.hisoft.hscloud.bss.sla.om.dao.RefundVmDao;
import com.hisoft.hscloud.bss.sla.om.entity.RefundVm;
import com.hisoft.hscloud.common.util.HsCloudException;
@Repository
public class RefundVmDaoImpl extends HibernateDao<RefundVm, Long> implements
		RefundVmDao {
	
	private static Logger logger=Logger.getLogger(UserRefundDaoImpl.class);
	@Override
	public void saveRefundVm(RefundVm rv) throws HsCloudException {
		try{
		super.save(rv);
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
	}

}
