/* 
* 文 件 名:  VpdcRenewalDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.dao; 

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcRenewal;

/** 
 * <续订业务数据操作> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class VpdcRenewalDaoImpl extends HibernateDao<VpdcRenewal, Long> implements VpdcRenewalDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public long saveVpdcRenewal(VpdcRenewal vpdcRenewal)
			throws HsCloudException {
		long id = 0L;
		try{
//			this.save(vpdcRenewal);
			hibernateTemplate.saveOrUpdate(vpdcRenewal);
			id = vpdcRenewal.getId();
		}catch (Exception e) {
			logger.error("saveVpdcRenewal Exception:", e);
		}		
		return id;
	}

	@Override
	public long getIdByReferenceId(long referenceId) throws HsCloudException {
		long id = 0L;
		VpdcRenewal vpdcRenewal = this.findUniqueBy("referenceId", referenceId);
		if(vpdcRenewal!=null){
			id = vpdcRenewal.getId();
		}
		return id;
	}

	@Override
	public boolean deleteVpdcRenewalByReferenceId(long referenceId)
			throws HsCloudException {
		boolean result = false;
		long id = this.getIdByReferenceId(referenceId);
		try{
			if(id>0){
				this.delete(id);
				result = true;
			}
		}catch (Exception e) {
			logger.error("deleteVpdcRenewalByReferenceId Exception:", e);
		}
		return result;
	}

	@Override
	public Page<VpdcRenewal> findVpdcRenewal(Page<VpdcRenewal> page, String field, Object fieldValue, String query,
			long userId) throws HsCloudException {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from VpdcRenewal as vre where vre.userId =:userId");
		map.put("userId", userId);
		if (StringUtils.isNotEmpty(query)) {
			hql.append(" and vre.name like :name ");
			map.put("name", "%" + query + "%");
		}
		if (StringUtils.isNotEmpty(field) && fieldValue != null && StringUtils.isNotEmpty(fieldValue.toString())) {
			if (StringUtils.isNumeric(fieldValue.toString())) {
				fieldValue = Integer.parseInt(fieldValue.toString());
			}
			hql.append(" and vre.").append(field).append(" =:fieldValue");
			map.put("fieldValue", fieldValue);
		}
		hql.append(" order by vre.endTime, (vre.endTime-vre.startTime)");
		page = this.findPage(page, hql.toString(), map);
		return page;
	}

	@Override
	public VpdcRenewal getVpdcRenewalByReferenceId(long referenceId)
			throws HsCloudException {
		return this.findUniqueBy("referenceId", referenceId);
	}

    @Override
    public void saveVpdcRenewal(Map<String, Object> condition) {
        String sql = "INSERT INTO hc_vpdc_renewal(create_date, create_id, name, update_date " +
        		", update_id, version, business_status" +
        		" ,business_type, end_time, orderNO, referenceId, sc_id, start_time, userId, vmUUID,is_enable,buy_type) VALUES " +
        		"(:createDate, 0, :name, :updateDate, 0, 0, :businessStatus" +
        		", :businessType, :endTime, :orderNo, :referenceId, :serviceCatalogId, :startTime, :userId, :vmUuid,:isEnable,:buyType)";
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        for (Entry<String, Object> entry : condition.entrySet()) {
            sqlQuery.setParameter(entry.getKey(), entry.getValue());
        }
        sqlQuery.executeUpdate();
    }

    @Override
    public void updateVpdcRenewal(Map<String, Object> condition) {
        String sql = "update hc_vpdc_renewal set name=:name, update_date=:updateDate,"
                + " update_id=0, version=0, business_status=:businessStatus "
                + " , business_type=:businessType, end_time=:endTime, orderNO=:orderNo "
                + " , referenceId=:referenceId, sc_id=:serviceCatalogId, start_time=:startTime "
                + " , userId=:userId, vmUUID=:vmUuid, is_enable=:isEnable,buy_type =:buyType  where id=:id";
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        for (Entry<String, Object> entry : condition.entrySet()) {
            sqlQuery.setParameter(entry.getKey(), entry.getValue());
        }
        sqlQuery.executeUpdate();
    }

}
