package com.hisoft.hscloud.common.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
public class MSMUtil {
	private static Logger logger = Logger.getLogger(OpenstackUtil.class);
	public static Map<String, String> MSMMap = new HashMap<String, String>();
	private String account;
	private String password;
	private String company;

	public MSMUtil(){
		 MSMMap = PropertiesUtils.getPropertiesMap("msm.properties", MSMMap);
		 account = SecurityHelper.DecryptData(MSMMap.get("msm.ID").trim());
		 password = SecurityHelper.DecryptData(MSMMap.get("msm.PWD").trim());
		 company = MSMMap.get("msm.COMPANY").trim();
	 }
	
	/**
	 * 获取6位随机值
	 * @return
	 */
	private int getCode(){
		int code = (int)((Math.random()*9+1)*100000);
		return code;
	}
	
	/**
	 * @param Mobile
	 * @param Content
	 * @param send_time
	 * @return 返回0 或者 1 都是  提交成功
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	private int sendSMS(String Mobile,String Content,String send_time) throws MalformedURLException, UnsupportedEncodingException {
		URL url = null;
		//String send_content=URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");//发送内容
		//String url_ = "http://inolink.com/WS/BatchSend.aspx?CorpID="+account+"&Pwd="+password+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time;
		Content = company+Content;
		String send_content=URLEncoder.encode(Content.replaceAll("<br/>", " "), "utf-8");//发送内容
		String url_ = "http://116.213.72.20/SMSHttpService/send.aspx?username="+account+"&password="+password+"&mobile="+Mobile+"&content="+send_content+"&Extcode=&senddate=&batchID=";
		url = new URL(url_);
		BufferedReader in = null;
		int inputLine = 0;
		try {
			logger.info("开始发送短信手机号码为 ："+Mobile);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			logger.error("网络异常,发送短信失败！");
			inputLine=-2;
			e.printStackTrace();
		}
		logger.info("发送短信接口返回值：  "+inputLine);
		return inputLine;
	}
	
    /**
     * 注册验证码
     * @param Mobile
     * @return String[]数组[0]  SendSMSResult 短信发送的结果
     * @return String[]数组[1]  verificationCode 成功返回6位验证码,失败返回null
     */
	public String[] registMSM(String Mobile) {
		String[] sendSMSResult_and_verificationCode = new String[2];
		String verificationCode = String.valueOf(this.getCode());
		String Content = "您的注册验证码为" + verificationCode + "，请您在10分钟之内验证，逾期将失效。";
		int sendSMSResult = 0;
		try {
			sendSMSResult = this.sendSMS(Mobile, Content, "");
			if (sendSMSResult != 0 && sendSMSResult != 1) {
				verificationCode = null;
			}
			sendSMSResult_and_verificationCode[0] = String.valueOf(sendSMSResult);
			sendSMSResult_and_verificationCode[1] = verificationCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendSMSResult_and_verificationCode;
	}
	
	/**
	 * 订单支付验证验证码
	 * @param Mobile
     * @return String[]数组[0]  SendSMSResult 短信发送的结果
     * @return String[]数组[1]  verificationCode 成功返回6位验证码,失败返回null
	 */
	public String[] payOrderMSM(String Mobile){
		String[] sendSMSResult_and_verificationCode = new String[2];
		String verificationCode = String.valueOf(this.getCode());
		String Content = "您的支付验证码为" + verificationCode + "，请您在10分钟之内验证，逾期将失效。";
		int sendSMSResult = 0;
		try {
			sendSMSResult = this.sendSMS(Mobile, Content, "");
			if (sendSMSResult != 0 && sendSMSResult != 1) {
				verificationCode = null;
			}			
			sendSMSResult_and_verificationCode[0] = String.valueOf(sendSMSResult);
			sendSMSResult_and_verificationCode[1] = verificationCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendSMSResult_and_verificationCode;
	}
	
	/**
	 * 试用云主机到期提醒：提醒一次
	 * @param Mobile
	 * @return
	 */
	public int tryVmExpireMSM(String Mobile,String vm_name){
		String Content = "您的试用云主机["+vm_name+"]距离到期还差1天。";
		int result = 0;
		try {
			result = this.sendSMS(Mobile, Content, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 正试云主机到期提醒：提醒三次，距离到期日分别为7，3，1天时提醒
	 * @param Mobile
	 * @return
	 */
	public int VMExpireMSM(String Mobile,String vm_name,int num){
		String Content = "您的云主机["+vm_name+"]距离到期还差"+num+"天。";
		int result = 0;
		try {
			result = this.sendSMS(Mobile, Content,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 信息手动发送
	 * @param Mobile
	 * @return
	 */
	public int adminOperateMSM(String Mobile,String content){
		int result = 0;
		try {
			result = this.sendSMS(Mobile, content,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//test
	public static void main(String[] arg){
		MSMUtil mu = new MSMUtil();
		System.out.println(mu.registMSM("15901329294"));
	}
}
