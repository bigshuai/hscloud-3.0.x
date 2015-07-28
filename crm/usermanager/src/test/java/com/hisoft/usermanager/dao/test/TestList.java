package com.hisoft.usermanager.dao.test;

import java.util.ArrayList;
import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Permission;

public class TestList {
	
	public static void main(String[] agrs){
		
		
		List<Permission> list1 = new ArrayList<Permission>();
		Permission p1 = new Permission();
		p1.setId(1);
		p1.setActionId(1);
		p1.setResourceId(1);
		
		Permission p2 = new Permission();
		p2.setId(2);
		p2.setActionId(2);
		p2.setResourceId(2);
		
		list1.add(p1);
		list1.add(p2);
		List<Permission> list2 = new ArrayList<Permission>();
		Permission p3 = new Permission();
		p3.setId(2);
		p3.setActionId(2);
		p3.setResourceId(2);
		
		Permission p4 = new Permission();
		p4.setId(4);
		p4.setActionId(4);
		p4.setResourceId(4);
		Permission p5 = new Permission();
		p5.setId(5);
		p5.setActionId(5);
		p5.setResourceId(5);
		
		list2.add(p3);
		list2.add(p4);
		list2.add(p5);
		
//		list1.retainAll(list2);
		list2.removeAll(list1);
		list1.addAll(list2);
		
System.out.println();
		
	}

}
