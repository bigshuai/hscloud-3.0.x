package com.hisoft.hscloud.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.web.dao.RestOperationLogDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;
import com.hisoft.hscloud.web.vo.OperationLogVo;

@Repository
public class RestOperationLogDaoImpl implements RestOperationLogDao {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private DataSource dataSource;

	private Connection conn;

	@Override
	public OperationLogVo getOperationLogById(Long logId,String jobType,String userId) {
		 // 从数据库中查找
        logger.info("getting OperationLog from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<OperationLogVo> result = null;
        try {
        	String sql =null;
            conn = dataSource.getConnection();
            if("RESOURCE".equals(jobType)){
            	sql = DBUtil.getSql(DBConstants.GET_OPERATION_RESOURCE_RESULT);
            }else if("OPS".equals(jobType)){
            	sql = DBUtil.getSql(DBConstants.GET_OPERATION_RESULT);
            }
            if(sql!=null){
            	pst = conn.prepareStatement(sql);
            	pst.setLong(1,logId);
            	pst.setString(2, userId);
                rs = pst.executeQuery();
                while (rs.next()) {
                	result=new ArrayList<OperationLogVo>();
                	OperationLogVo element=new OperationLogVo();
                	//element.setJobId(rs.getLong("jobId"));
                	element.setTaskId(rs.getLong("logId"));
                	element.setResult(rs.getShort("result"));
                	result.add(element);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        logger.info("getting OperationLog from db is ending......");
        if(result!=null&&result.size()==1){
        	return result.get(0);
        }else{
        	return null;
        }
       
        
        
	}

}
