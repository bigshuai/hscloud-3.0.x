package com.hisoft.hscloud.crm.usermanager.dao.impl; 

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.crm.usermanager.dao.UserProfileDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
@Repository
public class UserProfileDaoImpl extends HibernateDao<UserProfile, Long> implements UserProfileDao {

	@Override
	public void modifyUserProfile(UserProfile userProfile) {
		super.save(userProfile);
	}

	@Override
	public UserProfile getUserProfileById(long id) {
		return super.get(id);
	}

	@Override
	public void save(UserProfile userProfile) {
		super.save(userProfile);
	}

	@Override
	public UserProfile findUniqueBy(String propertyName, Object value) {
		return super.findUniqueBy(propertyName, value);
	}

}
