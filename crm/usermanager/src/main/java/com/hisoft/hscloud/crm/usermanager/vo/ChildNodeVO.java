/* 
* 文 件 名:  ChildNodeVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2012-10-15 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2012-10-15] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ChildNodeVO {
    private long actionId;
    private String acitonName;
    private long permissionId;
    public long getActionId() {
        return actionId;
    }
    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
    public String getAcitonName() {
        return acitonName;
    }
    public void setAcitonName(String acitonName) {
        this.acitonName = acitonName;
    }
    public long getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(long permissionId) {
        this.permissionId = permissionId;
    }
}
