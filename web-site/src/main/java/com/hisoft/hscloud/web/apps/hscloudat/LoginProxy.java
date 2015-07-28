package com.hisoft.hscloud.web.apps.hscloudat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.struts2.ServletActionContext;

/**
 * Servlet implementation class loginProxy
 */
public class LoginProxy extends HttpServlet {

	String url = "http://localhost:8080/portal/hiCloudAT/loginUser";

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String data =req.getParameter("req");
		try {
			/*String str1= "Access-Control-Allow-Origin";
	        String str2 =  "*";
	        resp.setHeader(str1, str2);*/
			resp.setContentType("application/x-www-form-urlencoded; charset=UTF-8"); 
			resp.getWriter().write(invoke(data));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println();
	}

	public String invoke(String data) throws Exception {
		String response = null;
		PostMethod postMethod = new PostMethod(url);
		/*byte[] bytes = ("req=" + data).getBytes("utf-8");
		InputStream inputStream = new ByteArrayInputStream(bytes, 0,
				bytes.length);
		RequestEntity requestEntity = new InputStreamRequestEntity(inputStream,
				bytes.length, "application/x-www-form-urlencoded; charset=utf-8");
		postMethod.setRequestEntity(requestEntity);*/
		postMethod.setParameter("req",	data);
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setSoTimeout(12000);
		httpClient.setParams(params);
		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode == HttpStatus.SC_OK) {
			response = postMethod.getResponseBodyAsString();
		} else {
			response = "statusCode:" + statusCode;
		}
		postMethod.releaseConnection();
		return response;
	}

}