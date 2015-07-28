package com.hisoft.hscloud.crm.usermanager.vo;

/**
 * Created with IntelliJ IDEA.
 * User: roger
 * Date: 12-6-18
 * Time: 下午10:35
 * To change this template use File | Settings | File Templates.
 */
public class RoleMenuVO {
    private  int id;
    private  long  roleId;
    private  long  menuId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }
}

