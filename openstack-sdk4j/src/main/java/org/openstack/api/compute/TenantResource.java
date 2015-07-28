package org.openstack.api.compute;

import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.filter.LoggingFilter;
import org.openstack.api.common.Resource;
import org.openstack.api.compute.ext.FloatingIpsResource;
import org.openstack.api.compute.ext.KeyPairsResource;
import org.openstack.api.compute.ext.QuotasResource;
import org.openstack.api.compute.ext.SecurityGroupRulesResource;
import org.openstack.api.compute.ext.SecurityGroupsResource;
import org.openstack.api.compute.ext.SimpleTenantUsageResource;
import org.openstack.api.compute.ext.SnapshotsResource;
import org.openstack.api.compute.ext.VolumeTypesResource;
import org.openstack.api.compute.ext.VolumesResource;
import org.openstack.api.compute.notavailable.AccountsResource;
import org.openstack.api.compute.notavailable.CloudPipeResource;
import org.openstack.api.compute.notavailable.FloatingIpDnsResource;
import org.openstack.api.compute.notavailable.NetworksResource;
import org.openstack.api.hscloud.IpResource;
import org.openstack.client.JobHeader;
import org.openstack.model.hscloud.impl.ImageBase;
import org.openstack.model.hscloud.impl.ImageForCreate;

public class TenantResource extends Resource {
	
	private LoggingFilter loggingFilter = new LoggingFilter(Logger.getLogger(TenantResource.class.getPackage().getName()),true);
	
	public TenantResource(Target target, Properties properties) {
		super(target, properties);
		if(Boolean.parseBoolean(properties.getProperty("verbose"))) {
			target.configuration().register(loggingFilter);
		}
	}
	
	public static TenantResource endpoint(Client client, String tenantEndpoint, Properties properties) {
		return new TenantResource(client.target(tenantEndpoint), properties);
	}

	public ServersResource servers(JobHeader jobHeader) {
		if(jobHeader != null){
			target.configuration().register(jobHeader);
		}
        return path("/servers", ServersResource.class);
    }
	
	public ServersResource servers() {
        return path("/servers", ServersResource.class);
    }
	
	public WqServerResource wqservers() {
        return path("/wqservers", WqServerResource.class);
    }

    public FlavorsResource flavors() {
    	return path("/flavors", FlavorsResource.class);
    }

    public ImagesResource images() {
    	return path("/images", ImagesResource.class);
    }

    public VolumeTypesResource volumeTypes() {
    	return path("/os-volume-types", VolumeTypesResource.class);
    }

    public VolumesResource volumes() {
    	return path("/os-volumes", VolumesResource.class);
    }
    
    public HsInfoResource hsInfo(){
    	return path("/hs-info", HsInfoResource.class);
    }

    public SimpleTenantUsageResource usage() {
    	return path("/os-simple-tenant-usage", SimpleTenantUsageResource.class);
    }

    public QuotasResource quotas() {
    	return path("/os-quota-sets", QuotasResource.class);
    }

    public NetworksResource networks() {
    	return path("/os-networks", NetworksResource.class);
    }

    public FloatingIpsResource floatingIps() {
    	return path("/os-floating-ips", FloatingIpsResource.class);
    }

	public FloatingIpDnsResource floatingIpDns() {
		return path("/os-floating-ip-dns", FloatingIpDnsResource.class);
	}

	public CloudPipeResource cloudPipe() {
		return path("/os-cloudpipe", CloudPipeResource.class);
	}

	public AccountsResource accounts() {
		return path("/accounts", AccountsResource.class);
	}

    public KeyPairsResource keyPairs() {
    	return path("/os-keypairs", KeyPairsResource.class);
    }

    public SecurityGroupsResource securityGroups() {
    	return path("/os-security-groups", SecurityGroupsResource.class);
    }

    public SecurityGroupRulesResource securityGroupRules() {
    	return path("/os-security-group-rules", SecurityGroupRulesResource.class);
    }

    public ExtensionsResource extensions() {
    	return path("/extensions", ExtensionsResource.class);
    }

	public SnapshotsResource snapshots() {
		return path("/os-snapshots", SnapshotsResource.class);
	}
	
	public VpdcLanResource vpdcLans() {
		return path("/os-security-lan", VpdcLanResource.class);
	}
	
	public VpdcNetworkResource vpdcNetworks() {
		return path("/vpdc_networks", VpdcNetworkResource.class);
	}
	
	public IpResource ipResource(){
		return path("/os-floating-ip-pools", IpResource.class);
	}
	
	public HostActionsResource hostActionsResource(){
		return path("/host-actions", HostActionsResource.class);
	}
	
	public ImageBase createImage(ImageForCreate imageForCreate) {
		return target
				.path("/images")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(imageForCreate, MediaType.APPLICATION_JSON),
						ImageBase.class);
	}
	
}
