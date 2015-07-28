package com.hisoft.hscloud.crm.usermanager.dao.impl;


import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.IndustryDao;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;

@Repository
public class IndustryDaoImpl extends HibernateDao<Industry, Long> implements IndustryDao {

   private Logger logger = Logger.getLogger(this.getClass());
	
   public List<Industry> getAll(String orderBy,boolean isAsc){
	   
	   try{
		   
			if(logger.isDebugEnabled()){
				logger.debug("enter IndustryDaoImpl getAll method.");
				logger.debug("orderBy:"+orderBy);
				logger.debug("isAsc:"+isAsc);
			}
			
			List<Industry> Industrys = (List<Industry>)super.getAll(orderBy, isAsc);
			if(logger.isDebugEnabled()){
				logger.debug("exit IndustryDaoImpl getAll method");
			}
			return Industrys;
			
	   } catch (Exception e) {
		    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	   }
		
   }

}
