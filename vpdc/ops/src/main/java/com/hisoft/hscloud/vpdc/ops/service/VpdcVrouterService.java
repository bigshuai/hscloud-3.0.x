/* 
* 文 件 名:  VpdcVrouterTemplateService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.service; 

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.vo.VrouterTemplateVO;

/** 
 * <VRouter> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2014-1-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface VpdcVrouterService {    
    public VpdcVrouterTemplate getVrouterTemplate(long id);
    
 //   public void updateVrouterTemplate(VrouterTemplateVO vrouterTemplateVO);

    public Page<VrouterTemplateVO> pageVrouterTemplateVO(Page<VrouterTemplateVO> page);

    public void deleteVrouterTemplate(long id);

  /*  public long addVrouterTemplate(VrouterTemplateVO vrouterTemplateVO);

    public List<VpdcVrouterTemplate> getVrouterTemplateByName(String name);
*/
    public String editVrouterTemplate(VrouterTemplateVO vrouterTemplateVO);

	public VrouterTemplateVO getVrouterTemplate();
}
