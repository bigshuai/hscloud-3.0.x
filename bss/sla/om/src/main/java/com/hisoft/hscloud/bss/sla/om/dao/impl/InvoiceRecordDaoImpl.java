/* 
* 文 件 名:  InvoiceRecordDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.dao.impl; 

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.bss.sla.om.dao.InvoiceRecordDao;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class InvoiceRecordDaoImpl extends HibernateDao<InvoiceRecord, Long> implements InvoiceRecordDao {

    @Override
    public long writeInvoiceRecord(InvoiceRecord invoiceRecord) {
        this.save(invoiceRecord);
        return invoiceRecord.getId();
    }
    
    @Override
    public InvoiceRecord getInvoiceRecord(long id) {
        return this.findUniqueBy("id", id);
    }
    
    @Override
    public List<InvoiceRecord> findInvoiceRecordByInvoiceNo(String invoiceNo) {
        return this.findBy("invoiceNo", invoiceNo);
    }
    
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> findInvoiceRecordList(Page<Map<String, Object>> page, Map<String, Object> condition) {
        StringBuilder hql = new StringBuilder("select new Map(i.id as id, i.invoiceNo as invoiceNo, ");
        hql.append("i.invoiceAmount as invoiceAmount, u.email as email, u.name as username, ");
        hql.append("i.status as status, i.applicationTime as applicationTime, ");
        hql.append("i.billingTime as billingTime, i.innerDescription as innerDescription, ");
        hql.append("i.invoiceTitle as invoiceTitle, i.invoiceAmount as invoiceAmount, ");
        hql.append("i.sendTime as sendTime, i.description as description, ");
        hql.append("i.takeInvoiceType as takeInvoiceType, i.courierDeliveryTime as courierDeliveryTime, ");
        hql.append("i.recipient as recipient, i.recipientCompany as recipientCompany, ");
        hql.append("i.province as province, i.city as city, ");
        hql.append("i.address as address, i.postcode as postcode, ");
        hql.append("i.mobile as mobile, i.telephone as telephone, ");
        hql.append("i.invoiceType as invoiceType, ");
        hql.append("i.fax as fax, u.domain.abbreviation as abbreviation ) ");
        hql.append("from InvoiceRecord i, User u where i.userId = u.id");
        if(condition.containsKey("status")) {
            hql.append(" and (i.status = :status)");
        }
        if(condition.containsKey("username") && condition.containsKey("invoiceNo")
                && condition.containsKey("invoiceTitle")) {
            hql.append(" and (u.name like :username or i.invoiceNo like :invoiceNo or i.invoiceTitle like :invoiceTitle )");
        } else if(condition.containsKey("email")) {
            hql.append(" and u.email = :email ");
        }
        if(condition.containsKey("domainIdList")) {
            hql.append(" and u.domain.id in ( :domainIdList ) ");
        }
        if(condition.containsKey("domainId")) {
            hql.append(" and u.domain.id = :domainId ");
        }
        if(condition.containsKey("fromDateApply")) {
            hql.append(" and (i.applicationTime >= :fromDateApply)");
        }
        if(condition.containsKey("toDateApply")) {
            hql.append(" and (i.applicationTime < :toDateApply )");
        }
        if(condition.containsKey("fromDateBill")) {
            hql.append(" and (i.billingTime >= :fromDateBill)");
        }
        if(condition.containsKey("toDateBill")) {
            hql.append(" and (i.billingTime < :toDateBill )");
        }
        if(condition.containsKey("fromDateSend")) {
            hql.append(" and (i.sendTime >= :fromDateSend)");
        }
        if(condition.containsKey("toDateSend")) {
            hql.append(" and (i.sendTime <= :toDateSend )");
        }
        hql.append(" order by i.id desc");
        Query query = this.createQuery(hql.toString(), condition);
        int offset = 0;
        if (page.getPageNo() == 1) {
            offset = 0;
        } else {
            offset = (page.getPageNo() - 1) * page.getPageSize();
        }
        
        page.setTotalCount(query.list().size());
        query.setFirstResult(offset);
        query.setMaxResults(page.getPageSize());
        page.setResult(query.list());
        return page;
    }

    @Override
    public List<Map<String, Object>> invoiceExcelExport(
            Map<String, Object> condition) {
        StringBuilder hql = new StringBuilder("select new Map(i.id as id, i.invoiceNo as invoiceNo, ");
        hql.append("i.invoiceAmount as invoiceAmount, u.email as email, u.name as username, ");
        hql.append("i.status as status,i.invoiceType as invoiceType ,i.applicationTime as applicationTime, ");
        hql.append("i.billingTime as billingTime, i.innerDescription as innerDescription, ");
        hql.append("i.invoiceTitle as invoiceTitle, i.invoiceAmount as invoiceAmount, ");
        hql.append("i.sendTime as sendTime, i.description as description, ");
        hql.append("i.takeInvoiceType as takeInvoiceType, i.courierDeliveryTime as courierDeliveryTime, ");
        hql.append("i.recipient as recipient, i.recipientCompany as recipientCompany, ");
        hql.append("i.province as province, i.city as city, ");
        hql.append("i.address as address, i.postcode as postcode, ");
        hql.append("i.mobile as mobile, i.telephone as telephone, ");
        hql.append("i.fax as fax, u.domain.abbreviation as abbreviation )  ");
        hql.append("from InvoiceRecord i, User u where i.userId = u.id");
        if(condition.containsKey("status")) {
            hql.append(" and (i.status = :status)");
        }
        if(condition.containsKey("username") && condition.containsKey("invoiceNo")
                && condition.containsKey("invoiceTitle")) {
            hql.append(" and (u.name like :username or i.invoiceNo like :invoiceNo or i.invoiceTitle like :invoiceTitle )");
        } else if(condition.containsKey("email")) {
            hql.append(" and u.email = :email ");
        }
        if(condition.containsKey("domainIdList")) {
            hql.append(" and u.domain.id in ( :domainIdList ) ");
        }
        if(condition.containsKey("domainId")) {
            hql.append(" and u.domain.id = :domainId ");
        }
        if(condition.containsKey("fromDateApply")) {
            hql.append(" and (i.applicationTime >= :fromDateApply)");
        }
        if(condition.containsKey("toDateApply")) {
            hql.append(" and (i.applicationTime < :toDateApply )");
        }
        if(condition.containsKey("fromDateBill")) {
            hql.append(" and (i.billingTime >= :fromDateBill)");
        }
        if(condition.containsKey("toDateBill")) {
            hql.append(" and (i.billingTime < :toDateBill )");
        }
        if(condition.containsKey("fromDateSend")) {
            hql.append(" and (i.sendTime >= :fromDateSend)");
        }
        if(condition.containsKey("toDateSend")) {
            hql.append(" and (i.sendTime <= :toDateSend )");
        }
        hql.append(" order by i.id desc");
        Query query = this.createQuery(hql.toString(), condition);
        return query.list();
    }

}
