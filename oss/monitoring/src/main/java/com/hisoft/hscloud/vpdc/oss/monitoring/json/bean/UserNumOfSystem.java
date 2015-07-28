package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;

public class UserNumOfSystem {
	private int commonUserNum;
	private int enterpriseUserNum;
	/**    
	 * commonUserNum    
	 *    
	 * @return  the commonUserNum    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public int getCommonUserNum() {
		return commonUserNum;
	}
	/**    
	 * @param commonUserNum the commonUserNum to set    
	 */
	
	public void setCommonUserNum(int commonUserNum) {
		this.commonUserNum = commonUserNum;
	}
	/**    
	 * enterpriseUserNum    
	 *    
	 * @return  the enterpriseUserNum    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public int getEnterpriseUserNum() {
		return enterpriseUserNum;
	}
	/**    
	 * @param enterpriseUserNum the enterpriseUserNum to set    
	 */
	
	public void setEnterpriseUserNum(int enterpriseUserNum) {
		this.enterpriseUserNum = enterpriseUserNum;
	}
	   
	       /* (non-Javadoc)    
	        * @see java.lang.Object#toString()    
	        */    
	       
	@Override
	public String toString() {
		return "UserNumOfSystem [commonUserNum=" + commonUserNum
				+ ", enterpriseUserNum=" + enterpriseUserNum + "]";
	}
	
}
