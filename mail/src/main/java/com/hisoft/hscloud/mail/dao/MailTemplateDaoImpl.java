package com.hisoft.hscloud.mail.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.mail.entity.MailTemplate;
@Repository
public class MailTemplateDaoImpl extends HibernateDao<MailTemplate,Long> implements  MailTemplateDao{
    
    private Logger logger = Logger.getLogger(this.getClass());
    
	public MailTemplate findUniqueBy(String propertyName, Object value){
		return super.findUniqueBy(propertyName, value);
	}
	
	/**
	 * 获取邮件模板
	* @param keyword
	* @param domainId
	* @return
	 */
	@Override
	public MailTemplate findMailTemplate(String keyword, long domainId) {
	    String hql = "from MailTemplate where keyword = :keyword and domainId = :domainId";
	    Map<String, Object> condition = new HashMap<String, Object>();
	    condition.put("keyword", keyword);
	    condition.put("domainId", domainId);
	    List<MailTemplate> list = this.find(hql, condition);
	    if(list != null && list.size() == 1) {
	        return list.get(0);
	    }
	    throw new HsCloudException(Constants.MAIL_TEMPLATE_FIND_EXCEPTION, "获取邮件模板异常", logger);
	}
	
	/**
     * <创建分平台模板> 
    * <功能详细描述> 
    * @param domainId 
    * @see [类、类#方法、类#成员]
     */
    @Override
    public void createMailTemplate(long domainId) throws HsCloudException{
        String sql = "insert into hc_mail_template(keyword, title, template, domain_id) " +
        		"(select keyword, title, template, :domainId " +
        		"from hc_mail_template where domain_id = 0)";
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("domainId", domainId);
        int result = query.executeUpdate();
        if(result<1){
        	throw new HsCloudException(Constants.MAIL_TEMPLATE_FIND_EXCEPTION, "创建分平台邮件模板异常", logger);
        }
    }

}
