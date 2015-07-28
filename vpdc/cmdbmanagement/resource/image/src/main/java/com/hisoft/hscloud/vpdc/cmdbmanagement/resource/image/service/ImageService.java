package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.service; 

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.ImageVO;

public interface ImageService {
	public List<FileVO> getFileList();
	
	public boolean deleteFile(String fileName);
	
	public void addUploadInfo(ImageVO image, String fileName);
	
	public List<ImageVO> showImageList();
	
	public void deleteImage(String imageId);
	
	public void editImage(ImageVO image);

	public List<ImageVO> showImageList(String query);
	public Map<String,String> getImageType();
}
