/* 
* 文 件 名:  VpdcVrouterTemplateDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.dao; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;

/** 
 * <VRouter配置模板> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface VpdcVrouterTemplateDao {
    public long addVrouterTemplate(VpdcVrouterTemplate vpdcVrouterTemplate);

    public VpdcVrouterTemplate getVrouterTemplate(long id);

    public Page<VrouterTemplateVO> pageVrouterTemplateVO(Page<VrouterTemplateVO> page);

    public List<VpdcVrouterTemplate> getVrouterTemplateByName(String name);

	public List<VpdcVrouterTemplate> getAllVrouterTemplate();
}
