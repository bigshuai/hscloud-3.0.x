package com.hisoft.hscloud.message.entity; 

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
/**
 * 
* <公告> 
* <功能详细描述> 
* 
* @author  lihonglei 
* @version  [版本号, 2013-1-24] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
@Entity
@Table(name = "hc_announcement")
public class Announcement {
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "title", length = 50, nullable = false)
    private String title;
    
    @Column(name = "content", length = 1000, nullable = false)
    private String content;
    
    //创建时间
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();
    
    //修改时间
    @Column(name = "update_time", nullable = false)
    private Date updateTime = new Date();
    
    //0:有效，1:无效
    @Column(name = "status", length = 4, nullable = false)
    private int status = 0;
    
    //0:站内，1：站外
    @Column(name = "type", length = 4, nullable = false)
    private int type = 0;
    
    @Column(name = "create_id", nullable = false)
    private Long createId;
    
    @Column(name = "update_id", nullable = false)
    private Long updateId;
    
    //备注
    @Column(name = "remark", nullable = true)
    private String remark;
    
    //平台
    @Column (name = "domainIds", nullable = false)
    private String domainIds;
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public String getDomainIds() {
		return domainIds;
	}

	public void setDomainIds(String domainIds) {
		this.domainIds = domainIds;
	}

	

	
}
