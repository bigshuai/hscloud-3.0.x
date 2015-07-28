package com.hisoft.hscloud.vpdc.oss.monitoring.vo; 

import java.util.ArrayList;
import java.util.List;

public class VmRealtimeMonitorVO {
	private CPUMonitorVO cPUMonitorVO;
	private MemoryMonitorVO memoryMonitorVO;
	private List<DiskMonitorVO> diskMonitorVOList =new ArrayList<DiskMonitorVO>();
	private List<NetMonitorVO> netMonitorVOList =new ArrayList<NetMonitorVO>();
	public CPUMonitorVO getcPUMonitorVO() {
		return cPUMonitorVO;
	}
	public void setcPUMonitorVO(CPUMonitorVO cPUMonitorVO) {
		this.cPUMonitorVO = cPUMonitorVO;
	}
	public MemoryMonitorVO getMemoryMonitorVO() {
		return memoryMonitorVO;
	}
	public void setMemoryMonitorVO(MemoryMonitorVO memoryMonitorVO) {
		this.memoryMonitorVO = memoryMonitorVO;
	}
	public List<DiskMonitorVO> getDiskMonitorVOList() {
		return diskMonitorVOList;
	}
	public void setDiskMonitorVOList(List<DiskMonitorVO> diskMonitorVOList) {
		this.diskMonitorVOList = diskMonitorVOList;
	}
	public List<NetMonitorVO> getNetMonitorVOList() {
		return netMonitorVOList;
	}
	public void setNetMonitorVOList(List<NetMonitorVO> netMonitorVOList) {
		this.netMonitorVOList = netMonitorVOList;
	}
	@Override
	public String toString() {
		return "VmRealtimeMonitorVO [cPUMonitorVO=" +cPUMonitorVO
				+", memoryMonitorVO=" +memoryMonitorVO
				+", diskMonitorVOList=" +diskMonitorVOList
				+", netMonitorVOList=" +netMonitorVOList
				+"]";
	}

}
