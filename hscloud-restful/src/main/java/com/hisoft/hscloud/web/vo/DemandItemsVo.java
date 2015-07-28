/* 
* 文 件 名:  DemandItemsVo.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-3-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-3-21] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class DemandItemsVo {
	
	private String accessIp;//获取请求Ip
	private String accessId;//鉴权用户
	private String accessKey;//鉴权密码  MD5加密
	private String osId;//操作系统ID
	private String paymode;//支付方式：1：月付 3：季付 	6：半年付  12：年付
	private String vmnum;//购买虚拟机数量
	private String vcpu;//CPU核数
	private String extDisk;//数据盘
	private String memSize;//内存大小
	private String bandwidth;//带宽
	private String datacenter;//数据中心编码
	private String useCoupon;//是否使用返点(true;false)
	private String useGift; //是否使用礼金(true;false)
	public String getAccessIp() {
		return accessIp;
	}
	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getOsId() {
		return osId;
	}
	public void setOsId(String osId) {
		this.osId = osId;
	}
	public String getPaymode() {
		return paymode;
	}
	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}
	public String getVmnum() {
		return vmnum;
	}
	public void setVmnum(String vmnum) {
		this.vmnum = vmnum;
	}
	public String getVcpu() {
		return vcpu;
	}
	public void setVcpu(String vcpu) {
		this.vcpu = vcpu;
	}
	public String getExtDisk() {
		return extDisk;
	}
	public void setExtDisk(String extDisk) {
		this.extDisk = extDisk;
	}
	public String getMemSize() {
		return memSize;
	}
	public void setMemSize(String memSize) {
		this.memSize = memSize;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getDatacenter() {
		return datacenter;
	}
	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter;
	}
	public String getUseCoupon() {
		return useCoupon;
	}
	public void setUseCoupon(String useCoupon) {
		this.useCoupon = useCoupon;
	}
	public String getUseGift() {
		return useGift;
	}
	public void setUseGift(String useGift) {
		this.useGift = useGift;
	}
}
