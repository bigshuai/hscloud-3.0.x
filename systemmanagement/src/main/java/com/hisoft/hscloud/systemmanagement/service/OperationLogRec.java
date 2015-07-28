package com.hisoft.hscloud.systemmanagement.service;

import com.hisoft.hscloud.common.vo.ProcessThread;
 public class OperationLogRec extends ProcessThread  {
	private String threadName = "OperationLogRec";
	@Override
	public void run() {
		super.setRunFlag(true);
		super.setThreadName(threadName);
		while(super.isRunFlag()){
			try{
				//.....
				
				//......
				Thread.sleep(10000);
			}
			catch(InterruptedException e){
				super.setRunFlag(false);
				e.printStackTrace();
			}
		}
	}
}
