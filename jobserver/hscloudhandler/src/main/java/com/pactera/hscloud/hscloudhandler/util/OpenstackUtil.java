package com.pactera.hscloud.hscloudhandler.util; 


import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openstack.api.compute.TenantResource;
import org.openstack.api.hscloud.HscloudResource;
import org.openstack.client.OpenStackClient;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.pactera.hscloud.common4j.util.FileUtil;

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
	private HscloudResource hscloud ;
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
			Properties properties = new Properties();
			properties.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream(configuration),
					FileUtil.fileEncode));
			String cryptedPasswd=properties.getProperty("auth.password");
			String decrypedPasswd=SecurityHelper.DecryptData(cryptedPasswd,Constants.DEFAULT_SECURITY_KEY);
			properties.setProperty("auth.password",decrypedPasswd);
			properties.putAll(System.getProperties());
			client = OpenStackClient.authenticate(properties);
		} catch (Exception e) {
			throw new HsCloudException(Constants.READ_PROPERTIES_EXCEPTION, "initClient Excepiton", logger, e);
		}
		return client;
	}
	
	public TenantResource getCompute() throws HsCloudException{
		logger.debug("Get OpenStackClient computeEndpoint.");
		if(compute == null){
			compute = getClient().getComputeEndpoint();
		}
		else if(!isValidateToken()){
			compute = initClient().getComputeEndpoint();
		}
		return compute;
	}
	
	public HscloudResource getHscloudResource() {
		if (hscloud == null) {
			hscloud = getClient().getHscloudEndpoint();
		} else if (!isValidateToken()) {
			hscloud = initClient().getHscloudEndpoint();
		}
		return hscloud;
	}
	
	/*public void init(){
		init("etc/openstack.properties");
		hscloud = client.getHscloudEndpoint();
		compute = client.getComputeEndpoint();
	}*/
	
}
