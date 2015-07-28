package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.dao.Impl;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.dao.ImageDao;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.entity.UploadFile;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;

@Repository
public class ImageDaoImpl extends HibernateDao<UploadFile, Long> implements
		ImageDao {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private String getPath() {
		Map<String, String> map = PropertiesUtils.getPropertiesMap(
				"image.properties", PropertiesUtils.IMAGE_MAP);
		return map.get("path");
	}

	/**
	 * 在ftp上删除文件 <功能详细描述>
	 * 
	 * @param name
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Override
	public boolean deleteFile(String name) {
		File file = new File(getPath() + name);
		if (file.exists()) {
			/*
			 * List<UploadFile> list = this.findBy("fileName", name);
			 * for(UploadFile uploadFile : list) { this.delete(uploadFile); }
			 */
			return file.delete();
		}
		return false;
	}

	@Override
	public List<FileVO> getFileList() {
		String path = getPath();
		List<String> fileNameList = this
				.find("SELECT fileName FROM UploadFile");

		List<FileVO> list = new ArrayList<FileVO>();
		File file = new File(path);
		if(!file.exists()) {
		    throw new HsCloudException("002", "getFileList Exception", logger, new RuntimeException());
		}
		
		String[] array = file.list();
		FileVO image = null;
		for (String str : array) {
            File temp = new File(path + str);
            if (!temp.isDirectory()) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(temp.lastModified());
                image = new FileVO();
                image.setSize(transferString(temp.length()));
                image.setName(str);
                if (fileNameList.contains(str)) {
                    image.setFlag(true);
                }
                image.setCreateDate(cal.getTime());
                list.add(image);
            }
		}
		return list;
	}
	
	public String transferString(long length){
		float size = length / 1024;
		if(size < 500) {
			return String.format("%.1f", size) + "K";
		}
		size = size / 1024;
		if(size < 500) {
			return String.format("%.1f", size) + "M";
		}
		size = size / 1024;
		return String.format("%.1f", size) + "G";
	}

	@Override
	public void add(UploadFile uploadFile) {
		this.save(uploadFile);
	}
}
