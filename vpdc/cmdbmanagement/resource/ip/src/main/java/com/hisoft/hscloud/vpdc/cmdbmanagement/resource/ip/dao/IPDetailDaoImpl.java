package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPStatistics;

@Repository
public class IPDetailDaoImpl extends HibernateDao<IPDetail, Long> implements
		IPDetailDao {

	private Logger logger = Logger.getLogger(this.getClass());
	private IPStatistics iPStatistics;

	public long createIPDetail(IPDetail iPDetail) throws HsCloudException {
		long id = 0L;
		try {
			this.save(iPDetail);
			id = iPDetail.getId();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createIPDetail Exception:", e);
		}
		return id;
	}

	@Override
	public IPDetail getIPDetailById(long id) throws HsCloudException {
		return this.findUniqueBy("id", id);		
	}

	@Override
	public boolean updateIPDetail(long id, int status, long userId,String remark)
			throws HsCloudException {
		IPDetail iPDetail = getIPDetailById(id);
		boolean result = false;
		if (iPDetail != null) {
			try {
				iPDetail.setStatus(status);
				iPDetail.setRemark(remark);
				iPDetail.setModifyTime(new Date());
				iPDetail.setModifyUid(userId);
				this.save(iPDetail);
				result = true;
			} catch (Exception e) {
				logger.error("updateIPDetail Exception:", e);
			}
		}
		return result;
	}

	// @Override
	public List<IPDetail> findIPDetailByStatus(long ipRangeId, int status)
			throws HsCloudException {
		String hql = "from IPDetail ipD where ipRange.id = ? and status = ?";
		return this.find(hql, ipRangeId, status);		
	}

	@Override
	// @Transactional(readOnly = true)
	public IPStatistics getIPStatisticsByRangeId(long ipRangeId)
			throws HsCloudException {
		iPStatistics = new IPStatistics(0,0,0,0,0);
		try {			
			String hql = "SELECT status,count(status)  FROM IPDetail where ipRange.id =?  group by status";
			List<Object[]> list = this.find(hql, ipRangeId);
			Iterator<Object[]> it = list.iterator();
			while (it.hasNext()) {
				Object[] key = it.next();
				if (key[0].equals(Constants.IP_STATUS_FREE)) {
					iPStatistics.setFreeIPs(Integer.valueOf(key[1].toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_ASSIGNING)) {
					iPStatistics.setAssigningIPs(Integer.valueOf(key[1].toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_ASSIGNED)) {
					iPStatistics.setAssignedIPs(Integer.valueOf(key[1]
							.toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_DISABLED)) {
					iPStatistics.setDisabledIPs(Integer.valueOf(key[1]
							.toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_RELEASING)) {
					iPStatistics.setReleasingIPs(Integer.valueOf(key[1]
							.toString()));
				}
			}			
		} catch (Exception e) {
			logger.error("getIPStatisticsByRangeId Exception:", e);
		}
		return iPStatistics;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public Page<IPDetail> findIPDetail(long ipRangeId, Page page, Object objectType) {
		Page<IPDetail> pageIPDetail = null;
        try {
            StringBuffer hql = new StringBuffer(
                    "from IPDetail ipD where ipRange.id =:ipRangeId");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ipRangeId", ipRangeId);
            if (objectType == null) {
            	pageIPDetail = this.findPage(page, hql.toString(), map);
            } else {
                hql.append(" and objectType = :objectType");
                map.put("objectType", objectType);
                pageIPDetail = this.findPage(page, hql.toString(), map);
            }
        } catch (Exception e) {
        	logger.error("findIPDetail Exception:", e);
        }
        return pageIPDetail;
    }

	public List<IPDetail> findIPDetailByIP(long startIP, long endIP)
			throws HsCloudException {
		String hql = "from IPDetail ipD where ip between ? and ?)";
		return this.find(hql, startIP, endIP);
	}
	
	@SuppressWarnings("unchecked")
    public List<IPDetail> findIPDetail(long ipRangeId, String field,
			String fieldValue, int offset, int length) throws HsCloudException {
		List<IPDetail> ipDetailList = null;
		try {
			Session session = this.getSession();
			StringBuffer hql = new StringBuffer(
					"from IPDetail ipD where ipRange.id =?");
			if (!(field == null || fieldValue == null)) {
				hql.append(" and " + field + " = ?");
				Query q = session.createQuery(hql.toString());
				q.setParameter(0, ipRangeId);
				q.setParameter(1, fieldValue);
				q.setFirstResult(offset);
				q.setMaxResults(length);
				ipDetailList = q.list();
			} else {
				Query q = session.createQuery(hql.toString());
				q.setParameter(0, ipRangeId);
				q.setFirstResult(offset);
				q.setMaxResults(length);
				ipDetailList = q.list();
			}
		} catch (Exception e) {
			logger.error("findIPDetail Exception:", e);
		}
		return ipDetailList;
	}
	
	@SuppressWarnings("unchecked")
    public List<IPDetail> findIPDetail(long ipRangeId, String field,
			long fieldValue, int offset, int length) throws HsCloudException {
		List<IPDetail> ipDetailList = null;
		try {
			Session session = this.getSession();
			StringBuffer hql = new StringBuffer(
					"from IPDetail ipD where ipRange.id =?");
			if (!(field == null || fieldValue == 0)) {
				hql.append(" and " + field + " = ?");
				Query q = session.createQuery(hql.toString());
				q.setParameter(0, ipRangeId);
				q.setParameter(1, fieldValue);
				q.setFirstResult(offset);
				q.setMaxResults(length);
				ipDetailList = q.list();
			} else {
				Query q = session.createQuery(hql.toString());
				q.setParameter(0, ipRangeId);
				q.setFirstResult(offset);
				q.setMaxResults(length);
				ipDetailList = q.list();
			}
		} catch (Exception e) {
			logger.error("findIPDetail Exception:", e);
		}
		return ipDetailList;
	}

	public IPDetail getIPDetailByIP(long ip) throws HsCloudException {
		return this.findUniqueBy("ip", ip);
	}

	@Override
	public boolean updateIPDetail(IPDetail iPDetail) throws HsCloudException {
		boolean result = false;
		if (iPDetail != null) {
			try {
			    iPDetail.setModifyTime(new Date());
			    this.save(iPDetail);
				result = true;
			} catch (Exception e) {
				logger.error("updateIPDetail Exception:", e);
			}
		}
		return result;

	}

	@Override
	public IPStatistics getIPStatisticsByHostId()
			throws HsCloudException {
		iPStatistics = new IPStatistics(0,0,0,0,0);
		try {
			
			String hql = "SELECT status,count(status)  FROM IPDetail where 2>1 group by status";
			List<Object[]> list = this.find(hql);
			Iterator<Object[]> it = list.iterator();
			while (it.hasNext()) {
				Object[] key = it.next();
				if (key[0].equals(Constants.IP_STATUS_FREE)) {
					iPStatistics.setFreeIPs(Integer.valueOf(key[1].toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_ASSIGNING)) {
					iPStatistics.setAssigningIPs(Integer.valueOf(key[1].toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_ASSIGNED)) {
					iPStatistics.setAssignedIPs(Integer.valueOf(key[1]
							.toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_DISABLED)) {
					iPStatistics.setDisabledIPs(Integer.valueOf(key[1]
							.toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_RELEASING)) {
					iPStatistics.setReleasingIPs(Integer.valueOf(key[1]
							.toString()));
				}
			}			
		} catch (Exception e) {
			logger.error("getIPStatisticsByHostId Exception:", e);
		}
		return iPStatistics;
	}
	@Override
	public List<IPDetail> findIPDetailByRId(long ipRangeId) {
		 return this.findBy("ipRange.id", ipRangeId);
	}

	@Override
	public List<IPDetail> findIPDetailByStatus(int status)
			throws HsCloudException {
			String hql = "from IPDetail ipD where status = ?";
			return this.find(hql,status);
	}
	
	/**
	 * ip查询
	* @param ipRangeId
	* @param field
	* @param fieldValue
	* @param offset
	* @param length
	* @return
	* @throws HsCloudException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<IPDetailVO> findIPDetailVO(long ipRangeId, String field,
			String fieldValue, int offset, int length) throws HsCloudException {
		List<IPDetailVO> reIPDetailVOs =new ArrayList<IPDetailVO>();
		int totalCount=0;
		Map<Integer,Integer> statusMap=new HashMap<Integer,Integer>();
		statusMap.put(Constants.IP_STATUS_FREE, 0);
		statusMap.put(Constants.IP_STATUS_ASSIGNING, 0);
		statusMap.put(Constants.IP_STATUS_ASSIGNED, 0);
		statusMap.put(Constants.IP_STATUS_DISABLED, 0);
		statusMap.put(Constants.IP_STATUS_RELEASING, 0);
		Page<IPDetailVO> pageIPDetailVO= new Page<IPDetailVO>();		
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT a.id AS id,a.ip AS ip,a.status AS status,a.remark,c.name AS nodeName,e.name AS vmName,f.name AS userName,f.email AS email");
		sql.append(" FROM hc_ip_detail AS a");
		sql.append(" INNER JOIN hc_ip_range AS b ON (b.id=a.ip_range_id)");
		sql.append(" LEFT JOIN hc_node AS c ON (c.id=a.host_id) ");
		sql.append(" LEFT JOIN hc_vpdc_instance AS d ON (d.id=a.object_id)");
		sql.append(" LEFT JOIN hc_vpdc_reference AS e ON(e.id=d.VpdcRefrenceId)");
		sql.append(" LEFT JOIN hc_user AS f ON(f.id=e.vm_owner)");
		sql.append(" WHERE 2>1");
		if("ip".equals(field) && fieldValue !=null){
			sql.append(" and a.ip like '%").append(fieldValue).append("%' ");
		}else if("nodeName".equals(field) && fieldValue !=null){
			sql.append(" and c.name like '%").append(fieldValue).append("%' ");
		}else if("vmName".equals(field) && fieldValue !=null){
			sql.append(" and e.name like '%").append(fieldValue).append("%' ");
		}else if("userName".equals(field) && fieldValue !=null){
			sql.append(" and f.name like '%").append(fieldValue).append("%' ");
		}else if("userEmail".equals(field) && fieldValue !=null){
			sql.append(" and f.email like '%").append(fieldValue).append("%' ");
		}
		sql.append(" and b.id = ").append(ipRangeId);
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id",Hibernate.LONG);
		query.addScalar("ip",Hibernate.LONG);
		query.addScalar("status",Hibernate.INTEGER);
		query.addScalar("nodeName",Hibernate.STRING);
		query.addScalar("vmName",Hibernate.STRING);
		query.addScalar("userName",Hibernate.STRING);
		query.addScalar("email",Hibernate.STRING);
		query.addScalar("remark",Hibernate.STRING);
		query.setResultTransformer(Transformers.aliasToBean(IPDetailVO.class));
		List<IPDetailVO> listTotalIPDetailVO =query.list();
		for(IPDetailVO iPDetailVO :listTotalIPDetailVO){
			if(statusMap.get(iPDetailVO.getStatus())==null){
				statusMap.put(iPDetailVO.getStatus(), 1);
			}else{
				statusMap.put(iPDetailVO.getStatus(), statusMap.get(iPDetailVO.getStatus())+1);
			}
		}
		totalCount=query.list().size();
//		query.setFirstResult(offset);
//		query.setMaxResults(length);
		List<IPDetailVO> listIPDetailVO = query.list();
		for(IPDetailVO ipd:listIPDetailVO){
			/*ipd.setFreeIPs(statusMap.get(1).intValue()
					+ statusMap.get(0).intValue());
			ipd.setUsedIPs(statusMap.get(2));
			ipd.setAssignedIPs(statusMap.get(1).intValue()
					+ statusMap.get(0).intValue());
			ipd.setDisabledIPs(statusMap.get(3));
			ipd.setTotalIPs(statusMap.get(0) + statusMap.get(1)
					+ statusMap.get(2) + statusMap.get(3));*/
			
			ipd.setFreeIPs(statusMap.get(Constants.IP_STATUS_FREE));
			ipd.setAssigningIPs(statusMap.get(Constants.IP_STATUS_ASSIGNING));
			ipd.setAssignedIPs(statusMap.get(Constants.IP_STATUS_ASSIGNED));
			ipd.setDisabledIPs(statusMap.get(Constants.IP_STATUS_DISABLED));
			ipd.setReleasingIPs(statusMap.get(Constants.IP_STATUS_RELEASING));
			ipd.setTotalIPs(statusMap.get(Constants.IP_STATUS_FREE) + 
			        statusMap.get(Constants.IP_STATUS_ASSIGNING) + 
			        statusMap.get(Constants.IP_STATUS_ASSIGNED) + 
			        statusMap.get(Constants.IP_STATUS_DISABLED) + 
			        statusMap.get(Constants.IP_STATUS_RELEASING));
			reIPDetailVOs.add(ipd);
		}
		pageIPDetailVO.setTotalCount(totalCount);
		pageIPDetailVO.setPageNo(offset);
		pageIPDetailVO.setPageSize(length);
		pageIPDetailVO.setResult(reIPDetailVOs);
		return pageIPDetailVO;
	}
	
	/**
	 * 通过userId查询ip列表
	* @param userId
	* @return
	 */
    @SuppressWarnings("unchecked")
    @Override
    public List<BigInteger> getIPListByUserId(long referenceId) {
        /*String sql = "select ip.ip from hc_vpdc_reference r,  hc_vpdc_instance i, hc_ip_detail ip " + 
                "where r.id=i.VpdcRefrenceId and i.id=ip.object_id and r.vm_owner=:userId " +
                "and r.status = 0 and i.status = 0 ";*/
        /*String sql = "select ip.ip from hc_vpdc_reference r,  hc_vpdc_instance i, hc_ip_detail ip , hc_vpdcReference_orderItem vr, hc_order_item oi, hc_order o " +
        		" where r.id=i.VpdcRefrenceId and i.id=ip.object_id and vr.renferenceId = r.id " +
        		" and vr.order_item_id = oi.id and oi.order_id = o.id and o.status = 1 and r.id= :referenceId " +
        		" and r.status = 0 and i.status = 0 ";*/
        String sql = "select ip.ip from hc_vpdc_reference r,  hc_vpdc_instance i, hc_ip_detail ip " +
        		"where r.id=i.VpdcRefrenceId and i.id=ip.object_id and r.id= :referenceId and r.status = 0 and i.status = 0 and r.vm_type=1 ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("referenceId", referenceId);
        return query.list();
    }

	@Override 
	public List<IPDetail> findAvailableIPDetailOfServerZone(ServerZone serverZone)
			throws HsCloudException {
		List<IPDetail> ipDetailList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from IPDetail ipD where ipD.status = 0 ");
		if(serverZone == null){
			return ipDetailList;
		}
		try {			
			if(serverZone.getId()>0){
				hql.append(" and ipD.ipRange.serverZone.id = :zoneId");				
				map.put("zoneId",serverZone.getId());				
			}
			if(!"".equals(serverZone.getCode()) && serverZone.getCode()!=null){
				hql.append(" and ipD.ipRange.serverZone.code = :code");				
				map.put("code",serverZone.getCode());
			}	
			ipDetailList = this.find(hql.toString(),map);
		} catch (Exception e) {
			logger.error("findAvailableIPDetailOfServerZone Exception:", e);
		}
		return ipDetailList;
	}

	@Override
	public IPStatistics getIPStatisticsByzoneCode(String zoneCode) throws HsCloudException {
		iPStatistics = new IPStatistics(0,0,0,0,0);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuffer hql = new StringBuffer("SELECT ipD.status,count(ipD.status)  FROM IPDetail AS ipD where 2>1 ");
			if(!"".equals(zoneCode) && zoneCode!=null){
				hql.append(" AND ipD.ipRange.serverZone.code = :code");				
				map.put("code",zoneCode);
			}
			hql.append(" GROUP BY ipD.status");
			List<Object[]> list = this.find(hql.toString(),map);
			Iterator<Object[]> it = list.iterator();
			while (it.hasNext()) {
				Object[] key = it.next();
				if (key[0].equals(Constants.IP_STATUS_FREE)) {
					iPStatistics.setFreeIPs(Integer.valueOf(key[1].toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_ASSIGNING)) {
					iPStatistics.setAssigningIPs(Integer.valueOf(key[1].toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_ASSIGNED)) {
					iPStatistics.setAssignedIPs(Integer.valueOf(key[1]
							.toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_DISABLED)) {
					iPStatistics.setDisabledIPs(Integer.valueOf(key[1]
							.toString()));
				}
				if (key[0].equals(Constants.IP_STATUS_RELEASING)) {
					iPStatistics.setReleasingIPs(Integer.valueOf(key[1]
							.toString()));
				}
			}			
		} catch (Exception e) {
			logger.error("getIPStatisticsByzoneCode Exception:", e);
		}
		this.getSession().clear();
		return iPStatistics;
	}
	
	@SuppressWarnings("unchecked")
	public IPDetail getAvailableIPDetailOfServerZone(ServerZone serverZone) throws HsCloudException{
		IPDetail ipDetail = null;
		StringBuffer hql = new StringBuffer("select ipD.id,ipD.ip from hc_ip_detail ipD, ");
		hql.append(" hc_ip_zone ipZ, hc_zone zone ");
		hql.append(" where ipD.status = 0 and ipD.ip_range_id = ipZ.ip_id and ipZ.zone_id = zone.id ");
		if(serverZone == null){
			return ipDetail;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(serverZone.getId()>0){
            hql.append(" and zone.id = :zoneId");             
            map.put("zoneId",serverZone.getId());               
        }
        if(!"".equals(serverZone.getCode()) && serverZone.getCode()!=null){
            hql.append(" and zone.code = :code");             
            map.put("code",serverZone.getCode());
        }
		Session s = null;
		try {			
			s = this.getSessionFactory().openSession();
			SQLQuery sqlQuery = s.createSQLQuery(hql.toString());
			for (Entry<String, Object> entry : map.entrySet()) {
			    sqlQuery.setParameter(entry.getKey(), entry.getValue());
	        }
			List<Object[]> list = sqlQuery.list();
			if (list != null && list.size() > 0) {
				Object[] result = list.get(0);
				ipDetail = new IPDetail();
				ipDetail.setId(result[0] == null ? 0 : Long.valueOf(result[0].toString()));
				ipDetail.setIp(result[1] == null ? 0 : Long.valueOf(result[1].toString()));
			}
		} catch (Exception e) {
			logger.error("getAvailableIPDetailOfServerZone Exception:", e);
		}finally{
			s.close();
		}
		return ipDetail;
	}
	public void updateIPStatus(long id,int status){
		Session session = null;
		try {
			String sql = "update hc_ip_detail set status="+status+" where id="+id;
			session = this.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.executeUpdate();
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "updateIPStatus异常", logger, e);
		} finally {
			session.close();
		}
	}

	@Override
	public IPDetail getIPDetailByIP(String ip) throws HsCloudException {
		IPDetail ipDetail = null;
		Map<String,Object>map = new HashMap<String,Object>();
		StringBuffer hql = new StringBuffer("from IPDetail ipD where ipD.ip = inet_aton(:ip) and ipD.status = 0");
		map.put("ip", ip);
		try{
			ipDetail = this.findUnique(hql.toString(), map);
			
		}catch(Exception e){
			throw new HsCloudException("OPS-Dao001", "getIPDetailByIP异常", logger, e);
		}
		return ipDetail;
	}

    @Override
    public List<Object[]> getIpAndNetwork(long zoneGroupId) {
        String sql = "select t.ip,t2.network_id from hc_ip_detail t,hc_ip_range t1, hc_vpdc_network t2 " +
        		" where t.ip_range_id=t1.id and t1.id=t2.ipRange_id and t2.zoneGroup_id=:zoneGroupId " +
        		" and t.status = 0 limit 10 ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("zoneGroupId", zoneGroupId);
        return query.list();

    }
}
