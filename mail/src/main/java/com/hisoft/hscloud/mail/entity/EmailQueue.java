package com.hisoft.hscloud.mail.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "hc_email_queue")
public class EmailQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private long id;
	
	@Column(name = "status", length = 4)
	private int status;
	
	@Column(name = "receive_users", length = 1024)
	private String receiveUsers;
	
	@Column(name = "send_user", length = 64)
	private String sendUser;
	
	@Column(name = "subject", length = 64)
	private String subject;
	
	@Column(name = "body", length = 10240)
	private String body;
	
	@Column(name = "create_time")
	private Date createTime;	
	
	@Column(name = "post_time")	
	private Date postTime;
	
	@Column(name = "domain_id")
    private long domainId;
	
	@Column(name = "remark", length = 64)
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReceiveUsers() {
		return receiveUsers;
	}

	public void setReceiveUsers(String receiveUsers) {
		this.receiveUsers = receiveUsers;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }
}
