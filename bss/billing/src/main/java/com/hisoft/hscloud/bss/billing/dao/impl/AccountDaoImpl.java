package com.hisoft.hscloud.bss.billing.dao.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;
import com.hisoft.hscloud.bss.billing.dao.AccountDao;
import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;

@Repository
public class AccountDaoImpl extends HibernateDao<Account, Long> implements AccountDao{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void save(Account account){
		super.save(account);
	}

	
	public Account findById(Object value){
		return super.findUniqueBy("id", value);
	}
	
	public Account getAccountByUserId(Long user_id){
		
		//return findUnique("from Account where userId=?", user_id);
		return findUnique("from Account a where a.user.id=?", user_id);
	}


	@Override
	public Page<Account> findPage(Page<Account> page, String hql,Map<String,?> map) {
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter UserDaoImpl findPage method.");
				logger.debug("hql:"+hql);
				logger.debug("page:"+page);
				logger.debug("map:"+map);
			}
			Page<Account> accounts =super.findPage(page, hql, map);
			if(logger.isDebugEnabled()){
				logger.debug("exit UserDaoImpl findPage method");
			}
			return accounts;
			
		} catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
	}

}
