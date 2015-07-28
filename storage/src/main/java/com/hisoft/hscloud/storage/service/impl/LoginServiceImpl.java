/* 
* 文 件 名:  LoginService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.service.impl; 

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openstack.client.SwiftClient;
import org.openstack.model.identity.swift.SwiftKeystoneEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.storage.dao.UserTenantDao;
import com.hisoft.hscloud.storage.entity.UserTenant;
import com.hisoft.hscloud.storage.service.LoginService;
import com.hisoft.hscloud.storage.vo.UserVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserTenantDao userTenantDao;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public UserVO authenticate(String username, String password) {
        UserTenant userTenant = userTenantDao.findUniqueBy(username);
        if(userTenant == null) {
            return null;
        }
        Properties properties = new Properties();
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("/");
            properties.load(new FileInputStream(url.getPath() + "swift.properties" ));
            properties.setProperty("auth.username", username);
            properties.setProperty("auth.password", password);
            properties.setProperty("auth.tenant.id", userTenant.getTenantId());
            properties.setProperty("auth.project", userTenant.getTenantName());
        } catch (FileNotFoundException e) {
            throw new HsCloudException(Constants.STORAGE_EXCEPTION, "配置文件找不到异常", logger, e);
        } catch (IOException e) {
            throw new HsCloudException(Constants.STORAGE_EXCEPTION, "IO异常", logger, e);
        }
        try {
            SwiftClient client = SwiftClient.authenticate(properties);
            SwiftKeystoneEndpoint endp = client.getAccess().getEndpoint("object-store", "RegionOne");
            
            UserVO userVO = new UserVO();
            userVO.setName(username);
            userVO.setEndPoint(endp.getPublicURL());
            userVO.setTenantId(userTenant.getTenantId());
            if(userTenant.getImg() != null) {
                userVO.setImg(userTenant.getImg().trim());
            }
            return userVO;
        } catch(Exception ex) {
            return null;
        }
        
        
    }
}
