package com.hisoft.hscloud.message.entity; 

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author yubenjie
 * 后台对发送信息的管理
 *
 */
@Entity
@Table(name = "hc_sms_message")
public class SMSMessage {
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //创建时间
    @Column(name = "create_date", nullable = true)
    private Date createDate;
    //更新时间
    @Column(name = "update_date", nullable = true)
    private Date updateDate;
    //创建人
    @Column(name = "creater", nullable = true)
    private String creater;
    //删除人
    @Column(name = "deleter", nullable = true)
    private String deleter; 
    //发送内容
    @Column(name = "content", nullable = false)
    private String content;
    //手机号码
    @Column(name = "mobile", nullable = true)
    private String mobile;
    //短信发送类型 0：个人发送  1：群发所有用户
    @Column(name = "type", nullable = false)
    private int type=0;
    //状态 0:已发送  1：删除
    @Column(name = "status", nullable = false)
    private int status=0;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getDeleter() {
		return deleter;
	}
	public void setDeleter(String deleter) {
		this.deleter = deleter;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	} 
    
}
