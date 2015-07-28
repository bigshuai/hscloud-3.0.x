package com.pactera.hscloud.hscloudhandler.dao.impl;

import com.pactera.hscloud.common4j.util.DBUtil;
import com.pactera.hscloud.hscloudhandler.dao.HsCloudHandlerDao;

public class HsCloudHandlerDaoImpl implements HsCloudHandlerDao {

	public void updateFlavor(Object flavor) throws Exception{
		DBUtil.save(flavor,"flavor.update");
	}

}
