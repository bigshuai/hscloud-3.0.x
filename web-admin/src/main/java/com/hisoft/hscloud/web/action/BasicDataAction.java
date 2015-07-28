package com.hisoft.hscloud.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.web.facade.Facade;

public class BasicDataAction extends HSCloudAction {
	
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 21960489025057463L;

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Facade facade;
	
	public void loadUserBrand(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadUserBrand method.");			
		}
		List<UserBrand> brand = facade.loadUserBrand();
		fillActionResult(brand);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadUserBrand method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <加载省份列表> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void loadProvince() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadProvince method.");			
		}
	    List<Province> provinceList = facade.loadProvince();
        fillActionResult(provinceList);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadProvince method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 加载Domin的基础信息
	 * 通过Admin id 查询
	 */
	public void loadDomain(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadDomain method.");			
		}
		Admin admin=(Admin)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
		List<Domain> domains = facade.loadDomain(admin.getIsSuper(), admin.getId());
		fillActionResult(domains);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadDomain method.takeTime:" + takeTime + "ms");
		}
	}
	
}
