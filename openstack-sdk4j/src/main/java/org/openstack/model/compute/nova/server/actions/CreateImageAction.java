package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;
import org.openstack.model.compute.nova.NovaServer;

/**
 * This operation creates a new image for the given server. 
 * 
 * Once complete, a new image will be available that can be used to rebuild or create servers. 
 * 
 * @author luis@woorea.es
 *
 */
@XmlRootElement(name="createImage")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("createImage")
public class CreateImageAction implements Serializable, ServerAction {

	@XmlAttribute(required=true)
	private String name;

	private Map<String, String> metadata = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	@Override
	public Class<? extends Serializable> getReturnType() {
		return String.class;
	}

}
