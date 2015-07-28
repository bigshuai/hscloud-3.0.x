/* 
* 文 件 名:  AnnouncementDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.message.dao.Impl; 

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.message.dao.AnnouncementDao;
import com.hisoft.hscloud.message.entity.Announcement;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;

/** 
 * 公告Dao实现类  
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class AnnouncementDaoImpl extends HibernateDao<Announcement,Long> implements AnnouncementDao {
    @Override
    public long saveAnnouncement(Announcement announcement) {
        this.save(announcement);
        return announcement.getId();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Announcement> findAnnouncementByConditoin(int counter, int type, Long domainId) {
//    	StringBuffer sql = new StringBuffer("select an.create_id as createId, an.update_id AS updateId, an.create_time AS createTime, an.update_time AS updateTime,");
//    	sql.append("an.title AS title,an.content AS content");
//    	sql.append("from hc_announcement an where an.type=:type and FIND_IN_SET(:domainId,an.domainIds) order by an.id desc");
        String hql = "select an.create_id as createId, an.update_id AS updateId, an.create_time AS createTime, an.update_time AS updateTime,an.title AS title,an.content AS content from hc_announcement an where an.type=:type and FIND_IN_SET(:domainId,an.domainIds) order by an.id desc";
//        Query query = this.createQuery(hql);
        SQLQuery query = this.getSession().createSQLQuery(hql);
        query.setParameter("type", type);
        query.setParameter("domainId", domainId);
        query.setFirstResult(0);
        query.setMaxResults(counter);
        query.addScalar("createId",Hibernate.LONG);
        query.addScalar("updateId",Hibernate.LONG);
        query.addScalar("title",Hibernate.STRING);
        query.addScalar("content",Hibernate.STRING);
        query.addScalar("createTime",Hibernate.TIMESTAMP);
        query.addScalar("updateTime",Hibernate.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(Announcement.class));
        return query.list();
    }
    
    @Override
    public Announcement getAnnouncement(long announcementId) {
        return this.findUniqueBy("id", announcementId);
    }
    
    /**
     * 查询公告页
    * @param announcementPage
    * @param query
    * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<Map<String, Object>> findAnnouncementPage(
            Page<Map<String, Object>> announcementPage, String query) {
        Map<String, Object> condition = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("select a.id as id, a.title as title, a.content as content, a.create_time as create_time,a.update_time as update_time, ad.email as email, a.type as type, GROUP_CONCAT(hd.abbreviation SEPARATOR ',') as abbreviation ");
        hql.append(" from hc_announcement a, hc_admin ad, hc_domain hd");
        hql.append(" where a.create_id = ad.id and FIND_IN_SET(hd.id,a.domainIds)");
        if(StringUtils.isNotBlank(query)){
            hql.append(" and (a.title like :query or a.content like :query) ");
        }
        hql.append(" group by a.id");
        hql.append(" order by a.id desc ");
        SQLQuery query1 = this.getSession().createSQLQuery(hql.toString());
        if(StringUtils.isNotBlank(query)){
        	 query1.setParameter("query", query);
        }
       
        int offset = 0;
        if (announcementPage.getPageNo() == 1) {
            offset = 0;
        } else {
            offset = (announcementPage.getPageNo() - 1) * announcementPage.getPageSize();
        }
        List<Object[]> list = query1.list();
        announcementPage.setTotalCount(query1.list().size());
        query1.setFirstResult(offset);
        query1.setMaxResults(announcementPage.getPageSize());
        list = query1.list();
        announcementPage.setResult(new ArrayList());
        for(Object[] array : list) {
        	Map map = new HashMap();
        	map.put("id", array[0]);
        	map.put("title", array[1]);
        	map.put("content", array[2]);
        	map.put("create_time", array[3]);
        	map.put("update_time", array[4]);
        	map.put("email", array[5]);
        	map.put("type", array[6]);
        	map.put("abbreviation", array[7]);
        	announcementPage.getResult().add(map);
        }
        return announcementPage;
    }
    
    /**
     * 删除公告
    * @param announcementId
     */
    @Override
    public void delAnnouncement(long announcementId) {
        this.delete(announcementId);
    }
}
