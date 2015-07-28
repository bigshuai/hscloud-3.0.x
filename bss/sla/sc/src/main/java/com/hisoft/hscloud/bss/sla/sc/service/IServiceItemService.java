/**
 * @title IServiceItemService.java
 * @package com.hisoft.hscloud.bss.sla.sc.service
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-4 上午11:30:56
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.sc.service;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceType;
import com.hisoft.hscloud.bss.sla.sc.vo.OAZoneGroupVO;
import com.hisoft.hscloud.bss.sla.sc.vo.ServiceItemVo;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-4 上午11:30:56
 */
public interface IServiceItemService {

	/**
	 * @title: pageServiceItem
	 * @description 用一句话说明这个方法做什么
	 * @param serviceType
	 * @return 设定文件
	 * @return List 返回类型
	 * @throws
	 * @version 1.0
	 * @author jiaquan.hu
	 * @update 2012-5-7 上午9:42:41
	 */
	public List<ServiceItem> listServiceItem(int serviceType,List<Sort> sorts);
	/**
	 * <获取操作系统资源列表> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public List<ServiceItem> listOSItem(int serviceType, List<Sort> sortList, String family);

	public List<ServiceItem> listServiceItem(int serviceType, List<Sort> sorts,
			Map<String, Object> map);
	/**
	 * @title: save
	 * @description 用一句话说明这个方法做什么
	 * @param serviceItem
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author jiaquan.hu
	 * @update 2012-5-7 下午1:52:15
	 */
	public int save(ServiceItem serviceItem);
	/**
	 * <修改时使用，用于保留一些属性不被修改> 
	* <功能详细描述> 
	* @param serviceItemVo
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public int saveSIVo(ServiceItemVo serviceItemVo)throws HsCloudException;

	/**
	 * @title: getServiceType
	 * @description 用一句话说明这个方法做什么 设定文件
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author jiaquan.hu
	 * @update 2012-5-7 下午5:09:05
	 */
	public Map<Integer, ServiceType> getServiceType();

	/**
	 * @title: getAllServiceItems
	 * @description 用一句话说明这个方法做什么
	 * @return 设定文件
	 * @return List<ServiceItem> 返回类型
	 * @throws
	 * @version 1.0
	 * @author jiaquan.hu
	 * @update 2012-5-9 下午4:33:57
	 */
	public List<ServiceItem> getAllServiceItems();

	public Page<ServiceItem> pageServiceItemByType(Page<ServiceItem> paging, int serviceType, 
			List<Sort> sorts, String query);

	public boolean isUsed(int id);

	public void delete(int id);

	public boolean isExist(int serviceType, Map<String, Object> map);

	public List<Object> getNetworkType();

	public List<ServiceItem> getServiceItemByProperty(int serviceType, Map<String, Object> map);

	public boolean checkServiceItemRepeat(String itemName, int serviceType);

	public ServiceItem getSIById(int id) throws HsCloudException;

	public Os getOs(int osId);
	
	public ServiceItem getSIBySize(int size, String category) throws HsCloudException;
	
	public List<OAZoneGroupVO> listServiceItemByZoneGroup(int serviceType,int zoneGroupId,List<Sort> sorts);
}