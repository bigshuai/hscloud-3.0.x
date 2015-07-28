package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openstack.model.compute.nova.floatingip.DeleteFloatingIpAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.sc.entity.ServerZone;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.common.util.OpenstackUtil;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao.IPDetailDao;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao.IPRangeDao;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPDetail;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.entity.IPRange;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailInfoVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPDetailVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPRangeVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo.IPStatistics;

@Service
public class IPServiceImpl implements IPService {

	@Autowired
	private IPDetailDao iPDetailDao;
	@Autowired
	private IPRangeDao iPRangeDao;
	
	private OpenstackUtil openstackUtil = new OpenstackUtil();
	private Logger logger = Logger.getLogger(this.getClass());
	private DeleteFloatingIpAction deleteFloatingIpAction = null;
	
	/**
	 * <一句话功能简述> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	private void floatingIpCreate(String startIp, String endIp,String zoneCode){
		try{
			openstackUtil.getCompute().ipResource().createFloatingIps(zoneCode,startIp,endIp);
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_CREATE_EXCEPTION, "floatingIpCreate Exception:", logger, e);
		}		
	}
	
	public String createIP(IPRange ipRange) throws HsCloudException {
		String result = null;
		IPDetail iPDetail = null;
		long id = 0;
		long iRange = 0;
		long startIP = ipRange.getStartIP();
		long endIP = ipRange.getEndIP();
		long createUid = ipRange.getCreateUid();
		if(startIP==0||endIP==0||createUid==0){
			return null;
		}
		iRange=endIP-startIP;
		if(iRange>=0){
			try {				
				ipRange.setCreateTime(new Date());
				id=iPRangeDao.createIPRange(ipRange);				
				String startIPStr = IPConvert.getStringIP(startIP);
				String endIPStr = IPConvert.getStringIP(endIP);
				floatingIpCreate(startIPStr, endIPStr,ipRange.getServerZone().getCode());
				
				if (id != 0) {						
					for(int i=0;i<=iRange;i++){						
						iPDetail=new IPDetail();		
						iPDetail.setCreateTime(new Date());
						iPDetail.setCreateUid(createUid);
						iPDetail.setIp(startIP+i);
						iPDetail.setIpRange(ipRange);
						iPDetail.setStatus(Constants.IP_STATUS_ENABLED);
						iPDetailDao.createIPDetail(iPDetail);
					}					
					result = String.valueOf(id);
				}
			} catch (Exception e) {
				throw new HsCloudException(Constants.IP_CREATE_EXCEPTION, "createIP Exception:", logger, e);
			}
		}		
		return result;
	}
	
	public Long createWanNetworkIP(IPRange ipRange,long useStartIp,long useEndIp) throws HsCloudException, Exception{
		IPDetail iPDetail = null;
		long id = 0;
		long iRange = 0;
		long startIP = ipRange.getStartIP();
		long endIP = ipRange.getEndIP();
		long createUid = ipRange.getCreateUid();
		if(startIP==0||endIP==0||createUid==0){
			return null;
		}
		iRange=endIP-startIP;
		if(iRange>=0){
			try {				
				ipRange.setCreateTime(new Date());
				id=iPRangeDao.createIPRange(ipRange);				
				if (id != 0) {
					long longIP = 0;
					for(int i=0;i<=iRange;i++){						
						iPDetail=new IPDetail();		
						iPDetail.setCreateTime(new Date());
						iPDetail.setCreateUid(createUid);
						longIP = startIP+i;
						iPDetail.setIp(longIP);
						iPDetail.setIpRange(ipRange);
						if(longIP>=useStartIp && longIP<=useEndIp){
							iPDetail.setStatus(Constants.IP_STATUS_ENABLED);
						}else{
							iPDetail.setStatus(Constants.IP_STATUS_DISABLED);
						}
						iPDetailDao.createIPDetail(iPDetail);
					}					
				}
			} catch (Exception e) {
				throw new HsCloudException(Constants.IP_CREATE_EXCEPTION, "createIP Exception:", logger, e);
			}
		}		
		return id;
	}
	
	
	public boolean deleteIP(long id) throws HsCloudException {
		boolean resultFlg = false;
		try{
			if(id!=0){
				IPRange iPRange = iPRangeDao.getIPRangeById(id);
				String startIp = IPConvert.getStringIP(iPRange.getStartIP());
				String endIp = IPConvert.getStringIP(iPRange.getEndIP());
				if(iPRange!=null){					
					//删除ip
					floatingIpDelete(startIp, endIp);
					resultFlg = iPRangeDao.deleteIPRange(id);
				}
			}
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_DELETE_EXCEPTION, "deleteIP Exception:", logger, e);
		}		
		return resultFlg;
	}
	
	private void floatingIpDelete(String startIp, String endIp){
		deleteFloatingIpAction = new DeleteFloatingIpAction();
		deleteFloatingIpAction.setFrom(startIp);
		deleteFloatingIpAction.setTo(endIp);
		openstackUtil.getCompute().ipResource().deleteFloatingIps(deleteFloatingIpAction);
	}
	
	public boolean updateIPDetail(long id, int status, long userId,String remark) throws HsCloudException {
		boolean resultFlg = false;
		String zoneCode = "";
		IPDetail iPDetail = null;
		IPRange ipRange = null;
		try{
			if(id > 0){
				iPDetail = iPDetailDao.getIPDetailById(id);
			}
			if(iPDetail == null){
				return false;
			}
			if(iPDetail.getStatus()==Constants.IP_STATUS_ASSIGNED){
				return false;
			}
			ipRange = iPDetail.getIpRange();
			if((ipRange != null) && (ipRange.getServerZone() != null)){
				zoneCode = ipRange.getServerZone().getCode();
			}
			String sIP=IPConvert.getStringIP(iPDetail.getIp());
			if((status==Constants.IP_STATUS_DISABLED) && (iPDetail.getStatus()==Constants.IP_STATUS_FREE)){
				floatingIpDelete(sIP, sIP);
			}
			if((status==Constants.IP_STATUS_FREE) && (iPDetail.getStatus()==Constants.IP_STATUS_DISABLED)){
				floatingIpCreate(sIP, sIP,zoneCode);
			}
			resultFlg=iPDetailDao.updateIPDetail(id, status, userId,remark);
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_UPDATE_STATUS_EXCEPTION, "updateIPDetail Exception:", logger, e);
		}		
		return resultFlg;
	}
	
	@Override
	public Page<IPRangeVO> findIPRanges(Page<IPRangeVO> page,String field,String fieldValue) throws HsCloudException {
		List<IPRangeVO> iPRangeVOs = new ArrayList<IPRangeVO>();
		IPRangeVO iPRangeVO = null;
		IPStatistics status = null;
		try {
			Page<IPRange> pageIPRange =  iPRangeDao.findIPRange(page, field, fieldValue);
			List<IPRange> ipRange = pageIPRange.getResult();
			for (IPRange ipRanges :ipRange) {
				iPRangeVO = new IPRangeVO();
				status = iPDetailDao.getIPStatisticsByRangeId(ipRanges.getId());
				iPRangeVO.setId(ipRanges.getId());
				iPRangeVO.setStartIP(ipRanges.getStartIP());
				iPRangeVO.setEndIP(ipRanges.getEndIP());
				iPRangeVO.setCreateTime(ipRanges.getCreateTime());
				iPRangeVO.setCreateUid(ipRanges.getCreateUid());
				iPRangeVO.setGateWay(ipRanges.getGateway());
				if(ipRanges.getServerZone()!=null){
					iPRangeVO.setZoneId(ipRanges.getServerZone().getId());
					iPRangeVO.setZoneCode(ipRanges.getServerZone().getCode());
					iPRangeVO.setZoneName(ipRanges.getServerZone().getName());
				}
				iPRangeVO.setTotalIPs(status.getFreeIPs() + status.getAssigningIPs()
						+ status.getAssignedIPs() + status.getDisabledIPs()+ status.getReleasingIPs());
				iPRangeVO.setUsedIPs(status.getAssigningIPs());
				iPRangeVO.setAssignedIPs(status.getAssignedIPs());
				iPRangeVO.setFreeIPs(status.getFreeIPs());
				iPRangeVOs.add(iPRangeVO);
			}
			page.setResult(iPRangeVOs);
		} catch (Exception ex) {
			throw new HsCloudException(Constants.IP_RANGE_FIND_EXCEPTION, "findIPRangesByUId Exception:", logger, ex);
		}
		return page;
	}
	
	@Override
	public IPStatistics getIPStatisticsByRangeId(long rangeId) {
	    return iPDetailDao.getIPStatisticsByRangeId(rangeId);
	}

	public List<IPDetail> findIPDetailByStatus(long ipRangeId, int status) throws HsCloudException {
		List<IPDetail> list=new ArrayList<IPDetail>();
		try{
			if(ipRangeId!=0){				
				list=iPDetailDao.findIPDetailByStatus(ipRangeId, status);							
			}
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_FIND_EXCEPTION, "findIPDetailByStatus Exception:", logger, e);
		}
		return list;
	}

	public List<IPDetail> findIPDetailByIP(long startIP, long endIP) throws HsCloudException {
		List<IPDetail> list=new ArrayList<IPDetail>();
		try{
			if(startIP!=0&&endIP!=0){				
				list=iPDetailDao.findIPDetailByIP(startIP, endIP);							
			}
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_FIND_EXCEPTION, "findIPDetailByIP Exception:", logger, e);
		}
		return list;
	}

	@Override
	public Page<IPDetailVO> findIPDetailsByCondition(Page<IPDetailVO> page,
			long ipRangeId, String field, String fieldValue) throws HsCloudException {
		int offset, length;
		if (page.getPageNo() == 1) {
			offset = 0;
			length = page.getPageSize();
		} else {
			offset = (page.getPageNo() - 1) * page.getPageSize();
			length = page.getPageSize();
		}
		page = iPDetailDao.findIPDetailVO(ipRangeId, field, fieldValue, offset, length);	
		return page;
	}

	@Transactional(readOnly = false)
	public boolean updateIPDetail(long id, int status, long userId,
			long objectId, int objectType) throws HsCloudException {
		boolean resultFlg = false;
		try{
			if(id!=0){
				IPDetail iPDetail=iPDetailDao.getIPDetailById(id);
				if(iPDetail!=null){
					iPDetail.setStatus(status);
					iPDetail.setModifyUid(userId);
					iPDetail.setModifyTime(new Date());
					iPDetail.setObjectId(objectId);
					iPDetail.setObjectType(objectType);
					resultFlg=iPDetailDao.updateIPDetail(iPDetail);
				}				
			}
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_UPDATE_STATUS_EXCEPTION, "updateIPDetail Exception:", logger, e);
		}		
		return resultFlg;
	}

	@Override
	public List<IPDetailInfoVO> findIPDetailByStatus(int status)
			throws HsCloudException {
		List <IPDetailInfoVO> ipList=null;
		IPDetailInfoVO ipDetaiInfo = null;
		try{						
			List<IPDetail> list=iPDetailDao.findIPDetailByStatus(status);
			ipList = new ArrayList<IPDetailInfoVO>();
			for(IPDetail ipDetail :list){
				ipDetaiInfo =new IPDetailInfoVO();
				ipDetaiInfo.setId(ipDetail.getId());
				ipDetaiInfo.setIp(IPConvert.getStringIP(ipDetail.getIp()));
				ipDetaiInfo.setStatus(ipDetail.getStatus());
				ipList.add(ipDetaiInfo);
			}
		}catch (Exception e) {
			throw new HsCloudException(Constants.IP_FIND_EXCEPTION, "findIPDetailByStatus Exception:", logger, e);
		}
		return ipList;
	}

	@Override
	public IPDetail getIPDetailByIP(long ip) throws HsCloudException {
		IPDetail iPDetail = null;
		if(ip > 0){
			iPDetail = iPDetailDao.getIPDetailByIP(ip);
		}
		return iPDetail;
	}
	
	/**
	 * 通过userId查询ip列表
	* @param userId
	* @return
	 */
    @Override
    public List<BigInteger> getIPListByUserId(long referenceId) {
        return iPDetailDao.getIPListByUserId(referenceId);
    }

	@Override
	public List<IPRange> getAllIPsByServerZone(long zoneId)
			throws HsCloudException {
		return iPRangeDao.getAllIPsByServerZone(zoneId);
	}

	@Override
	public List<IPDetailInfoVO> findAvailableIPDetailOfServerZone(ServerZone serverZone)
			throws HsCloudException {
		List <IPDetailInfoVO> ipList=null;
		IPDetailInfoVO ipDetaiInfo = null;
		try{						
			List<IPDetail> list=iPDetailDao.findAvailableIPDetailOfServerZone(serverZone);
			ipList = new ArrayList<IPDetailInfoVO>();
			for(IPDetail ipDetail :list){
				ipDetaiInfo =new IPDetailInfoVO();
				ipDetaiInfo.setId(ipDetail.getId());
				ipDetaiInfo.setIp(IPConvert.getStringIP(ipDetail.getIp()));
				ipDetaiInfo.setStatus(ipDetail.getStatus());
				ipList.add(ipDetaiInfo);
			}
		}catch (Exception e) {
			logger.error("findAvailableIPDetailOfServerZone Exception:", e);
		}
		return ipList;
	}

    public String assignIPDetail(long zoneId) {
    	synchronized(this){
    		try {
				ServerZone serverZone = new ServerZone();
				serverZone.setId(zoneId);
				IPDetail ip = iPDetailDao.getAvailableIPDetailOfServerZone(serverZone);
				if(ip == null) {
				    return null;
				}
				iPDetailDao.updateIPStatus(ip.getId(), Constants.IP_STATUS_ASSIGNING);
				return IPConvert.getStringIP(ip.getIp());
			} catch (HsCloudException e) {
				e.printStackTrace();
				return null;
			}
    	}
    }
    
    @Override
    public boolean releaseIp(String sIP) {
        Long ip = IPConvert.getIntegerIP(sIP);
        IPDetail iPDetail = iPDetailDao.getIPDetailByIP(ip);
       // iPDetail.setStatus(Constants.IP_STATUS_FREE);
        iPDetailDao.updateIPStatus(iPDetail.getId(), Constants.IP_STATUS_FREE);
        return true;
    }

	@Override
	public IPStatistics getIPStatisticsByzoneCode(String zoneCode)
			throws HsCloudException {
		return iPDetailDao.getIPStatisticsByzoneCode(zoneCode);
	}

	@Override
	public IPStatistics getAllIPStatistics() throws HsCloudException {
		return iPDetailDao.getIPStatisticsByHostId();
	}

	@Override
	public IPDetail getIPDetailByIP(String ip) throws HsCloudException {
		return iPDetailDao.getIPDetailByIP(ip);
	}
	
	@Override
	public Map<String, String> getIpAndNetwork(long zoneGroupId) {
	    List<Object[]> list = iPDetailDao.getIpAndNetwork(zoneGroupId);
	    if(list != null && !list.isEmpty()) {
	        int length = list.size();
	        int index = (int)(Math.random() * length);
	        Object[] array = list.get(index);
	        Map<String, String> map = new HashMap<String, String>();
	        map.put("ip", IPConvert.getStringIP(((BigInteger)array[0]).longValue()));
            map.put("uuid", (String)array[1]);
	        return map;
	    }
	    return null;
	}
}
