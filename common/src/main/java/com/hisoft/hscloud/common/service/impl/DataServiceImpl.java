/* 
* 文 件 名:  DataServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.service.impl; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.dao.CityDao;
import com.hisoft.hscloud.common.dao.ProvinceDao;
import com.hisoft.hscloud.common.entity.City;
import com.hisoft.hscloud.common.entity.Province;
import com.hisoft.hscloud.common.service.DataService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private ProvinceDao provinceDao;
    
    @Autowired
    private CityDao cityDao;
    
    @Override
    public List<Province> getProvinceList() {
        return provinceDao.getProvinceList();
    }

    private String getProvince(String provinceCode) {
        Province province = getProvinceByRegionCode(provinceCode);
        return province.getProvinceCode();
    }

    private Province getProvinceByRegionCode(String regionCode) {
        return provinceDao.getProvinceByRegionCode(regionCode);
    }

    @Override
    public String getProvinceCode(String regionCode) {
        Province province = getProvinceByRegionCode(regionCode);
        return province.getProvinceCode();
    }
    
    @Override
    public List<City> getCityList(String provinceCode) {
        return cityDao.getCityList(provinceCode);
    }
}
