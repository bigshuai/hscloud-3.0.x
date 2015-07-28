package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.constant.ResourceType;
import com.hisoft.hscloud.crm.usermanager.dao.MenuDao;
import com.hisoft.hscloud.crm.usermanager.entity.Menu;
import com.hisoft.hscloud.crm.usermanager.service.MenuService;
import com.hisoft.hscloud.crm.usermanager.vo.MenuVO;

@Service
public class MenuServiceImpl implements MenuService{
	
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> getMenuPermission(List<Long> pIds) {
		
		List<Menu> menus = menuDao.findByIds(pIds);
		return menus;
		
	}
	

    /** 
    *  
    */
    @Override
    public List<Menu> getMenuTree() {
        String hql = "from Menu a ORDER BY a.parentId  ,a.position";
        return menuDao.find(hql);
    }
    
    @Override
    public List<Menu> getMenuTree(long roleId) {
        String sql = "select * from hc_menu a, hc_resource b,  hc_permission c, hc_role_permission d where a.id = b.primKey and b.resourceType = '" + ResourceType.MENU.getEntityName() + "' and b.id = c.resource_id and c.id = d.permission_id and d.role_id = ?";
        return menuDao.getMenuTree(sql, roleId);
    }


	@Override
	public List<MenuVO> getAllMenu() {
		List<Menu> menus = new ArrayList<Menu>();
		menus =menuDao.getAll();
		List<MenuVO> menuVOs = this.setMenuVO(menus);
		return menuVOs;
	}
	
	private List<MenuVO> setMenuVO(List<Menu> menus){
		List<MenuVO> menuVOs = new ArrayList<MenuVO>();
		for (Menu menu : menus) {
			MenuVO menuVO = new MenuVO();
            try {
				BeanUtils.copyProperties(menuVO, menu);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			menuVOs.add(menuVO);
		}
		return menuVOs;
	}


	/*@Override
	public Menu getMenuByUrl(String url) {
		String hql = "from Menu m where m.name like :name";
		Map<String, String> values = new HashMap<String, String>();
		values.put("name", "%" + url + "%");
		List<Menu> list = menuDao.findByMap(hql, values);
		if(!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
*/

}
