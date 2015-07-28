/**
 * 
 */
package com.hisoft.hscloud.bss.sla.sc.utils;

/**
 * @author houyh
 * 
 */
public enum ProductType {
	CPU(1, "CPU"), MEM(2, "MEM"),
	// 1,cpu 2,ram 3,disk 4,os 5,network 6,software 7,commonService 8,extDisk
	// 9,ip
	DISK(3, "DISK"), OS(4, "OS"), NETWORK(5, "NETWORK"), EXTDISK(8, "EXTDISK"), IP(
			9, "IP"),UNKNOWN(0,"UNKNOWN_TYPE");

	private int index;
	private String name;

	ProductType(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static ProductType getProductTypeByIndex(int index){
		for(ProductType pt:values()){
			if(pt.getIndex()==index){
				return pt;
			}
		}
		
		return ProductType.UNKNOWN;
	}

}
