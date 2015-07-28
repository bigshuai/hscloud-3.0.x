package org.openstack.client;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Target;

import org.openstack.api.common.Resource;
import org.openstack.api.common.RestClient;
import org.openstack.api.identity.IdentityAdministrationEndpoint;
import org.openstack.api.identity.IdentityInternalEndpoint;
import org.openstack.api.identity.IdentityPublicEndpoint;
import org.openstack.api.storage.AccountResource;
import org.openstack.model.exceptions.OpenstackException;
import org.openstack.model.identity.Authentication;
import org.openstack.model.identity.keystone.KeystoneAuthentication;
import org.openstack.model.identity.swift.SwiftKeystoneAccess;

import com.google.common.base.Preconditions;

public class SwiftClient {

	//private LoggingFilter loggingFilter = new LoggingFilter(Logger.getLogger(SwiftClient.class.getPackage().getName()),false);
	
	//private LoggingFilter loggingEntityFilter = new LoggingFilter(Logger.getLogger(SwiftClient.class.getPackage().getName()),true);
	
	private Properties properties;

	private SwiftKeystoneAccess access;

	private XAuthTokenFilter authFilter;

	private XAuthTokenFilter authAsAdministratorFilter;
	
	private SwiftClient() {
		
	}
	
	public static SwiftClient authenticate(Properties properties, SwiftKeystoneAccess access) {
		SwiftClient client = new SwiftClient();
		client.properties = properties;
		client.access = access;
		client.authFilter = new XAuthTokenFilter(access.getToken().getId());
		client.authAsAdministratorFilter = new XAuthTokenFilter(properties.getProperty("identity.admin.token"));
		return client;
	}
	
	public static SwiftClient authenticate(Properties properties) {
		SwiftClient client = new SwiftClient();
		client.properties = properties;
		String username = properties.getProperty("auth.username");
		String password = properties.getProperty("auth.password");
		String tenantId = properties.getProperty("auth.tenant.id");
		String tenantName = properties.getProperty("auth.tenant.name");
		String project = properties.getProperty("auth.project");
		KeystoneAuthentication authentication = new KeystoneAuthentication().withPasswordCredentials(username, password);
		if(tenantId != null) {
			authentication.setTenantId(tenantId);
		} else if(tenantName != null) {
			authentication.setTenantName(tenantName);
		}
		if(project != null){
			authentication.setProject(project);
		}
		SwiftKeystoneAccess access = client.getIdentityEndpoint().tokens().swiftPost(authentication);
		return authenticate(properties, access);
	}
	
	public static SwiftClient authenticate() {
		try {
			Properties properties = new Properties();
			properties.load(SwiftClient.class.getResourceAsStream("/openstack.properties"));
			return authenticate(properties);
		} catch (IOException e) {
			throw new OpenstackException("openstack.properties not found in the CLASSPATH", e);
		}
	}
	
	public void reauthenticateOnTenant(String tenantName) {
		properties.setProperty("auth.tenant.name", tenantName);
		authenticate(properties);
	}

	public void exchangeTokenForTenant(String tenantId) {
		String endpoint = properties.getProperty("identity.endpoint.publicURL");
		Authentication authentication = new KeystoneAuthentication().withTokenAndTenant(access.getToken().getId(), tenantId);
		this.access = target(endpoint, IdentityPublicEndpoint.class).tokens().swiftPost(authentication);
		System.out.println("EX " + this.access);
	}
	
	public SwiftKeystoneAccess getAccess() {
		return this.access;
	}
	
	public IdentityClient getIdentityClient() {
		return new IdentityClient(getIdentityAdministationEndpoint());
	}
	
	public IdentityPublicEndpoint getIdentityEndpoint() {
		String url = properties.getProperty("identity.endpoint.publicURL");
		System.out.println(url);
		Preconditions.checkNotNull(url, "'identity.endpoint.publicURL' property not found");
		return target(url, IdentityPublicEndpoint.class);
	}
	
	public IdentityInternalEndpoint getIdentityInternalEndpoint() {
		String url = properties.getProperty("identity.endpoint.public");
		Preconditions.checkNotNull(url, "'identity.endpoint.internalURL' property not found");
		return target(url, IdentityInternalEndpoint.class);
	}

	public IdentityAdministrationEndpoint getIdentityAdministationEndpoint() {
		String url = properties.getProperty("identity.endpoint.adminURL");
		Preconditions.checkNotNull(url, "'identity.endpoint.adminURL' property not found");
		return target(url, IdentityAdministrationEndpoint.class, true);
	}
	
	public <T extends Resource> T target(String absoluteURL, Class<T> clazz) {
		return target(absoluteURL, clazz, false);
	}

	public AccountResource getAccountResource(){
		return target(access.getEndpoint("object-store", null).getPublicURL(), AccountResource.class);
	}
	
	private <T extends Resource> T target(String absoluteURL, Class<T> clazz, boolean useAdministrationToken) {
		try {
			Target target = RestClient.INSTANCE.getJerseyClient().target(absoluteURL);
			if (access != null) {
				target.configuration().register(useAdministrationToken ? authAsAdministratorFilter : authFilter);
			}
			return clazz.getConstructor(Target.class, Properties.class).newInstance(target, properties);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	

	
}
