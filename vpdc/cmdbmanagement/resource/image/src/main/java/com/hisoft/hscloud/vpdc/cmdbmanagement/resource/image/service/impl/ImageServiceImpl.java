package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.service.impl; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openstack.api.compute.TenantResource;
import org.openstack.model.compute.Image;
import org.openstack.model.compute.ImageList;
import org.openstack.model.compute.nova.MetadataForCreateOrUpdate;
import org.openstack.model.hscloud.impl.ImageForCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.CharacterSetUtil;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.OpenstackUtil;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.dao.ImageDao;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.entity.UploadFile;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.service.ImageService;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.ImageVO;

@Service
public class ImageServiceImpl implements ImageService{
	
	@Autowired
	private ImageDao imageDao;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private OpenstackUtil openstackUtil = new OpenstackUtil();
	
	private String getPath() {
		Map<String, String> map = PropertiesUtils.getPropertiesMap(
				"image.properties", PropertiesUtils.IMAGE_MAP);
		return map.get("path");
	}
	private Map<String, String> imageMap = new HashMap<String, String>();
	private List<Image> list = null;
	@Override
	public List<FileVO> getFileList() {
		try {
			return imageDao.getFileList();
		} catch (Exception e) {
			throw new HsCloudException(Constants.IMAGE_UPLOAD_LIST_EXCEPTION, "getFileList Exception",
					logger, e);
		}
	}

	@Override
	public boolean deleteFile(String fileName) {
		return imageDao.deleteFile(fileName);
	}

	@Override
	public void addUploadInfo(ImageVO image, String fileName) {
		logger.debug("addUploadInfo start");
		try {
			ImageForCreate ifc = new ImageForCreate();
			ifc.setName(image.getName());
			ifc.setDiskFormat(image.getDiskFormat());
			ifc.setDistro(CharacterSetUtil.toUnicodeAndCheck(image.getDistro()));
			ifc.setFile(getPath() + CharacterSetUtil.toUnicodeAndCheck(fileName));
			ifc.setDesc(CharacterSetUtil.toUnicodeAndCheck(image.getDesc()));
			ifc.setOsType(image.getOsType());
			ifc.setUsername(CharacterSetUtil.toUnicodeAndCheck(image.getUsername()));
			ifc.setPasswd(CharacterSetUtil.toUnicodeAndCheck(image.getPasswd()));
			ifc.setDefaultSize(image.getVirtualSize());
			
			openstackUtil = new OpenstackUtil();
			openstackUtil.getCompute().createImage(ifc);
			
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFileName(fileName);
			imageDao.add(uploadFile);
		} catch (Exception e) {
			throw new HsCloudException(Constants.IMAGE_ADD_EXCEPTION, "getFileList Exception",
					logger, e);
		}
		logger.debug("addUploadInfo end");
	}

	@Override
	public List<ImageVO> showImageList() {
	    openstackUtil = new OpenstackUtil();
		ImageList imageList = openstackUtil.getCompute().images().get();
		List<Image> list = imageList.getList();
		List<ImageVO> result = new ArrayList<ImageVO>();
		for(Image image : list) {
			ImageVO vo = new ImageVO();
			vo.setId(image.getId());
			vo.setFileName(CharacterSetUtil.fromUnicodeAndcheck(image.getName()));
			Map<String, String> map = image.getMetadata();
			String size = map.get("default_size");
			if(StringUtils.isBlank(size)) {
				size = "0";
			}
			vo.setSize(size);
			String username = map.get("username");
			if(StringUtils.isBlank(username)) {
				username = "";
			}
			vo.setUsername(CharacterSetUtil.fromUnicodeAndcheck(username));
			String password = map.get("passwd");
			if(StringUtils.isBlank(password)) {
				password = "";
			}
			vo.setPasswd(CharacterSetUtil.fromUnicodeAndcheck(password));
			vo.setOsType(map.get("os_type"));
			
			vo.setDesc(CharacterSetUtil.fromUnicodeAndcheck(map.get("desc")));
			String distro = map.get("distro");
			if(StringUtils.isNotBlank(distro)) {
				vo.setDistro(CharacterSetUtil.fromUnicodeAndcheck(map.get("distro")));
				result.add(vo);
			}
		}
		return result;
	}
	
	@Override
	public List<ImageVO> showImageList(String query) {
		List<ImageVO> result = new ArrayList<ImageVO>();
		List<ImageVO> list = showImageList();
		if (!StringUtils.isBlank(query)) {
			for (ImageVO image : list) {
				if (invalidate(query, image)) {
					result.add(image);
				}
			}
		}
		return result;
	}
	
	private boolean invalidate(String query, ImageVO vo) {
		String fileName = vo.getFileName();
		String osType = vo.getOsType();
		String distro = vo.getDistro();
		String desc = vo.getDesc();
		String imageId = vo.getId();
		if((fileName != null && fileName.indexOf(query) >= 0) 
				|| (osType != null && osType.indexOf(query) >= 0) 
				|| (distro != null && distro.indexOf(query) >= 0) 
				|| (desc != null && desc.indexOf(query) >= 0) 
				|| (imageId != null && imageId.indexOf(query) >= 0) ) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteImage(String imageId) {
		try {
		    openstackUtil = new OpenstackUtil();
		    openstackUtil.getCompute().images().image(imageId).delete();
		} catch (Exception e) {
			throw new HsCloudException(Constants.IMAGE_DELETE_EXCEPTION, "getFileList Exception",
					logger, e);
		}
	}

	@Override
	public void editImage(ImageVO image) {
		try {
		    openstackUtil = new OpenstackUtil();
		    TenantResource compute = openstackUtil.getCompute();
			MetadataForCreateOrUpdate m = new MetadataForCreateOrUpdate();
			m.setDistro(CharacterSetUtil.toUnicodeAndCheck(image.getDistro()));
			m.setOsType(image.getOsType());
			m.setDesc(CharacterSetUtil.toUnicodeAndCheck(image.getDesc()));
			m.setUsername(CharacterSetUtil.toUnicodeAndCheck(image.getUsername()));
			m.setPasswd(CharacterSetUtil.toUnicodeAndCheck(image.getPasswd()));
			compute.images().image(image.getId()).setMetadata(m);
		} catch (Exception e) {
			throw new HsCloudException(Constants.IMAGE_EDIT_EXCEPTION, "getFileList Exception",
					logger, e);
		}
	}

	@Override
	public Map<String, String> getImageType() {		
		try{	
//			if(list == null){
//				list = openstackUtil.getCompute().images().get().getList();
//			}
			if(list == null || list.size()>imageMap.size()){
				list = openstackUtil.getCompute().images().get().getList();
				for(Image image : list) {
					Map<String, String> map = image.getMetadata();
					imageMap.put(image.getId(), map.get("os_type"));
				}
			}			
		} catch (Exception e) {
			throw new HsCloudException(Constants.IMAGE_INFO_EXCEPTION, "getFileList Exception",
					logger, e);
		}
		return imageMap;
	}
}

	
