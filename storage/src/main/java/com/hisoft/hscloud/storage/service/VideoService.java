/* 
* 文 件 名:  VideoService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.service; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.storage.entity.Module;
import com.hisoft.hscloud.storage.entity.Video;
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
public interface VideoService {
    public void saveVideo(Video video);

    public Page<VideoVO> findVideo(Page<VideoVO> resultPage, Map<String, Object> condition);

    public String getPlayUrl(VideoVO videoVO, String url, String tenantId);
    /**
     * <加载模块列表> 
    * <功能详细描述> 
    * @param userName
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Module> loadModule(String userName);
}
