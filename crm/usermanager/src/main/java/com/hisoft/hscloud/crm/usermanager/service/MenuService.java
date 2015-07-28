package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Menu;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;

public interface MenuService {
	
	public List<Menu> getMenuPermission(List<Long> pIds);
	
	public List<Menu> getMenuTree();
	

    /** <一句话功能简述> 
    * <功能详细描述> 
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员] 
    */
    List<Menu> getMenuTree(long roleId);
    
    List<MenuVO> getAllMenu();

}
