package com.pactera.hscloud.openstackhandler.eventhandler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.pactera.hscloud.common4j.commonhandler.CommonHandler;
import com.pactera.hscloud.common4j.util.CommonThreadLocal;
import com.pactera.hscloud.common4j.util.InvokeMethodUtil;
import com.pactera.hscloud.openstackhandler.adapter.OpenstackAdapter;
import com.pactera.hscloud.openstackhandler.bo.SyncEvent;
import com.pactera.hscloud.openstackhandler.exception.OpenstackHandlerException;
import com.pactera.hscloud.openstackhandler.util.MessageDiscerner;

public class OpenstackEventHandler extends CommonHandler {

	private static Log logger = LogFactory.getLog(OpenstackEventHandler.class);

	public OpenstackEventHandler(String job, int threads) {
		super(job, threads);
	}

	@Override
	public void Handler(Object... params) {

		final String message = (String) params[0];// discerner		
		Runnable run = new Runnable() {
			public void run() {
				logger.info("receive the event of Openstack,begin to handle message:"
						+ message);
				long start_time= new Date().getTime();
				try {
					//String m ="{\"vm_state\": \"building\", \"host\": \"HSCloudNode001\", \"sync_type\": \"syncvmstate\", \"event_time\": \"2013-05-15 14:16:22\", \"task_state\": null, \"vm_id\": \"140\"}";
//					 String method = MessageDiscerner.discern(m);
					// 对消息分类，调用相应方法。
					
					String method = MessageDiscerner.discern(message);
					// 设置 JobName
					((SyncEvent) CommonThreadLocal.getSyncEvent())
							.setMessager(getJob_name());
					// 获取线程许 可
					semp.acquire();
					InvokeMethodUtil.invokeMethod(new OpenstackAdapter(),
							method, new Object[] {});

				} catch (OpenstackHandlerException oshe) {
					logger.error("" + oshe.getMessage(), oshe);
				} catch (Exception e) {
					logger.error("" + e.getMessage(), e);
				} finally {
					CommonThreadLocal.remove();
				}
				long finished_time= new Date().getTime();
				long interval=finished_time-start_time;				
				logger.debug("task cost time(ms):" + interval);
				// 处理完成后，释放线程许可
				semp.release();
			}
		};
		pool.execute(run);
	}

}
