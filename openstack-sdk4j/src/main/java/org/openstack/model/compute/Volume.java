package org.openstack.model.compute;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openstack.model.compute.nova.volume.NovaVolumeAttachment;

public interface Volume {

	Integer getId();

	String getStatus();

	Integer getSizeInGB();

	String getAvailabilityZone();

	String getType();

	String getCreatedAt();

	String getName();

	String getDescription();

	Integer getSnapshotId();

	Map<String, String> getMetadata();

	List<NovaVolumeAttachment> getAttachments();
}