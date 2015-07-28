/* 
* 文 件 名:  VideoDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.dao.Impl; 


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.storage.dao.VideoDao;
import com.hisoft.hscloud.storage.entity.Video;
import com.hisoft.hscloud.storage.vo.VideoVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class VideoDaoImpl extends HibernateDao<Video,Long> implements VideoDao{
    @Override
    public void saveVideo(Video video){
        this.save(video);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findVideo(Page<VideoVO> page, Map<String, Object> condition) {
        String tenantId = (String)condition.get("tenantId");
        condition.remove("tenantId");
       /* StringBuilder sql = new StringBuilder("select id as id, length_tag as lengthStatus, " +
        		"channel as channel, ymd as date, hms as time, " +
        		"timestamp as dateShow, plan_tag as planStatus, " +
        		"device_name as device, name as name, size_kb as fileSize from ");
        sql.append(tenantId);
        sql.append(" v where 1 = 1 ");*/
        
        StringBuilder sql = new StringBuilder("`");
        sql.append(tenantId);
        sql.append("` v where 1 = 1 ");
        if(condition.containsKey("query")) {
            sql.append(" and v.name like :query ");
        }
        if(condition.containsKey("startDate")) {
            sql.append(" and v.timestamp_end >= :startDate ");
        }
        if(condition.containsKey("endDate")) {
            sql.append(" and v.timestamp <= :endDate ");
        }
        if(condition.containsKey("module")) {
            sql.append(" and v.module = :module ");
        }
        
        
     //   SQLQuery query = getSession().createSQLQuery(sql.toString());
        String sql_counter = "select count(id) from ";
        SQLQuery countQuery = getSession().createSQLQuery(sql_counter + sql.toString());
        for (Entry<String, Object> entry : condition.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        page.setTotalCount(Long.valueOf(countQuery.uniqueResult().toString()));
        
        
        
        if(StringUtils.isNotBlank(page.getOrderBy())) {
            sql.append(" order by v.").append(convertName(page.getOrderBy()))
            .append(" ").append(page.getOrder());
        } else {
            sql.append(" order by v.id desc ");
        }
        StringBuilder sql_query = new StringBuilder("select id, length_tag , ");
        sql_query.append("channel, ymd , hms , ");
        sql_query.append("timestamp , plan_tag , ");
        sql_query.append("device_name , name , size_kb, module from ");
        SQLQuery query = getSession().createSQLQuery(sql_query.toString() + sql.toString());
        for (Entry<String, Object> entry : condition.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        
        query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        return query.list();
    }
    
    private String convertName(String oldName) {
        String result = "";
        if("dateShow".equals(oldName)) {
            result = "timestamp";
        } else if("fileSize".equals(oldName)) {
            result = "size_kb";
        } else {
            result = oldName;
        }
        return result;
    }
}
