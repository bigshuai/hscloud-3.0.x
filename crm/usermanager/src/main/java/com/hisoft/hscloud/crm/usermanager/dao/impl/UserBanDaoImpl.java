/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.UserBankDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserBank;

/**
 * @author lihonglei
 *
 */
@Repository
public class UserBanDaoImpl extends HibernateDao<UserBank, Long> implements UserBankDao {

	@Override
	public void saveUserBank(UserBank userBank) {
		this.save(userBank);
	}

	@Override
	public UserBank findBankByUserId(Long userId) {
		List<UserBank> list = this.findBy("userId", userId);
		if(list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

}
