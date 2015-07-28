package com.hisoft.hscloud.crm.usermanager.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.crm.usermanager.dao.MenuDao;
import com.hisoft.hscloud.crm.usermanager.entity.Menu;

@Repository
public class MenuDaoImpl extends HibernateDao<Menu, Long> implements MenuDao {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Menu> find(String hql, Object... values){
		
     try{
    	 
		if(logger.isDebugEnabled()){
			logger.debug("enter MenuDaoImpl find method.");
			logger.debug("hql:"+hql);
			logger.debug("values:"+values);
		}
		List<Menu>  menus = super.find(hql, values);
		if(logger.isDebugEnabled()){
			logger.debug("exit MenuDaoImpl find method");
		}
		return menus;
		
	   } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	   }
		
	}
	
	@Override
	public List<Menu> findByIds(List<Long> ids){
		
		try{
		
			if(logger.isDebugEnabled()){
				logger.debug("enter MenuDaoImpl findByIds method.");
				logger.debug("ids:"+ids);
			}
			List<Menu>  menus = super.findByIds(ids);
			if(logger.isDebugEnabled()){
				logger.debug("exit MenuDaoImpl find method");
			}
			return menus;
			
	    } catch (Exception e) {
	 	    throw new HsCloudException(Constants.DATEBASE_ACCESS_ERROR,e.getMessage(),logger, e);
	    }
		
	}
	
	@Override
	public List<Menu>  getAll(){
		return (List<Menu>)super.getAll();
	}

    /** 
    * @param roleId
    * @return 
    */
    @Override
    public List<Menu> getMenuTree(String sql, long roleId) {
        SQLQuery query = getSession().createSQLQuery(sql);
        /*for(int i = 0;i<objects.length;i++){
            query.setParameter(i, objects[i]);
        }*/
        query.setParameter(0, roleId);
        query.addEntity(Menu.class);
        return query.list();
    }
    
    @Override
	public List<Menu> findByMap(String hql, Map<String, String> map){
		return super.find(hql, map);
	}
    

}
