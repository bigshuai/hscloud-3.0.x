package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.ActionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Action;

@Repository
public class ActionDaoImpl extends HibernateDao<Action, Long> implements ActionDao{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Action> find(String hql, Object... values){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter ActionDaoImpl find method.");
				logger.debug("hql:"+hql);
				logger.debug("values:"+values);
			}
			List<Action> actions = super.find(hql, values);
			if(logger.isDebugEnabled()){
				logger.debug("exit ActionDaoImpl find method");
			}
			return actions;
		
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public List<Action> findByIds(List<Long> ids){
		
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("enter ActionDaoImpl findByIds method");
				logger.debug("ids:"+ids);
			}
			List<Action> actions = (List<Action>)super.findByIds(ids);
			if(logger.isDebugEnabled()){
				logger.debug("exit ActionDaoImpl findByIds method");
			}
			return actions;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

	@Override
	public Action getActionByClassKey(String classKey) {
		
		try{
			
			String hql=" from Action a where a.classKey=?";
			if(logger.isDebugEnabled()){
				logger.debug("enter ActionDaoImpl getActionByClassKey method");
				logger.debug("classKey:"+classKey);
				logger.debug("hql:"+hql);
			}
			
			Action action = findUnique(hql,classKey);
			
			if(logger.isDebugEnabled()){
				logger.debug("exit ActionDaoImpl getActionByClassKey method");
			}
			return action;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}

}
