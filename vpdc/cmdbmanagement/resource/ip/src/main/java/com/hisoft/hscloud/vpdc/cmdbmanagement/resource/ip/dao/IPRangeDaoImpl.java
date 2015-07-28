package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;

@Repository
public class IPRangeDaoImpl extends HibernateDao<IPRange, Long> implements
		IPRangeDao {

	private Logger logger = Logger.getLogger(this.getClass());
	
    public long createIPRange(IPRange iPRange) {
        long id = 0L;
        this.save(iPRange);
        id = iPRange.getId();

        return id;
    }

	public IPRange getIPRangeById(long id) throws HsCloudException {
		IPRange ipRange = null;
		try {
			ipRange = this.get(id);
		} catch (Exception e) {
			logger.error("getIPRangeById Exception:", e);
		}
		return ipRange;
	}

	public boolean deleteIPRange(long id) throws HsCloudException {
		boolean result = false;
		IPRange iPRange = getIPRangeById(id);
		if (iPRange != null) {
			try {
				this.delete(id);
				result = true;
			} catch (Exception e) {
				logger.error("deleteIPRange Exception:", e);
			}
		}

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public Page<IPRange> findIPRange(Page page, String field, String fieldValue) {
		StringBuffer hql = new StringBuffer("from IPRange ipR where 2>1 and type=0 ");		
		Map<String, Object> values = new HashMap<String, Object>();
		if(fieldValue != null && !"".equals(fieldValue)){
			hql.append(" and ipR.serverZone.name like :name");
			values.put("name","%"+fieldValue+"%");
		}
        return this.findPage(page, hql.toString(), values);
    }

	@Override
	public List<IPRange> getAllIPsByServerZone(long zoneId)
			throws HsCloudException {
		List<IPRange> listIPRange = null;
		try{
			if(!"".equals(zoneId)){
				StringBuffer hql = new StringBuffer(
		                "select distinct ipr from IPRange ipr,IPDetail ipd where ipr.serverZone.id = :zoneId ");
				//hql.append(" and ipr in elements(ipd.ipRange)");
				//hql.append(" and ipd.status>0");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("zoneId",zoneId);
				listIPRange=this.find(hql.toString(), map);
			}
		}catch (Exception e) {
			logger.error("getAllIPsByServerZone Exception:", e);
		}
		return listIPRange;
	}
    

}
