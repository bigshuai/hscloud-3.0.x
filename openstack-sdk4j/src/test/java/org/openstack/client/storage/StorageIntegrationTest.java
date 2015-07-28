package org.openstack.client.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.openstack.api.storage.AccountResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.model.storage.StorageContainer;
import org.openstack.model.storage.swift.SwiftStorageObjectProperties;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StorageIntegrationTest extends AbstractOpenStackTest {
	
	private AccountResource storage;
	
	private String container = "test-container";
	private String object = "test-object";

	@BeforeClass
	public void init() {
		init("etc/openstack.public.properties");
		storage = client.getStorageEndpoint();
	}
	
	public void showAccount() {
		Response response = storage.head();
	}
	
	public void updateAccount() {
		//Response response = storage.post();
	}
	
	@Test(priority=1)
	public void createContainer() {
		Response response = storage.container(container).put();
	}
	@Test(priority=2)
	public void listContainers() {
		List<StorageContainer> containers = storage.get();
	}
	@Test(dependsOnMethods="createContainer", priority=1)
	public void showContainer() {
		storage.container(container).head();
	}
	@Test(dependsOnMethods="createContainer", priority=2)
	public void updateContainer() {
		//storage.container(container.getName()).post();
	}
	
	@Test(dependsOnMethods="createContainer", priority=3)
	public void createObject() {
		SwiftStorageObjectProperties properties = new SwiftStorageObjectProperties();
		properties.setName("test-object");
		int size = 1024;
		InputStream is = new ByteArrayInputStream(new byte[size]);
		Response response = storage.container(container).object(object).put(is,size, properties);
	}
	
	@Test(dependsOnMethods={"createObject"}, priority=4)
	public void listObjects() {
		storage.container(container).get();
	}
	
	@Test(dependsOnMethods={"createObject"}, priority=5)
	public void showObject() {
		storage.container(container).object(object).head();
	}
	
	@Test(dependsOnMethods="createObject", priority=6)
	public void updateObject() {
		//storage.container(container).object(object).post(null);
	}
	
	@Test(dependsOnMethods={"createObject"}, priority=7)
	public void deleteObject() {
		storage.container(container).object(object).delete();
	}
	
	@Test(dependsOnMethods={"createContainer","deleteObject"}, priority=8)
	public void deleteContainer() {
		storage.container(container).delete();
	}
	
}
