/* 
* 文 件 名:  OpenstackUtil.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2012-11-15 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.util; 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.PropertyFilter;

import org.apache.log4j.Logger;
import org.openstack.api.compute.TenantResource;
import org.openstack.client.OpenStackClient;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2012-11-15] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class OpenstackUtil {
	private TenantResource compute;
	private OpenStackClient client;
	
	private static Logger logger = Logger.getLogger(OpenstackUtil.class);
	
	private static String configuration = "openstack.properties";
	
	public OpenStackClient getClient() {
		if (client == null || !isValidateToken())
			client = initClient();
		return client;
	}
	
	public boolean isValidateToken() throws HsCloudException{
		logger.debug("Get OpenStackClient tokenExpires");
		String tokenExpires = client.getAccess().getToken().getExpires();
		logger.debug("The OpenStackClient tokenExpires is"+tokenExpires);
		tokenExpires=tokenExpires.replace("Z", "");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//yyyy-MM-dd hh:mm:ss
		try {
			Date expiresDate = dateFormat.parse(tokenExpires);
			Date nowDate = Calendar.getInstance().getTime();
			long expiresLong = expiresDate.getTime();
			long nowDateLong = nowDate.getTime();
			if((nowDateLong + Constants.OPENSTACK_TOKEN_EXPIRES_DIFF) > expiresLong){
				return false;
			}
		} catch (ParseException e) {
			throw new HsCloudException("XX001", "校验异常isValidateToken", logger, e);
		}
		return true;
	}

	public OpenStackClient initClient() {
		try {
			Properties properties = PropertiesUtils.getPropertiesObject(configuration);
			properties.putAll(System.getProperties());
			client = OpenStackClient.authenticate(properties);
		} catch (Exception e) {
			throw new HsCloudException(Constants.READ_PROPERTIES_EXCEPTION, "initClient Excepiton", logger, e);
		}
		return client;
	}
	
	public TenantResource getCompute() throws HsCloudException{
		logger.debug("Get OpenStackClient computeEndpoint.");
		if(compute == null || !isValidateToken()){
			compute = initClient().getComputeEndpoint();
		}else{
			compute = getClient().getComputeEndpoint();
		}
		return compute;
	}
	
	public TenantResource getCompute(String zone) throws HsCloudException{
		logger.debug("Get OpenStackClient computeEndpoint by zone");
		if(compute == null || !isValidateToken()){
			compute = initClient().getComputeEndpoint(zone);
		}else{
			compute = getClient().getComputeEndpoint(zone);
		}
		return compute;
	}
	
	/*public HscloudResource getHscloudResource() {
		if (hscloud == null) {
			hscloud = getClient().getHscloudEndpoint();
		} else if (!isValidateToken()) {
			hscloud = initClient().getHscloudEndpoint();
		}
		return hscloud;
	}*/
	
	/*public void init(){
		init("etc/openstack.properties");
		hscloud = client.getHscloudEndpoint();
		compute = client.getComputeEndpoint();
	}*/
	public static class JsonDefaultValueProcessor implements JsonValueProcessor{
        public Object processArrayValue(Object value, JsonConfig config){
        	 if(value!=null && value instanceof java.lang.String){
                 String tempStr=(String)value;
                 if(""==tempStr){
               	  return null;
                 }
           }else if(value==null){
           	return "";
           }
           return value;
     }

      public Object processObjectValue(String key, Object value, JsonConfig config){
            if(value!=null && value instanceof java.lang.String){
                  String tempStr=(String)value;
                  if(""==tempStr){
                	  return null;
                  }
            }else if(value==null){
            	return "";
            }
            return value;
     }
	}

	public static JsonConfig getJSONConfig(){
		JsonConfig jc=new JsonConfig();
		//jc.setJavaPropertyFilter(javaPropertyFilter)
		jc.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if(arg2==null){
					return true;
				}
				return false;
			}
		});
		//jc.registerJsonValueProcessor(java.lang.Object. class, new JsonDefaultValueProcessor());
         return jc;
	}
}
