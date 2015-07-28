/* 
* 文 件 名:  VpdcVrouterTemplateDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.dao; 

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;

/** 
 * <VRouter配置模板> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class VpdcVrouterTemplateDaoImpl extends HibernateDao<VpdcVrouterTemplate, Long> implements VpdcVrouterTemplateDao{
	
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private HibernateTemplate hibernateTemplate;

    public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	@Override
    public long addVrouterTemplate(VpdcVrouterTemplate vpdcVrouterTemplate) {
        this.save(vpdcVrouterTemplate);
        return vpdcVrouterTemplate.getId();
    }

    @Override
    public VpdcVrouterTemplate getVrouterTemplate(long id) {
        return this.get(id);
    }

    @Override
    public Page<VrouterTemplateVO> pageVrouterTemplateVO(Page<VrouterTemplateVO> page) {
        String sql = "select v.id, v.cpu_core cpuCore, v.disk_capacity diskCapacity, v.mem_size memSize, " +
        		" v.osId, si.name version, v.name " +
        		" from hc_service_item si, hc_vpdc_vrouterTemplate v " +
        		" where si.item_id = v.osId and v.deleted = 0 order by id desc ";
        
        SQLQuery query = getSession().createSQLQuery(sql);
       /* for (Entry<String, Object> entry : map.entrySet()) {
              if(entry.getValue() instanceof Collection){
                  query.setParameterList(entry.getKey(), (Collection<?>)entry.getValue());
              }else{
                  query.setParameter(entry.getKey(), entry.getValue());
              }
          }*/
        
        query.addScalar("id", Hibernate.LONG);
        query.addScalar("name", Hibernate.STRING);
        query.addScalar("cpuCore", Hibernate.INTEGER);
        query.addScalar("diskCapacity", Hibernate.INTEGER);
        query.addScalar("memSize", Hibernate.INTEGER);
        query.addScalar("osId", Hibernate.INTEGER);
        query.addScalar("version", Hibernate.STRING);
        query.setResultTransformer(Transformers.aliasToBean(VrouterTemplateVO.class));
        
        query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        
        @SuppressWarnings("unchecked")
        List<VrouterTemplateVO> list = query.list();
        page.setResult(list);
        
        String counterSql = "select count(1) " + sql.substring(sql.indexOf("from"));
        SQLQuery countQuery = getSession().createSQLQuery(counterSql.toString());
        page.setTotalCount(Long.valueOf(countQuery.uniqueResult().toString()));
        
        
        
        return page;
    }

    @Override
    public List<VpdcVrouterTemplate> getVrouterTemplateByName(String name) {
        return this.findBy("name", name);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<VpdcVrouterTemplate> getAllVrouterTemplate() {
		List<VpdcVrouterTemplate> vvt_list = null;
		try {
			vvt_list = hibernateTemplate.find("from VpdcVrouterTemplate");
		}
		catch (Exception e) {
			throw new HsCloudException("OPS-Dao001", "getAllVrouterTemplate异常", log, e);
		}
		return vvt_list;
	}
    
}