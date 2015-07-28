package com.hisoft.hscloud.crm.usermanager.dao;

import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;

/**
 * @className: MaintenanceLogDao
 * @package: com.hisoft.hscloud.crm.usermanager.dao
 * @description: 用于处理维护日志的增删改查
 * @author: liyunhui
 * @createTime: Sep 6, 2013 10:34:45 AM
 */
public interface MaintenanceLogDao {

	void save(MaintenanceLog ml);

}