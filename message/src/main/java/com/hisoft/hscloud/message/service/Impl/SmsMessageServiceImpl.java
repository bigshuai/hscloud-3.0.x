package com.hisoft.hscloud.message.service.Impl; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.MSMUtil;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.message.dao.SmsMessageDao;
import com.hisoft.hscloud.message.entity.SMSMessage;
import com.hisoft.hscloud.message.service.SmsMessageService;

/**
 * @author yubenjie
 *短信信息管理
 */
@Service
public class SmsMessageServiceImpl implements SmsMessageService{
	private MSMUtil msm = new MSMUtil();
	@Autowired
	private SmsMessageDao smsMessageDao;
	
	@Override
	public void saveSmsMessage(SMSMessage smsMessage,List<String> mobileList) {
		if(smsMessage.getType()==1){
			for(String mobile:mobileList){
				//msm.adminOperateMSM(mobile,smsMessage.getContent());
			}
		}else{
			//msm.adminOperateMSM(smsMessage.getMobile(),smsMessage.getContent());
		}
		smsMessageDao.saveSmsMessage(smsMessage);
	}

	@Override
	public Page<SMSMessage> findSmsMessagePage(Page<SMSMessage> page,String query){
		return smsMessageDao.findSmsMessagePage(page,query);
	}

	@Override
	public void delSmsMessage(SMSMessage smsMessage) {
		smsMessageDao.delSmsMessage(smsMessage);
	}

	@Override
	public SMSMessage findSmsMessageById(long id) {
		return smsMessageDao.findSmsMessageById(id);
	}
	
}
