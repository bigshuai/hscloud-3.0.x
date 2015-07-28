package com.hisoft.hscloud.vpdc.oss.monitoring.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.vpdc.oss.monitoring.entity.HostMonitorHistory;
import com.hisoft.hscloud.vpdc.oss.monitoring.entity.VmMonitorHistory;

@Repository 
public class MonitorDaoImp extends HibernateDao<Object, Long>implements MonitorDao{
	
    @Autowired
    private HibernateTemplate hibernateTemplate;
	 
	public boolean saveVmMonitorHistory(List<VmMonitorHistory> vmMonitorHistories) {
		hibernateTemplate.saveOrUpdateAll(vmMonitorHistories);
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<VmMonitorHistory> getVmMonitorHistoryByVmId(String vmId,Date fromTime,Date toTime) {
		List<VmMonitorHistory> monitorHistories=new ArrayList<VmMonitorHistory>();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat;
		String sql = null;
		if(fromTime.getTime() == toTime.getTime()){
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			sql = "from VmMonitorHistory vms where  vms.vmId='" + vmId+"' and DATE_FORMAT(vms.monitorTime ,'%Y-%m-%e')='"+dateFormat.format(fromTime)+
					"' order by vms.monitorTime ASC";
		}else{
			dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			sql = "from VmMonitorHistory vms where  vms.vmId='" + vmId+"' and vms.monitorTime>='"+dateFormat.format(fromTime)+
					"' and vms.monitorTime<='"+dateFormat.format(toTime)+"'" +"order by vms.monitorTime ASC";
		}
		monitorHistories = hibernateTemplate.find(sql);
		return monitorHistories;
	}
	@SuppressWarnings("unchecked")
	public List<VmMonitorHistory> getVmMonitorHistoryByOrderItemId(
			Long order_item_id) {
		
		List<VmMonitorHistory> monitorHistories=new ArrayList<VmMonitorHistory>();
		String sql = "from VmMonitorHistory vms where  vms.order_item_id=" + order_item_id;
		monitorHistories = hibernateTemplate.find(sql);
		return monitorHistories;
	}

	public boolean saveHostMonitorHistory(List<HostMonitorHistory> hostMonitorHistories) {
		  hibernateTemplate.saveOrUpdateAll(hostMonitorHistories);
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<HostMonitorHistory> getHostMonitorHistoryByHostName(String name,Date fromTime, Date toTime) {
		List<HostMonitorHistory> hostMonitorHistories=new ArrayList<HostMonitorHistory>();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer hql=new StringBuffer();
		hql.append(" from HostMonitorHistory hms where  hms.name= ? ");
		hql.append(" and hms.monitorTime between ? and ? ");
		hql.append(" order by hms.monitorTime ASC ");
		hostMonitorHistories = hibernateTemplate.find(hql.toString(), name,fromTime,toTime);
//		SimpleDateFormat dateFormat;
//		String sql = null;
//		if(fromTime.getTime() == toTime.getTime()){
//			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			sql = "from HostMonitorHistory hms where  hms.name='" + name+"' and DATE_FORMAT(hms.monitorTime ,'%Y-%m-%e')='"+dateFormat.format(fromTime)+
//					"' order by hms.monitorTime ASC";
//		}else{
//			dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			sql = "from HostMonitorHistory hms where  hms.name='" + name+"' and hms.monitorTime>='"+dateFormat.format(fromTime)+
//			 		"' and hms.monitorTime<='"+dateFormat.format(toTime)+"'" +"order by hms.monitorTime ASC";
//		}		

//		hostMonitorHistories = hibernateTemplate.find(sql);
		return hostMonitorHistories;
	}
 

}
