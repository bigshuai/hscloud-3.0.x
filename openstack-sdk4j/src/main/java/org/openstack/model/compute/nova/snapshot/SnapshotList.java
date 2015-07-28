package org.openstack.model.compute.nova.snapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.Flavor;
import org.openstack.model.compute.nova.NovaFlavor;

@XmlRootElement(name = "snapshots")
@XmlAccessorType(XmlAccessType.NONE)
public class SnapshotList implements Serializable {

	@XmlElement(name = "snapshots")
	@JsonProperty("snapshots")
	private List<Snapshot> list = new ArrayList<Snapshot>();

	public SnapshotList() {
		this.list = new ArrayList<Snapshot>();
	}

	public List<Snapshot> getList() {
		return list;
	}

	public void setList(List<Snapshot> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "SnapshotList [list=" + list + "]";
	}
	
	public Iterator<Snapshot> iterator() {
		return getList().iterator();
	}

}
