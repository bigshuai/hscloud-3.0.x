package com.hisoft.hscloud.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.web.facade.Facade;

public class BasicDataAction extends HSCloudAction{
	
	private static final long serialVersionUID = -8035145607669514657L;
	private Logger logger = Logger.getLogger(this.getClass());
	private String enUid = "";
	private String code = "";//平台code.
	private long domainId;//平台id.
	@Autowired
	private Facade facade;
	
	/**
	 * 加载所有国家
	 */
	public void loadCountry(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadCountry method.");			
		}
		List<Country> countries = facade.loadCountry();
		fillActionResult(countries);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadCountry method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 加载所以行业
	 */
	public void loadIndustry(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadIndustry method.");			
		}
		List<Industry> industries = facade.loadIndustry();
		fillActionResult(industries);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadIndustry method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 加载域。
	 */
	public void loadDomain(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadDomain method.");			
		}
		Domain domain = facade.loadDomain(code);
		fillActionResult(domain);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadDomain method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 设置 前途切换的企业的id
	 */
	public void setCompany(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter setCompany method.");			
		}
		super.getSession().setAttribute("enUid", enUid);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit setCompany method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 获得前途切换的企业的id
	 */
	public void getCompany(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getCompany method.");			
		}
		String id = super.getSession().getAttribute("enUid").toString();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("enUid", id);
		super.fillActionResult(map);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getCompany method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 通过分平台id去后台数据库hc_domain里面拿到copyright_cn和copyright_en
	 */
	public String loadDomainById(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter loadDomainById method.");			
		}
		Domain domain = facade.getDomainById(domainId);
		fillActionResult(domain);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit loadDomainById method.takeTime:" + takeTime + "ms");
		}
		return null;
	}

	public String getEnUid() {
		return enUid;
	}

	public void setEnUid(String enUid) {
		this.enUid = enUid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

}