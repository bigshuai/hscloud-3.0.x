/**
 * @title PageNoUtil.java
 * @package com.hisoft.hscloud.vpdc.ops.util
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-5-30 下午6:03:49
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.util;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

/**
 * @description 为hibernateTemplate 进行分页
 * @version 1.0
 * @author hongqin.li
 * @update 2012-5-30 下午6:03:49
 */
@Service
public class PageNoUtil {
	
 	@Autowired
 	private HibernateTemplate hibernateTemplate;
	/**
	 * 
	 * @title: getList
	 * @description 用一句话说明这个方法做什么
	 * @param session
	 * @param hql
	 * @param offset
	 * @param length
	 * @return 设定文件
	 * @return List<?>    返回类型
	 * @throws
	 * @version 1.0
	 * @author hongqin.li
	 * @update 2012-5-30 下午6:07:34
	 */
    public static List<?> getList( Session session , String hql , int offset, int length){
        Query q = session.createQuery(hql);
        q.setFirstResult(offset);
        q.setMaxResults(length);
        List<?> list = q.list(); 
        return list;
     }
    
	public List<?> getListForPage(final String hql, final int offset,
			final int length) {
		HibernateCallback<?> callback =	new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List<?> list2 = PageNoUtil.getList(session, hql, offset,
						length);
				return list2;
			}
		};
		List<?> list1 = hibernateTemplate.executeFind(callback);
		return list1;
	} 
  
}
