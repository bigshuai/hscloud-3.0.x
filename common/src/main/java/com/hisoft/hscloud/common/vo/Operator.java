/* 
* 文 件 名:  Operator.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-12-3 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-12-3] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public enum Operator {
  EQ("="),
  LT("<"),
  GT(">"),
  LIKE("LIKE");
  
  private String operator;
  
  Operator(String operator){ 
	  this.operator=operator;
  }
  
  public  String getOperator(){
	  return this.operator;
  }

}
