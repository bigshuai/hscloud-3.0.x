/**
 * @title InstanceVolumeTest.java
 * @package org.openstack.client.compute.test
 * @description 
 * @author YuezhouLi
 * @update 2012-6-4 下午5:16:52
 * @version V1.0
 */
package org.openstack.client.compute.test;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.TenantResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.client.JobHeader;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.Snapshot;
import org.openstack.model.compute.SnapshotList;
import org.openstack.model.compute.Volume;
import org.openstack.model.compute.nova.snapshot.NovaSnapshotForCreate;
import org.openstack.model.compute.nova.volume.NovaVolumeAttachment;
import org.openstack.model.compute.nova.volume.NovaVolumeForCreate;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-6-4 下午5:16:52
 */
public class InstanceVolumeTest extends AbstractOpenStackTest{

	protected TenantResource compute;
	@Before
	public void init(){
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint();
	}
	
	public void createVolume() throws Exception {
		System.out.println("ddddddddddd");
		NovaVolumeForCreate nv = new NovaVolumeForCreate();
		nv.setName("v2");
		nv.setSizeInGB(1);
//		nv.setAvailabilityZone("SC_ZONE");
		Volume volume = compute.volumes().post(nv);
		System.out.println(volume);
	}
	
	
	public void createVolumeBySnapshot(){
		NovaVolumeForCreate nv = new NovaVolumeForCreate();
		nv.setName("v2-recover");
		nv.setSizeInGB(1);
		nv.setSnapshotId(3);
		Volume volume = compute.volumes().post(nv);
		System.out.println(volume);
	}
	
	public void listVolumes(){
		List<Volume> volumes = compute.volumes().get().getList();
		for(Volume v : volumes){
			System.out.println(v.getName());
			System.out.println(v);
		}
	}
	
	public void attachVolume(){
		List<Volume> volumes = compute.volumes().get().getList();
		Volume volume = null;
		List<Server> serverss = compute.servers().get().getList();
		Server server = null;
		for(Server s : serverss){
			if(s.getName().equals("qq")){
				server = s;
			}
		}
		for(Volume v : volumes){
			System.out.println(v.getName());
			volume = v;
		}
		if(volume != null && server != null){
			NovaVolumeAttachment attachment = new NovaVolumeAttachment();
			attachment.setVolumeId(volume.getId());
			System.out.println("volume id :"+volume.getId());
			System.out.println("server id :"+server.getId());
			attachment.setDevice("/dev/vdd");
			compute.servers().server(server.getId()).attachments().post(attachment);
		}
	}
	public void dettachVolume(){
		List<Server> serverss = compute.servers().get().getList();
		Server server = null;
		for(Server s : serverss){
			if(s.getName().equals("dfd")){
				server = s;
			}
		}
		compute.servers().server(server.getId()).attachments().attachment(313).delete();
		compute.volumes().volume(313).delete();
		//compute.volumes().volume(312).delete();
	}
	
	public void snapshotVolume(){
		List<Volume> volumes = compute.volumes().get().getList();
		Volume volume = null;
		for(Volume v : volumes){
			if(v.getName().equals("v2")){
				volume = v;
				break;
			}
		}
		NovaSnapshotForCreate ns = new NovaSnapshotForCreate();
		ns.setVolumeId(volume.getId());
		ns.setName("v2-snapshot");
		ns.setDescription("This is a snapshot of volume");
		Snapshot snapshot = compute.snapshots().post(ns);
	}
	
	
	public void listSnapshots(){
		List<Snapshot> snapshots = compute.snapshots().get().getList();
		for(Snapshot s : snapshots){
			System.out.println(s);
		}
	}
	
	public void deleteSnapshot(){
		List<Snapshot> snapshots = compute.snapshots().get().getList();
		Snapshot snapshot = null;
		for(Snapshot s : snapshots){
			System.out.println(s);
			snapshot = s;
		}
		compute.snapshots().snapshot(snapshot.getId()).delete();
	}
	
	public void createImage(){
		compute.servers().server("111d7386-aeea-4de8-ae89-b867a09f154e").createImage("snapshot", null);
	}
	
	public void recoverVolumeSnapshot(){
		
	}
	@Test
	public void volumeCreate(){
		//compute.servers().server("3198fa4e-c58b-46db-b4bf-f56f4fa58eab").createAndAttachDisk("test2", 1);
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		compute.servers(job).server("3198fa4e-c58b-46db-b4bf-f56f4fa58eab").createAndAttachDisk("test2", 1);
	}
	
	public void volumeGet(){
		System.out.println("ddd"+compute.servers().server("0d7b4086-42b2-4f26-8ff1-0e71b70414f4").getScsis());
	}
	
	public void volumeDelete(){
		compute.servers().server("fa05c50c-7f9b-4600-a937-5a59c75d4785").deleteAndDetachDisk(2);
	}
	
}
