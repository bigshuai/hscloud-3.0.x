package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name = "hc_monitoring_record")
public class MonitorRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "name", length = 128)
	private String name;
 
	@Column(name = "resourceAction")
	private String resourceAction;
	
	@Column(name = "operate_time")
	private Date operate_time;
	
	@Column(name = "resource_id")
	private String resource_id;
	
	@Column(name = "order_item_id")
	private int order_item_id;

	/**    
	 * id    
	 *    
	 * @return  the id    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public int getId() {
		return id;
	}

	/**    
	 * @param id the id to set    
	 */
	
	public void setId(int id) {
		this.id = id;
	}

	/**    
	 * name    
	 *    
	 * @return  the name    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public String getName() {
		return name;
	}

	/**    
	 * @param name the name to set    
	 */
	
	public void setName(String name) {
		this.name = name;
	}

	/**    
	 * resourceAction    
	 *    
	 * @return  the resourceAction    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public String getResourceAction() {
		return resourceAction;
	}

	/**    
	 * @param resourceAction the resourceAction to set    
	 */
	
	public void setResourceAction(String resourceAction) {
		this.resourceAction = resourceAction;
	}

	/**    
	 * operate_time    
	 *    
	 * @return  the operate_time    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public Date getOperate_time() {
		return operate_time;
	}

	/**    
	 * @param operate_time the operate_time to set    
	 */
	
	public void setOperate_time(Date operate_time) {
		this.operate_time = operate_time;
	}

	/**    
	 * resource_id    
	 *    
	 * @return  the resource_id    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public String getResource_id() {
		return resource_id;
	}

	/**    
	 * @param resource_id the resource_id to set    
	 */
	
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	/**    
	 * order_item_id    
	 *    
	 * @return  the order_item_id    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public int getOrder_item_id() {
		return order_item_id;
	}

	/**    
	 * @param order_item_id the order_item_id to set    
	 */
	
	public void setOrder_item_id(int order_item_id) {
		this.order_item_id = order_item_id;
	}

	   
	       /* (non-Javadoc)    
	        * @see java.lang.Object#toString()    
	        */    
	       
	@Override
	public String toString() {
		return "MonitorRecord [id=" + id + ", name=" + name
				+ ", resourceAction=" + resourceAction + ", operate_time="
				+ operate_time + ", resource_id=" + resource_id
				+ ", order_item_id=" + order_item_id + "]";
	}
}
