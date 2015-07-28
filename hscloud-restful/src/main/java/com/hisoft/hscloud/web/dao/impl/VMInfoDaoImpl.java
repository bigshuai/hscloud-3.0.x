package com.hisoft.hscloud.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.web.dao.VMInfoDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;
import com.hisoft.hscloud.web.vo.VMInfoVo;

@Repository
public class VMInfoDaoImpl implements VMInfoDao {
	@Resource
	private DataSource dataSource;
	private static Connection conn;
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	@Override
	public List<VMInfoVo> getVMInfo(Map<String,String> args) {
		// 从数据库中查找
		logger.info("getting vminfo list from db is starting......");
		PreparedStatement pst = null;
		ResultSet rs = null;
		VMInfoVo pv = null;
		List<VMInfoVo> oList = new ArrayList<VMInfoVo>();
		try {
			conn = dataSource.getConnection();
           /* for (Map.Entry<String, String> entry : args.entrySet()) {
                if (entry.getKey().equals("machine_no")) {
                    String sql = DBUtil.getSql(DBConstants.GET_ALL_VMINFO_BY_MACHINENO);
                    pst = conn.prepareStatement(sql);
                    pst.setObject(1, entry.getValue());
                    break;
                } else if (entry.getKey().equals("vm_id")) {
                    String sql = DBUtil.getSql(DBConstants.GET_ALL_VMINFO_BY_VMID);
                    pst = conn.prepareStatement(sql);
                    pst.setObject(1, entry.getValue());
                    break;
                }
            }*/
            
            if(args.containsKey("machine_no")) {
                String sql = DBUtil.getSql(DBConstants.GET_ALL_VMINFO_BY_MACHINENO);
                pst = conn.prepareStatement(sql);
                pst.setObject(1, args.get("machine_no"));
                pst.setObject(2, args.get("user_id"));
            } else if(args.containsKey("vm_id")) {
                String sql = DBUtil.getSql(DBConstants.GET_ALL_VMINFO_BY_VMID);
                pst = conn.prepareStatement(sql);
                pst.setObject(1, args.get("vm_id"));
                pst.setObject(2, args.get("user_id"));
            } else {
                String sql = DBUtil.getSql(DBConstants.GET_ALL_VMINFO);
                pst = conn.prepareStatement(sql);
                pst.setObject(1, args.get("user_id"));
            }
            
			rs = pst.executeQuery();
			while (rs.next()) {
				pv = new VMInfoVo();
				pv.setId(rs.getBigDecimal("id"));
				pv.setVmname(rs.getString("vmname"));
				pv.setMachineno(rs.getString("machineno"));
				pv.setFixedip(rs.getString("fixedip"));
				pv.setFloatingip(rs.getString("floatingip"));
				pv.setCreateon(rs.getString("createon"));
				pv.setExpireon(rs.getString("expiredon"));
				pv.setVmstate(rs.getString("vmstate"));
				pv.setBizstate(rs.getString("bizstate"));
				pv.setOsLoginUser(rs.getString("osLoginUser"));
				pv.setOsLoginPwd(rs.getString("osLoginPwd"));
				pv.setCreateflag(rs.getString("createflag"));
				oList.add(pv);
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
		logger.info("getting vminfo list from db is ending......");
		return oList;
	}
}
