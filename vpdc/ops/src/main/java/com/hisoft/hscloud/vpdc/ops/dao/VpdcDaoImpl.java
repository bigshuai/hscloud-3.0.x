/**
 * @title VpdcDaoImpl.java
 * @package com.hisoft.hscloud.vpdc.ops.dao
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-3-29 上午11:54:49
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.openstack.model.compute.nova.NovaInstanceThin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.entity.SiteConfig;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.vpdc.ops.entity.VmExpireEmailLog;
import com.hisoft.hscloud.vpdc.ops.entity.VmExtranetSecurity;
import com.hisoft.hscloud.vpdc.ops.entity.VmIntranetSecurity;
import com.hisoft.hscloud.vpdc.ops.entity.VmIntranetSecurity_Instance;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShot;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotPlan;
import com.hisoft.hscloud.vpdc.ops.entity.VmSnapShotTask;
import com.hisoft.hscloud.vpdc.ops.entity.VmVNCPool;
import com.hisoft.hscloud.vpdc.ops.entity.Vpdc;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcFlavor;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcInstance;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcLan;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcNetwork;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcNetwork_Object;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_Period_Log;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_extdisk;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_OrderItem;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_Period;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRouter_Period_Log;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmOsBean;
import com.hisoft.hscloud.vpdc.ops.util.PageNoUtil;
import com.hisoft.hscloud.vpdc.ops.util.VMStatusBussEnum;
import com.hisoft.hscloud.vpdc.ops.vo.ExpireRemindVO;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceDetail_ServiceCatalogVo;
import com.hisoft.hscloud.vpdc.ops.vo.VmInfoVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcInstanceVO;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceQuotaInfo;
import com.hisoft.hscloud.vpdc.ops.vo.VpdcReferenceVO;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0df
 * @author hongqin.li
 * @update 2012-3-29 上午11:54:49
 * 
 */
@Repository
public class VpdcDaoImpl extends HibernateDao<VpdcReference, Long> implements
		VpdcDao {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Autowired
	private PageNoUtil noUtil;
	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
	
	public void saveVpdc(Vpdc vpdc) throws HsCloudException {
		hibernateTemplate.saveOrUpdate(vpdc);
	}
	
	public Vpdc getVpdc(Long id) throws HsCloudException{
		return hibernateTemplate.get(Vpdc.class, id);
	}
	
	public void createVpdcReference(VpdcReference vrBean) throws HsCloudException{
		hibernateTemplate.save(vrBean);
	}
	
	public void updateVpdcReference(VpdcReference vr) throws HsCloudException{
		try {
			hibernateTemplate.saveOrUpdate(vr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HsCloudException("OPS-Dao001", "updateVpdcReference异常", log, e);
		}
	}

	public boolean updateVR(List<VpdcInstance> list) throws HsCloudException {
		boolean bl;
		try {
			hibernateTemplate.saveOrUpdateAll(list);
			bl = true;
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "updateVR异常", log, e);
		}
		return bl;
	}

	public List<Object> findVMIdsByOwnerId(long ownerId) throws HsCloudException {
		List<Object> VMIds = null;
		try {
			String hql = "select vr.id from VpdcReference vr where vr.status = 0 and vr.owner="+ ownerId+" order by vr.id desc";
			VMIds = hibernateTemplate.find(hql);
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "findVMIdsByOwnerId异常", log, e);
		}
		return VMIds;
	}

	public String findOsName(Integer osId)
			throws HsCloudException {
		String sql = "select si.name"
				+ " from hc_service_item si where si.item_id=:os_id";
		Session session = null;
		String osStr = "";
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setInteger("os_id", osId==null?0:osId);
			List osInfo = sqlQuery.list();
			if (osInfo != null && osInfo.size() > 0) {
				osStr = osInfo.get(0) == null ? null : osInfo.get(0).toString();
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findOsName异常", log, e);
		} finally {
			session.close();
		}
		return osStr;
	}
	
	public String findZoneName(String code) throws HsCloudException{
		String sql = "select zone.name"
				+ " from hc_zone zone where zone.code=:code";
		Session session = null;
		String zoneStr = "";
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setString("code", code);
			List zoneInfo = sqlQuery.list();
			if (zoneInfo != null && zoneInfo.size() > 0) {
				zoneStr = zoneInfo.get(0) == null ? null : zoneInfo.get(0).toString();
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findOsName异常", log, e);
		} finally {
			session.close();
		}
		return zoneStr;
	}
	
	public List<VmOsBean> findOsListByReferenceId(Long referenceId) throws HsCloudException{
		List<VmOsBean> lvob = null;
		VmOsBean vob = null;
		SQLQuery sqlQuery = null;
		String sql = "select si.item_id,si.name"
				+ " from hc_service_item si,hc_service_catalog_item sci";
		try {
			VpdcReference vr = findVpdcReferenceById(referenceId);
			VpdcReference_OrderItem vroi = getOrderItemByReferenceId(referenceId);
			if(vroi==null){
				sql = sql+ " where sci.item_id = si.item_id and si.service_type = 4 and sci.service_catalog_id=:obj_id";
				sqlQuery = this.getSession().createSQLQuery(sql);
				sqlQuery.setInteger("obj_id", vr.getScId());

			}else{
				sql = sql+ ",hc_order_item oi where sci.item_id = si.item_id and si.service_type = 4 and sci.service_catalog_id=oi.serviceCatalogId and oi.id=:obj_id";
				sqlQuery = this.getSession().createSQLQuery(sql);
				sqlQuery.setString("obj_id", vroi.getOrder_item_id());
			}
			List<Object[]> osList = sqlQuery.list();
			if (osList != null && osList.size() > 0) {
				lvob = new ArrayList<VmOsBean>();
				Object[] os = null;
				for(int i = 0;i<osList.size();i++){
					vob = new VmOsBean();
					os = osList.get(i);
					vob.setId(Long.valueOf(os[0].toString()));
					vob.setName(os[1].toString());
					lvob.add(vob);
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findOsListByScId异常", log, e);
		}
		return lvob;
	}
	
	public String findScName(Integer scId) throws HsCloudException{
		String sql = "select sc.name"
				+ " from hc_service_catalog sc where sc.id=:sc_id";
		Session session = null;
		String scStr = "";
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setInteger("sc_id", scId==null?0:scId);
			List scInfo = sqlQuery.list();
			if (scInfo != null && scInfo.size() > 0) {
				scStr = scInfo.get(0) == null ? null : scInfo.get(0).toString();
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findScName异常", log, e);
		} finally {
			session.close();
		}
		return scStr;
	}
	
	public String findOsInfo(String field, int osId)
			throws HsCloudException {
		String sql = "select os." + field
				+ " from hc_os os where os.os_id=:os_id";
		Session session = null;
		String osStr = "";
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setInteger("os_id", osId);
			List osInfo = sqlQuery.list();
			if (osInfo != null && osInfo.size() > 0) {
				osStr = osInfo.get(0) == null ? null : osInfo.get(0).toString();
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findOsInfo异常", log, e);
		} finally {
			session.close();
		}
		return osStr;
	}
	
	public String findRuleInfo(String field, int id) throws HsCloudException {
		String sql = "select rule." + field
				+ " from hc_bill_rule rule where rule.id=:ruleId";
		Session session = null;
		String ruleStr = "";
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setInteger("ruleId", id);
			List ruleInfo = sqlQuery.list();
			ruleStr = ruleInfo.get(0).toString();
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findRuleInfo异常", log, e);
		} finally {
			session.close();
		}
		return ruleStr;
	}

	public InstanceDetail_ServiceCatalogVo findVmServiceCatalog(
			List<VpdcReference_OrderItem> lvroi) throws HsCloudException {
		if(lvroi==null || lvroi.size()==0){
			return null;
		}
		List<String> orderItemIds = new ArrayList<String>();
		for(VpdcReference_OrderItem oi : lvroi){
			if(oi.getOrder_item_id()!=null){
				orderItemIds.add(oi.getOrder_item_id());
			}
		}
		if(orderItemIds==null || orderItemIds.size()==0){
			return null;
		}
		String querySql = "SELECT "
				+ "oi.serviceCatalogName,oi.effective_date,order_.createDate,"
				+ "oi.price,order_.orderNo,oi.priceType,oi.pricePeriodType,"
				+ "oi.pricePeriod,oi.expiration_date,oi.id FROM hc_order_item oi"
				+ " LEFT JOIN hc_order order_ on oi.order_id=order_.id"
				+ " where oi.id in (:oiId)";
		Session session = null;
		InstanceDetail_ServiceCatalogVo idscv = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(querySql);
			sqlQuery.setParameterList("oiId", orderItemIds);
			List<Object[]> list = sqlQuery.list();
			if (list != null && list.size() > 0) {
				Object[] result_ = null;
				Object[] result = null;
				for(int i = 0;i<list.size();i++){
					result_ = list.get(i);
					Date start = result_[1] == null ? null : (Date) result_[1];
					Date end = result_[8] == null ? null : (Date) result_[8];
					if(start != null && end !=null && start.before(new Date()) && end.after(new Date())){
						result = result_;
						break;
					}else if(start != null && end !=null && start.after(new Date()) && end.after(new Date())){
						result = result_;
						break;
					}
				}
				if(result==null){
					result = list.get(list.size()-1);
				}
				idscv = new InstanceDetail_ServiceCatalogVo();
				idscv.setOrderItemId(result[9] == null ? "" : result[9]
						.toString());
				idscv.setServiceCatalogName(result[0] == null ? "" : result[0]
						.toString());
				idscv.setEffectiveDate(result[1] == null ? null
						: (Date) result[1]);
				idscv.setOrderDate(result[2] == null ? null : (Date) result[2]);
				idscv.setPrice(result[3] == null ? null : BigDecimal
						.valueOf(new Double(result[3].toString())));
				idscv.setOrderNo(result[4] == null ? "" : result[4].toString());
				idscv.setPriceType(result[5] == null ? "" : result[5]
						.toString());
				idscv.setPeriodType(result[6] == null ? "" : result[6]
						.toString());
				idscv.setPeriod(result[7] == null ? 0 : Integer.valueOf(result[7].toString()));
				if("1".equals(idscv.getPriceType())){
					if(Constants.scPriceType_MONTH.equals(idscv.getPeriodType())){
						idscv.setPriceTypeName(Constants.scPriceType_MONTH);
					}
					//一次性付费时，周期为设定周期
					
				}/*else if("3".equals(idscv.getPriceType())){
					idscv.setPriceTypeName(Constants.scPriceType_MONTH);
				}else if("4".equals(idscv.getPriceType())){
					idscv.setPriceTypeName(Constants.scPriceType_YEAR);
				}*/
				idscv.setExpirationDate(result[8] == null ? null
						: (Date) result[8]);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"InstanceDetail_ServiceCatalogVo异常", log, e);
		} finally {
			session.close();
		}
		return idscv;
	}
	
	public int getVRCountByIds(List<Object> ids,Long statusId) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		hql.append("select count(*) from VpdcReference vr where vr.status = 0 and vr.createrType='user' and vr.createId=0 and vr.owner="+statusId+" and vr.id in (");
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(""+idStr.toString()+")");
		hql.append(" order by vr.id desc");
		Long count=0L;
		try {
			count = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByIds异常", log, e);
		}
		return count.intValue();
	}
	
	public List<VpdcReference> findVpdcReferencesByIds(int offSet,int length,List<Object> ids,String sort,Long statusId) throws HsCloudException{
		List<VpdcReference> lvr = null;
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_Period vp where vr.id=vp.vpdcreferenceId and vr.createId=0 and vr.status = 0 and vr.createrType='user' and vr.owner="+statusId+" and vr.id in (");
		
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(""+idStr.toString()+")");
		//1:按到期时间正序排列
		if("1".equals(sort)){
			hql.append(" order by vp.endTime");
		}else{//默认VM创建时间倒序排列
			hql.append(" order by vp.startTime desc,vr.id desc");
		}
		try {
			lvr = (List<VpdcReference>) noUtil
					.getListForPage(hql.toString(), offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferencesByIds异常", log, e);
		}
		return lvr;
	}
	
	public int getVRCountByCondition(List<Object> ids,String field,String value,Long statusId) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		hql.append("select count(*) from VpdcReference vr where vr.status = 0 and vr.createrType='user' and vr.createId=0 and vr.owner="+statusId+" and vr.id in (");
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(""+idStr.toString()+")");
		if(value!=null){
			hql.append(" and vr."+field+" like '%"+value+"%'");
		}
		hql.append(" order by vr.id desc");
		Long count = 0L;
		try {
			count = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByCondition异常", log, e);
		}
		return count.intValue();
	}
	
	/**
	 * <新加的接口：UI3.0   分页获取User用户所有的虚拟机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	public int getVRCountByQuery(List<Object> ids, String query, Long statusId,boolean isBuy) throws HsCloudException {
		StringBuilder idStr = new StringBuilder();
		StringBuilder hql = new StringBuilder(" select count(*) from VpdcReference vr ");
		if(isBuy){
			hql.append(" where vr.status = 0 and vr.vpdc is NULL and vr.owner=" + statusId + " and vr.createId =0 and vr.id in (");
		}else{
			hql.append(" where vr.status = 0 and vr.vpdc is NULL and vr.owner=" + statusId + " and vr.createId !=0 and vr.id in (");
		}
		
		if (ids != null && ids.size() > 0) {
			for (Object id : ids) {
				idStr.append(id + ",");
			}
		}
		else {
			idStr.append("0");
		}
		if (idStr.lastIndexOf(",") != -1) {
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append("" + idStr.toString() + ")");
		if (StringUtils.isNotEmpty(query)) {
			hql.append(" and (vr.vm_innerIP like '%"+query+"%' or vr.vm_outerIP like '%"+query+"%' or vr.name like '%"+query+"%')");
		}
		hql.append(" order by vr.id desc");
		Long count = 0L;
		try {
			count = (Long) hibernateTemplate.find(hql.toString()).listIterator().next();
		}
		catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByCondition异常", log, e);
		}
		return count.intValue();
	}
	
	public int getVRCountByCondition(List<Long> ids, String value, Long statusId)
			throws HsCloudException {
		StringBuilder idStr = new StringBuilder();
		StringBuilder hql = new StringBuilder(" select count(*) from VpdcReference vr ");
		hql.append(" where vr.status = 0 and vr.owner=" + statusId + " and vr.id in (");
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				idStr.append(id + ",");
			}
		}
		else {
			idStr.append("0");
		}
		if (idStr.lastIndexOf(",") != -1) {
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append("" + idStr.toString() + ")");
		if (value != null) {
			hql.append(" and (vr.vm_innerIP like '%"+value+"%' or vr.vm_outerIP like '%"+value+"%' or vr.name like '%"+value+"%')");
		}
		hql.append(" order by vr.id desc");
		Long count = 0L;
		try {
			count = (Long) hibernateTemplate.find(hql.toString()).listIterator().next();
		}
		catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByCondition异常", log, e);
		}
		return count.intValue();
	}
	
	/**
	 * <用途: 获取当前虚拟机的已经添加外网安全策略的数据
	 * @author liyunhui
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public List<VmExtranetSecurity> getExtranetSecurityVmInfoByUuid(String uuid)
			throws HsCloudException {
		try {
			String hql = "from VmExtranetSecurity where uuid='" + uuid + "'";
			List<VmExtranetSecurity> ves_list = hibernateTemplate.find(hql);
			return ves_list;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"getExtranetSecurityVmInfoByUuid异常", log, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<VpdcReference> findVpdcReferencesByCondition(int offSet,int length,String sort,List<Object> ids,String field,String value,Long statusId) throws HsCloudException{
		List<VpdcReference> lvr = null;
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_Period vp where vr.id=vp.vpdcreferenceId and vr.createId=0 and vr.status = 0 and vr.createrType='user' and vr.owner="+statusId+" and vr.id in (");
		
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(""+idStr.toString()+")");
		if(value!=null){
			hql.append(" and vr."+field+" like '%"+value+"%'");
		}
		//1:按到期时间正序排列
		if("1".equals(sort)){
			hql.append(" order by vp.endTime");
		}else{//默认VM创建时间倒序排列
			hql.append(" order by vp.startTime desc");
		}
		try {
			lvr = (List<VpdcReference>) noUtil
					.getListForPage(hql.toString(), offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferencesByCondition异常", log, e);
		}
		return lvr;
	}
	
	/**
	 * <新加的接口：UI3.0   分页获取User用户所有的虚拟机> 
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */	
	@SuppressWarnings("unchecked")
	public List<VpdcReference> findVpdcReferencesByQuery(int offSet,
			int length, String sort, List<Object> ids, String query, 
			Long statusId,boolean isBuy) throws HsCloudException {
		List<VpdcReference> lvr = null;
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		if(isBuy){
			hql.append("select vr from VpdcReference vr,VpdcReference_Period vp where vr.id=vp.vpdcreferenceId and vr.status = 0 and vr.vpdc is NULL and vr.owner="+statusId+" and vr.createId=0 and vr.id in (");
		}else{
			hql.append("select vr from VpdcReference vr,VpdcReference_Period vp where vr.id=vp.vpdcreferenceId and vr.status = 0 and vr.vpdc is NULL and vr.owner="+statusId+" and vr.createId!=0 and vr.id in (");
		}
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(""+idStr.toString()+")");
		if(StringUtils.isNotEmpty(query)){
			hql.append(" and (vr.vm_innerIP like '%"+query+"%' or vr.vm_outerIP like '%"+query+"%' or vr.name like '%"+query+"%')");
		}		
		//1:按到期时间正序排列
		if("1".equals(sort)){
			hql.append(" order by vp.endTime");
		}else{//默认VM创建时间倒序排列
			hql.append(" order by vp.startTime desc");
		}
		try {
			lvr = (List<VpdcReference>) noUtil
					.getListForPage(hql.toString(), offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferencesByCondition异常", log, e);
		}
		return lvr;		
	}
	
	/**
	 * <前台根据条件和权限查询分页> <功能详细描述>
	 * @author liyunhui
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public List<VpdcReference> findVpdcReferencesByCondition(int offSet,
			int length, String sort, List<Long> ids, String value, Long statusId)
			throws HsCloudException {
		List<VpdcReference> lvr = null;
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_Period vp where vr.id=vp.vpdcreferenceId and vr.status = 0 and vr.owner="+statusId+" and vr.id in (");
		if(ids!=null && ids.size()>0){
			for(Long id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(""+idStr.toString()+")");
		if(value!=null){
			hql.append(" and (vr.vm_innerIP like '%"+value+"%' or vr.vm_outerIP like '%"+value+"%' or vr.name like '%"+value+"%')");
		}
		//1:按到期时间正序排列
		if("1".equals(sort)){
			hql.append(" order by vp.endTime");
		}else{//默认VM创建时间倒序排列
			hql.append(" order by vp.startTime desc");
		}
		try {
			lvr = (List<VpdcReference>) noUtil
					.getListForPage(hql.toString(), offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferencesByCondition异常", log, e);
		}
		return lvr;
	}

	public int getVRCount(String nodeName,String zoneCode) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		hql.append("select count(vr.id) from");
		if(!StringUtils.isEmpty(nodeName)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr where vr.status=0" +
					" and vi.status = 0 and vi.nodeName='"+nodeName+"'");
		}else{
			hql.append(" VpdcReference vr where vr.status=0");
		}
		if(!StringUtils.isEmpty(zoneCode)&& !"null".equals(zoneCode)){
			hql.append(" AND substring_index(vr.vmZone,'$',1) ='").append(zoneCode).append("'");
		}
		//虚拟中心模块不需要看到【申请中、已取消的VM】
		hql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
		int count = 0;
		try {
			Long countl = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();;
			count = countl.intValue();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCount异常", log, e);
		}
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<VpdcReference> findVpdcReference(int offSet,int length,String nodeName,String zoneCode) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from");
		if(nodeName!=null && !"".equals(nodeName)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr where vr.status=0" +
					" and vi.status = 0 and vi.nodeName='"+nodeName+"'");
		}else{
			hql.append(" VpdcReference vr where vr.status=0");
		}
		if(!StringUtils.isEmpty(zoneCode)&& !"null".equals(zoneCode)){
			hql.append(" AND substring_index(vr.vmZone,'$',1) ='").append(zoneCode).append("'");
		}
		//虚拟中心模块不需要看到【申请中、已取消的VM】
		hql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
		hql.append(" order by vr.id desc");
		List<VpdcReference> vpdcReferences = null;
		try {
			vpdcReferences = (List<VpdcReference>) noUtil.getListForPage(hql.toString(),
					offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReference异常", log, e);
		}
		return vpdcReferences;
	}
	
	public int getVRCount(String nodeName,String field,String value,String zoneCode) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		hql.append("select count(vr.id) from");
		if(nodeName!=null && !"".equals(nodeName)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr where vr.status=0" +
					" and vi.status = 0 and vi.nodeName='"+nodeName+"'");
		}else{
			hql.append(" VpdcReference vr where vr.status=0");
		}
		if(!StringUtils.isEmpty(zoneCode)&& !"null".equals(zoneCode)){
			hql.append(" AND substring_index(vr.vmZone,'$',1) ='").append(zoneCode).append("'");
		}
		//虚拟中心模块不需要看到【申请中、已取消的VM】
		hql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
		int count = 0;
		hql.append(" and vr."+field+" like '%"+value+"%' order by vr.id desc");
		try {
			Long countl = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();;
			count = countl.intValue();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCount异常", log, e);
		}
		return count;
	}
	
	public List<VpdcReference> findVpdcReference(int offSet,int length,String nodeName,String field,String value,String zoneCode) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from");
		if(nodeName!=null && !"".equals(nodeName)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr where vr.status=0" +
					" and vi.status = 0 and vi.nodeName='"+nodeName+"'");
		}else{
			hql.append(" VpdcReference vr where vr.status=0");
		}
		if(!StringUtils.isEmpty(zoneCode)&& !"null".equals(zoneCode)){
			hql.append(" AND substring_index(vr.vmZone,'$',1) ='").append(zoneCode).append("'");
		}
		//虚拟中心模块不需要看到【申请中、已取消的VM】
		hql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
		hql.append(" and vr."+field+" like '%"+value+"%' order by vr.id desc");
		List<VpdcReference> vpdcReferences = null;
		try {
			vpdcReferences = (List<VpdcReference>) noUtil.getListForPage(hql.toString(),
					offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReference异常", log, e);
		}
		return vpdcReferences;
	}	
	
	public int getVRCountByIdsAdminBuss(String vmType,String vm_Buss,String field,String value) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		hql.append("select count(vr.id) from");
		if(!StringUtils.isEmpty(value)&&"vmId".equals(field)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr where vr.status=0 and vr.createId=0 and vi.status=0 and vi.vmId like '%"+value+"%'");
		}else{
			hql.append(" VpdcReference vr where vr.status=0");
			if(!StringUtils.isEmpty(value)&&!StringUtils.isEmpty(field)){
				hql.append(" and vr."+field+" like '%"+value+"%'");
			}
		}
		if(!StringUtils.isEmpty(vmType)){
			hql.append(" and vr.vm_type="+vmType);
		}
		if(!StringUtils.isEmpty(vm_Buss)){
			hql.append(" and vr.vm_business_status="+vm_Buss);
		}
		Long count = 0L;
		try {
			count = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByIdsAdminBuss异常", log, e);
		}
		return count.intValue();
	}
	
	public List<VpdcReference> findVpdcReferenceByIdsAdminBuss(int offSet,int length,String vmType,String vm_Buss,String field,String value,String sort) throws HsCloudException{
		List<VpdcReference> lvr = null;
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from");
		if(!StringUtils.isEmpty(value)&&"vmId".equals(field)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr,VpdcReference_Period vrp where vr.id=vrp.vpdcreferenceId and vr.status=0 and vi.status=0 and vi.vmId like '%"+value+"%'");
		}else{
			hql.append(" VpdcReference vr,VpdcReference_Period vrp where vr.id=vrp.vpdcreferenceId and vr.status=0");
			if(!StringUtils.isEmpty(value)&&!StringUtils.isEmpty(field)){
				hql.append(" and vr."+field+" like '%"+value+"%'");
			}
		}
		if(!StringUtils.isEmpty(vmType)){
			hql.append(" and vr.vm_type="+vmType);
		}
		if(!StringUtils.isEmpty(vm_Buss)){
			hql.append(" and vr.vm_business_status="+vm_Buss);
		}
		//1:按到期时间正序排列
		if("1".equals(sort)){
			hql.append(" order by vrp.endTime");
		}else if("2".equals(sort)){//VM申请时间
			hql.append(" order by vr.createDate desc");
		}else{//默认VM创建时间
			hql.append(" order by vrp.startTime desc");
		}
		try {
			lvr = (List<VpdcReference>) noUtil
					.getListForPage(hql.toString(), offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferenceByIdsAdminBuss异常", log, e);
		}
		return lvr;
	}
	public int getVRCountByIdsAdminBuss(List<Object> ids,String vmType,String vm_Buss,String field,String value) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		hql.append("select count(vr.id) from");
		if(!StringUtils.isEmpty(value)&&"vmId".equals(field)){
			hql.append(" hc_vpdc_instance vi right join hc_vpdc_reference vr on vi.VpdcRefrenceId = vr.id where vr.status=0 and vi.status=0 and vi.vm_id like '%"+value+"%'");
		}else{
			hql.append(" hc_vpdc_reference vr where vr.status=0");
			if(!StringUtils.isEmpty(value)&&!StringUtils.isEmpty(field)){
				hql.append(" and vr."+field+" like '%"+value+"%'");
			}
		}
		//in(id)
		StringBuilder idStr = new StringBuilder();
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
				}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(" and vr.id in ("+idStr+")");
		//类型条件
		if(!StringUtils.isEmpty(vmType)){
			hql.append(" and vr.vm_type="+vmType);
		}
		if(!StringUtils.isEmpty(vm_Buss)){
			hql.append(" and vr.vm_business_status="+vm_Buss);
		}
		Long count = 0L;
		try {
			//count = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();
			SQLQuery sqlQuery = this.getSession().createSQLQuery(hql.toString());
			count = sqlQuery.uniqueResult()==null?0l:Long.valueOf(sqlQuery.uniqueResult().toString());
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByIdsAdminBuss异常", log, e);
		}
		return count.intValue();
		
		
	}
	
	public List<VpdcReference> findVpdcReferenceByIdsAdminBuss(int offSet,int length,List<Object> ids,String vmType,String vm_Buss,String field,String value,String sort) throws HsCloudException{
		List<VpdcReference> lvr = null;
		//创建临时表
		this.insertReferenceTemp(ids);
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from");
		if(!StringUtils.isEmpty(value)&&"vmId".equals(field)){
			hql.append(" VpdcInstance vi right join vi.vpdcreference vr,VpdcReference_Period vrp,VpdcReferenceTemp vrt where vr.id=vrt.referenceId and vr.id=vrp.vpdcreferenceId and vr.status=0 and vi.status=0 and vi.vmId like '%"+value+"%'");
		}else{
			hql.append(" VpdcReference vr,VpdcReference_Period vrp,VpdcReferenceTemp vrt where vr.id = vrt.referenceId and vr.id=vrp.vpdcreferenceId and vr.status=0");
			if(!StringUtils.isEmpty(value)&&!StringUtils.isEmpty(field)){
				hql.append(" and vr."+field+" like '%"+value+"%'");
			}
		}
		//类型条件
		if(!StringUtils.isEmpty(vmType)){
			hql.append(" and vr.vm_type="+vmType);
		}
		if(!StringUtils.isEmpty(vm_Buss)){
			hql.append(" and vr.vm_business_status="+vm_Buss);
		}
		//1:按到期时间正序排列
		if("1".equals(sort)){
			hql.append(" order by vrp.endTime");
		}else if("2".equals(sort)){//VM申请时间
			hql.append(" order by vr.createDate desc");
		}else{//默认VM创建时间倒序排列
			hql.append(" order by vrp.startTime desc");
		}
		try {Date d = new Date();
			lvr = (List<VpdcReference>) noUtil
					.getListForPage(hql.toString(), offSet, length);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferenceByIdsAdminBuss异常", log, e);
		}
		return lvr;
	}

	public boolean updateVmNodeName(String vmNodeName, String vmId)
			throws DataAccessException, HsCloudException {
		boolean flag;
		try {
			VpdcInstance instance = findVmByVmId(vmId);
			instance.setNodeName(vmNodeName);
			hibernateTemplate.saveOrUpdate(instance);
			flag = true;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "updateVmNodeName异常", log, e);
		}
		return flag;
	}

	public VpdcReference findVpdcReferenceById(long id) throws HsCloudException{
		return hibernateTemplate.get(VpdcReference.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public VpdcReference findVpdcReferenceByVmId(String vmId)
			throws HsCloudException {
		VpdcReference reference = null;
		String hql = "select vi.vpdcreference from VpdcInstance vi where vi.vpdcreference.status = 0 and vi.vmId='" + vmId + "'";
		try {
			List<VpdcReference> list = hibernateTemplate.find(hql);
			if(list!=null && list.size()>0){
				reference = list.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"findVpdcReferenceByVmId异常", log, e);
		}
		return reference;
	}
	
	/**
	 * author: liyunhui
	 * description: 用于解决虚拟机回收站的恢复问题
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public VpdcInstance findRecycleInstanceByVmId(String vmId)
			throws HsCloudException {
		VpdcInstance instance = null;
		String hql = "from VpdcInstance vi where vi.vmId='" + vmId + "'";
		try {
			List<VpdcInstance> list = hibernateTemplate.find(hql);
			if(list!=null && list.size()>0){
				instance = list.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"findVpdcReferenceByVmId异常", log, e);
		}
		return instance;
	}	
	
	@Transactional(readOnly = true)
	public VpdcReference findVpdcReferenceByVmName(String vmName) throws HsCloudException{
		VpdcReference reference = null;
		String hql = "from VpdcReference vr where vr.status=0 and vr.name='" + vmName + "'";
		try {
			List<VpdcReference> list = hibernateTemplate.find(hql);
			if(list!=null && list.size()>0){
				reference = list.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"findVpdcReferenceByVmName异常", log, e);
		}
		return reference;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public VpdcInstance findVmByVmId(String vmId) throws HsCloudException {
		String hql = "from VpdcInstance vi where vi.status = 0 and vi.vmId='" + vmId + "'";
		VpdcInstance instance = null;
		try {
			List<VpdcInstance> list = hibernateTemplate.find(hql);
			if (list != null && list.size() > 0) {
				instance = list.get(0);
			}
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "findVmByVmId异常", log, e);
		}
		return instance;
	}
	
    /**
	 * <删除快照:删除超过30分钟都还没有创建成功的快照> 
	* @param instanceId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean deleteSnapshot(Long instanceId) throws HsCloudException {
		String sql = "delete from hc_vm_snapshot where instanceId=" + instanceId 
				   + " and snapShot_id is null and status=0" ;
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(sql);
			sqlQuery.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "deleteSnapshot异常", log, e);
		}
	}
	
	public boolean saveReferenceOrderItem(VpdcReference_OrderItem vroi)
			throws HsCloudException {
		boolean flag = false;
		try {
			hibernateTemplate.saveOrUpdate(vroi);
			flag = true;
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"saveReferenceOrderItem异常", log, e);
		}
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	public VpdcReference_OrderItem getOrderItemByReferenceId(long referenceId)
			throws HsCloudException {
		VpdcReference_OrderItem vroi = null;
		try {
			List<VpdcReference_OrderItem> listVroi = hibernateTemplate
					.find("from VpdcReference_OrderItem vroi where vroi.vpdcRenferenceId="
							+ referenceId);
			if (listVroi != null && listVroi.size() > 0) {
				vroi = listVroi.get(0);
			}
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"findOrderItemIdByReferenceId异常", log, e);
		}
		return vroi;
	}
	
	@SuppressWarnings("unchecked")
	public List<VpdcReference_OrderItem> getOrderItemsByReferenceId(long referenceId) throws HsCloudException{
		List<VpdcReference_OrderItem> lvroi = null;
		try {
			lvroi = hibernateTemplate
					.find("from VpdcReference_OrderItem vroi where vroi.vpdcRenferenceId="
							+ referenceId);
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"findOrderItemIdByReferenceId异常", log, e);
		}
		return lvroi;
	}
	
	
	@SuppressWarnings("unchecked")
	public VpdcReference_OrderItem getOrderItemByOrderItemId(String orderItemId)
			throws HsCloudException {
		VpdcReference_OrderItem vroi = null;
		try {
			List<VpdcReference_OrderItem> listVroi = hibernateTemplate
					.find("from VpdcReference_OrderItem vroi where vroi.order_item_id='"
							+ orderItemId+"'");
			if (listVroi != null && listVroi.size() > 0) {
				vroi = listVroi.get(0);
			}
//			hibernateTemplate.flush();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"findOrderItemIdByReferenceId异常", log, e);
		}
		return vroi;
	}
	
	@Override
	public boolean saveRouterOrderItem(VpdcRouter_OrderItem vroi)
			throws HsCloudException {
		boolean flag = false;
		try {
			hibernateTemplate.saveOrUpdate(vroi);
			flag = true;
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"saveRouterOrderItem异常", log, e);
		}
		return flag;
	}

	@Override
	public VpdcRouter_OrderItem getOrderItemByRouterId(long routerId)
			throws HsCloudException {
		VpdcRouter_OrderItem vroi = null;
		try {
			List<VpdcRouter_OrderItem> listVroi = hibernateTemplate
					.find("from VpdcRouter_OrderItem vroi where vroi.vpdcRouterId="
							+ routerId);
			if (listVroi != null && listVroi.size() > 0) {
				vroi = listVroi.get(0);
			}
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"getOrderItemByRouterId异常", log, e);
		}
		return vroi;
	}

	@Override
	public List<VpdcRouter_OrderItem> getOrderItemsByRouterId(
			long referenceId) throws HsCloudException {
		List<VpdcRouter_OrderItem> lvroi = null;
		try {
			lvroi = hibernateTemplate
					.find("from VpdcRouter_OrderItem vroi where vroi.vpdcRouterId="
							+ referenceId);
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"getOrderItemsByRouterId异常", log, e);
		}
		return lvroi;
	}

	@Override
	public VpdcRouter_OrderItem getRouterOrderItemByOrderItemId(
			String orderItemId) throws HsCloudException {
		VpdcRouter_OrderItem vroi = null;
		try {
			List<VpdcRouter_OrderItem> listVroi = hibernateTemplate
					.find("from VpdcRouter_OrderItem vroi where vroi.order_item_id='"
							+ orderItemId+"'");
			if (listVroi != null && listVroi.size() > 0) {
				vroi = listVroi.get(0);
			}
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"getRouterOrderItemByOrderItemId异常", log, e);
		}
		return vroi;
	}
	
	public boolean isTryOrder(String orderItemId) throws HsCloudException{
		Boolean bl = false;
		String sql = "select oi.try_or_no from hc_order_item oi where oi.id="+ orderItemId;
		Session session = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			bl = Boolean.valueOf(sqlQuery.uniqueResult().toString());
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getHostIdByHostName异常", log,e);
		} finally {
			session.close();
		}
		return bl;
	}
	
	@SuppressWarnings("unchecked")
	public List<VpdcReference> findReferenceByCompanyId(long companyId,String value) throws HsCloudException{
		List<VpdcReference> listVR = null;
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_Company vrc where vr.id=vrc.vpdcRenferenceId and vrc.companyId="
							+ companyId);
		if(value!=null){
			hql.append(" and vr.name like '%"+value+"%'");
		}
		hql.append(" and vr.status = 0 order by vr.id desc");
		try {
			listVR = hibernateTemplate
					.find(hql.toString());
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001",
					"findReferenceByCompanyId异常", log, e);
		}
		return listVR;
	}
	
	@SuppressWarnings("unchecked")
	public List<VpdcReference> findReferenceByCompanyId(int first,int max,long companyId,String value) throws HsCloudException{
		List<VpdcReference> listVR = null;
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_Company vrc where vr.id=vrc.vpdcRenferenceId and vrc.companyId="
				+ companyId);
		if(value!=null){
		hql.append(" and vr.name like '%"+value+"%'");
		}
		hql.append(" and vr.status = 0 order by vr.id desc");
		try {
			listVR = (List<VpdcReference>) noUtil.getListForPage(
					hql.toString(), first, max);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"findReferenceByCompanyId异常", log, e);
		}
		return listVR;
	}
	
	public boolean createVmSnapShot(VmSnapShot snapShot)
			throws DataAccessException, HsCloudException {
		boolean bl;
		try {
			hibernateTemplate.save(snapShot);
			bl = true;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "createVmSnapShot异常", log,e);
		}
		return bl;
	}
	
	public boolean updateVmSnapShotTask(VmSnapShotTask vsst)throws HsCloudException{
		boolean bl;
		try {
			hibernateTemplate.saveOrUpdate(vsst);
			bl = true;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "createVmSnapShotTask异常", log,e);
		}
		return bl;
	}
	
	public VmSnapShotTask getLastDoSnapShotTasks()throws HsCloudException{
		VmSnapShotTask vsst = null;
		try {
			String hql = "from VmSnapShotTask where status = 1 order by id desc";
			List<VmSnapShotTask> lvsst = hibernateTemplate.find(hql);
			if(lvsst!=null && lvsst.size()>0){
				vsst = lvsst.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getLastDoSnapShotTasks异常", log,e);
		}
		return vsst;
	}
	
	public VmSnapShotTask getUnSnapShotTask()throws HsCloudException{
		VmSnapShotTask vsst = null;
		try {
			String hql = "from VmSnapShotTask where status = 0 order by id asc";
			List<VmSnapShotTask> lvsst = hibernateTemplate.find(hql);
			if(lvsst!=null && lvsst.size()>0){
				vsst = lvsst.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findUnSnapShotTasks异常", log,e);
		}
		return vsst;
	}
	
	public VmSnapShotTask getUnSnapShotTaskByVmId(String VmId)throws HsCloudException{
		VmSnapShotTask vsst = null;
		String hql = "from VmSnapShotTask vsst where vsst.status = 0 and vsst.vmSnapShot.instance.vmId = '"+VmId+"'";
		List<VmSnapShotTask> lvsst = hibernateTemplate.find(hql);
		if(lvsst!=null && lvsst.size()>0){
			vsst = lvsst.get(0);
		}
		return vsst;
	}
	
	public List<VmSnapShotTask> findDoSnapShotTasksByVmId(String VmId)throws HsCloudException{
		String hql = "from VmSnapShotTask vsst where vsst.status = 1 and vsst.vmSnapShot.instance.vmId = '"+VmId+"'";
		List<VmSnapShotTask> lvsst = hibernateTemplate.find(hql);
		if(lvsst!=null && lvsst.size()>0){
			return lvsst;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void deleteSnapShotTasks(String snapShotId) throws HsCloudException{
		String hql = "from VmSnapShotTask vsst where vsst.vmSnapShot.snapShot_id = '"+snapShotId+"'";
		List<VmSnapShotTask> lvsst = hibernateTemplate.find(hql);
		if(lvsst!=null && lvsst.size()>0){
			for(VmSnapShotTask vsst : lvsst){
				hibernateTemplate.delete(vsst);
			}
		}
	}

	public boolean createVmSnapShotPlan(VmSnapShotPlan vssp)
			throws DataAccessException, HsCloudException {
		boolean bl;
		try {
			hibernateTemplate.clear();
			hibernateTemplate.saveOrUpdate(vssp);
			bl = true;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "createVmSnapShotPlan异常", log,e);
		}
		return bl;
	}

	public VmSnapShotPlan getVmSnapShotPlanByVmId(String vmId)
			throws DataAccessException, HsCloudException {
		VmSnapShotPlan vssp = null;
		try {
			List<VmSnapShotPlan> vsspList = hibernateTemplate
					.find("from VmSnapShotPlan vssp where vssp.vmId='" + vmId + "'");
			if (vsspList != null && vsspList.size() > 0) {
				vssp = vsspList.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getVmSnapShotPlanByVmId异常", log,e);
		}
		return vssp;
	}

	public List<VmSnapShotPlan> findVmSnapShotPlan(Date now)
			throws DataAccessException, HsCloudException {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
		List<VmSnapShotPlan> vssp = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
			int date = cal.get(Calendar.DAY_OF_MONTH);
			String hql = "from VmSnapShotPlan vssp where vssp.planType!=0 and vssp.planTime='"
					+ sdf.format(now)
					+ "' and ((vssp.planType=1 and vssp.planDate="
					+ day
					+ ") or (vssp.planType=2 and vssp.planDate=" + date + "))";
			vssp = (List<VmSnapShotPlan>) hibernateTemplate
					.find(hql);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVmSnapShotPlan异常", log,e);
		}
		return vssp;
	}

	@Transactional(readOnly = true)
	public List<VmSnapShot> getVmSnapShot(String vmId) throws HsCloudException {
		List<VmSnapShot> vmSnapShots = null;
		try {
			VpdcInstance instance = findVmByVmId(vmId);
			Set<VmSnapShot> snapShots = instance.getSnapShots();
			vmSnapShots = new ArrayList<VmSnapShot>();
			for (VmSnapShot snapShot : snapShots) {
				//显示已备份完成的(1:完成)
				if(1==snapShot.getStatus()){
					vmSnapShots.add(snapShot);
				}
			}
		} catch (HsCloudException e) {
			throw new HsCloudException("OPS-Dao001", "getVmSnapSot异常", log,e);
		}
		return vmSnapShots;
	}

	public VmSnapShot getNewestVmSnapShot(String vmId) throws HsCloudException {
		VmSnapShot shot = null;
		try {
			VpdcInstance instance = findVmByVmId(vmId);
			Set<VmSnapShot> snapShots = instance.getSnapShots();
			for (VmSnapShot snapShot : snapShots) {
				//显示已备份完成的(1:完成)
				if(snapShot!=null && 0==snapShot.getStatus()){
					shot = snapShot;
					break;
				}else if(snapShot!=null && 1==snapShot.getStatus()){
					if(!StringUtils.isEmpty(snapShot.getSnapShot_id())
							&& !snapShot.getSnapShot_id().contains("return")){
						shot = snapShot;
						break;
					}
				}
			}
		} catch (HsCloudException e) {
			throw new HsCloudException("OPS-Dao001", "getNewestVmSnapSot异常", log,e);
		}
		return shot;
	}

	public VmSnapShot getVmSnapShotById(Integer Id) throws HsCloudException {
		VmSnapShot shot = null;
		try {
			shot = hibernateTemplate.get(VmSnapShot.class, Id);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getVmSnapShotById异常", log,e);
		}
		return shot;
	}

	@Transactional(readOnly = false)
	public void deleteSnapShot(VmSnapShot vss) throws HsCloudException{
		hibernateTemplate.delete(vss);
	}
	
	public boolean updateVpdcInstance(VpdcInstance vpdcInstance) throws HsCloudException {
		boolean bl;
		try {
			hibernateTemplate.saveOrUpdate(vpdcInstance);
			bl = true;
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "updateVpdcInstance异常", log,e);
		}
		return bl;
	}

	public VpdcInstance findVmById(long id) throws HsCloudException {
		VpdcInstance vpdcInstance = null;
		try {
			vpdcInstance = hibernateTemplate.get(VpdcInstance.class, id);
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "findVmById异常", log,e);
		}
		return vpdcInstance;
	}

	@SuppressWarnings("unchecked")
	public List<VpdcInstance> getVmIdsByNodeName(String nodeName,List<Object> referenceIds) throws HsCloudException {
		List<VpdcInstance> vpdcInstance = null;
//		List<Long> referenceList = new ArrayList<Long>();
//		StringBuffer ids = new StringBuffer();
		String hql = "";
		try {
//			if(referenceIds!=null && referenceIds.size()>0){
//				for(Object obj :referenceIds){
//					ids.append(obj+",");
//				}
//			}else{
//				ids.append("0");
//			}
//			if(ids.lastIndexOf(",")!=-1){
//				ids.deleteCharAt(ids.lastIndexOf(","));
//			}	
//			referenceList.add(0L);
			if(referenceIds !=null && referenceIds.size()==0){
				referenceIds.add(0L);
//				for(Object obj : referenceIds){
//					referenceList.add((Long)obj);
//				}				
			}
			if(referenceIds==null){
				hql = "from VpdcInstance vi where vi.status = 0 and vi.vpdcreference.status = 0 and vi.nodeName =?";
				vpdcInstance = hibernateTemplate.find(hql,nodeName);				
			}else{
				//hql = "from VpdcInstance vi where vi.status = 0 and vi.vpdcreference.status = 0 and vi.nodeName = ? and vi.vpdcreference.id in ("+ids+")";
				//vpdcInstance = hibernateTemplate.find(hql,nodeName);
				hql = "from VpdcInstance vi where vi.status = 0 and vi.vpdcreference.status = 0 and vi.nodeName = :nodeName and vi.vpdcreference.id in (:list)";
				Query query = getSession().createQuery(hql);
				query.setParameter("nodeName", nodeName);
				query.setParameterList("list", referenceIds);
				vpdcInstance = query.list();
			}			
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVmIdsByNodeName异常", log,e);
		}
		return vpdcInstance;
	}

	@SuppressWarnings("unchecked")
	public List<VpdcReference> getVpdcReferencesByVmName(String vmName) throws HsCloudException {
		List<VpdcReference> vdcReferences = null;
		String hql = "from VpdcReference vr where vr.status = 0 and vr.name like ?";
		try {
			vdcReferences = hibernateTemplate.find(hql, "%" + vmName + "%");
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVmIdsByVmName异常", log,e);
		}
		return vdcReferences;
	}

	@SuppressWarnings("unchecked")
	public List<VpdcReference> findVmIds(int userId, int adminId,
			String nodeName) throws HsCloudException {
		List<VpdcReference> vpdcReferences;
		try {
			vpdcReferences = new ArrayList<VpdcReference>();
			StringBuilder hql = new StringBuilder();
			hql.append("select vr from ");
			if (!("".equals(nodeName) || nodeName == null)) {
				hql.append("VpdcInstance vi right join vi.vpdcreference vr left join vr.vpdc v where vr.status=0 and vi.status = 0"
						+ " and v.userId=" + adminId + " and vr.owner=" + userId);
				
				hql.append(" and vi.nodeName= ?");
				vpdcReferences = hibernateTemplate.find(hql.toString(), nodeName);
			} else {
				hql.append("VpdcReference vr left join vr.vpdc v where vr.status=0"
						+ " and v.userId=" + adminId + " and vr.owner=" + userId);
				
				vpdcReferences = hibernateTemplate.find(hql.toString());
			}
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "findVmIds异常", log,e);
		}
		return vpdcReferences;
	}

	public int getHostIdByHostName(String hostName) throws HsCloudException {
		String sql = "select host.id from hc_node host where host.name='"
				+ hostName + "'";
		Session session = null;
		int hostId = 0;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			hostId = Integer.valueOf(sqlQuery.uniqueResult().toString());
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getHostIdByHostName异常", log,e);
		} finally {
			session.close();
		}
		return hostId;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void resetIPstatus(int status,Long uid,String Ip) throws HsCloudException{
		try {
			if(!StringUtils.isEmpty(Ip)){
				String sql = "UPDATE hc_ip_detail ipdetail" 
						+ " SET ipdetail.status=" + status
						+ ",ipdetail.modify_time=NOW()," + "ipdetail.modify_uid=" + uid
						+ " WHERE ipdetail.ip="+ IPConvert.getIntegerIP(Ip);
				SQLQuery sqlQuery = getSession().createSQLQuery(sql);
				sqlQuery.executeUpdate();
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "resetIPstatus异常", log,e);
		}
	}

	@Override
	public void updateIpDetailByIp(Long objectId, int objectType,
			Long uid, int hostId, long Ip) throws HsCloudException {
		String sql = "UPDATE hc_ip_detail ipdetail " + "SET ipdetail.object_id=" + objectId + ","
				+ "ipdetail.object_type=" + objectType
				+ ",ipdetail.modify_time=NOW()," + "ipdetail.modify_uid=" + uid
				+ ",ipdetail.host_id=" + hostId + " " + "WHERE ipdetail.ip="
				+ Ip + "";
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(sql);
			sqlQuery.executeUpdate();
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "updateIpDetailByIp异常", log,e);
		}
	}
	
	/**
	 * <获取VpdcReference内的可用instance> 
	* <功能详细描述> 
	* @param reference
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	private VpdcInstance getActiveVmFromVpdcReference(VpdcReference reference) {
		Set<VpdcInstance> svi = reference.getInstance();
		VpdcInstance instance = null;
		if (svi != null && svi.size() > 0) {
			for (VpdcInstance vi : svi) {
				if (0 == vi.getStatus()) {
					instance = vi;
					break;
				}
			}
		}
		return instance;
	}
	
	@Override
	public List<VpdcReference> findByIds(List<Long> ids){
		return (List<VpdcReference>)super.findByIds(ids);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getFloatingIpsFromDetailIp(long instanceId){
		String sql = "select ip.ip from hc_ip_detail ip where ip.object_id="+ instanceId + " and ip.object_type=0";
		Session session = null;
		List<String> ips = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			
			List<BigInteger> list = sqlQuery.list();
			if (list != null && list.size() > 0) {
				ips = new ArrayList<String>();
				for(int i=0 ;i<list.size();i++){
					ips.add(list.get(i) == null ? "" : IPConvert.getStringIP(Long.valueOf(list.get(i).toString())));
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getFloatingIpsFromDetailIp异常", log,e);
		} finally {
			session.close();
		}
		return ips;
	}
	
	@Override
	public List<VpdcInstance> findAllInstance() throws HsCloudException {
		List<VpdcInstance> vis = hibernateTemplate.find("from VpdcInstance vi where vi.status != 1");
		return vis;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public VpdcReference getReferenceByOrderItemId(Long orderItemId) {
		// TODO Auto-generated method stub 
		VpdcReference vr = null;
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_OrderItem vroi " +
				"where vr.id = vroi.vpdcRenferenceId " +
				"and vr.status=0 " +
				"and vroi.order_item_id = '"+orderItemId+"'");
		List<VpdcReference> vrs = hibernateTemplate.find(hql.toString());
		if(vrs!=null && vrs.size()>0){
			vr = vrs.get(0);
		}
		return vr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcReference> findReferencesByOrderItems(
			List<Long> orderItemIds) throws HsCloudException {
		if(orderItemIds==null || orderItemIds.size()==0){
			return null;
		}
		List<VpdcReference> vrs = null;
		String ids = "";
		for(Long id : orderItemIds){
			ids+=id+",";
		}
		ids = ids.substring(0, ids.length()-1);
		StringBuilder hql = new StringBuilder();
		hql.append("select vr from VpdcReference vr,VpdcReference_OrderItem vroi " +
				"where vr.id = vroi.vpdcRenferenceId " +
				"and vr.status=0 " +
				"and vroi.order_item_id in ("+ids+")");
		
		vrs = hibernateTemplate.find(hql.toString());
		return vrs;
	}

	/** 
	* @param referencePeriod
	* @return 
	*/
	@Override
	public void updateReferencePeriod(VpdcReference_Period referencePeriod) {
		hibernateTemplate.saveOrUpdate(referencePeriod);
	}

	/** 
	* @param referenceId 
	*/
	@SuppressWarnings("unchecked")
	@Override
	public VpdcReference_Period getReferencePeriod(long referenceId) {
		// TODO Auto-generated method stub 
		VpdcReference_Period vrp = null;
		String hql = "from VpdcReference_Period vrp where vrp.vpdcreferenceId = "+referenceId;
		List<VpdcReference_Period> lvrp = hibernateTemplate.find(hql);
		if(lvrp!=null && lvrp.size()>0){
			vrp = lvrp.get(0);
		}
		return vrp;
	}
	
	@Override
	public void saveVMPeriodLog(VpdcReference_Period_Log vrpl) throws HsCloudException{
		this.hibernateTemplate.save(vrpl);
	}
	
	@Override
	public void updateRouterPeriod(VpdcRouter_Period routerPeriod) {
		hibernateTemplate.saveOrUpdate(routerPeriod);
	}

	@Override
	public VpdcRouter_Period getRouterPeriod(long routerId) {
		VpdcRouter_Period vrp = null;
		String hql = "from VpdcRouter_Period vrp where vrp.vpdcrouterId = "+routerId;
		List<VpdcRouter_Period> lvrp = hibernateTemplate.find(hql);
		if(lvrp!=null && lvrp.size()>0){
			vrp = lvrp.get(0);
		}
		return vrp;
	}

	@Override
	public void saveRouterPeriodLog(VpdcRouter_Period_Log vrpl)
			throws HsCloudException {
		hibernateTemplate.save(vrpl);
	}


	/** 
	* @param vied 
	*/
	@Override
	public void saveExtDisk(VpdcReference_extdisk vied)throws HsCloudException{
		hibernateTemplate.saveOrUpdate(vied);
	}

	/** 
	* @param vied
	* @throws HsCloudException 
	*/
	@Override
	public void deleExtDisk(VpdcReference_extdisk vied) throws HsCloudException {
		hibernateTemplate.delete(vied);
	}

	/** 
	* @param id
	* @throws HsCloudException 
	*/
	@Override
	public VpdcReference_extdisk getExtDiskByVolumeId(String vmId,int volumeId) throws HsCloudException {
		String hql = "from VpdcReference_extdisk vied where vied.vmId ='"+vmId+"' and vied.volumeId ="+volumeId;
		List<VpdcReference_extdisk> lvied = hibernateTemplate.find(hql);
		if(lvied!=null && lvied.size()>0){
			return lvied.get(0);
		}
		return null;
	}
	
	@Override
	public VpdcReference_extdisk getExtDiskByVolumeId(String vmId,String name)throws HsCloudException {
		String hql = "from VpdcReference_extdisk vied where vied.vmId ='"+vmId+"' and vied.name ="+name;
		List<VpdcReference_extdisk> lvied = hibernateTemplate.find(hql);
		if(lvied!=null && lvied.size()>0){
			return lvied.get(0);
		}
		return null;
	}
	
	@Override
	public List<VpdcReference_extdisk> getExtDisksByVmId(String vmId)throws HsCloudException{
		String hql = "from VpdcReference_extdisk vied where vied.vmId ='"+vmId+"'";
		List<VpdcReference_extdisk> lvied = hibernateTemplate.find(hql);
		return lvied;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcReferenceVO> getAllAvailableVM(List<Object> referenceIds,String zoneCode)
			throws HsCloudException {
		List<VpdcReferenceVO> vpdcReferenceVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT vr.id AS vmId,\"\" AS hostName,vr.vm_status AS vmStatus,vr.vm_type AS businessType,vr.creater_type AS createrType,vr.imageId AS osType ");
		sql.append(" FROM hc_vpdc_reference AS vr ");
		sql.append(",hc_zone as zone ");
		sql.append(" WHERE vr.status = 0 ");
		sql.append(" AND (vr.vm_business_status IS NULL OR (vr.vm_business_status <> ").append(VMStatusBussEnum.TRYWAIT.getCode()).append(" AND vr.vm_business_status <> ").append(VMStatusBussEnum.CANCEL.getCode()).append("))");
		try {
			Session session = getSession();
			SQLQuery query;
			if(referenceIds !=null && referenceIds.size()==0){
				referenceIds.add(0L);
			}
			if(!"".equals(zoneCode) && zoneCode!=null){
				sql.append(" AND substring_index(vr.vm_zone,'$',1)='").append(zoneCode).append("'");
			}
			if(referenceIds!=null && referenceIds.size()>0){
				sql.append(" AND substring_index(vr.vm_zone,'$',1) =zone.`code`");
				sql.append(" AND zone.id IN(:list)");
//				sql.append(" and vr.id in (:list)");
			}
			query = session.createSQLQuery(sql.toString());
			query.addScalar("vmId",Hibernate.STRING);
			query.addScalar("hostName",Hibernate.STRING);
			query.addScalar("vmStatus",Hibernate.STRING);
			query.addScalar("businessType",Hibernate.INTEGER);
			query.addScalar("createrType",Hibernate.STRING);
			query.addScalar("osType",Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(VpdcReferenceVO.class));
			if(referenceIds!=null && referenceIds.size()>0){
				query.setParameterList("list", referenceIds);
			}
			vpdcReferenceVO=query.list();
		} catch (DataAccessException e) {
			throw new HsCloudException(Constants.VM_LIST_ADMIN_ERROR, "getAllAvailableVM异常", log,e);
		}
		return vpdcReferenceVO;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Object> findReferencesByType(List<Object> ids,String type,String status_buss) throws HsCloudException{
		StringBuilder hql = new StringBuilder();
		StringBuilder idStr = new StringBuilder();
		hql.append("select vr.id from hc_vpdc_reference vr where vr.status=0");
		if(!StringUtils.isEmpty(type)){
			hql.append(" and vr.vm_type="+type);
			if(!StringUtils.isEmpty(status_buss)){
				hql.append(" and vr.vm_business_status="+status_buss);
			}
		}else{
			hql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
		}
		if(ids!=null && ids.size()>0){
			for(Object id : ids){
				idStr.append(id+",");
			}
		}else{
			idStr.append("0");
		}
		if(idStr.lastIndexOf(",")!=-1){
			idStr.deleteCharAt(idStr.lastIndexOf(","));
		}
		hql.append(" and vr.id in ("+idStr.toString()+")");
		List<Object> referenceIds = new ArrayList<Object>();
		SQLQuery sqlQuery = this.getSession().createSQLQuery(hql.toString());
		List vmids = sqlQuery.list();
		for(int i=0;i<vmids.size();i++){
			referenceIds.add(Long.valueOf(vmids.get(i).toString()));
		}
		return referenceIds;
	}
	
	@Override
	public Long findObjectIdByIp(String IP){
		Session session = null;
		Long objectId = 0l;
		long formatIP;
		try {
			formatIP = IPConvert.getIntegerIP(IP);
		} catch (Exception e1) {
			log.error("输入IP值不正常");
			return objectId;
		}
		try {
			String sql = "select ip.object_id from hc_ip_detail ip where ip.status!=0 and ip.status!=3 and ip.ip="+formatIP;
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			objectId = sqlQuery.uniqueResult()==null?0l:Long.valueOf(sqlQuery.uniqueResult().toString());
		} catch (Exception e) {
			log.error("Find ObjectID by IP from hc_ip_detail Exception:", e);
		} finally {
			session.close();
		}
		return objectId;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public String findIPByObjectId(Long instanceId) throws HsCloudException{
		Session session = null;
		String formatIP = "";
		Long IP = null;
		try {
			String sql = "select ip.ip from hc_ip_detail ip where ip.status!=0 and ip.status!=3 and ip.object_id="+instanceId;
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			List<Object> IPList = sqlQuery.list();
			if(IPList!=null && IPList.size()>0){
				IP = IPList.get(0)==null?null:Long.valueOf(IPList.get(0).toString());
			}
			if(null!= IP){
				formatIP = IPConvert.getStringIP(IP);
			}
			
		} catch (Exception e) {
			log.error("Find IP by ObjectID from hc_ip_detail Exception", e);
		} finally {
			session.close();
		}
		return formatIP;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getVMIdByOrderItem(String orderItemId){
		VpdcInstance vi = null;
		String vmId = "";
		String hql = "select distinct vi from VpdcInstance vi,VpdcReference_OrderItem vroi where vi.vpdcreference.id = vroi.vpdcRenferenceId and vroi.order_item_id = '"+orderItemId+"' order by vi.id desc";
		List<VpdcInstance> lvi = hibernateTemplate.find(hql);
		if(lvi!=null && lvi.size()>0){
			vi = lvi.get(0);
			vmId = vi.getVmId();
		}
		return vmId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcReferenceVO> getVpdcReferenceByNodeName(String nodeName,
			List<Object> referenceIds) throws HsCloudException {
		List<VpdcReferenceVO> vpdcReferenceVO = null;
		StringBuilder sql = new StringBuilder();
//		StringBuffer idStr = new StringBuffer();
		sql.append("SELECT vi.vm_id AS vmId,vi.nodeName AS hostName,vr.vm_status AS vmStatus,vr.vm_type AS businessType ");
		sql.append(" FROM hc_vpdc_reference AS vr ");
		sql.append(" LEFT JOIN hc_vpdc_instance AS vi ON (vi.VpdcRefrenceId=vr.id) ");
		sql.append(" WHERE vr.status = 0 ");
		sql.append(" AND (vr.vm_business_status IS NULL OR (vr.vm_business_status <> ").append(VMStatusBussEnum.TRYWAIT.getCode()).append(" AND vr.vm_business_status <> ").append(VMStatusBussEnum.CANCEL.getCode()).append("))");
		sql.append(" AND vi.status = 0");
//		StringBuffer hql = new StringBuffer(
//				"select vr from VpdcInstance vi right join vi.vpdcreference vr  where vr.status = 0 ");//vi.status = 0 and
//		hql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
//		if (!("".equals(nodeName) || nodeName == null)) {
//			hql.append(" and vi.nodeName='").append(nodeName).append("'");
//		}
		if (!("".equals(nodeName) || nodeName == null)) {
			sql.append(" and vi.nodeName='").append(nodeName).append("'");
		}
		try {
//			if(referenceIds !=null && referenceIds.size()==0){
//				referenceIds.add(0L);
//			}
//			if(referenceIds==null){
//				vpdcReferenceVO = hibernateTemplate.find(hql.toString());				
//			}else{
//				hql.append(" and vr.id in (:list)");
//				Query query = getSession().createQuery(hql.toString());
//				query.setParameterList("list", referenceIds);
//				vpdcReferenceVO = query.list();
//			}
			Session session = getSession();
			SQLQuery query;
			if(referenceIds !=null && referenceIds.size()==0){
				referenceIds.add(0L);
			}
			/*if(referenceIds!=null && referenceIds.size()>0){
				for(Object id : referenceIds){
					idStr.append(id+",");
				}
			}
			if(idStr.lastIndexOf(",")!=-1){
				idStr.deleteCharAt(idStr.lastIndexOf(","));
			}
			if(referenceIds!=null && referenceIds.size()>0){
				sql.append(" and vr.id in (").append(idStr.toString()).append(")");
			}*/
			if(referenceIds!=null && referenceIds.size()>0){
				sql.append(" and vr.id in (:list)");
			}
			query = session.createSQLQuery(sql.toString());
			query.addScalar("vmId",Hibernate.STRING);
			query.addScalar("hostName",Hibernate.STRING);
			query.addScalar("vmStatus",Hibernate.STRING);
			query.addScalar("businessType",Hibernate.INTEGER);
			query.setResultTransformer(Transformers.aliasToBean(VpdcReferenceVO.class));
			if(referenceIds!=null && referenceIds.size()>0){
				query.setParameterList("list", referenceIds);
			}
			vpdcReferenceVO=query.list();
		} catch (DataAccessException e) {
			throw new HsCloudException(Constants.VM_LIST_ADMIN_ERROR, "getVpdcReferenceByNodeName异常", log,e);
		}
		return vpdcReferenceVO;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NovaInstanceThin getNovaInstance(String vmId){
		Session session = null;
		NovaInstanceThin nit = null;
		try {
			String sql = "select fixip.address, i.uuid,i.vm_state,i.task_state from openstack.fixed_ips fixip left join openstack.instances i on fixip.instance_id=i.id where i.uuid =:uuid";
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setString("uuid", vmId);
			List<Object[]> instanceList = sqlQuery.list();
			if(instanceList!=null && instanceList.size()>0){
				nit = new NovaInstanceThin();
				Object[] vm = instanceList.get(0);
				nit.setUuid(vmId);
				nit.setFixed_ip(vm[0]==null?null:vm[0].toString());
				nit.setVmState(vm[2]==null?null:vm[2].toString());
				nit.setTaskState(vm[3]==null?null:vm[3].toString());
			}
		} catch (Exception e) {
			log.error("sql获取vm异常通过openstack", e);
		} finally {
			session.close();
		}
		return nit;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NovaInstanceThin getNovaInstanceByFixIp(String fixip){
		Session session = null;
		NovaInstanceThin nit = null;
		try {
			String sql = "select fixip.address, i.uuid,i.vm_state,i.task_state from openstack.fixed_ips fixip left join openstack.instances i on fixip.instance_id=i.id where fixip.address =:fixip";
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setString("fixip", fixip);
			List<Object[]> instanceList = sqlQuery.list();
			if(instanceList!=null && instanceList.size()>0){
				nit = new NovaInstanceThin();
				Object[] vm = instanceList.get(0);
				nit.setUuid(vm[1]==null?null:vm[1].toString());
				nit.setFixed_ip(fixip);
				nit.setVmState(vm[2]==null?null:vm[2].toString());
				nit.setTaskState(vm[3]==null?null:vm[3].toString());
			}
		} catch (Exception e) {
			log.error("sql获取vm异常通过openstack", e);
		} finally {
			session.close();
		}
		return nit;
	}

	/** 
	* @param f 
	*/
	@Override
	public void saveVpdcFlavor(VpdcFlavor vf) {
		hibernateTemplate.save(vf);
	}

	/** 
	* @param disk
	* @param memory
	* @param vcpu
	* @return 
	*/
	@SuppressWarnings("unchecked")
	@Override
	public Long sameFlavor(Integer disk, Integer memory, Integer vcpu) {
		List<VpdcFlavor> lvf = hibernateTemplate.find("from VpdcFlavor vf where vf.root_gb=? and vf.memory_mb=? and vf.vcpus=? ",disk,memory,vcpu);
		if(lvf!=null && lvf.size()>0){
			return lvf.get(0).getId();
		}
		return 0l;
	}

	/** 
	* @return 
	*/
	@Override
	public Long getFlavorMaxId() {
		Long maxId = (Long)hibernateTemplate.find("select max(id) from VpdcFlavor vf").listIterator().next();
		if(maxId!=null){
			return maxId.longValue();
		}
		return 0l;
	}

	/** 
	* @param veel 
	*/
	@Override
	public void saveMailLogForExpire(VmExpireEmailLog veel) {
		hibernateTemplate.save(veel); 
	}

	/** 
	* @param remindDay
	* @param expireDate 
	*/
	@SuppressWarnings("unchecked")
	@Override
	public boolean ifSendMail(long referenceId,int remindCycle, Date expireDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// yyyy-MM-dd hh:mm:ss
		boolean bl = false;
		Calendar c = Calendar.getInstance();
		c.setTime(expireDate);
		c.add(Calendar.DATE, -remindCycle);
		String remindTimeStr = dateFormat.format(c.getTime());
		String nowTimeStr = dateFormat.format(new Date());
		String hql = "from VmExpireEmailLog veel where veel.referenceId="+referenceId+" and veel.createTime>'"+remindTimeStr+"' and veel.createTime<'"+nowTimeStr+"'";
		List<VmExpireEmailLog> lveel = hibernateTemplate.find(hql);
		if(lveel!=null && lveel.size()>0){
			bl = true;
		}
		return bl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcInstanceVO> getVpdcInstanceVOForExpireOperate() {
		List<VpdcInstanceVO> vpdcInstanceVOList = null;
		StringBuilder hql=new StringBuilder("");
		hql.append("SELECT vi.create_id AS userId,vi.vm_id AS vmId,");
		hql.append("vr.id AS referenceId,vr.vm_type AS vmType,vr.is_enable AS isEnable,");
		hql.append("vp.end_time AS endTime,vp.start_time AS startTime,TIMESTAMPDIFF(SECOND,NOW(),vp.end_time) AS spareTime");
		hql.append(" FROM hc_vpdc_instance as vi ");
		hql.append(" LEFT JOIN hc_vpdc_reference AS vr on(vr.id=vi.VpdcRefrenceId) ");
		hql.append(" LEFT JOIN hc_vpdcReference_period AS vp ON (vp.renferenceId=vr.id)");
		hql.append(" WHERE vi.`status` !=1");
		hql.append(" AND vp.end_time>0");
		hql.append(" AND vp.start_time>0");
		hql.append(" AND TIMESTAMPDIFF(SECOND,NOW(),vp.end_time)<=0");
		Session session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(hql.toString());
		query.addScalar("userId",Hibernate.LONG);
		query.addScalar("vmId",Hibernate.STRING);
		query.addScalar("referenceId",Hibernate.LONG);
		query.addScalar("vmType",Hibernate.INTEGER);
		query.addScalar("isEnable",Hibernate.INTEGER);
		query.addScalar("endTime",Hibernate.DATE);
		query.addScalar("startTime",Hibernate.DATE);
		query.addScalar("spareTime",Hibernate.LONG);
		query.setResultTransformer(Transformers.aliasToBean(VpdcInstanceVO.class));
		vpdcInstanceVOList =query.list();
		return vpdcInstanceVOList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getConfigValue(String config, String belongTo)
			throws HsCloudException {
		try {
			String configValue = null;
			Session session = hibernateTemplate.getSessionFactory().openSession();
			Query query = null;
			StringBuilder hql = new StringBuilder("from SiteConfig where 1=1 ");
			query = session.createQuery(hql.toString());
			if(StringUtils.isNotBlank(config) && StringUtils.isNotBlank(belongTo)){
				hql.append(" and config = :config and belongTo = :belongTo ");
				query = session.createQuery(hql.toString());
				query.setParameter("config", config);
				query.setParameter("belongTo", belongTo);
//				query.setResultTransformer(Transformers.aliasToBean(SiteConfig.class));
			}			
			List<SiteConfig> configs = query.list();
			if(configs!=null&&configs.size()==1){
				SiteConfig configObj=configs.get(0);
				if(configObj!=null)
					configValue=configObj.getConfigValue();
			}
			return configValue;
		} catch (DataAccessException e) {
			throw new HsCloudException(Constants.GET_SITE_CONFIG_ERROR, "getConfigValueException：", log,e);
		}
	}
	
    @SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
    public List<ExpireRemindVO> findForExpireRemind() {
    	StringBuilder sql=new StringBuilder("select vi.vm_id as vmId, vr.id as referenceId, vr.name as name,");
    	sql.append(" vr.create_id as createId,vr.vm_type vmType, vr.vm_business_status vmBusinessStatus,");
    	sql.append(" vr.sc_id scId, vr.is_enable isEnable, vr.buy_type buyType, vp.start_time startTime,  vp.end_time endTime, ");
    	sql.append(" u.id userId, u.name userName, u.domain_id domainId, u.email email ");
    	sql.append(" from hc_vpdc_instance vi, hc_vpdc_reference vr, hc_vpdcReference_period vp, hc_user u ");
    	sql.append(" where vi.VpdcRefrenceId = vr.id and vi.status != 1 and vr.vm_owner = u.id ");
    	sql.append("  and vr.id = vp.renferenceId ");
    	sql.append("  and vr.status = 0 ");
    	sql.append(" and DATE_SUB(vp.end_time,INTERVAL 31 DAY) < NOW()");
    	sql.append(" and vp.start_time is not null and vp.end_time is not null ");
    	
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("vmId", Hibernate.STRING)
        .addScalar("referenceId", Hibernate.LONG)
        .addScalar("name", Hibernate.STRING)
        .addScalar("createId", Hibernate.LONG)
        .addScalar("vmType", Hibernate.INTEGER)
        .addScalar("vmBusinessStatus", Hibernate.INTEGER)
        .addScalar("scId", Hibernate.LONG)
        .addScalar("isEnable", Hibernate.INTEGER)
        .addScalar("buyType",Hibernate.INTEGER)
        .addScalar("startTime", Hibernate.TIMESTAMP)
        .addScalar("endTime", Hibernate.TIMESTAMP)
        .addScalar("userId", Hibernate.LONG)
        .addScalar("userName", Hibernate.STRING)
        .addScalar("domainId", Hibernate.LONG)
        .addScalar("email", Hibernate.STRING)       
        .setResultTransformer(Transformers.aliasToBean(ExpireRemindVO.class));
        return query.list();
    }
    
    private void insertReferenceTemp(List<Object> referenceIds){
		String sql_delete = "delete from hc_vpdc_reference_temp";
		// String sql_drop = "DROP TABLE IF EXISTS `hc_vpdc_reference_temp`";
		// String sql_create =
		// "CREATE TABLE `hc_vpdc_reference_temp` (`id` bigint(20) NOT NULL AUTO_INCREMENT,`referenceId` bigint(20) DEFAULT NULL, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		if (referenceIds != null && referenceIds.size() > 0) {
			StringBuilder sql_insert = new StringBuilder(
					"insert into hc_vpdc_reference_temp(id,referenceId) values ");
			try {
				// 清空临时表
				SQLQuery sq_drop = getSession().createSQLQuery(sql_delete);
				sq_drop.executeUpdate();
				// SQLQuery sq_create = getSession().createSQLQuery(sql_create);
				// sq_create.executeUpdate();
				// 赋值临时表
				int i = 0;
				for (Object id : referenceIds) {
					i++;
					sql_insert.append("(" + (i) + "," + id + "),");
				}
				if (sql_insert.lastIndexOf(",") != -1) {
					sql_insert.deleteCharAt(sql_insert.lastIndexOf(","));
				}
				SQLQuery sq_insert = getSession().createSQLQuery(
						sql_insert.toString());
				sq_insert.executeUpdate();

			} catch (Exception e) {
				throw new HsCloudException("OPS-Dao001",
						"insertReferenceTemp异常", log, e);
			}
		}
	}
	/**
	 * <用途: 通过虚拟机的instanceId查找VmIntranetSecurityInstance
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VmIntranetSecurity_Instance> getIntranet_Instance(
			Long instance_id, String group_id) throws HsCloudException {
		// 情形1:如果groupId=null,直接根据instance_id查询出VmIntranetSecurity_Instance
		// 情形2:如果instance_id=null,查询所有不在group_id这一组里面的虚拟机
		try {
			String hql = "from VmIntranetSecurity_Instance vi ";
			// 判断取得正常值
			if (instance_id != null && instance_id > 0) {
				hql += " where vi.instance_id=" + instance_id;
			}
			if (!StringUtils.isBlank(group_id)) {
				hql += " where vi.intranetsecurity_id !=" + Long.valueOf(group_id);
			}
			List<VmIntranetSecurity_Instance> list = hibernateTemplate.find(hql);
			return list;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"getIntranet_Instance异常", log, e);
		}
	}
	/**
	 * <用途: 通过已经添加内网安全的虚拟机的instanceId查找它的VpdcRefrenceId
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getAddIntranetVpdcRefrenceIds(
			List<Long> addIntranetInstanceIds) throws HsCloudException {
		try {
			String param = "";
			for (int i = 0; i < addIntranetInstanceIds.size(); i++) {
				if (i == 0) {
					param += addIntranetInstanceIds.get(i);
				} else {
					param += "," + addIntranetInstanceIds.get(i);
				}
			}
			String sql = "select VpdcRefrenceId from hc_vpdc_instance where id in ("
					+ param + ")";
			SQLQuery sqlQuery = getSession().createSQLQuery(sql);
			List<Object> obj_list = sqlQuery.list();
			List<Long> vpdcRefrenceIds = new ArrayList<Long>();
			for (int i = 0; i < obj_list.size(); i++) {
				vpdcRefrenceIds.add(Long.valueOf(obj_list.get(i).toString()));
			}
			return vpdcRefrenceIds;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getInstanceIds异常", log, e);
		}
	}
	/**
	 * <用途: 通过intranetSecurityId查找同一组内网的所有虚拟机列表
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcInstance> findIntranetVmsByIntranetId(Long intranetSecurityId)
			throws HsCloudException {
		try {
			String hql = "select vi from VpdcInstance vi,VmIntranetSecurity_Instance visi where vi.id=visi.instance_id and visi.intranetsecurity_id="
					+ intranetSecurityId;
			List<VpdcInstance> vi_list = hibernateTemplate.find(hql);
			return vi_list;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"findIntranetVmsByIntranetId异常", log, e);
		}
	}
	/**
	 * <用途: 通过instanceId数组删除在同一组里面已经添加内网安全策略的虚拟机
	 *       这些虚拟机的instanceId in (传进来的要删除的数组 )
	 * @author liyunhui
	 * @param isDeleteGroupRecord: false 只删除hc_vm_intranetsecurity记录,组的记录保留
	 *                             true 要把hc_vm_intranetsecurity_instance的组的记录也一起清掉
	 * @param groupId: 组在hc_vm_intranetsecurity_instance中的序号
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteAddedIntranetSecurityVms(List<String> deleted_instanceIds,
			Long groupId, boolean isDeleteGroupRecord) throws HsCloudException {
		try {
			String instancedIds = "";
			for (int i = 0; i < deleted_instanceIds.size(); i++) {
				if (i == 0) {
					instancedIds += deleted_instanceIds.get(i);
				} else {
					instancedIds += "," + deleted_instanceIds.get(i);
				}
			}
			String hql = "from VmIntranetSecurity_Instance visi where visi.instance_id in ("
					+ instancedIds + ") ";
			List<VmIntranetSecurity_Instance> visi_list = hibernateTemplate
					.find(hql);
			if (visi_list != null && visi_list.size() > 0) {
				hibernateTemplate.deleteAll(visi_list);
			}
			// 如果用户需要删除groupId,不需要删除groupId可设置为null,isDeleteGroupRecord为false
			if (isDeleteGroupRecord && groupId > 0) {
				String hql2 = "from VmIntranetSecurity vis where vis.id="
						+ groupId;
				List<VmIntranetSecurity> vis_list = hibernateTemplate.find(hql2);
				if (vis_list != null && vis_list.size() > 0) {
					hibernateTemplate.delete(vis_list.get(0));
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"deleteAddedIntranetSecurityVms异常", log, e);
		}
	}
	/**
	 * <用途: 通过instanceId删除虚拟机的内网安全
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteVmIntranet(Long instanceId) throws HsCloudException {
		try {
			String hql = "from VmIntranetSecurity_Instance visi where visi.instance_id="+ instanceId;
			List<VmIntranetSecurity_Instance> visi_list = hibernateTemplate.find(hql);
			if (visi_list != null && visi_list.size() > 0) {
				hibernateTemplate.deleteAll(visi_list);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"deleteAddedIntranetSecurityVm异常", log, e);
		}
	}	
	/**
	 * <用途: 添加内网安全新的组>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void saveOrUpdateIntranetSecurityGroup(VmIntranetSecurity vis)
			throws HsCloudException {
		try {
			hibernateTemplate.saveOrUpdate(vis);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"saveOrUpdateIntranetSecurityGroup异常", log, e);
		}
	}
	/**
	 * <用途: 通过虚拟机组的组号获得VmIntranetSecurity>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public VmIntranetSecurity getIntranetSecurityGroup(String groupName)
			throws HsCloudException {
		try {
			VmIntranetSecurity result = null;
			String hql = "from VmIntranetSecurity vis where vis.name='"
					+ groupName + "'";
			List<VmIntranetSecurity> vis_list = hibernateTemplate.find(hql);
			if (vis_list != null && vis_list.size() > 0) {
				result = vis_list.get(0);
			}
			return result;
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"getIntranetSecurityGroup异常", log, e);
		}
	}
	/**
	 * <用途: 把instanceId集合里面的虚拟机全部添加到groupId这一组>
	 * @param addedVm_list: 需要添加到同一组的虚拟机的instanceId集合
	 * @param groupId: 组在hc_vm_intranetsecurity_instance中的序号
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void saveOrUpdateIntranetSecurityVms(List<String> addedVm_list,
			Long groupId) throws HsCloudException {
		try {
			if (groupId != null && groupId > 0) {
				List<VmIntranetSecurity_Instance> visi_list = new ArrayList<VmIntranetSecurity_Instance>();
				for (int i = 0; i < addedVm_list.size(); i++) {
					VmIntranetSecurity_Instance visi = new VmIntranetSecurity_Instance();
					visi.setInstance_id(Long.valueOf(addedVm_list.get(i)));
					visi.setIntranetsecurity_id(groupId);
					visi_list.add(visi);
				}
				hibernateTemplate.saveOrUpdateAll(visi_list);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"saveOrUpdateIntranetSecurityVms异常", log, e);
		}
	}
	/**
	 * <用途: 删除uuid这台虚拟机的外网端口>
	 * <适用情形: 1.deleted_port_list为null时,直接删除uuid这台虚拟机的所有的外网端口
	 *          2.deleted_port_list非空时,删除uuid这台虚拟机需要删除的外网端口(不是全部)>
	 * @param deleted_port_list: 用户需要删除的端口集合
	 * @param uuid: 虚拟机的uuid
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void deleteExtranetSecurity(List<String> deleted_port_list,
			String uuid) throws HsCloudException {
		try {
			// 适用情形1:deleted_port_list为null时,直接删除instanceId这台虚拟机的所有的外网端口
			if (deleted_port_list == null) {
				String hql = "from VmExtranetSecurity ves where ves.uuid='"
						+ uuid + "'";
				List<VmExtranetSecurity> ves_list = hibernateTemplate.find(hql);
				if (ves_list != null && ves_list.size() > 0) {
					hibernateTemplate.deleteAll(ves_list);
				}
			} else {
				// 适用情形2.deleted_port_list非空时,删除uuid这台虚拟机需要删除的外网端口(不是全部)
				if (deleted_port_list.size() > 0) {
					List<Integer[]> deletedPort_list = new ArrayList<Integer[]>();
					for (int i = 0; i < deleted_port_list.size(); i++) {
						String[] ele1 = deleted_port_list.get(i).split("@");
						Integer[] ele2 = { Integer.valueOf(ele1[0]),
								Integer.valueOf(ele1[1]), Integer.valueOf(ele1[2]) };
						deletedPort_list.add(ele2);
					}
					for (int i = 0; i < deletedPort_list.size(); i++) {
						Integer protocal = deletedPort_list.get(i)[0];
						Integer port_from = deletedPort_list.get(i)[1];
						Integer port_to = deletedPort_list.get(i)[2];
						String hql = "from VmExtranetSecurity ves where ves.protocal="
								+ protocal + " and ves.port_from="
								+ port_from + " and ves.port_to="
								+ port_to + " and ves.uuid='" + uuid + "'";
						List<VmExtranetSecurity> ves_list = hibernateTemplate.find(hql);
						if (ves_list != null && ves_list.size() > 0) {
							hibernateTemplate.delete(ves_list.get(0));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"deleteExtranetSecurityByInstanceId异常", log, e);
		}
	}
	/**
	 * <用途: 删除instanceId这台虚拟机的外网端口>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void deleteVmExtranet(Long instanceId) throws HsCloudException {
		try {
			//修改人 张建伟 修改时间 20130924
			String hql = "from VmExtranetSecurity ves where ves.instance.id=" + instanceId;
			List<VmExtranetSecurity> ves_list = hibernateTemplate.find(hql);
			if (ves_list != null && ves_list.size() > 0) {
				hibernateTemplate.deleteAll(ves_list);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"deleteVmExtranet异常", log, e);
		}		
	}
	/**
	 * <用途: 向数据库中批量插入虚拟机的外网安全端口数据>
	 * @author liyunhui
	 * @return 
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public void saveOrUpdateAllExtranetSecurity(List<VmExtranetSecurity> ves_list) 
			throws HsCloudException {
		try {
			hibernateTemplate.saveOrUpdateAll(ves_list);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"saveOrUpdateAllExtranetSecurity异常", log, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<VpdcReference> fuzzyFindVmsAdmin(Page<VpdcReference> page,
			String nodeName, String field, String value,
			List<Object> ids, String zoneCode,long createrId) throws HsCloudException {
		int totalCount = 0;
		List<VpdcReference> lvr = null;
		StringBuilder sql = new StringBuilder("select DISTINCT vr.id,vr.name,vr.sc_id as scId,vr.radom_user,");
		sql.append("vr.create_date as createDate,vr.is_enable as isEnable,vr.bwtIn,vr.bwtOut,vr.ipConnIn,");
		sql.append("vr.ipConnOut,vr.tcpConnIn,vr.tcpConnOut,vr.udpConnIn,vr.udpConnOut,vr.cpuLimit,vr.diskRead,");
		sql.append("vr.diskWrite,vr.vm_owner as owner,vr.vm_business_status,vr.disk_capacity,vr.cpu_core,vr.mem_size,");
		sql.append("vr.osId,vr.vm_zone as vmZone,vr.vm_status,vr.vm_innerIP,vr.vm_outerIP,vr.process_state as processState,vr.comments,vr.outComments ");
		sql.append(" from hc_zone as zone");
		sql.append(" ,hc_vpdc_reference as vr");
		if("vmOwner".equals(field)){
			sql.append(" ,hc_user as user");
		}		
//		StringBuffer idStr = new StringBuffer();
		if(!"".equals(nodeName)){
			sql.append(" ,hc_vpdc_instance as vi where vi.VpdcRefrenceId=vr.id and vr.status=0 and vi.status = 0 and vi.nodeName='").append(nodeName).append("'");
		}else{
			sql.append(" where vr.status=0 ");
		}
		if(!StringUtils.isEmpty(zoneCode)&& !"null".equals(zoneCode)){
			sql.append(" AND substring_index(vr.vm_zone,'$',1) ='").append(zoneCode).append("'");
		}
		//虚拟中心模块不需要看到【申请中、已取消的VM】
		sql.append(" AND (vr.vm_business_status IS NULL OR (vr.vm_business_status <> ").append(VMStatusBussEnum.TRYWAIT.getCode()).append(" AND vr.vm_business_status <> ").append(VMStatusBussEnum.CANCEL.getCode()).append("))");
		if(ids !=null && ids.size()==0){
			ids.add(0L);
		}
		if(ids!=null && ids.size()>0){
			sql.append(" AND substring_index(vr.vm_zone,'$',1) =zone.`code`");
			sql.append(" AND zone.id IN(:list)");
//			sql.append(" and vr.id in (:list)");
		}
//		sql.append(" and (user.id=vr.vm_owner or vr.create_id =:createrId)");
		if("vmOwner".equals(field)){
			sql.append(" and user.email like '%").append(value).append("%' and user.id=vr.vm_owner ");
		}else if(!StringUtils.isEmpty(value)&&!StringUtils.isEmpty(field)){
			sql.append("  and vr."+field+" like '%"+value+"%' ");
		}
		sql.append(" order by vr.id DESC");
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.addScalar("id",Hibernate.LONG);
			sqlQuery.addScalar("name",Hibernate.STRING);
			sqlQuery.addScalar("scId",Hibernate.INTEGER);
			sqlQuery.addScalar("radom_user",Hibernate.STRING);
			sqlQuery.addScalar("createDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("isEnable",Hibernate.INTEGER);
			
			sqlQuery.addScalar("bwtIn",Hibernate.INTEGER);
			sqlQuery.addScalar("bwtOut",Hibernate.INTEGER);
			sqlQuery.addScalar("ipConnIn",Hibernate.INTEGER);
			sqlQuery.addScalar("ipConnOut",Hibernate.INTEGER);			
			sqlQuery.addScalar("tcpConnIn",Hibernate.INTEGER);
			sqlQuery.addScalar("tcpConnOut",Hibernate.INTEGER);
			sqlQuery.addScalar("udpConnIn",Hibernate.INTEGER);
			sqlQuery.addScalar("udpConnOut",Hibernate.INTEGER);
			sqlQuery.addScalar("cpuLimit",Hibernate.INTEGER);
			sqlQuery.addScalar("diskRead",Hibernate.INTEGER);
			sqlQuery.addScalar("diskWrite",Hibernate.INTEGER);
			
			sqlQuery.addScalar("owner",Hibernate.LONG);
			sqlQuery.addScalar("vm_business_status",Hibernate.INTEGER);
			sqlQuery.addScalar("disk_capacity",Hibernate.INTEGER);
			sqlQuery.addScalar("cpu_core",Hibernate.INTEGER);
			sqlQuery.addScalar("mem_size",Hibernate.INTEGER);
			sqlQuery.addScalar("osId",Hibernate.INTEGER);
			sqlQuery.addScalar("vmZone",Hibernate.STRING);
			sqlQuery.addScalar("vm_status",Hibernate.STRING);
			sqlQuery.addScalar("vm_innerIP",Hibernate.STRING);
			sqlQuery.addScalar("vm_outerIP",Hibernate.STRING);
			sqlQuery.addScalar("processState",Hibernate.STRING);
			sqlQuery.addScalar("comments",Hibernate.STRING);
			sqlQuery.addScalar("outComments",Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(VpdcReference.class));
			if(ids!=null && ids.size()>0){
				sqlQuery.setParameterList("list", ids);
			}
			totalCount = sqlQuery.list().size();
			sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
			sqlQuery.setMaxResults(page.getPageSize());
			lvr = sqlQuery.list();
			page.setTotalCount(totalCount);
			page.setResult(lvr);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferenceByIdsAdmin异常", log, e);
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VpdcInstance getActiveVmFromVpdcReference(long referenceId)
			throws HsCloudException {
		String hql = "from VpdcInstance vi where vi.status = 0 and vi.vpdcreference.id='" + referenceId + "'";
		VpdcInstance instance = null;
		try {
			List<VpdcInstance> list = hibernateTemplate.find(hql);
			if (list != null && list.size() > 0) {
				instance = list.get(0);
			}
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getActiveVmFromVpdcReference异常", log, e);
		}
		return instance;
	}

	@Override
	public Page<VmInfoVO> findRecyclingVmsAdmin(Page<VmInfoVO> page,
			String vmType, String vm_Buss, String field, String value,
			List<Object> ids, String sort) throws HsCloudException {
		int totalCount = 0;
		List<VmInfoVO> lvr = null;
		StringBuilder sql = new StringBuilder("select DISTINCT vr.id as referenceId,vr.name as vmName,vr.sc_id as scId,");
		sql.append("vr.is_enable as isEnable,vr.vm_status as vmStatus,vr.vm_business_status as vmStatus_buss,");
		sql.append("vr.cpu_core as cpuCore ,vr.mem_size as memory,vr.disk_capacity as disk,");
		sql.append("vr.vm_zone as zoneCode,vr.vm_innerIP as ipInner,vr.vm_outerIP as ipOuter, ");
		sql.append("'' as vmId,'' as hostName,");
		sql.append("vrp.start_time as createTime,vrp.end_time as expireTime,");
		sql.append("user.email as userName");
		sql.append(" from hc_zone as zone ");
		sql.append(" ,hc_vpdc_reference as vr ");
		sql.append(" right join hc_vpdc_instance as vi ON (vi.VpdcRefrenceId=vr.id),");
		sql.append(" hc_vpdcReference_period as vrp, hc_user as user");
		sql.append(" where vr.id=vrp.renferenceId and vr.status=1 and vi.status=1 and vr.vm_owner=user.id ");
		if(!StringUtils.isEmpty(value)&&"vmId".equals(field)){
			
		}else if(!StringUtils.isEmpty(value)&&!StringUtils.isEmpty(field)){
			sql.append(" and (");
			sql.append(" vr."+field+" like '%"+value+"%'");
			sql.append(" or vi.vm_id like '%"+value+"%'");
			sql.append(" )");
		}
		sql.append(" and vr.vm_status='").append(Constants.VM_STATUS_SOFTDELETED).append("' ");
		if(!StringUtils.isEmpty(vmType)){
			sql.append(" and vr.vm_type="+vmType);
		}
		if(!StringUtils.isEmpty(vm_Buss)){
			sql.append(" and vr.vm_business_status="+vm_Buss);
		}
		if(ids !=null && ids.size()==0){
			ids.add(0L);
		}
		if(ids!=null && ids.size()>0){
			sql.append(" AND substring_index(vr.vm_zone,'$',1) =zone.`code`");
			sql.append(" AND zone.id IN(:list)");
		}
		/*if(ids !=null && ids.size()==0){
			ids.add(0L);
		}
		if(ids!=null && ids.size()>0){
			sql.append(" and vr.id in (:list)");
		}*/
		//1:按到期时间正序排列
		if("1".equals(sort)){
			sql.append(" order by vrp.end_time");
		}else if("2".equals(sort)){//VM申请时间
			sql.append(" order by vr.create_date desc");
		}else{//默认VM创建时间
			sql.append(" order by vrp.start_time desc");
		}		
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.addScalar("referenceId",Hibernate.LONG);
			sqlQuery.addScalar("vmName",Hibernate.STRING);
			sqlQuery.addScalar("scId",Hibernate.INTEGER);
			sqlQuery.addScalar("isEnable",Hibernate.INTEGER);
			sqlQuery.addScalar("vmStatus",Hibernate.STRING);
			sqlQuery.addScalar("vmStatus_buss",Hibernate.STRING);
			sqlQuery.addScalar("cpuCore",Hibernate.INTEGER);
			sqlQuery.addScalar("memory",Hibernate.INTEGER);
			sqlQuery.addScalar("disk",Hibernate.INTEGER);
			sqlQuery.addScalar("zoneCode",Hibernate.STRING);
			sqlQuery.addScalar("ipInner",Hibernate.STRING);
			sqlQuery.addScalar("ipOuter",Hibernate.STRING);
			sqlQuery.addScalar("vmId",Hibernate.STRING);
			sqlQuery.addScalar("hostName",Hibernate.STRING);
			sqlQuery.addScalar("createTime",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("expireTime",Hibernate.TIMESTAMP);			
			sqlQuery.addScalar("userName",Hibernate.STRING);
			
			sqlQuery.setResultTransformer(Transformers.aliasToBean(VmInfoVO.class));
			if(ids!=null && ids.size()>0){
				sqlQuery.setParameterList("list", ids);
			}
			totalCount = sqlQuery.list().size();
			sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
			sqlQuery.setMaxResults(page.getPageSize());
			lvr = sqlQuery.list();
			page.setTotalCount(totalCount);
			page.setResult(lvr);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findRecyclingVmsAdmin异常", log, e);
		}
		return page;
	}

	@Override
	public boolean clearVNCPortPool(){
		List<VmVNCPool> list = hibernateTemplate.find("from VmVNCPool");
		if(list!=null && list.size()>0){
			hibernateTemplate.deleteAll(list);
		}
		return true;
	}
	
	@Override
	public boolean saveVNCPortPool(Object[] ports,Long domainId) {
		VmVNCPool port = null;
		Date d = null;
		for(int i=0;i<ports.length;i++){
			d = new Date();
			port = new VmVNCPool();
			port.setCreateDate(d);
			port.setCreateId(0l);
			port.setName(ports[i].toString());
			port.setStatus(0);
			port.setUpdateDate(d);
			port.setUpdateId(0l);
			port.setDomainId(domainId);
			hibernateTemplate.save(port);
		}
		return true;
	}
	
	@Override
	public List<VmVNCPool> loadVNCPortPool(Long domainId) {
		// TODO Auto-generated method stub
		List<VmVNCPool> lvnc = null;
		try {
			String queryString = "from VmVNCPool vnc where 1=1";
			if(domainId!=null){
				queryString += " and vnc.status =0 and vnc.domainId=?";
				lvnc = hibernateTemplate.find(queryString,domainId);
			}else{
				lvnc = hibernateTemplate.find(queryString);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return lvnc;
	}

	@Override
	public boolean updateVNCPort(VmVNCPool port) {
		boolean bl = false;
		try {
			hibernateTemplate.update(port);
			bl = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return bl;
	}

	@Override
	public List<Object> getAvailableDomainIds(){
		String querySql = "select d.id from hc_domain d where d.status = 1";
		Session session = null;
		List<Object> list = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(querySql);
			list = sqlQuery.list();
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"InstanceDetail_ServiceCatalogVo异常", log, e);
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public VmVNCPool getPoolByPort(String port,Long domainId) {
		// TODO Auto-generated method stub
		VmVNCPool pool = null;
		String queryString = "from VmVNCPool pool where pool.name='"+port+"'";
		if(domainId!=null){
			queryString += " and pool.domainId="+domainId;
		}
		List<VmVNCPool> lpool = (List<VmVNCPool>)hibernateTemplate.find(queryString);
		if(lpool!=null && lpool.size()>0){
			pool = lpool.get(0);
		}
		return pool;
	}

	@Override
	public VpdcInstance getLatestVpdcInstance(long referenceId, int status)
			throws HsCloudException {
		VpdcInstance vpdcInstance = null;
		StringBuilder hql = new StringBuilder("from VpdcInstance vi where vi.status =:status");
		hql.append(" and vi.vpdcreference.id =:referenceId");
		hql.append(" order by vi.createDate DESC ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("status", status);
		query.setParameter("referenceId", referenceId);
		query.setMaxResults(1);
//		vpdcInstance = (VpdcInstance) query.list().get(0);
		vpdcInstance =(VpdcInstance) query.uniqueResult();
		return vpdcInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<VpdcReference> findVpdcReferenceByOwnerAdmin(Page<VpdcReference> page,
			String owner,String nodeName,String zoneCode)throws HsCloudException {
		int totalCount = 0;
		List<VpdcReference> lvr = null;
		StringBuilder sql = new StringBuilder("select DISTINCT vr.id,vr.name,vr.sc_id as scId,vr.radom_user,");
		sql.append("vr.create_date as createDate,vr.is_enable as isEnable,vr.bwtIn,vr.bwtOut,vr.ipConnIn,");
		sql.append("vr.ipConnOut,vr.tcpConnIn,vr.tcpConnOut,vr.udpConnIn,vr.udpConnOut,vr.cpuLimit,vr.diskRead,");
		sql.append("vr.diskWrite,vr.vm_owner as owner,vr.vm_business_status,vr.disk_capacity,vr.cpu_core,vr.mem_size,");
		sql.append("vr.osId,vr.comments,vr.vm_zone as vmZone,vr.vm_status,vr.vm_innerIP,vr.vm_outerIP,vr.process_state as processState,vr.comments,vr.outComments ");
		sql.append(" from hc_vpdc_reference as vr");
//		StringBuffer idStr = new StringBuffer();
		if(!"".equals(nodeName)){
			sql.append(" left join hc_vpdc_instance as vi on (vi.VpdcRefrenceId=vr.id)" +
					"  where vr.status=0 and vi.status = 0 and vi.nodeName='").append(nodeName).append("'");
		}else{
			sql.append(" where vr.status=0 ");
		}
		if(!StringUtils.isEmpty(zoneCode)&& !"null".equals(zoneCode)){
			sql.append(" AND substring_index(vr.vm_zone,'$',1) ='").append(zoneCode).append("'");
		}
		//虚拟中心模块不需要看到【申请中、已取消的VM】
		sql.append(" and (vr.vm_business_status is null or (vr.vm_business_status!="+VMStatusBussEnum.TRYWAIT.getCode()+" and vr.vm_business_status!="+VMStatusBussEnum.CANCEL.getCode()+"))");
		
		sql.append(" and vr.id in (");
		sql.append(" select r.primKey from hc_resource r,hc_user u where r.resourceType='com.hisoft.hscloud.vpdc.ops.entity.VpdcReference' and primKey!=0 and r.ownerId = u.id and u.email LIKE '%").append(owner).append("%'");
		sql.append(" )");
		
		sql.append(" order by vr.id DESC");
		try {
			SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
			sqlQuery.addScalar("id",Hibernate.LONG);
			sqlQuery.addScalar("name",Hibernate.STRING);
			sqlQuery.addScalar("scId",Hibernate.INTEGER);
			sqlQuery.addScalar("radom_user",Hibernate.STRING);
			sqlQuery.addScalar("createDate",Hibernate.TIMESTAMP);
			sqlQuery.addScalar("isEnable",Hibernate.INTEGER);
			
			sqlQuery.addScalar("bwtIn",Hibernate.INTEGER);
			sqlQuery.addScalar("bwtOut",Hibernate.INTEGER);
			sqlQuery.addScalar("ipConnIn",Hibernate.INTEGER);
			sqlQuery.addScalar("ipConnOut",Hibernate.INTEGER);			
			sqlQuery.addScalar("tcpConnIn",Hibernate.INTEGER);
			sqlQuery.addScalar("tcpConnOut",Hibernate.INTEGER);
			sqlQuery.addScalar("udpConnIn",Hibernate.INTEGER);
			sqlQuery.addScalar("udpConnOut",Hibernate.INTEGER);
			sqlQuery.addScalar("cpuLimit",Hibernate.INTEGER);
			sqlQuery.addScalar("diskRead",Hibernate.INTEGER);
			sqlQuery.addScalar("diskWrite",Hibernate.INTEGER);
			
			sqlQuery.addScalar("owner",Hibernate.LONG);
			sqlQuery.addScalar("vm_business_status",Hibernate.INTEGER);
			sqlQuery.addScalar("disk_capacity",Hibernate.INTEGER);
			sqlQuery.addScalar("cpu_core",Hibernate.INTEGER);
			sqlQuery.addScalar("mem_size",Hibernate.INTEGER);
			sqlQuery.addScalar("osId",Hibernate.INTEGER);
			sqlQuery.addScalar("vmZone",Hibernate.STRING);
			sqlQuery.addScalar("vm_status",Hibernate.STRING);
			sqlQuery.addScalar("vm_innerIP",Hibernate.STRING);
			sqlQuery.addScalar("vm_outerIP",Hibernate.STRING);
			sqlQuery.addScalar("processState",Hibernate.STRING);
			sqlQuery.addScalar("comments",Hibernate.STRING);
			sqlQuery.addScalar("outComments",Hibernate.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(VpdcReference.class));			
			totalCount = sqlQuery.list().size();
			sqlQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
			sqlQuery.setMaxResults(page.getPageSize());
			lvr = sqlQuery.list();
			page.setTotalCount(totalCount);
			page.setResult(lvr);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferenceByIdsAdmin异常", log, e);
		}
		return page;
	}

	@Override
	public VpdcReference getVpdcReferenceByVmId(String vmId)
			throws HsCloudException {
		VpdcReference reference = null;
		String hql = "select vi.vpdcreference from VpdcInstance vi where vi.vmId='" + vmId + "'";
		try {
			List<VpdcReference> list = hibernateTemplate.find(hql);
			if(list!=null && list.size()>0){
				reference = list.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"findVpdcReferenceByVmId异常", log, e);
		}
		return reference;
	}

	@Override
	public void saveRouterTemplate(VpdcVrouterTemplate rTemplate)
			throws HsCloudException {
		hibernateTemplate.save(rTemplate);
	}

	@Override
	public VpdcVrouterTemplate getRouterTemplate(Long id)
			throws HsCloudException {
		VpdcVrouterTemplate routerTemplate = hibernateTemplate.get(VpdcVrouterTemplate.class, id);
		return routerTemplate;
	}

	@Override
	public Long saveVpdcNetwork(VpdcNetwork network) throws HsCloudException {
		hibernateTemplate.save(network);
		return network.getId();
	}
	
	@Override
	public VpdcNetwork getVpdcNetwork(Long id) throws HsCloudException{
		return hibernateTemplate.get(VpdcNetwork.class, id);
	}
	
	public void deleVpdcNetwork(Long id,Long userId) throws HsCloudException{
		VpdcNetwork vn = hibernateTemplate.get(VpdcNetwork.class, id);
		vn.setDeleted(1);
		vn.setUpdateDate(new Date());
		vn.setUpdateId(userId);
		hibernateTemplate.update(vn);
	}

	@Override
	public Long saveVpdcLan(VpdcLan lan) throws HsCloudException {
		hibernateTemplate.saveOrUpdate(lan);
		return lan.getId();
	}

	@Override
	public String getValidWanNetworkIP(Long zoneGroupId)
			throws HsCloudException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VpdcNetwork getWanNetwork(String ip) throws HsCloudException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveNetwork_Object(VpdcNetwork_Object networkObject) {
		hibernateTemplate.save(networkObject);
	}
	
	@Override
	public void deleNetwork_Object(VpdcNetwork_Object vno){
		hibernateTemplate.delete(vno);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcNetwork_Object> findNetwork_Objects(Long objectId,int objectType){
		String hql = "from VpdcNetwork_Object vno where vno.objectId = "+objectId+" and vno.objectType="+objectType;
		List<VpdcNetwork_Object> lvno = hibernateTemplate.find(hql);
		return lvno;
	}
	@Override
	public List<VpdcNetwork_Object> findNetwork_Objects(Long networkId){
		String hql = "from VpdcNetwork_Object vno where vno.networkId = "+networkId;
		List<VpdcNetwork_Object> lvno = hibernateTemplate.find(hql);
		return lvno;
	}

	@Override
	public int getVpdcsCountByUser(int vpdcType, String name, User user) 
			throws HsCloudException {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from Vpdc v where v.deleted=0 and v.owner=" + user.getId());
		Long count = 0L;
		try {
			if (StringUtils.isNotEmpty(name)) {
				sb.append(" and v.name like '%" + name + "%'");
			}
			if (vpdcType != -1) {
				sb.append(" and v.type=" + vpdcType);
			}
			count = (Long) hibernateTemplate.find(sb.toString()).listIterator().next();
		}
		catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVpdcsCountByUser异常", log, e);
		}
		return count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vpdc> findVpdcsByUser(int offSet, int length, int vpdcType, 
			String name, User user) throws HsCloudException {
		List<Vpdc> vpdc_list = null;
		StringBuilder hql = new StringBuilder();
		hql.append("from Vpdc v where v.deleted=0 and v.owner=" + user.getId());
		try {
			if (StringUtils.isNotEmpty(name)) {
				hql.append(" and v.name like '%" + name + "%'");
			}
			if (vpdcType != -1) {
				hql.append(" and v.type=" + vpdcType);
			}
			hql.append(" order by id desc");
			vpdc_list = (List<Vpdc>) noUtil.getListForPage(hql.toString(), offSet, length);
		}
		catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcByUser异常", log, e);
		}
		return vpdc_list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String findZoneGroupName(Long zoneGroupId) throws HsCloudException {
		List<Object> object_list = null;
		String zoneGroupName = null;
		try {
			String hql = "select zg.name from ZoneGroup zg where zg.id=" + zoneGroupId;
			object_list = hibernateTemplate.find(hql);
			if (object_list != null && object_list.size() > 0) {
				zoneGroupName = object_list.get(0).toString();
			}
		}
		catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findZoneGroupName异常", log, e);
		}
		return zoneGroupName;
	}

	@Override
	public int getVRCountByQuery(String query, Long statusId, long vpdcId) 
			throws HsCloudException {
		StringBuilder hql = new StringBuilder(" select count(*) from VpdcReference vr ");
		hql.append(" where vr.status = 0 and vr.owner="+statusId+" and vr.vpdc="+vpdcId);
		if (StringUtils.isNotEmpty(query)) {
			hql.append(" and (vr.vm_innerIP like '%"+query+"%' or vr.vm_outerIP like '%"+query+"%' or vr.name like '%"+query+"%')");
		}		
		hql.append(" order by vr.id desc");
		Long count = 0L;
		try {
			count = (Long)hibernateTemplate.find(hql.toString()).listIterator().next();
		} catch (DataAccessException e) {
			throw new HsCloudException("OPS-Dao001", "getVRCountByCondition异常", log, e);
		}
		return count.intValue();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcReference> findVpdcReferencesByQuery(int offSet, int length, 
			String sort, String query, Long statusId, long vpdcId) throws HsCloudException {
		List<VpdcReference> vr_list = null;
		StringBuilder hql = new StringBuilder(" select vr from VpdcReference vr,VpdcReference_Period vp ");
		hql.append(" where vr.id=vp.vpdcreferenceId and vr.status = 0 and vr.owner="+statusId+" and vr.vpdc="+vpdcId);
		if (StringUtils.isNotEmpty(query)) {
			hql.append(" and (vr.vm_innerIP like '%"+query+"%' or vr.vm_outerIP like '%"+query+"%' or vr.name like '%"+query+"%')");
		}		
		// 1:按到期时间正序排列
		if ("1".equals(sort)) {
			hql.append(" order by vp.endTime");
		}
		else {// 默认VM创建时间倒序排列
			hql.append(" order by vp.startTime desc");
		}
		try {
			vr_list = (List<VpdcReference>) noUtil.getListForPage(hql.toString(), offSet, length);
		}
		catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "findVpdcReferencesByCondition异常", log, e);
		}
		return vr_list;			
	}
	
	@Override
	public Vpdc getVpdcById(long vpdcId) throws HsCloudException {
		Vpdc vpdc = null;
		try {
			vpdc = hibernateTemplate.get(Vpdc.class, vpdcId);
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getVpdcById异常", log, e);
		}
		return vpdc;
	}

	@Override
	public VpdcRouter getRouter(String uuid) throws HsCloudException {
		VpdcRouter router = null;
		String hql = "from VpdcRouter r where r.deleted = 0 and r.routerUUID='" + uuid + "'";
		try {
			List<VpdcRouter> list = hibernateTemplate.find(hql);
			if(list!=null && list.size()>0){
				router = list.get(0);
			}
		} catch (Exception e) {
			throw new HsCloudException("OPS-Dao001",
					"getRouter异常", log, e);
		}
		return router;
	}

	@Override
	public void saveRouter(VpdcRouter router) throws HsCloudException {
		hibernateTemplate.saveOrUpdate(router);
	}
	
	@Override
	public void deleteVPDC(Vpdc vpdc) throws HsCloudException {
		hibernateTemplate.delete(vpdc);
	}
	
	@Override
	public VpdcRouter getRouter(Long id) throws HsCloudException{
		VpdcRouter vr = hibernateTemplate.get(VpdcRouter.class, id);
		return vr;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public VpdcRouter getRouterByVpdcId(long vpdcId) throws HsCloudException {
		VpdcRouter vr = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("from VpdcRouter vr where vr.deleted=0 and vr.vpdc=" + vpdcId);
			List<VpdcRouter> vr_list = hibernateTemplate.find(hql.toString());
			if (vr_list != null && vr_list.size() > 0) {
				vr = vr_list.get(0);
			}
		}
		catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getRouterByVpdcId异常", log, e);
		}
		return vr;
	}

	@Override
	@Transactional
	public synchronized String getVMNameSerialNumber(String tableName) throws HsCloudException {
		BigInteger cacheSize =  BigInteger.valueOf(1);
		BigInteger ten = BigInteger.valueOf(10);
		BigInteger hunded  = BigInteger.valueOf(100);
		BigInteger zero = BigInteger.valueOf(0);
		BigInteger thousand = BigInteger.valueOf(1000);
		BigInteger cur ;
		BigInteger base;
		String vmSerialName;
		
		String sql = ("SELECT current_max_id FROM hc_id_generator WHERE table_name = '"
					+ tableName + "' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		BigInteger  result =  (BigInteger) query.uniqueResult();
		
		cur = result;
		
		sql = ("UPDATE hc_id_generator SET current_max_id = "
				+ (cur.add(cacheSize))
				+ " WHERE table_name = '"
				+ tableName + "' ");
		query = getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
		base = cur.add(cacheSize);
		if(base.divide(ten) == zero){
			vmSerialName="000"+base.toString();
		}else if(base.divide(hunded) == zero){
			vmSerialName="00"+base.toString();
		}else if(base.divide(thousand) == zero){
			vmSerialName ="0"+ base.toString();
		}else{
			vmSerialName = base.toString();
		}
		return vmSerialName;
	}

	@Override
	public String getDomainIdByUserId(Long userId) throws HsCloudException {
		BigInteger res;
		BigInteger ten= BigInteger.valueOf(10);
		BigInteger zero = BigInteger.valueOf(0);
		String domainid;
		String heard = "S";
		SimpleDateFormat sdf=new SimpleDateFormat("ddMMyy");
		String date = sdf.format(new java.util.Date()); 
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("select d.id from hc_domain d LEFT JOIN hc_user u ON d.id = u.domain_id WHERE d.`status` = 1 AND u.id=" + userId);
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			res =  (BigInteger) query.uniqueResult();
			if(res.divide(ten) == zero){
				domainid = "0"+res.toString();
			}else{
				domainid = res.toString();
			}
			
		}catch(Exception e){
			throw new HsCloudException("OPS-Dao001", "getDomainIdByUserId异常", log, e);
		}
		return heard + domainid + date;
	}

	@Override
	public int getIPCountByZoneCode(String zoneCode) throws HsCloudException {
		int IpCount = 0;
		try{
			StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM hc_ip_detail a ");
			sb.append(" LEFT JOIN hc_ip_range b ON a.ip_range_id = b.id ");
			sb.append(" LEFT JOIN hc_ip_zone c ON b.id = c.ip_id ");
			sb.append(" LEFT JOIN hc_zone d ON d.id = c.zone_id ");
			sb.append(" WHERE a.status=0 AND ");
			sb.append(" d.code ='"+ zoneCode+"'");
			SQLQuery query = getSession().createSQLQuery(sb.toString());
			IpCount =Integer.valueOf(query.list().get(0).toString());
			return IpCount;
		}catch(Exception e){
			throw new  HsCloudException("OPS-Dao001", "getIPCountByZoneCode异常", log, e);
		}
	}

	@Override
	public int getVmBuilingCountByZoneCode(String zoneCode)
			throws HsCloudException {
		int builingVMCount = 0;
		try{
			StringBuffer sb  = new StringBuffer("SELECT COUNT(*) FROM hc_vpdc_reference hvr WHERE ");
			sb.append(" hvr.vm_status = 'BUILDING' AND hvr.status = 0 AND ");
			sb.append(" hvr.vm_zone LIKE '"+zoneCode+"%'");
			SQLQuery query = getSession().createSQLQuery(sb.toString());
			builingVMCount = Integer.valueOf(query.list().get(0).toString());
			
		}catch(Exception e){
			throw new  HsCloudException("OPS-Dao001", "getVmBuilingCountByZoneCode异常", log, e);
		}
		return builingVMCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcReferenceQuotaInfo> findVpdcReferenceByUserId(long userId) {
		List<VpdcReferenceQuotaInfo> lst=new ArrayList<VpdcReferenceQuotaInfo>();
		StringBuilder sql=new StringBuilder();
		Map<String, Long> map=new HashMap<String, Long>();
		sql.append("SELECT hvi.vm_id,hvr.cpu_core, hvr.mem_size, hvr.disk_capacity,");
		sql.append("hvr.network_bandwidth,hvr.osId,hvr.name, hvr.vm_zone,hvr.vm_outerIP, hvr.status,");
		sql.append("hvr.vm_task_status,hvr.process_state,hvr.vm_innerIP ");
		sql.append("FROM hc_vpdc_reference hvr,hc_vpdc_instance hvi,");
		sql.append("hc_vpdcReference_period hvp WHERE hvr.id = hvi.VpdcRefrenceId ");
		sql.append("AND hvr.id =hvp.renferenceId AND hvr.status=0 AND hvr.is_enable=0 ");
		sql.append("AND hvi.status=0 AND hvp.start_time<NOW() AND hvp.end_time>NOW() ");
		sql.append("AND hvr.creater_type='user' AND hvr.vm_owner=:userId ");
		map.put("userId", userId);
		try {
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.addScalar("vm_id",Hibernate.STRING);
			query.addScalar("cpu_core",Hibernate.INTEGER);
			query.addScalar("mem_size",Hibernate.INTEGER);
			query.addScalar("disk_capacity",Hibernate.INTEGER);
			query.addScalar("network_bandwidth",Hibernate.INTEGER);
			query.addScalar("osId",Hibernate.INTEGER);
			query.addScalar("name",Hibernate.STRING);
			query.addScalar("vm_zone",Hibernate.STRING);
			query.addScalar("vm_outerIP",Hibernate.STRING);
			query.addScalar("status",Hibernate.INTEGER);
			query.addScalar("vm_task_status",Hibernate.STRING);
			query.addScalar("process_state",Hibernate.STRING);
			query.addScalar("vm_innerIP",Hibernate.STRING);
			query.setResultTransformer(Transformers.aliasToBean(VpdcReferenceQuotaInfo.class));
			query.setParameter("userId", userId);
			lst=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lst;
	}

	@Override
	public int getVmsCountByUser(long userId) throws HsCloudException {
		Map<String, Long> map=new HashMap<String, Long>();
		StringBuilder hql=new StringBuilder();
		Object count;
		hql.append("select COUNT(*) from VpdcReference where owner="+userId);
		count = hibernateTemplate.find(hql.toString()).iterator().next();
		return Integer.parseInt(count.toString());
	}

	@Override
	public int getVmRenewalCountByUser(long userId) throws HsCloudException {
		Object count;
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT COUNT(hv.id) FROM hc_vpdc_reference hv,hc_vpdc_renewal hvr,");
		sql.append("hc_vpdcReference_period hvp WHERE hv.id = hvr.referenceId AND ");
		sql.append("hvr.referenceId=hvp.renferenceId AND hvp.start_time<NOW() AND ");
		sql.append("hvp.end_time>NOW() AND hv.status=0 AND hv.is_enable=0 AND hv.vm_owner=:userId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("userId", userId);
		count=query.list().iterator().next();
		return Integer.parseInt(count.toString());
	}

}