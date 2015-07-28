package com.pactera.hscloud.hscloudhandler.eventhandler;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.Constants;
import com.pactera.hscloud.common4j.commonhandler.CommonHandler;
import com.pactera.hscloud.common4j.util.DBUtil;
import com.pactera.hscloud.common4j.util.InvokeMethodUtil;
import com.pactera.hscloud.hscloudhandler.adapter.HsCloudAdapter;
import com.pactera.hscloud.hscloudhandler.bo.APITaskBean;

public class HSCloudEventHandler extends CommonHandler {

	private static Log logger = LogFactory.getLog(CommonHandler.class);

	public HSCloudEventHandler(String job, int threads) {
		super(job, threads);
	}

	@Override
	public void Handler(Object... params) {

		final String message = (String) params[0];
		logger.info("receive the event of HSCloud,begin to handle message:"
				+ message);
		Runnable run = new Runnable() {
			public void run() {
				Date deal_time = new Date();
				Long jobId = 0L;
				Object returnValue = null;
				StringBuilder errorInfo = new StringBuilder();
				Short resultInfo = new Short((short)0);
				Object resourceLogObject = null;
				Object opsLogObject = null;
				String jobType="";
				// 获取线程许 可
				try {
					semp.acquire();
					// 处理message，调用Restful Api
					// 更新日志信息
					JSONObject messageObj = JSONObject.fromObject(message);
					if (messageObj.size() != 3) {
						throw new Exception(
								"The message convert to map but map size not equal 3.");
					}
					String methodName = "";
					Object methodNameObj = messageObj
							.get(Constants.RABBITMQ_METHOD_NAME);
					if (methodNameObj != null) {
						methodName = (String) methodNameObj;
					}
					resourceLogObject = messageObj
							.get(Constants.RABBITMQ_RESOURCELOG);
					opsLogObject = messageObj.get(Constants.RABBITMQ_OPSLOG);
					if (resourceLogObject != null && opsLogObject == null) {
						jobId = newResourceLog(resourceLogObject, deal_time,
								message);
						jobType=Constants.JOB_TYPE_RESOURCE;
					} else if (opsLogObject != null
							&& resourceLogObject == null) {
						jobId = newVmOpsLog(opsLogObject, deal_time,
								message);
						jobType=Constants.JOB_TYPE_OPS;
					}
					Object[] args = new Object[3];
					Object paramObject = messageObj
							.get(Constants.RABBITMQ_PARAM);
					if (paramObject != null) {
						if (paramObject instanceof net.sf.ezmorph.bean.MorphDynaBean) {
							JSONObject paramJsonObject = JSONObject
									.fromObject(paramObject);
							args[0] = paramJsonObject;
						} else {
							args[0] = paramObject;
						}
					}
					args[1] = jobId;
					args[2] = jobType;
					returnValue = InvokeMethodUtil.invokeMethod(
							new HsCloudAdapter(), methodName, args);
				} catch (Exception e) {
					errorInfo.append(e);
					logger.error(e.getMessage(), e);
				} finally {
					Date finish_time = new Date();
					try {
						if (returnValue != null) {
							resultInfo = new Short(returnValue.toString());
						}
					} catch (Exception e) {
						errorInfo.append(e);
						logger.error(e.getMessage(),e);
					}
					if (resourceLogObject != null && opsLogObject == null) {
						try {
							updateResourceLog(jobId, finish_time, errorInfo.toString(),
									resultInfo);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					} else if (opsLogObject != null
							&& resourceLogObject == null) {
						try {
							updateVmOpsLog(jobId, finish_time, errorInfo.toString(),
									resultInfo);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
				// 处理完成后，释放线程许可
				semp.release();
			}
		};
		pool.execute(run);
	}

	private Long newResourceLog(Object resourceLogObject, Date dealTime,
			String message) throws Exception {
		JSONObject logJSONObj = JSONObject.fromObject(resourceLogObject);
		HcEventResource eventResource = (HcEventResource) JSONObject.toBean(
				logJSONObj, HcEventResource.class);
		eventResource.setDeal_time(dealTime);
		eventResource.setMessage(message);
		eventResource.setJob_server(this.getJob_name());
		Long job_id=DBUtil.save(eventResource, "eventResource.new");
		Long task_id=eventResource.getTask_id();
		try{
			if(task_id!=null&&task_id.longValue()!=0){
				APITaskBean apiTask=new APITaskBean();
				apiTask.setId(task_id);
				apiTask.setJob_id(job_id);
				apiTask.setJob_type("RESOURCE");
				DBUtil.save(apiTask, "api_task.update");
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return job_id;
	}

	private Long newVmOpsLog(Object vmOpsLogObject, Date dealTime,
			String message) throws Exception {
		JSONObject logJSONObj = JSONObject.fromObject(vmOpsLogObject);
		HcEventVmOps eventVmOps = (HcEventVmOps) JSONObject.toBean(logJSONObj,
				HcEventVmOps.class);
		eventVmOps.setDeal_time(dealTime);
		eventVmOps.setMessage(message);
		eventVmOps.setJob_server(this.getJob_name());
		Long job_id=DBUtil.save(eventVmOps, "eventVmOps.new");
		Long task_id=eventVmOps.getTask_id();
		try{
			if(task_id!=null&&task_id.longValue()!=0){
				APITaskBean apiTask=new APITaskBean();
				apiTask.setId(task_id);
				apiTask.setJob_id(job_id);
				apiTask.setJob_type("OPS");
				DBUtil.save(apiTask, "api_task.update");
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return job_id;
	}

	private void updateResourceLog(Long id, Date finish_time,
			String error_info, Short result) throws Exception {
		HcEventResource resourceLog = new HcEventResource();
		resourceLog.setId(id);
		resourceLog.setFinish_time(finish_time);
		if(StringUtils.isNotBlank(error_info)){
			resourceLog.setError_info(error_info);
			DBUtil.save(resourceLog, "eventResource.update");
		}else{
			DBUtil.save(resourceLog, "eventResource_noError.update");
		}
	}

	private void updateVmOpsLog(Long id, Date finish_time, String error_info,
			Short result) throws Exception {
		HcEventVmOps vmOpsLog = new HcEventVmOps();
		vmOpsLog.setId(id);
		vmOpsLog.setError_info(error_info);
		vmOpsLog.setFinish_time(finish_time);
		vmOpsLog.setResult(result);
		DBUtil.save(vmOpsLog, "eventVmOps.update");
	}

}
