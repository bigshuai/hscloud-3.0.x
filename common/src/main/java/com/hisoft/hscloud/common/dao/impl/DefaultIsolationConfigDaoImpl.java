package com.hisoft.hscloud.common.dao.impl;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.dao.DefaultIsolationConfigDao;
import com.hisoft.hscloud.common.entity.DefaultIsolationConfig;
@Repository
public class DefaultIsolationConfigDaoImpl extends HibernateDao<DefaultIsolationConfig,Long> implements DefaultIsolationConfigDao {
	@Override
	public DefaultIsolationConfig getIsolationConfigById(long id) {
		return super.findUniqueBy("id", id);
	}

}
