package com.pactera.hscloud.openstackhandler.bo;

import java.util.Map;

public class O_MailTemplate {
	
	private Long id;
	
	private Long domain_id;
	
	private String keyword;
	
	private String title;
	
	private String template;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	
	public   String getReplacedTemplate(Map<String, String> map){
		
		String templateStr = this.template;
		for (String key : map.keySet()) {
			if (map.get(key) != null) {
				templateStr = templateStr.replaceAll(
						"\\{params:" + key + "\\}", map.get(key));
			}
		}
		return templateStr;
		
	}
	

}
