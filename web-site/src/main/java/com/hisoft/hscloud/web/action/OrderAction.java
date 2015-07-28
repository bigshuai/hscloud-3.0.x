/**
 * @title OrderAction.java
 * @package com.hisoft.hscloud.bss.sla.om.action
 * @description 订单Action
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.web.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.Order;
import com.hisoft.hscloud.bss.sla.om.util.SubmitOrderData;
import com.hisoft.hscloud.bss.sla.om.util.Utils;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;
import com.hisoft.hscloud.bss.sla.om.vo.OrderVo;
import com.hisoft.hscloud.bss.sla.om.vo.QueryCondition;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.RabbitMQUtil;
import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.User;
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
	private long feeTypeId;
	private long referenceId;
	private int scId;
	private String query;
	private int id;
	private int osId;
	private String sort;
	private String dateFrom;
	private String dateTo;
	private String orderStatus;//订单状态
	private String couponAmount;
	private String giftAmount;
	private short orderType;
	private String vmId;
//	private int[] serviceItemIds;
//	private int vmNum;//购买虚拟机个数
//	private int ipNum;//购买IP个数
//	private String zoneGroupCode;//机房线路code
//	private int buyPeriod;
	private SubmitOrderData submitData;
	@Autowired
	private Facade facade;
	
	/**
	 * 取消用户全部为支付订单
	 * @throws Exception
	 */
	public void cancelUnpaidOrderByUser()throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter cancelUnpaidOrderByUser method.");			
		}
		User user=null;
		String operateObject = "Order[全部未支付订单]";
		try {
			user=queryCurrentUser();
			if(user!=null){
				facade.cancelUnpaidOrderByUser(user.getId());
			}
			facade.insertOperationLog(user,"取消全部未支付订单","取消全部未支付订单",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"取消全部未支付订单出现错误","取消全部未支付订单",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "cancelUnpaidOrderByUser exception",
					logger, e),Constants.CANCEL_ALL_UNPAID_ORDER_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancelUnpaidOrderByUser method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 升级
	 * @throws Exception
	 */
	public void upgradeVM()throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter upgradeVM method.");			
		}
		User user=null;
		String operateObject = "VM[vmId:" + vmId + "]";
		try {
			user=queryCurrentUser();
			if(user!=null){
				submitData.setUser(user);
				facade.upgradeVM(submitData, vmId);
			}
			facade.insertOperationLog(user,"升级云主机","升级云主机",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			String code=Constants.UPGADE_VM_ERROR;
			if(e.getClass()==new HsCloudException().getClass()){
				HsCloudException ex = (HsCloudException)e;
				if(Constants.UPGRADE_VM_BLANCE_NOT_ENOUGH.equals(ex.getCode())){
					code=Constants.UPGRADE_VM_BLANCE_NOT_ENOUGH;
				}
			}
			facade.insertOperationLog(user,"升级云主机出现错误","升级云主机",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "upgradeVM exception",
					logger, e),code,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit upgradeVM method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 按需购买接口
	 * @throws Exception
	 */
	public void submitOrder4Need()throws Exception{
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitOrder4Need method.");			
		}
		User user=null;
		String operateObject = "Order[submitData.toString():" + submitData.getZoneGroupId() + "]";
		try {
			user=queryCurrentUser();
			if(user!=null){
				submitData.setUser(user);
				facade.submitOrder4Need(submitData);
			}
			facade.insertOperationLog(user,"按需生成未支付订单","按需生成未支付订单",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"按需生成未支付订单出现错误","按需生成未支付订单",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "submitOrder4Need exception",
					logger, e),Constants.SUBMIT_ORDER_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitOrder4Need method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 路由按需购买接口
	 * @author liyunhui
	 * @throws Exception
	 */
	public void submitRouterOrder() throws Exception {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitRouterOrder method.");
		}
		User user = null;
		String operateObject = "Order[vpdcName:" + submitData.getVlanParams()[0] + "]";
		try {
			user = queryCurrentUser();
			if (user != null) {
				submitData.setUser(user);
				facade.submitRouterOrder(submitData);
			}
			facade.insertOperationLog(user, "按需生成未支付订单", "按需生成未支付订单", Constants.RESULT_SUCESS,operateObject);
		}
		catch (Exception e) {
			facade.insertOperationLog(user, "按需生成未支付订单出现错误", "按需生成未支付订单", Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "submitRouterOrder exception", logger, e), 
					Constants.SUBMIT_ORDER_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitRouterOrder method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <把通过VPDC添加云主机的订单项存入session> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public void addVMItem() throws Exception {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addVMItem method.");
		}
		try {
			User user = queryCurrentUser();
			submitData.setUser(user);
			List<SubmitOrderData> vmCart = (List<SubmitOrderData>) getMapSession().get("vmCart");
			if (vmCart == null) {
				vmCart = new LinkedList<SubmitOrderData>();
			}
			vmCart.add(submitData);
			getMapSession().put("vmCart", vmCart);
		}
		catch (Exception e) {
			dealThrow(new HsCloudException("", "addVMItem exception", logger, e), Constants.SAVE_CART_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addVMItem method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 提交通过VPDC添加云主机的订单
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void submitVMOrder() throws Exception {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitVMOrder method.");
		}
		User user = null;
		String operateObject = null;
		try {
			user = queryCurrentUser();
			if (user != null) {
				List<SubmitOrderData> vmCart = (List<SubmitOrderData>) getMapSession().get("vmCart");
				operateObject = "VM[vpdc_id:" + id + "]";
				facade.submitVMOrder(vmCart, id);//id是当前这些云主机所在的VPDC_ID
				getMapSession().remove("vmCart");
			}
			facade.insertOperationLog(user, "按需生成未支付订单", "按需生成未支付订单", Constants.RESULT_SUCESS,operateObject);
		}
		catch (Exception e) {
			facade.insertOperationLog(user, "按需生成未支付订单出现错误", "按需生成未支付订单", Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "submitVMOrder exception", logger, e), 
					Constants.SUBMIT_ORDER_ERROR, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitVMOrder method.takeTime:" + takeTime + "ms");
		}
	}	
	/**
	 * <分页获取订单信息> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	public void page() throws Exception {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter page method.");			
		}
		try {
			pageOrder = new Page<Order>(limit);
			pageOrder.setPageNo(page);
			if(StringUtils.isNotBlank(query)){
				query=new String(query.getBytes("iSO8859_1"),"UTF-8");
			}
			pageOrder = facade.getOrderByPage(pageOrder, order,
					queryCurrentUser(), parseSort(),query);
			fillActionResult(pageOrder);
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"getOrder By page exception", logger, e),Constants.ORDER_PAGING_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit page method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <根据订单号查询订单项> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
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
					"queryAllOrderItemsByOrder exception", logger, e),Constants.ORDERITEM_DETAIL_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit queryAllOrderItemsByOrder method.takeTime:" + takeTime + "ms");
		}
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
	 * <完成支付version2> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void payV2() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter payV2 method.");			
		}
		Map<NovaServerForCreate,HcEventResource> rabbitMQ = null;
		User user=null;
		String operateObject = "Order[orderId:" + order.getId() + "]";
		try {
			//long start=System.nanoTime();
		    user=queryCurrentUser();
		    if(isCouponFormat(couponAmount) == false) {
		        dealThrow(Constants.ORDER_COUPON_FORMAT_ERROR, new RuntimeException(), logger);
		        return;
		    }
		    if(isCouponFormat(giftAmount) == false) {
		        dealThrow(Constants.GIFT_FORMAT_ERROR, new RuntimeException(), logger);
		        return;
		    }
			Map<String,Object> resultMap = facade.orderPay(order.getId(),user,couponAmount,giftAmount);
			//long end=System.nanoTime()-start;
			String result="";
			//消息在事务提交后发送
			rabbitMQ = (Map<NovaServerForCreate,HcEventResource>)resultMap.get(Constants.ORDER_PAY_SUCCESS);
			RabbitMQUtil rabbitMqUtil = new RabbitMQUtil();
			//long startSend=System.nanoTime();
			if(rabbitMQ!=null){
				for(NovaServerForCreate nsfc:rabbitMQ.keySet()){
					HcEventResource her = rabbitMQ.get(nsfc);
					rabbitMqUtil.sendMessage(her.getMessage(), nsfc,her,"HcEventResource");
				}
				result=Constants.ORDER_PAY_SUCCESS;
			}
			//long endSend=System.nanoTime()-startSend;
			facade.insertOperationLog(user,"订单支付","订单支付",Constants.RESULT_SUCESS,operateObject);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			//释放【待分配的IP】
			if(rabbitMQ!=null){
				for(NovaServerForCreate nsfc:rabbitMQ.keySet()){
					facade.releaseIp(nsfc.getFloatingIp());
				}
			}
			String code=Constants.ORDER_PAY_ERROR;
			if(e.getClass()==new HsCloudException().getClass()){
				HsCloudException ex = (HsCloudException)e;
				if(Constants.VM_ZONE_NO_ENOUGH.equals(ex.getCode())){
					code=Constants.VM_ZONE_NO_ENOUGH;
				}
			}
			facade.insertOperationLog(user,"订单支付出现错误:"+e.getMessage(),"订单支付",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("",logger,e),code,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit payV2 method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <取消订单操作> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void cancel() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter cancel method.");			
		}
		User user=null;
		String operateObject = "Order[orderId:" + order.getId() + "]";
		try {
			user=queryCurrentUser();
			boolean result=facade.cancelOrder(order.getId());
			facade.insertOperationLog(user,"取消订单","取消订单",Constants.RESULT_SUCESS,operateObject);
			super.getActionResult().setResultObject(result);
		} catch (Exception e) {
			facade.insertOperationLog(user,"取消订单错误","取消订单",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "cancelOrder exception",
					logger, e),Constants.ORDER_CANCEL_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit cancel method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <根据套餐Id查询套餐信息> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void queryServiceCatalog() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter queryServiceCatalog method.");			
		}
		logger.info("queryServiceCatalog start");
		try {
			fillActionResult(facade.getSCById(id));
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"queryServiceCatalog exception", logger, e),Constants.ORDER_QUERY_SC_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit queryServiceCatalog method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <把缓存在cookie中的订单项存入session> 
	* <功能详细描述> 
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public void addItem() throws Exception {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addItem method.");			
		}
		try {
			OrderItemVo orderItemVo = facade.addItem(scId, osId, feeTypeId);
			List<OrderItemVo> carts = (List<OrderItemVo>) getMapSession().get(
					"cart");

			if (carts == null) {
				carts = new LinkedList<OrderItemVo>();
			}

			carts.add(orderItemVo);
			getMapSession().put("cart", carts);
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "addItem exception",
					logger, e),Constants.SAVE_CART_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addItem method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <把session中的订单项信息存入database> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public void submitOrder(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitOrder method.");			
		}
		User user=null;
		String operateObject = null;
		try {
			user=queryCurrentUser();
			operateObject = "User[userId:" + user.getId() + "]";
			List<OrderItemVo> carts = (List<OrderItemVo>) getMapSession().get("cart");
			getMapSession().remove("cart");
			if(user!=null){
				fillActionResult(facade.submitOrder(carts,user));
			}
			facade.insertOperationLog(user,"生成未支付订单","生成未支付订单",Constants.RESULT_SUCESS,operateObject);
		} catch (Exception e) {
			facade.insertOperationLog(user,"生成未支付订单出现错误","生成未支付订单",Constants.RESULT_FAILURE,operateObject);
			dealThrow(new HsCloudException("", "submitOrder exception",
					logger, e),Constants.SUBMIT_ORDER_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitOrder method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * <提交试用云主机> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void submitTryVm(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter submitTryVm method.");			
		}
		try {
			User user=queryCurrentUser();
			if(user!=null){
				facade.submitTryVm(scId,osId,user);
			}
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "submitTryVm exception",
					logger, e), Constants.SUBMIT_TRY_VM_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit submitTryVm method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <解析排序json字符串为json对象> 
	* <功能详细描述> 
	* @return
	* @throws Exception 
	* @see [类、类#方法、类#成员]
	 */
	private List<Sort> parseSort() throws Exception {
		if(sort==null){
			return new ArrayList<Sort>();
		}
		return Utils.json2Object(sort, new TypeReference<List<Sort>>() {
		});
	}

	/**
	 * <获取当前的登陆用户> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	private User queryCurrentUser()throws Exception {
		// User user = (User)super.getCurrentLoginUser();
		Object obj = super.getCurrentLoginUser();
			if (obj != null&&(obj instanceof User)) {
				User user = (User) obj;
				return user;
			} else {
				throw new Exception("queryCurrentUser exception");
			}
	}
	
	private QueryCondition initQueryCondition()throws Exception{
		QueryCondition condition = new QueryCondition();
		Date dateFromObj=null;
		Date dateToObj=null;
		DateFormat df = null;
		if (StringUtils.isNotBlank(dateFrom)) {
			if(dateFrom.matches("\\d{2}/\\d{2}/\\d{4}")){
				df=new SimpleDateFormat("MM/dd/yyyy");
			}else if(dateFrom.matches("\\d{4}/\\d{2}/\\d{2}")){
				df=new SimpleDateFormat("yyyy/MM/dd");
			}
			dateFromObj=df.parse(dateFrom);
		}
		if (StringUtils.isNotBlank(dateTo)) {
			if(dateTo.matches("\\d{2}/\\d{2}/\\d{4}")){
				df=new SimpleDateFormat("MM/dd/yyyy");
			}else if(dateTo.matches("\\d{4}/\\d{2}/\\d{2}")){
				df=new SimpleDateFormat("yyyy/MM/dd");
			}
			Date tempDate = df.parse(dateTo);
			tempDate.setTime(tempDate.getTime() + 1000 * 60 * 60 * 24 - 1);
			dateToObj=tempDate;
		}
		condition.setFromDate(dateFromObj);
		condition.setToDate(dateToObj);
		condition.setUser(queryCurrentUser());
		condition.setSort(parseSort());
		condition.setOrder(order);
		return condition;
	}

	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void queryOrderByDate() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter queryOrderByDate method.");			
		}
		try {
			QueryCondition condition=initQueryCondition();
			pageOrder = new Page<Order>(limit);
			pageOrder.setPageNo(page);
			condition.setPages(pageOrder);
			facade.queryOrder(condition);
			fillActionResult(condition.getPages());
		} catch (Exception e) {
			dealThrow(new HsCloudException("",
					"queryOrderByDate exception", logger, e),Constants.QUERY_ORDER_BY_CONDITION_ERROR,true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit queryOrderByDate method.takeTime:" + takeTime + "ms");
		}
	}	

	/**
	 * <转正申请> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String regularApply(){		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter regularApply method.");			
		}
		boolean result = false;
		try{
			result = facade.regularApply(id, orderStatus);
		}catch (Exception ex) {
			dealThrow(new HsCloudException("",
					"regularApply异常", logger, ex),Constants.REGULAR_APPLY_ERROR,true);
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {			
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit regularApply method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	/**
	 * <延期申请> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public String delayApply(){		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delayApply method.");			
		}
		boolean result = false;
		try{
			result = facade.delayApply(id, orderStatus);
		}catch (Exception ex) {
			dealThrow(new HsCloudException("",
					"delayApply异常", logger, ex),Constants.DELAY_APPLY_ERROR,true);
			return null;
		}
		if (result) {
			super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} else {			
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delayApply method.takeTime:" + takeTime + "ms");
		}
		return null;
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
    
	//是否符合返点格式
    private static boolean isCouponFormat(String str) {  
        String regex = "^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$";
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(str);  
        return matcher.matches();  
    }  
	
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

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public long getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(long feeTypeId) {
		this.feeTypeId = feeTypeId;
	}

	public int getScId() {
		return scId;
	}

	public void setScId(int scId) {
		this.scId = scId;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }
	public SubmitOrderData getSubmitData() {
		return submitData;
	}
	public void setSubmitData(SubmitOrderData submitData) {
		this.submitData = submitData;
	}
	public short getOrderType() {
		return orderType;
	}
	public void setOrderType(short orderType) {
		this.orderType = orderType;
	}
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	public String getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(String giftAmount) {
		this.giftAmount = giftAmount;
	}
}
