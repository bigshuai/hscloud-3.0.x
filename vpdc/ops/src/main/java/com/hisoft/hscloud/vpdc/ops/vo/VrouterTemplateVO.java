/* 
* 文 件 名:  VrouterTemplateVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VrouterTemplateVO {
    private long id;
    
    private int cpuCore;

    private int diskCapacity;
    
    private int memSize;
    
    private int flavorId;
    
    private String imageId;
    
    private int osId;
    
    private String version;
    
    private String name;

    public int getCpuCore() {
        return cpuCore;
    }

    public void setCpuCore(int cpuCore) {
        this.cpuCore = cpuCore;
    }

    public int getDiskCapacity() {
        return diskCapacity;
    }

    public void setDiskCapacity(int diskCapacity) {
        this.diskCapacity = diskCapacity;
    }

    public int getMemSize() {
        return memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

    public int getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(int flavorId) {
        this.flavorId = flavorId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getOsId() {
        return osId;
    }

    public void setOsId(int osId) {
        this.osId = osId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
