/**
 * @title OrderAction.java
 * @package com.hisoft.hscloud.bss.sla.om.action
 * @description 订单Action
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.util.Utils;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.bss.sla.om.vo.TryOrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionContext;

/**
 * @description 订单Action
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
public class OrderAction extends HSCloudAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(OrderAction.class);
	private Page<Order> pageOrder = new Page<Order>(Constants.PAGE_NUM);
	private Order order;
	private int page;
	private int start;
	private int limit;
	private String vmName;
	private String vmId;
	private String ip;
	private String refundRemark;
	private String query;
	private String sort;
	private long id;// 订单记录id
	private String orderStatus;// 订单状态
	private long orderItemId;//
	private int delayDays;// 延期天数
	private String type;
	private long referenceId;
	private String orderItemIds;
	private Long domainId;
	private short orderType;
	@Autowired
	private Facade facade;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Map<String, Object> getMapSession() {

		return ActionContext.getContext().getSession();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getRefundRemark() {
		return refundRemark;
	}

	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(int delayDays) {
		this.delayDays = delayDays;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getOrderItemIds() {
		return orderItemIds;
	}

	public void setOrderItemIds(String orderItemIds) {
		this.orderItemIds = orderItemIds;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * 订单详情 包含按套餐购买和按需购买订单
	 */
	public void orderDetail(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter orderDetail method.");			
		}
		try {
			fillActionResult(facade.orderDetail(order.getId(),orderType));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"orderDetail exception", logger, e),Constants.ORDERITEM_DETAIL_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit orderDetail method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 
	 * @title: page
	 * @description 分页
	 * @return void 返回类型
	 * @throws
	 * @throws Exception
	 */
	public void page() throws Exception {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter page method.");			
		}
		try {
			Admin admin = (Admin)super.getCurrentLoginUser();
			if(admin!=null){
				pageOrder = new Page<Order>(limit);
				pageOrder.setPageNo(page);
				if(StringUtils.isNotBlank(query)){
					query=new String(query.getBytes("iSO8859_1"),"UTF-8");
				}
				System.out.println("+++++++>>>>>>" + sort);
				pageOrder = facade.getOrderByPage(pageOrder, order,
						 parseSort(),query,admin,domainId);
				fillActionResult(pageOrder);
			}
			
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getOrder By page exception",
					logger, e), Constants.ORDER_PAGING_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit page method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 
	 * @title: queryAllOrderItemsByOrder
	 * @description 订单项详情
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-3-29 上午9:21:43
	 */
	public void queryAllOrderItemsByOrder() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter queryAllOrderItemsByOrder method.");			
		}
		try {
			fillActionResult(facade.getAllOrderItemsByOrder(order.getId()));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"queryAllOrderItemsByOrder exception", logger, e),
					Constants.ORDERITEM_DETAIL_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit queryAllOrderItemsByOrder method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * @title: getOrderByOrderNo
	 * @description 通过查询hc_order表的orderNo获得Order
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author liyunhui
	 * @update 2013-6-20
	 */
	public void getOrderByOrderNo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getOrderByOrderNo method.");			
		}
		try {
			fillActionResult(facade.getOrderByOrderNo(order.getOrderNo()));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getOrderByOrderNo exception", logger, e),
					Constants.ORDERITEM_DETAIL_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getOrderByOrderNo method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 
	 * @title: cancel
	 * @description 取消
	 * @return void 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-3-29 上午9:22:40
	 */
	public void cancel() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter cancel method.");			
		}
		Admin admin=null;
		try {
			admin = (Admin) super.getCurrentLoginUser();
			facade.cancelOrder(order.getId());
			facade.insertOperationLog(admin,"后台取消订单","后台取消订单",Constants.RESULT_SUCESS);
		} catch (Exception e) {
			facade.insertOperationLog(admin,"后台取消订单错误:"+e.getMessage(),"后台取消订单",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "cancelOrder exception", logger,
					e), Constants.ORDER_CANCEL_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancel method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * @title: parseSort
	 * @description 解析排序对象
	 * @return
	 * @throws Exception
	 *             设定文件
	 * @return List<Sort> 返回类型
	 * @throws
	 * @version 1.0
	 * @author MaDai
	 * @update 2012-4-16 下午2:04:06
	 */
	private List<Sort> parseSort() throws Exception {
		return Utils.json2Object(sort, new TypeReference<List<Sort>>() {
		});
	}
	/**
	 * <延期审核> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public String delayAudit() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delayAudit method.");			
		}
		boolean result = false;
		try {
			Admin admin = (Admin) super.getCurrentLoginUser();
			result = facade.delayAudit(id, delayDays, orderStatus, admin);
		} catch (Exception ex) {
			dealThrow(new HsCloudException("", "delayAudit异常", logger, ex),
					Constants.DELAYAUDIT_ERROR, true);
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delayAudit method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
    /**
     * <查看管理员类型 用于限制大客户没有退款权限> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
	public void checkAdminType(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkAdminType method.");			
		}
		boolean result=true;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin.getType()!=null&&admin.getType().intValue()==2){
			result=false;
		}
		super.getActionResult().setResultObject(result);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkAdminType method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <获取OrderItemVMVo List 用于退款展示> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getOrderItemVmList() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getOrderItemVmList method.");			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<OrderItemVMVo> result = facade.getOrderItemVMByOrderId(id);
			map.put("totalCount", result.size());
			map.put("result", result);
			super.fillActionResult(map);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "getOrderItemVmListy异常", logger,
					e), Constants.ORDERITEM_VM_LIST_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getOrderItemVmList method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <vm退款> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void vmRefund() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter vmRefund method.");			
		}
		boolean result = false;
		Admin admin = null;
		try {
		    admin = (Admin) super.getCurrentLoginUser();
			if (admin != null && (admin.getType()==null||admin.getType()!=2)) {
				result = facade.vmRefund(referenceId, vmId, admin, ip,refundRemark);
				facade.insertOperationLog(admin,"虚拟机部分退款","虚拟机部分退款",Constants.RESULT_SUCESS);
			}
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"虚拟机部分退款错误："+ex.getMessage(),"虚拟机部分退款",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "vm refund异常", logger, ex),
					Constants.VM_REFUND_ERROR, true);
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit vmRefund method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <vm全额退款> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void vmRefundAll() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter vmRefundAll method.");			
		}
		boolean result = false;
		Admin admin=null;
		try {
			admin= (Admin) super.getCurrentLoginUser();
			if (admin != null && (admin.getType()==null||admin.getType()!=2)) {
				result = facade.vmRefundAll(referenceId, vmId, admin, ip,refundRemark);
				facade.insertOperationLog(admin,"虚拟机全额退款","虚拟机全额退款",Constants.RESULT_SUCESS);
			}
		} catch (Exception ex) {
			facade.insertOperationLog(admin,"虚拟机全额退款错误："+ex.getMessage(),"虚拟机全额退款",Constants.RESULT_FAILURE);
			dealThrow(new HsCloudException("", "Vm refund all异常", logger, ex),
					Constants.VM_REFUND_ALL_ERROR, true);
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit vmRefundAll method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取vm关联的已支付的可退款的订单> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getVMRelatedRefundOrderInfo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVMRelatedRefundOrderInfo method.");			
		}
		try {
			List<VmRefundOrderItemVo> result = new ArrayList<VmRefundOrderItemVo>();
			result = facade.getVmRefundOrderItemVo(referenceId);
			super.fillActionResult(result);
		} catch (Exception ex) {
			dealThrow(new HsCloudException("", "getVMRelatedRefundOrderInfo异常",
					logger, ex),
					Constants.GET_VM_RELATED_REFUNDABLE_ORDER_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVMRelatedRefundOrderInfo method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取vm关联的已支付的订单信息> <功能详细描述>
	 * @throws HsCloudException
	 * @see [类、类#方法、类#成员]
	 */
	public void getVmRelatedPaidOrder(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getVmRelatedPaidOrder method.");			
		}
		try {
			List<OrderVo> result = new ArrayList<OrderVo>();
			result = facade.getVmRelatedPaidOrder(referenceId);
			super.fillActionResult(result);
		} catch (Exception ex) {
			dealThrow(new HsCloudException("", "getVmRelatedPaidOrder异常",
					logger, ex),
					Constants.GET_VM_RELATED_PAID_ORDER_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getVmRelatedPaidOrder method.takeTime:" + takeTime + "ms");
		}
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public short getOrderType() {
		return orderType;
	}

	public void setOrderType(short orderType) {
		this.orderType = orderType;
	}
	
	
}
