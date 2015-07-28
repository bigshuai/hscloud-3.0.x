package com.hisoft.hscloud.mail.entity;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "hc_mail_template")
public class MailTemplate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private long id;
	
	@Column(name = "keyword", length = 64)
	private String keyword;
	
	@Column(name = "title", length = 64,nullable = false)
	private String title;
	
	@Column(name = "template", length = 10240,nullable = false)
	private String template;
	
	@Column(name = "domain_id", nullable = false)
	private long domainId;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	
	public  String getReplacedTemplate(Map<String, String> map){
		
		String templateStr = this.template;
		for (String key : map.keySet()) {
			if (map.get(key) != null) {
				templateStr = templateStr.replaceAll(
						"\\{params:" + key + "\\}", map.get(key));
			}
		}
		return templateStr;
		
	}

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }
	

}
