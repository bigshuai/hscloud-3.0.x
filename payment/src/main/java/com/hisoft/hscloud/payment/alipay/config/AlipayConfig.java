package com.hisoft.hscloud.payment.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.2
 *日期：2011-03-17
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”
	
 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public final static String ALIPAY_GATEWAY_OLD = "https://www.alipay.com/cooperate/gateway.do?";
	public static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";			
	//1商品购买 4捐赠 
	public static String payment_type="1";
	//授权令牌，该参数的值由快捷登录接口(alipay.auth.authorize)的页面跳转同步通知参数中获取
	public static String token="";
	
	// 调试用，创建TXT日志路径
	public static String log_path = "D:\\alipay_log_" + System.currentTimeMillis()+".txt";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "UTF-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	
	//访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
	public static String transport = "http";
	
	public static String subject="账户充值";

}
