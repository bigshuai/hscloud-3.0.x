
package com.hisoft.hscloud.bss.billing.dao;


import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Account;



public interface AccountDao {
	
	public void save(Account account);
	
	public Account findById(Object value);

	public Account getAccountByUserId(Long user_id);
	
	public Page<Account> findPage(Page<Account> page,String hql,Map<String,?> map);
	
}
