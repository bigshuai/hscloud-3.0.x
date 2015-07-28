/* 
* 文 件 名:  VpdcVrouterTemplateServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2014-1-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.service; 

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcVrouterTemplateDao;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcVrouterTemplate;
import com.hisoft.hscloud.vpdc.ops.vo.FlavorVO;
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
@Service
public class VpdcVrouterServiceImpl implements VpdcVrouterService{
    @Autowired
    private VpdcVrouterTemplateDao vpdcVrouterTemplateDao;
    
    @Autowired
    private Operation operation;
    
    private final int DELETED_TRUE = 1;
    
    
    public long addVrouterTemplate(VrouterTemplateVO vrouterTemplateVO) {
        FlavorVO flavorVO = new FlavorVO();
        flavorVO.setDisk(vrouterTemplateVO.getDiskCapacity());
        flavorVO.setRam(vrouterTemplateVO.getMemSize());
        flavorVO.setVcpus(vrouterTemplateVO.getCpuCore());
        String flavorId = operation.createFlavor(flavorVO);
        
//        vpdcVrouterTemplate.setFlavorId(Integer.valueOf(flavorId));
        
        VpdcVrouterTemplate vpdcVrouterTemplate = new VpdcVrouterTemplate();
        BeanUtils.copyProperties(vrouterTemplateVO, vpdcVrouterTemplate);
        
        vpdcVrouterTemplate.setFlavorId(Integer.valueOf(flavorId));
        return vpdcVrouterTemplateDao.addVrouterTemplate(vpdcVrouterTemplate);
    }

    @Override
    public VpdcVrouterTemplate getVrouterTemplate(long id) {
        return vpdcVrouterTemplateDao.getVrouterTemplate(id);
    }
    
    
    public void updateVrouterTemplate(VrouterTemplateVO vrouterTemplateVO) {
        VpdcVrouterTemplate temp = vpdcVrouterTemplateDao.getVrouterTemplate(vrouterTemplateVO.getId());
        temp.setCpuCore(vrouterTemplateVO.getCpuCore());
        temp.setDiskCapacity(vrouterTemplateVO.getDiskCapacity());
        temp.setMemSize(vrouterTemplateVO.getMemSize());
        temp.setImageId(vrouterTemplateVO.getImageId());
        temp.setOsId(vrouterTemplateVO.getOsId());
        temp.setName(vrouterTemplateVO.getName());
        temp.setUpdateDate(new Date());
        vpdcVrouterTemplateDao.addVrouterTemplate(temp);
    }
    
    @Override
    public Page<VrouterTemplateVO> pageVrouterTemplateVO(Page<VrouterTemplateVO> page) {
        return vpdcVrouterTemplateDao.pageVrouterTemplateVO(page);
    }
    
    @Override
    public void deleteVrouterTemplate(long id) {
        VpdcVrouterTemplate vpdcVrouterTemplate = vpdcVrouterTemplateDao.getVrouterTemplate(id);
        vpdcVrouterTemplate.setDeleted(DELETED_TRUE);
        vpdcVrouterTemplateDao.addVrouterTemplate(vpdcVrouterTemplate);
    }
    
    public List<VpdcVrouterTemplate> getVrouterTemplateByName(String name) {
        return vpdcVrouterTemplateDao.getVrouterTemplateByName(name);
    }

    @Override
    public String editVrouterTemplate(VrouterTemplateVO vrouterTemplateVO) {
        List<VpdcVrouterTemplate> list = getVrouterTemplateByName(vrouterTemplateVO.getName());
        if(list != null && !list.isEmpty()) {
            for(VpdcVrouterTemplate vrouter : list) {
                if(vrouter.getId() != vrouterTemplateVO.getId()) {
                    return "nameExisted";
                }
            }
        }
        
        if(vrouterTemplateVO.getId() == 0) {
            addVrouterTemplate(vrouterTemplateVO);
        } else {
            updateVrouterTemplate(vrouterTemplateVO);
        }
        return Constants.SUCCESS;
    }

	@Override
	public VrouterTemplateVO getVrouterTemplate() {
		VrouterTemplateVO vtVO = new VrouterTemplateVO();
		List<VpdcVrouterTemplate> vvt_list = vpdcVrouterTemplateDao.getAllVrouterTemplate();
		if (vvt_list != null && vvt_list.size() > 0) {
			VpdcVrouterTemplate vvt = vvt_list.get(0);
			vtVO.setId(vvt.getId());
			vtVO.setName(vvt.getName());
			vtVO.setCpuCore(vvt.getCpuCore());
			vtVO.setDiskCapacity(vvt.getDiskCapacity());
			vtVO.setMemSize(vvt.getMemSize());
			vtVO.setOsId(vvt.getOsId());
			vtVO.setVersion(vvt.getVersion().toString());
			vtVO.setFlavorId(vvt.getFlavorId());
			vtVO.setImageId(vvt.getImageId());
		}
		return vtVO;
	}
	
}
