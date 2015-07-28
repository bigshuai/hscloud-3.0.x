/**
 * @title vmOpsRes.java
 * @package com.hisoft.hscloud.vpdc.ops.json.bean
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-8 下午8:39:01
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.json.bean;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-8 下午8:39:01
 */
public class VmOpsRes {
private boolean success = true;//是否操作成功
private String msg;//消息内容
private String code ="00000";//消息代码
private Object vo;//返回值对象


/**
 * @return success : return the property success.
 */
public boolean isSuccess() {
	return success;
}
/**
 * @param success : set the property success.
 */
public void setSuccess(boolean success) {
	this.success = success;
}
/**
 * @return vo : return the property vo.
 */
public Object getVo() {
	return vo;
}
/**
 * @param vo : set the property vo.
 */
public void setVo(Object vo) {
	this.vo = vo;
}
public String getCode() {
	return code;
}
/**
 * @param code : set the property code.
 */
public void setCode(String code) {
	this.code = code;
}

public String getMsg() {
	return msg;
}
/**
 * @param msg : set the property msg.
 */
public void setMsg(String msg) {
	this.msg = msg;
}
/* (非 Javadoc) 
 * <p>Title: toString</p> 
 * <p>Description: </p> 
 * @return 
 * @see java.lang.Object#toString() 
 */
@Override
public String toString() {
	return "vmOpsRes [success=" + success + ", msg=" + msg + ",code="+code+"]";
}
}
