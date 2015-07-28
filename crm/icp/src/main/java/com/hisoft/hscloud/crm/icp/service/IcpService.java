package com.hisoft.hscloud.crm.icp.service; 

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.hisoft.hscloud.crm.icp.vo.IcpVO;

/**
* <一句话功能简述> 
* <功能详细描述> 
* @author  lihonglei 
* @version  [版本号, 2013-1-7] 
* @see  [相关类/方法] 
* @since  [产品/模块版本]
 */
public interface IcpService {
	/**
	 * icp备案 
	* <功能详细描述> 
	* @param user
	* @return 
	 * @throws IOException 
	 * @throws HttpException 
	* @see [类、类#方法、类#成员]
	 */
	public String icpPutOnRecord(IcpVO icpVO);

}
