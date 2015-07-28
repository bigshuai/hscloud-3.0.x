/* 
* 文 件 名:  AnnouncementServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.message.service.Impl; 

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.ExceptionServiceUtil;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.message.dao.AnnouncementDao;
import com.hisoft.hscloud.message.entity.Announcement;
import com.hisoft.hscloud.message.service.AnnouncementService;
import com.hisoft.hscloud.message.vo.AnnouncementVO;
import com.hisoft.hscloud.crm.usermanager.dao.DomainDao;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService{
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    private ExceptionServiceUtil eService = new ExceptionServiceUtil(logger);
    
    @Autowired
    private AnnouncementDao announcementDao;
    @Autowired
    private DomainDao domainDao;
    
    @Override
    public void saveAnnouncement(AnnouncementVO announcementVO, long adminId) {
        logger.debug("saveAnnouncement start");
        Announcement announcement =null;
        String[] plantformIdList=null;
        StringBuffer domainIds=new StringBuffer("");
        List<Long> listDomainId=new ArrayList<Long>();
        String plantformAll = announcementVO.getPlantformId().trim();
        if(plantformAll.equals("all")){
    		List<Domain> list =domainDao.getAllDomain();
    		for(Domain domain: list) {
        		listDomainId.add(domain.getId());
        	}
        }else{
        	plantformIdList = announcementVO.getPlantformId().split(",");
        	for(String str:plantformIdList){
        		listDomainId.add(Long.valueOf(str.trim()));
        	}
        }
        for( Long id: listDomainId){
	        if(announcementVO.getId() != 0L) {
	            announcement = announcementDao.getAnnouncement(announcementVO.getId());
	        }else{
	        	announcement= new Announcement();
	        }
	        domainIds.append(id).append(",");
        }
        try {
            BeanUtils.copyProperties(announcement, announcementVO);
        } catch (IllegalAccessException e) {
            eService.setMessage("announcementVO转换announcement异常");
            eService.throwException(Constants.ANNOUNCEMENT_BEAN_EXCEPTION, e);
        } catch (InvocationTargetException e) {
            eService.setMessage("announcementVO转换announcement异常");
            eService.throwException(Constants.ANNOUNCEMENT_BEAN_EXCEPTION, e);
        }
        
        announcement.setUpdateId(adminId);
        if(announcement.getId() == 0l) {
            announcement.setCreateId(adminId);
            announcement.setCreateTime(new Date());
        } else {
            announcement.setUpdateTime(new Date());
        }
        logger.debug("saveAnnouncement end");
//        return announcementDao.saveAnnouncement(announcement);S domainId=id;
        	announcement.setDomainIds(domainIds.toString());
        	announcementDao.saveAnnouncement(announcement);
        	
    }
    
    @Override
    public List<Announcement> findAnnouncementByConditoin(int counter, int type, Long domainId) {
        return announcementDao.findAnnouncementByConditoin(counter, type, domainId);
    }

    @Override
    public Page<Map<String, Object>> findAnnouncementPage(
            Page<Map<String, Object>> announcementPage, String query) {
        return announcementDao.findAnnouncementPage(announcementPage, query);
    }

    @Override
    public void delAnnouncement(long announcementId) {
        announcementDao.delAnnouncement(announcementId);
    }
}
