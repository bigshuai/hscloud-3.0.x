package com.hisoft.hscloud.common.dialect.mysql;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQLDialect extends MySQL5InnoDBDialect {
	
	public MySQLDialect() {
		super();
		registerHibernateType(Types.VARCHAR, Hibernate.TEXT.getName());
	}
	
}
