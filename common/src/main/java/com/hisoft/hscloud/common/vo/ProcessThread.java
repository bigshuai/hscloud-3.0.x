package com.hisoft.hscloud.common.vo;
 public abstract class ProcessThread extends Thread{
	 private String threadName;
	 private boolean runFlag;
	 public void stopThread(){
		 runFlag=false;
	 }
	 
	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public boolean isRunFlag() {
		return runFlag;
	}
	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}
	 
}
