/* 
* 文 件 名:  SiteConfigDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-12-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.dao.impl; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.dao.SiteConfigDao;
import com.hisoft.hscloud.common.entity.SiteConfig;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-12-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class SiteConfigDaoImpl extends HibernateDao<SiteConfig, Long> implements
		SiteConfigDao {
	private static final Logger logger=Logger.getLogger(SiteConfigDaoImpl.class);
	/** 
	 * @param property
	 * @param value
	 * @return
	 * @throws HsCloudException 
	 */
	public SiteConfig getSiteConfigByProperty(String property, Object value)
			throws HsCloudException {
		try{
			return super.findUniqueBy("config", value);
		}catch(Exception e){
			throw new HsCloudException("",logger,e);
		}
		
	}

	/** 
	 * @param config
	 * @param belongTo
	 * @return
	 * @throws HsCloudException 
	 */
	public String getConfigValue(String config, String belongTo)
			throws HsCloudException {
		try {
			String configValue = null;
			StringBuilder hql = new StringBuilder("from SiteConfig where 1=1 "); 
			Map<String, String> params = new HashMap<String, String>();
			if(StringUtils.isNotBlank(config)){
				hql.append(" and config = :config ");
				params.put("config", config);
			}
			if(StringUtils.isNotBlank(belongTo)){
				hql.append(" and belongTo = :belongTo ");
				params.put("belongTo", belongTo);
			}
			List<SiteConfig> configs = super.find(hql.toString(), params);
//			if (configs == null || configs.size() != 1) {
//				throw new HsCloudException(Constants.GET_SITE_CONFIG_ERROR, logger);
//			}
//			configValue=configs.get(0).getConfigValue();
//			if(configValue==null){
//				throw new HsCloudException(Constants.GET_SITE_CONFIG_ERROR, logger);
//			}
			if(configs!=null&&configs.size()==1){
				SiteConfig configObj=configs.get(0);
				if(configObj!=null)
					configValue=configObj.getConfigValue();
			}
			return configValue;
		} catch (Exception e) {
			throw new HsCloudException(Constants.GET_SITE_CONFIG_ERROR, logger, e);
		}
	}

}
