package org.openstack.api.compute.ext;

import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.SecurityGroupRule;
import org.openstack.model.compute.SecurityGroupRuleForCreate;
import org.openstack.model.compute.nova.securitygroup.NovaSecurityGroupRule;

public class SecurityGroupRulesResource extends Resource {
	
	public SecurityGroupRulesResource(Target target, Properties properties) {
		super(target, properties);
	}

    public SecurityGroupRule post(SecurityGroupRuleForCreate rule) {
		// OSAPI bug: Can't specify an SSH key in XML?
		return target.request(MediaType.APPLICATION_JSON).post(Entity.entity(rule, MediaType.APPLICATION_JSON), NovaSecurityGroupRule.class);
	}

    public SecurityGroupRuleResource rule(Integer id) {
    	return new SecurityGroupRuleResource(target.path("/{ruleId}").pathParam("ruleId", id), properties);
    }

}
