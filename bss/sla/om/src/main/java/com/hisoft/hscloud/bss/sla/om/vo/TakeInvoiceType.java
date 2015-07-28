/* 
* 文 件 名:  TakeInvoiceType.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.vo; 

import java.math.BigDecimal;

import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public enum TakeInvoiceType {
    
    EXPRESS_DELIVERY(0, "快递10元", new BigDecimal(10)),
    EMS(1, "EMS20元", new BigDecimal(20)),
    SELF_CREATED(2, "前台自取（免费）", new BigDecimal(0));
    
    
    private int index;
    private String description;
    private BigDecimal price;
    
    TakeInvoiceType(int index, String description, BigDecimal price) {
        this.index = index;
        this.description = description;
        this.price = price;
    }
    
    public static TakeInvoiceType getTakeInvoiceType(int index) {
        TakeInvoiceType[] all = TakeInvoiceType.values();
        for(TakeInvoiceType type : all) {
            if(type.getIndex() == index) {
                return type;
            }
        }
        throw new HsCloudException("TakeInvoiceType枚举类型错误", new RuntimeException());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
