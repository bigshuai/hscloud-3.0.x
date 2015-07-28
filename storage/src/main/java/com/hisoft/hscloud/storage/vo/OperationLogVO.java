/* 
* 文 件 名:  OperationLogVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.vo; 

import java.util.Date;
/** 
 * <操作日志vo> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class OperationLogVO {
    private long id;
    private int operationType;
    private Date date;
    private String operator;
    private String remark;
    private String ip;
    private String operationTypeText;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getOperationType() {
        return operationType;
    }
    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getOperationTypeText() {
        return operationTypeText;
    }
    public void setOperationTypeText(String operationTypeText) {
        this.operationTypeText = operationTypeText;
    }
}
