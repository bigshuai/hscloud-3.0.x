/**
* @title ServiceItemDao.java
* @package com.hisoft.hscloud.bss.sla.sc.dao
* @description 用一句话描述该文件做什么
* @author jiaquan.hu
* @update 2012-5-9 下午4:55:51
* @version V1.0
*/
package com.hisoft.hscloud.bss.sla.sc.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-9 下午4:55:51
 */
@Repository
public class ServiceItemDao<T> extends HibernateDao<ServiceItem, Integer> {
	public Os saveOs(Os os){
		this.save(os);
		return os;
	}
}
