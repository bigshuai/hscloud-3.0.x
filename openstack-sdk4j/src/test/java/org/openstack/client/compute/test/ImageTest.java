package org.openstack.client.compute.test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.TenantResource;
import org.openstack.api.hscloud.HscloudResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.compute.Image;
import org.openstack.model.compute.ImageList;
import org.openstack.model.hscloud.impl.ImageForCreate;

public class ImageTest extends AbstractOpenStackTest{
	protected HscloudResource hscloud ;
	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint();
	}
	
	
	public void createImage(){
		ImageForCreate ifc = new ImageForCreate();
		ifc.setName("windows2008");//必填项
		ifc.setDiskFormat("raw");//必填项
		ifc.setFile("/root/images/windows2008.img");
		ifc.setDistro("java_test_3");
		ifc.setDesc("aaaaaa");
		ifc.setOsType("linux");
		ifc.setUsername("root");
		ifc.setPasswd("HISOFT@123");
		compute.createImage(ifc);
	}
	
	@Test
	public void imageShow(){
		ImageList imageList = compute.images().get();
		List<Image> list = imageList.getList();
		for(Image image : list) {
			System.out.println(image.getId());
			System.out.println(image.getName());
			Map<String, String> map = image.getMetadata();
			for(Entry<String, String> entry : map.entrySet()) {
				System.out.println("key=" + entry.getKey() + ";value=" + entry.getValue());
			}
		}
	}
	
	public void imageDelete(){
		compute.images().image("b8162907-d9c5-45d2-b34f-f75024b75382").delete();
	}
}
