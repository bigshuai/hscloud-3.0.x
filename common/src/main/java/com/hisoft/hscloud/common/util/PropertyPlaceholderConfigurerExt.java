package com.hisoft.hscloud.common.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerExt extends
		PropertyPlaceholderConfigurer {

    @Override 
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) 
                    throws BeansException { 
            String password = props.getProperty("jdbc.password"); 
            String securityKey=props.getProperty("jdbc.soWhat");
            if (password != null) { 
                    //解密jdbc.password属性值，并重新设置 
            	    if(securityKey==null||"".equals(securityKey)){
            	    	securityKey=Constants.DEFAULT_SECURITY_KEY;
            	    }
                    props.setProperty("jdbc.password",SecurityHelper.DecryptData(password, securityKey)); 
            } 
            super.processProperties(beanFactory, props); 

    } 
}
