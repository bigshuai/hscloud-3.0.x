package com.hisoft.hscloud.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.openstack.model.identity.User;


public class RESTUtil {
	public static String load(String url) throws Exception {
		
		String result=null;

		URL restURL = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();

		conn.setRequestMethod("GET");

		conn.setDoOutput(true);

		conn.setAllowUserInteraction(false);
		
		int rspCode = conn.getResponseCode();
		if(rspCode==200){
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String line, resultStr = "";

			while (null != (line = bReader.readLine()))

			{
				resultStr += line;
			}
			bReader.close();
			result=resultStr;
		}else{
			throw new Exception("rest api call error");
		}
		
		return result;
		
	}
	//独自定义一个返回值类型为Object的get方法，来解决把get方法中把json给tostring的问题；
	public static JSONObject getJsonObject(String url) throws Exception{
		JSONObject responseJson=null;
		StringBuffer response = new StringBuffer("");
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);		
		int rspCode = conn.getResponseCode();
		if(rspCode==200){
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(),"utf-8"));
			String str=bReader.readLine();
			response.append(str);
			bReader.close();
			responseJson =JSONObject.fromObject(response.toString());
		}else{
			throw new Exception("转账异常，接口调用失败");
		}
		return responseJson;
	}
	
	//独自定义一个返回值类型为Object的get方法，来解决把get方法中把json给tostring的问题；
			public static Object getUserJson(String url,User user) throws Exception{
				Object obj=null;
				Object a;
				StringBuffer response = new StringBuffer("");
				URL restURL = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				conn.setAllowUserInteraction(false);		
				int rspCode = conn.getResponseCode();
				if(rspCode==200){
					BufferedReader bReader = new BufferedReader(new InputStreamReader(
							conn.getInputStream(),"utf-8"));
					String str=bReader.readLine();
					response.append(str);
					bReader.close();
					a =(JSONObject)JSONObject.fromObject(response.toString());
					((JSONObject) a).put("user", user);
					//obj=a.get("resultObject");
				}else{
					throw new Exception("rest api call error");
				}
				return a;
			}
	//独自定义一个返回值类型为Object的get方法，来解决把get方法中把json给tostring的问题；
		public static Object getJson(String url) throws Exception{
			Object obj=null;
			StringBuffer response = new StringBuffer("");
			URL restURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setAllowUserInteraction(false);		
			int rspCode = conn.getResponseCode();
			if(rspCode==200){
				BufferedReader bReader = new BufferedReader(new InputStreamReader(
						conn.getInputStream(),"utf-8"));
				String str=bReader.readLine();
				response.append(str);
				bReader.close();
				JSONObject a =JSONObject.fromObject(response.toString());
				obj=a.get("resultObject");
			}else{
				throw new Exception("rest api call error");
			}
			return obj;
		}

	public static String get(String url) throws Exception{
		String result=null;
		StringBuffer response = new StringBuffer("");
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		
		int rspCode = conn.getResponseCode();
//		HttpServletResponse respon = ServletActionContext.getResponse();
//
//		respon.setCharacterEncoding("utf-8");
		if(rspCode==200){
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(),"utf-8"));
			String str=bReader.readLine();
//			str.getBytes("utf-8");
//			while (null != str)
//			{
//				
//				
//			}
			response.append(str);
			bReader.close();
			result=response.toString();
		}else{
			throw new Exception("rest api call error");
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			String requestURL = "http://192.168.177.10:8070/billing.web/" +
					"calculatePrice2?planId=800042343&domainCode=\"XR\"&" +
					"CPU=4&memory=2048&extDisk=\"10,10\"&bandwidth=10&payMonth=3";
			RESTUtil restUtil = new RESTUtil();
			String resultString = restUtil.load(requestURL);
			System.out.print(resultString);
		} catch (Exception e) {
			System.out.print(e.getMessage());

		}
	}

}
