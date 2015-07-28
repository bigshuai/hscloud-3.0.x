/**
 * @title AdminHostVO.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-18 下午9:54:01
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-18 下午9:54:01
 */
public class AdminHostVO {

		private String id;
		private String name;
		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return the name
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
		/* (非 Javadoc) 
		 * <p>Title: toString</p> 
		 * <p>Description: </p> 
		 * @return 
		 * @see java.lang.Object#toString() 
		 */
		@Override
		public String toString() {
			return "AdminHostVO [id=" + id + ", name=" + name + "]";
		}
		

}
