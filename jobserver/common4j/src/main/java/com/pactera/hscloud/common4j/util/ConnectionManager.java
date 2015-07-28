package com.pactera.hscloud.common4j.util;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.pactera.hscloud.common4j.exception.HandlerException;
public final class ConnectionManager {
	private static ConnectionManager instance;

    private ComboPooledDataSource ds;
    
    public static Properties pro;
	
	private static Logger log = Logger.getLogger(ConnectionManager.class);

	static {
		try {
			pro = new Properties();
			pro.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream("jdbc.properties"),
					FileUtil.fileEncode));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error(" get jdbc.properties error ."+e.getStackTrace());
			throw new HandlerException(" get jdbc.properties error :"+e.getStackTrace());
		}
	}
	
    private ConnectionManager() throws Exception {
        ds = new ComboPooledDataSource();
        String securityKey=Constants.DEFAULT_SECURITY_KEY;
		String password=getProperty("jdbc.password");
		String decryptPasswd=SecurityHelper.DecryptData(password, securityKey);
        ds.setDriverClass(getProperty("jdbc.driverClassName"));
        ds.setJdbcUrl(getProperty("jdbc.url"));
        ds.setUser(getProperty("jdbc.username"));
        ds.setPassword(decryptPasswd);
        ds.setInitialPoolSize(Integer.valueOf(getProperty("jdbc.initConns")));
        if(StringUtils.isNumeric((getProperty("jdbc.checkoutTimeout")))){
        	ds.setCheckoutTimeout(Integer.valueOf(getProperty("jdbc.checkoutTimeout")));
        }else{
        	ds.setCheckoutTimeout(1000);
        }
        //StringUtils.isNumeric(arg0);
        //ds.setCheckoutTimeout(Integer.valueOf(getProperty("jdbc.checkoutTimeout")));
        ds.setMaxPoolSize(Integer.valueOf(getProperty("jdbc.maxConns").trim()));
        ds.setMinPoolSize(Integer.valueOf(getProperty("jdbc.minConns").trim()));
        ds.setAcquireIncrement(Integer.valueOf(getProperty("jdbc.increment").trim()));
        ds.setIdleConnectionTestPeriod(Integer.valueOf(getProperty("jdbc.connectionTestPeriod").trim()));
        ds.setMaxIdleTime(Integer.valueOf(getProperty("jdbc.maxIdleTime").trim()));
        ds.setAutoCommitOnClose(Boolean.valueOf(getProperty("jdbc.autoCommitOnClose").trim()));
        ds.setTestConnectionOnCheckout(Boolean.valueOf(getProperty("jdbc.connectionOnCheckout").trim()));
        ds.setTestConnectionOnCheckin(Boolean.valueOf(getProperty("jdbc.connectionOnCheckin").trim()));
        ds.setAcquireRetryAttempts(Integer.valueOf(getProperty("jdbc.acquireRetryAttempts").trim()));
        ds.setAcquireRetryDelay(Integer.valueOf(getProperty("jdbc.acquireRetryDelay").trim()));
        ds.setBreakAfterAcquireFailure(Boolean.valueOf(getProperty("jdbc.breakAfterAcquireFailure").trim()));
    }
   
	private static String getProperty(String key) {
		String ret = pro.getProperty(key, null);
		if (ret != null && ret.trim().equals(""))
			ret = null;
		return ret;
	}
    public static final ConnectionManager getInstance() {
        if (instance == null) {
            try {
                instance = new ConnectionManager();
            } catch (Exception e) {
            	log.error("StackTrace"+e.getStackTrace());
                throw new HandlerException(" new ConnectionManager error:"+e.getStackTrace());
            }
        }
        return instance;
    }
    public synchronized final Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            log.error("openstackhandler get connection exception:"+e.toString());
            log.error("StackTrace"+e.getStackTrace());
            throw new HandlerException(" getConnection error:"+e.getStackTrace());
        }
    }

    protected void finalize() throws Throwable {
        DataSources.destroy(ds); //关闭datasource
        super.finalize();
    }
    
}
