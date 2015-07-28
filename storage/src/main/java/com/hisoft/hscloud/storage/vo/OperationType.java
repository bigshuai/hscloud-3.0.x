/* 
* 文 件 名:  OperationType.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public enum OperationType {
    LOGIN(1, "login", "登录"),
    FIND(2, "find", "查询"),
    PLAY(3, "play", "播放"),
    DOWNLOAD(4, "download", "下载"),
    LOGOUT(9, "Quit", "退出");
    
    private int id;
    private String code;
    private String description;
    
    OperationType(int id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public static String getCodeById(int id) {
        for(OperationType operationType : OperationType.values()) {
            if(operationType.getId() == id) {
                return operationType.getCode();
            }
        }
        return null;
    }
}
