/* 
* 文 件 名:  IcpServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-6 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.icp.service.Impl; 

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.crm.icp.vo.IcpVO;
import com.hisoft.hscloud.crm.icp.dao.IcpLogDao;
import com.hisoft.hscloud.crm.icp.dao.IcpRecordDao;
import com.hisoft.hscloud.crm.icp.entity.IcpLog;
import com.hisoft.hscloud.crm.icp.entity.IcpRecord;
import com.hisoft.hscloud.crm.icp.service.IcpService;


/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-6] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class IcpServiceImpl implements IcpService{
/*	private static final String USER = "yunpt";
	private static final String PASSWD = "d65f27e370c36dfd";
	private static final String METHOD = "xzba";
	private static final String SERVICE_TYPE = "yunpt";*/
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	//已注册标记
	private static final String REGISTERED = "1";
	//未注册标记
	private static final String UNREGISTERED = "0";
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmss");
	
	public static int index = 0;
	
	@Autowired
    private IcpRecordDao icpRecordDao;
	
	@Autowired
    private IcpLogDao icpLogDao;

	private Map<String, String> getIcpArgsMap() {
	    PropertiesUtils.ICP_MAP = PropertiesUtils.getPropertiesMap(
                "icp.properties", PropertiesUtils.ICP_MAP);
        return PropertiesUtils.ICP_MAP;
    }
	
	@Override
	public String icpPutOnRecord(IcpVO icpVO){
	    long icpRecordId = saveIcpRecord(icpVO);
	    
	    HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(getIcpArgsMap().get("url"));
        
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK"); 
        
        method.setRequestBody(assembleParam(icpVO));
        
        try {
            client.executeMethod(method);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 打印服务器返回的状态
        logger.info("return status:" + method.getStatusLine());
        
        String result = null;
        try {
            result = method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 打印返回的信息
        logger.info("return result:" + result);
        
        // 释放连接
        method.releaseConnection();
        
        //结果入库
        saveIcpLog(icpRecordId, result);
        
        return result;
      /*  long icpRecordId = saveIcpRecord(icpVO, user.getId());
        saveIcpLog(icpRecordId, result, user.getId());*/
        
        
	}
	
	private long saveIcpRecord(IcpVO icpVO) {
	    IcpRecord icpRecord = new IcpRecord();
	    try {
            BeanUtils.copyProperties(icpRecord, icpVO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	    
	    return icpRecordDao.saveIcp(icpRecord);
	}
	
	private void saveIcpLog(long icpRecordId, String result) {
	    IcpLog icpLog = new IcpLog();
	    icpLog.setIcpRecordId(icpRecordId);
	    icpLog.setResult(result);
	    icpLogDao.saveIcpLog(icpLog);
	}
	
	private String createServiceId() {
		String result = dateFormat.format(Calendar.getInstance().getTime());
		
		int rand = (int)(Math.random() * 10);
		index++;
		if(index == 900) {
			index = 0;
		}
		int endIndex = 100 + index;
		
		result = result + rand + endIndex;
		return result;
	}
	
	
	private NameValuePair[] assembleParam(IcpVO icpVO) {
		/*NameValuePair userParam = new NameValuePair("user", USER);
		NameValuePair passwdParam = new NameValuePair("passwd", PASSWD);
		NameValuePair methodParam = new NameValuePair("method", METHOD);
		NameValuePair servicetypeParam = new NameValuePair("servicetype", SERVICE_TYPE);*/
	    NameValuePair userParam = new NameValuePair("user", getIcpArgsMap().get("user"));
        NameValuePair passwdParam = new NameValuePair("passwd", getIcpArgsMap().get("passwd"));
        NameValuePair methodParam = new NameValuePair("method", getIcpArgsMap().get("method"));
        NameValuePair servicetypeParam = new NameValuePair("servicetype", getIcpArgsMap().get("servicetype"));
		NameValuePair serviceidParam = new NameValuePair("serviceid", createServiceId());
		
		NameValuePair domainParam = new NameValuePair("domain", icpVO.getDomain());
		NameValuePair ipParam = new NameValuePair("ip", icpVO.getIp());
		NameValuePair registeredParam = new NameValuePair("registered", icpVO.getRegistered());
		
		NameValuePair memberloginParam = new NameValuePair("memberlogin", icpVO.getMemberLogin());
		NameValuePair memberpwdParam = new NameValuePair("memberpwd", icpVO.getMemberPwd());
		
		if(REGISTERED.equals(icpVO.getRegistered())) {
			NameValuePair[] array = {userParam, passwdParam, methodParam, servicetypeParam, serviceidParam,
					domainParam, ipParam, registeredParam, memberloginParam, memberpwdParam};
			return array;
		} else if(UNREGISTERED.equals(icpVO.getRegistered())) {
			NameValuePair membertypeParam = new NameValuePair("membertype", icpVO.getMemberType());
			NameValuePair nameP = new NameValuePair("name", icpVO.getName());
			NameValuePair orgcertidParam = new NameValuePair("orgcertid", icpVO.getOrgcertId());
			NameValuePair icpcertid = new NameValuePair("icpcertid", icpVO.getIcpcertId());
			NameValuePair icpurl = new NameValuePair("icpurl", icpVO.getIcpUrl());
			NameValuePair icpwebtitle = new NameValuePair("icpwebtitle", icpVO.getIcpwebTitle());
			NameValuePair contactname = new NameValuePair("contactname", icpVO.getContactName());
			NameValuePair idtype = new NameValuePair("idtype", icpVO.getIdType()); //1：身份证 2：护照 3：学生证 4：军官证
			NameValuePair idnumber = new NameValuePair("idnumber", icpVO.getIdNumber());
			
			NameValuePair country = new NameValuePair("country", "CN");
			NameValuePair province = new NameValuePair("province", icpVO.getProvince());
			
			
			NameValuePair city = new NameValuePair("city", icpVO.getCity());
			NameValuePair address = new NameValuePair("address",icpVO.getAddress());
			
			NameValuePair postcode = new NameValuePair("postcode", icpVO.getPostcode());
			NameValuePair telno = new NameValuePair("telno", icpVO.getTelno());
			NameValuePair mobile = new NameValuePair("mobile", icpVO.getMobile());
			NameValuePair email = new NameValuePair("email", icpVO.getEmail());
			
			NameValuePair[] array = {userParam, passwdParam, methodParam, servicetypeParam, serviceidParam,
					domainParam, ipParam, registeredParam, memberloginParam, memberpwdParam,
					membertypeParam,
					nameP, 
					orgcertidParam,
					icpcertid,
					icpurl,
					icpwebtitle,
					contactname,
					idtype,
					idnumber,
					country,
					province,
					city,
					address,
					postcode,
					telno,
					mobile,
					email};
			return array;
		}
		
		return null;
	}
	
	
} 
