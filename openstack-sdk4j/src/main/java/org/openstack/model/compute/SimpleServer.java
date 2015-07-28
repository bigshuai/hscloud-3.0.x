package org.openstack.model.compute;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openstack.model.atom.Link;
import org.openstack.model.compute.nova.NovaAddressList.Network;

public interface SimpleServer {

	String getId();

	
	List<Link> getLinks();

}