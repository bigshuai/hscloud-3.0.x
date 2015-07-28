package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.dao; 

import java.util.List;

import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.entity.UploadFile;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;

public interface ImageDao {
	public List<FileVO> getFileList();
	
	public boolean deleteFile(String name);

	public void add(UploadFile uploadFile);
}
