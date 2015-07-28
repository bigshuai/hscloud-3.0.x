/* 
* 文 件 名:  VideoServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.service.impl; 

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.storage.dao.ModuleDao;
import com.hisoft.hscloud.storage.dao.VideoDao;
import com.hisoft.hscloud.storage.entity.Module;
import com.hisoft.hscloud.storage.entity.Video;
import com.hisoft.hscloud.storage.service.VideoService;
import com.hisoft.hscloud.storage.util.SecretUtil;
import com.hisoft.hscloud.storage.vo.VideoVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class VideoServiceImpl implements VideoService {
    private final String macKey = "test";
    
    private final DecimalFormat format = new DecimalFormat("#.##");
        
    @Autowired
    private VideoDao videoDao;
    
    @Autowired
    private ModuleDao moduleDao;
    
    private Calendar calendar = Calendar.getInstance();
    
    @Override
    public void saveVideo(Video video) {
        videoDao.saveVideo(video);
    }
    
    @Override
    public List<Module> loadModule(String tenantId) {
        return moduleDao.loadModule(tenantId);
    }
    
    @Override
    public Page<VideoVO> findVideo(Page<VideoVO> resultPage, Map<String, Object> condition) {
        List<Object[]> list = videoDao.findVideo(resultPage, condition);

        List<VideoVO> result = new ArrayList<VideoVO>();
        for(Object[] array: list) {
            VideoVO videoVO = new VideoVO();
            videoVO.setId((Integer)array[0]);
            videoVO.setLengthStatus((String)array[1]);
            videoVO.setChannel((String)array[2]);
            videoVO.setDate((String)array[3]);
            videoVO.setTime((String)array[4]);
            calendar.setTimeInMillis((Long.valueOf((Integer)array[5])) * 1000);
            videoVO.setDateShow(calendar.getTime());
            videoVO.setPlanStatus((String)array[6]);
            videoVO.setDevice((String)array[7]);
            videoVO.setName((String)array[8]);
            double fileSize = ((Integer)array[9]) / 1024d / 1024d;
            videoVO.setFileSize(format.format(fileSize) + "M");
            videoVO.setModule((String)array[10]);
            result.add(videoVO);
        }
        resultPage.setResult(result);
        return resultPage;
    }
    
    @Override
    public String getPlayUrl(VideoVO videoVO, String url, String tenantId) {
        videoVO.setDate(videoVO.getDate().substring(0, 6));
        String videoTime = System.currentTimeMillis() / 1000 + 86400 + "";
        String signStr = SecretUtil.createSign(videoTime, videoVO.getDate(), videoVO.getName(), macKey, tenantId);
        StringBuilder result = new StringBuilder(url);
        result.append("/").append(videoVO.getDate()).append("/").append(videoVO.getName());
        result.append("?temp_url_sig=").append(signStr).append("&temp_url_expires=").append(videoTime);
        return result.toString();
    }
}