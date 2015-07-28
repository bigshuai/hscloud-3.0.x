package org.openstack.client.swift.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.storage.AccountResource;
import org.openstack.client.SwiftClient;
import org.openstack.model.identity.swift.SwiftKeystoneEndpoint;
import org.openstack.model.storage.StorageContainer;
import org.openstack.model.storage.swift.SwiftStorageObject;

public class SwiftTest {

	protected SwiftClient client;
	
	@Before
	public void auth(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("etc/swift.properties"));
//			properties.setProperty("verbose", "true");
			properties.setProperty("auth.username", "admin");
			properties.setProperty("auth.password", "admin");
			properties.setProperty("auth.tenant.id", "1e871ef2a88f42248d137ab6fd2c9a6c");
			properties.setProperty("auth.project", "admin");
//			properties.setProperty("auth.tenant.name", "admin");
//			properties.setProperty("identity.endpoint.publicURL", "http://192.168.7.130:5000/v2.0");
			client = SwiftClient.authenticate(properties);
			String id = client.getAccess().getToken().getId();
			System.out.println(id);
			SwiftKeystoneEndpoint endp = client.getAccess().getEndpoint("object-store", "RegionOne");
			System.out.println(endp.getPublicURL());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void test(){
		AccountResource a = client.getAccountResource();
		System.out.println(a.get());
//		List<StorageContainer> cs = a.get();
//		List<SwiftStorageObject> os = a.container("ssss").get();
		System.out.println(a.container(a.get().get(1).getName()).get());
	}
	
	
}
