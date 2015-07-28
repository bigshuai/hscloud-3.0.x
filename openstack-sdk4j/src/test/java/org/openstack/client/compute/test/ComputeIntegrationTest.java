/**
 * @title ComputeIntegrationTest.java
 * @package org.openstack.client.compute.test
 * @description 
 * @author YuezhouLi
 * @update 2012-5-30 上午10:36:53
 * @version V1.0
 */
package org.openstack.client.compute.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openstack.api.compute.HsInfoResource;
import org.openstack.api.compute.ServersResource;
import org.openstack.api.compute.TenantResource;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.client.JobHeader;
import org.openstack.model.compute.Flavor;
import org.openstack.model.compute.Image;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.ServerList;
import org.openstack.model.compute.nova.MetadataForCreateOrUpdate;
import org.openstack.model.compute.nova.NovaInstancesThin;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.openstack.model.compute.nova.NovaServerForCreate.NetworkLan;
import org.openstack.model.compute.nova.SnapshotBase;
import org.openstack.model.compute.nova.server.actions.RebuildAction;
import org.openstack.model.compute.nova.server.actions.SecurityIngressRules;
import org.openstack.model.compute.nova.server.actions.SecurityRules;
import org.openstack.model.compute.nova.server.actions.SetSecurityBandwidthRules;
import org.openstack.model.compute.nova.server.actions.SetSecurityBandwidthRules.BandwidthRule;
import org.openstack.model.compute.nova.server.actions.SetSecurityConnlimitRules;
import org.openstack.model.compute.nova.server.actions.SetSecurityConnlimitRules.ConnlimitRule;
import org.openstack.model.compute.nova.snapshot.SnapshotList;
import org.openstack.model.hscloud.impl.VifAdd;
import org.openstack.model.hscloud.impl.VifAdd.RequestedNetwork;
import org.openstack.model.hscloud.impl.VifRemove;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-30 上午10:36:53
 */
public class ComputeIntegrationTest extends AbstractOpenStackTest {

	protected TenantResource compute;

	@Before
	public void init() {
		init("etc/openstack.properties");
		compute = client.getComputeEndpoint("HK");
	}

	public void listFlavors() {
		Flavor bestFlavor = null;
		for (Flavor flavor : compute.flavors().get().getList()) {
			System.out.println(flavor.getName());
		}
	}

	public void listImages() {
		for (Image image : compute.images().get().getList()) {
			System.out.println(image.getName());
		}
		Image image = compute.images().get().getList().get(0);
		String ss = image.getMetadata().get("distro");
		System.out.println("distro:" + ss);
	}

	public void setImageMetadata() {
		MetadataForCreateOrUpdate m = new MetadataForCreateOrUpdate();
		m.setPasswd("QQQaaaa");
		m.setDesc("QQQQQQddddd");
		compute.images().image("75025ebe-8a99-4b9c-98b4-86f32d2056de")
				.setMetadata(m);
	}

	public void operateInstance() {
		ServersResource servers = compute.servers();
		for (Server server : servers.get().getList()) {
			System.out.println(server.getName());
		}
	}

	public void TempTest() {
		NovaServerForCreate.File f = new NovaServerForCreate.File();
		f.setContents("xxxx");
		f.setPath("ooxx.txt");
		List<NovaServerForCreate.File> lf = new ArrayList<NovaServerForCreate.File>();
		lf.add(f);
		System.out.println(f.getContents());
	}

	public void createInstance() {
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName("i-33");
		serverForCreate.setFlavorRef("1");
		serverForCreate.setImageRef("d71e6fb0-ae49-49ac-8642-df355c099d96");
		// froce associate host
		serverForCreate.setHost("nova", "openstack-n01", true);
		serverForCreate.setAdminPassword("12ddd34ssfd56");
		// serverForCreate.setJobId("1111");

		// NovaServerForCreate.File f = new NovaServerForCreate.File();
		// f.setContents("sssss");
		// f.setPath("ooxx.txt");
		// List<NovaServerForCreate.File> lf = new
		// ArrayList<NovaServerForCreate.File>();
		// lf.add(f);
		// serverForCreate.setPersonality(lf);
		//
		// serverForCreate.setReferenceId("beb92d89-9d6e-4f01-9c2c-1abd032207");
		// serverForCreate.setFloatingIp("11.0.0.10");
		// serverForCreate.setJobExt("qqqqqq");
		serverForCreate.setDisksize("disk1:10");

		// SET security rule
		serverForCreate.setSecurityLan(10);
		SecurityRules sr = new SecurityRules();
		SecurityRules.BandwidthRule br = new SecurityRules.BandwidthRule();
		br.setBwtIn(10);
		br.setBwtOut(10);
		SecurityRules.ConnlimitRule cr = new SecurityRules.ConnlimitRule();
		cr.setIpIn(50);
		cr.setIpOut(50);
		cr.setTcpIn(20);
		cr.setTcpOut(30);
		cr.setUdpIn(40);
		cr.setUdpOut(40);
		sr.setBandwidthRule(br);
		sr.setConnlimitRule(cr);
		serverForCreate.setSecurityRules(sr);

		JobHeader job = new JobHeader();
		// job.setJobId("22222");
		// job.setJobType("3333");
		// job.setJobExt("qqq");
		// not force assocciate host
		// serverForCreate.setHost("nova", "HSCloud003", true);

		// associate zone
		// serverForCreate.setZone("nova");

		// serverForCreate.setSecurityGroups(new
		// ArrayList<ServerForCreate.SecurityGroup>() {{
		// add(new ServerForCreate.SecurityGroup("test"));
		// }});

		Server server = compute.servers(job).post(serverForCreate);
	}
	
	//创建单网卡instance(通过Lan IP和Floating IP)
	public void createOneNetworkInstance_LanIPFloatingIP(){
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName("instance001");
		serverForCreate.setFlavorRef("6");
		serverForCreate.setImageRef("36cd007d-51c0-4de6-919c-2913d15396c7");
		serverForCreate.setHost("nova", "nova-main", true);
		serverForCreate.setFloatingIp("115.47.34.22");
		List<NetworkLan> networks = new ArrayList<NetworkLan>();
		
		NetworkLan nl = new NetworkLan();
		nl.setNetworkId("default");//注意：此参数固定只能传default值
		nl.setLanId(2);
		
		networks.add(nl);
		serverForCreate.setNetworks(networks);
		compute.servers().post(serverForCreate);
	}
	
	//创建单网卡instance(通过Lan IP)，在routed lan中创建虚拟机
	public void createOneNetworkInstance_LanIP(){
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName("instance001");
		serverForCreate.setFlavorRef("6");
		serverForCreate.setImageRef("36cd007d-51c0-4de6-919c-2913d15396c7");
		serverForCreate.setHost("nova", "nova-main", true);
		List<NetworkLan> networks = new ArrayList<NetworkLan>();
		
		//lan 网卡
		NetworkLan nl = new NetworkLan();
		nl.setNetworkId("71241fa5-d92a-410a-a995-f15cb1416c87");
		nl.setLanId(2);//必填项
		networks.add(nl);
		serverForCreate.setNetworks(networks);
		compute.servers().post(serverForCreate);
	}
	
	//创建单网卡instance(通过Wan IP)，虚拟机仅需要分配一个外网网卡
	public void createOneNetworkInstance_WanIP(){
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName("instance001");
		serverForCreate.setFlavorRef("6");
		serverForCreate.setImageRef("36cd007d-51c0-4de6-919c-2913d15396c7");
		serverForCreate.setHost("nova", "nova-main", true);
		List<NetworkLan> networks = new ArrayList<NetworkLan>();  
		
		//外网网卡
		NetworkLan nl = new NetworkLan();
		nl.setNetworkId("71241fa5-d92a-410a-a995-f15cb1416c87");
		networks.add(nl);
		
		serverForCreate.setNetworks(networks);
		compute.servers().post(serverForCreate);
	}
	
	//创建双网卡instance(通过Lan IP和Wan IP)
	public void createDoubleNetworkInstance_LanIPWanIP(){
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName("instance001");
		serverForCreate.setFlavorRef("6");
		serverForCreate.setImageRef("36cd007d-51c0-4de6-919c-2913d15396c7");
		serverForCreate.setHost("nova", "nova-main", true);
		List<NetworkLan> networks = new ArrayList<NetworkLan>();
		
		//Lan类型的network
		NetworkLan nl_lan = new NetworkLan();
		nl_lan.setNetworkId("71241fa5-d92a-410a-a995-f15cb1416c87");
		nl_lan.setLanId(2);
		networks.add(nl_lan);
		
		//Lan类型的network
		NetworkLan nl_wan = new NetworkLan();
		nl_wan.setNetworkId("667297cf-b3c2-41b3-b653-3d84793eff79");//必填项
		networks.add(nl_wan);
		
		serverForCreate.setNetworks(networks);
		compute.servers().post(serverForCreate);
	}

	//创建Vrouter(包含一条lan类型的network和一条wan类型的network)
	@Test
	public void createVrouter(){
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName("router001");
		serverForCreate.setFlavorRef("6");
		serverForCreate.setImageRef("36cd007d-51c0-4de6-919c-2913d15396c7");
		serverForCreate.setHost("nova", "nova-main", true);
		List<NetworkLan> networks = new ArrayList<NetworkLan>();
		
		//Wan类型的network
		NetworkLan nl_wan = new NetworkLan();
		//IP和networkUUID参数值通过判断needIP属性来调用供路由可用的空闲IP接口获得
		String IP = "192.168.2.103";
		String networkUUID = "667297cf-b3c2-41b3-b653-3d84793eff79";
		
		nl_wan.setNetworkId(networkUUID);
		nl_wan.setFixedIp(IP);
		networks.add(nl_wan);
		
		serverForCreate.setNetworks(networks);
		compute.servers().post(serverForCreate);
	}
	
	//添加网卡(支持添加多个，下例为添加两个)
	public void addVif(){
		VifAdd va = new VifAdd();
		List<VifAdd.RequestedNetwork> lvarn = new ArrayList<VifAdd.RequestedNetwork>();
		//第一个
		VifAdd.RequestedNetwork varn = new VifAdd.RequestedNetwork();
		varn.setNetworkId("71241fa5-d92a-410a-a995-f15cb1416c87");
		varn.setLanId(2);
		lvarn.add(varn);
		//第二个
		VifAdd.RequestedNetwork varn2 = new VifAdd.RequestedNetwork();
		varn2.setNetworkId("667297cf-b3c2-41b3-b653-3d84793eff79");
		lvarn.add(varn2);
		
		va.setRequestedNetworks(lvarn);
		compute.servers().server("7649c551-37f8-4d32-addb-16ed06469136").addVif(va);
	}

	//删除网卡(支持删除多个，下例为删除一个)
	public void removeVif(){
		VifRemove vr = new VifRemove();
		List<VifRemove.RequestedNetwork> lvrrn = new ArrayList<VifRemove.RequestedNetwork>();
		//待删的网卡
		VifRemove.RequestedNetwork vrrn = new VifRemove.RequestedNetwork();
		vrrn.setNetworkId("71241fa5-d92a-410a-a995-f15cb1416c87");
		vrrn.setFixedIp("192.168.101.5");
		lvrrn.add(vrrn);
		
		vr.setRequestedNetworks(lvrrn);
		compute.servers().server("7649c551-37f8-4d32-addb-16ed06469136").removeVif(vr);
	}
	
	public void expandDIsk() {
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		String[] ss = { "1:70" };
		compute.servers(job).server("ee0c49b2-6cb4-49a3-b4cf-8c1dabeac2ac")
				.expandDiskSize(ss);
	}

	public void setSecurityRules() {
		SecurityIngressRules sr = new SecurityIngressRules();

		// ingress rule, outer network rules
		SecurityIngressRules.IngressRule ir = new SecurityIngressRules.IngressRule();
		ir.setFromPort(11);
		ir.setToPort(22);
		ir.setIpProtocol("tcp");// tcp;udp;icmp;all(tcp&udp)
		ir.getIpRange().setCidr("0.0.0.0/0");

		SecurityIngressRules.IngressRule ir2 = new SecurityIngressRules.IngressRule();
		ir2.setFromPort(22);
		ir2.setToPort(33);
		ir2.setIpProtocol("tcp");
		ir2.getIpRange().setCidr("0.0.0.0/0");

		List<SecurityIngressRules.IngressRule> irs = new ArrayList<SecurityIngressRules.IngressRule>();
		irs.add(ir2);
		irs.add(ir);

		sr.setIngressRules(irs);

		compute.servers().server("f375e177-0416-46dd-8de9-126be8ad77e2")
				.setSecurityIngressRules(sr);
	}

	public void setSecurityLan() {
		// set security lan id
		compute.servers().server("f375e177-0416-46dd-8de9-126be8ad77e2")
				.setSecurityLan(10);
	}

	public void removeSecurityLan() {
		// remove security lan id
		compute.servers().server("f375e177-0416-46dd-8de9-126be8ad77e2")
				.removeSecurityLan(10);
	}

	public void injectAdminPass() {
		compute.servers().server("f375e177-0416-46dd-8de9-126be8ad77e2")
				.injectAdminPassword("qqdd33ss");
	}

	// 测试连接数限制API
	public void testSetSecurityConnlimitRules(){
			SetSecurityConnlimitRules sscr = new SetSecurityConnlimitRules();
			ConnlimitRule cr = new ConnlimitRule();
			cr.setIpIn(1111);
			cr.setIpOut(2222);
			cr.setTcpIn(1111);
			cr.setTcpOut(2222);
			cr.setUdpIn(1111);
			cr.setUdpOut(2222);
			sscr.setConnlimitRule(cr);
			compute.servers().server("f375e177-0416-46dd-8de9-126be8ad77e2").setSecurityConnlimitRules(sscr);
		}

	// 测试带宽限制API
	public void testSetSecurityBandwidthRules(){
		SetSecurityBandwidthRules ssbr = new SetSecurityBandwidthRules();
		BandwidthRule br = new BandwidthRule();
		br.setBwtIn(10);
		br.setBwtOut(20);
		ssbr.setBandwidthRule(br);
			compute.servers().server("f375e177-0416-46dd-8de9-126be8ad77e2").setSecurityBandwidthRules(ssbr);
		}

	public void getInstance() {
		/*
		 * ServersResource servers = compute.servers(); List<Server> serverss =
		 * servers.get().getList(); Server server = serverss.get(2); String uuid
		 * = server.getId(); System.out.println(server.getName());
		 * ServerResource serverResource = servers.server(uuid); //
		 * serverResource.reboot("SOFT"); RebuildAction rebuildAction = new
		 * RebuildAction();
		 * rebuildAction.setImageRef("e12d087a-0f2f-4c9f-b406-f146f2ab129d");
		 * serverResource.rebuild(rebuildAction);
		 */
		// serverResource.reboot("HARD");

		ServersResource servers = compute.servers();
		// ServerResource serverResource =
		// servers.server("8574db0a-4f0d-4c8a-a9c4-37ae5fa7cb56");
		long begin = new Date().getTime();
		// serverResource.get();
		System.out.println("ddddd");
		// Map a = servers.get("HSCloud002").getList().get(0).getAddresses();
		long end = new Date().getTime();
		// System.out.println(a);
		System.out
				.println("+++++++The vm detail time totla : " + (end - begin));

	}

	public void confirmInstance() {
		compute.servers().server("21f144c7-ecef-44ff-bf5f-8b73e7373608")
				.confirmResize();
	}

	public void migrate() {
		compute.servers().server("64fd3c4d-30ca-4060-87a8-6f1dcbc53acd")
				.migrate();
	}

	/**
	 * <虚拟机绑定/删除IP> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void floatIPattach() {
		// FloatingIp floatingIp = compute.floatingIps().post(null);
		// System.out.println(floatingIp);
		// compute.servers().server("e4138297-2f4a-4549-b8d6-67567b9311f9").addFloatingIp(floatingIp.getIp());
		String ip = "192.168.13.10";
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		compute.servers().server("e4138297-2f4a-4549-b8d6-67567b9311f9")
				.addFloatingIp(ip);
		compute.servers().server("e4138297-2f4a-4549-b8d6-67567b9311f9")
				.removeFloatingIp(ip);
		// System.out.println(compute.servers().server("e4138297-2f4a-4549-b8d6-67567b9311f9").get());
	}

	public void resize() {
		compute.servers().server("e4ab879c-952f-401b-bc5a-d9dc41e65de2")
				.resize("62");
	}

	public void delete() {
		// compute.servers().server("fd2fb2a3-8d83-453f-b22d-2436a8450845").delete();
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		compute.servers(job).server("fd2fb2a3-8d83-453f-b22d-2436a8450845")
				.delete();
		// RebuildAction ra = new RebuildAction();

	}

	public void forceDelete() {
		compute.servers().server("839758dd-e690-4ada-8226-62ac5c0d6486")
				.getConsoleOutput(1);
	}

	public void updateStatus() {
		compute.servers().server("fd2fb2a3-8d83-453f-b22d-2436a8450845")
				.updateStatus();
	}

	public void migrate2() {
		compute.servers().server("5bf744b4-25d1-4cdd-b040-9c6df5511e95")
				.migrate2("HSCloud003");
	}
	
	public void snapshotCreate() {
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		SnapshotBase sb = compute.servers(job)
				.server("2c9cf542-3f1d-4c32-a3d3-34a370e8589f")
				.createSnapshot("test1");
		System.out.println(sb);
	}

	public void snapshotDelete() {
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		compute.servers(job).server("ff59e805-172f-4abb-bde9-90dc8daa5720")
				.deleteSnapshot("test1-2013-01-14-171657");
	}

	public void snapshtoList() {
		SnapshotList l = compute.servers()
				.server("7561daa9-6b73-4751-b6c2-a160ad40770e").getSnapshots();
		System.out.println(l);
	}

	public void snapshotRecover() {
		compute.servers().server("8757a714-9593-4d6d-9edd-40a1c2993484")
				.recoverSnapshot("test1-2013-01-11-093506");
	}

	public void hsResize() {
		compute.servers().server("fe41ee07-2ac6-4198-8ac5-0fb309f8c18e")
				.hsResize(1000);
	}

	public void hsResizeConfirm() {
		compute.servers().server("fe41ee07-2ac6-4198-8ac5-0fb309f8c18e")
				.hsResizeConfirm();
	}

	public void hsResizeRevert() {
		compute.servers().server("fe41ee07-2ac6-4198-8ac5-0fb309f8c18e")
				.hsResizeRevert();
	}

	public void listThinTest() {
		Date d = new Date();
		long time1 = d.getTime();
		NovaInstancesThin t = compute.servers().getThin();
		Date d2 = new Date();
		long time2 = d2.getTime();
		System.out.println((time2 - time1) + " " + t.getList().size());

		d = new Date();
		long time3 = d.getTime();
		ServerList l = compute.servers().get();
		d2 = new Date();
		long time4 = d2.getTime();
		System.out.println((time4 - time3) + " " + l.getList().size());

		d = new Date();
		long time5 = d.getTime();
		compute.servers().server("c77e3894-aba4-48e2-82c7-5e5b4836bb15").get();
		d2 = new Date();
		long time6 = d2.getTime();
		System.out.println(time6 - time5);

		d = new Date();
		long time7 = d.getTime();
		Server s = compute.servers()
				.server("b1fffa22-8cf7-409f-b338-e37ad2187342").getThin();
		d2 = new Date();
		long time8 = d2.getTime();
		System.out.println(time8 - time7);
		System.out.println(s);

	}

	public void listThinTestNew() {
		HsInfoResource info = compute.hsInfo();
		Date d = new Date();
		long time1 = d.getTime();
		// NovaInstancesThin t = info.getServersThin();
		Date d2 = new Date();
		long time2 = d2.getTime();
		// System.out.println("1 : "+(time2 - time1));

		d = new Date();
		long time3 = d.getTime();
		ServerList l = compute.servers().get();
		d2 = new Date();
		long time4 = d2.getTime();
		System.out.println("2 : " + (time4 - time3) + " " + l.getList().size());

		d = new Date();
		long time5 = d.getTime();
		compute.servers().server("c77e3894-aba4-48e2-82c7-5e5b4836bb15").get();
		d2 = new Date();
		long time6 = d2.getTime();
		System.out.println("3: " + (time6 - time5));

		d = new Date();
		long time7 = d.getTime();
		Server s = info.getServerThin("c77e3894-aba4-48e2-82c7-5e5b4836bb15");
		d2 = new Date();
		long time8 = d2.getTime();
		System.out.println("4:" + (time8 - time7));
		System.out.println(s);
	}

	public void opsVM() {
		JobHeader job = new JobHeader();
		job.setJobId("22222");
		job.setJobType("3333");
		job.setJobExt("qqq");
		// 关闭VM
		// compute.servers().server("60ac0445-2742-4582-9215-0c83ed85db0e").suspend();
		// 启动VM
		// compute.servers(job).server("60ac0445-2742-4582-9215-0c83ed85db0e").resume();
		// 重启VM
		// compute.servers(job).server("60ac0445-2742-4582-9215-0c83ed85db0e").reboot("HARD");
		// 重置系统
		RebuildAction action = new RebuildAction();
		String imageId = "0a6bfe9d-9592-40e7-83eb-0c71e33d2c57";
		action.setImageRef(imageId);
		action.setAdminPass("ssssss");
		compute.servers(job).server("2c9cf542-3f1d-4c32-a3d3-34a370e8589f")
				.rebuild(action);
	}
	
	/**
	 * 虚拟机系统修复功能
	 */
	public void testOsRepair(){
		JobHeader job = new JobHeader();
		compute.servers(job).server("2c9cf542-3f1d-4c32-a3d3-34a370e8589f").osRepair();
	}
	
	/**
	 * 虚拟机恢复功能
	 */
	public void testVMRestore(){
		JobHeader job = new JobHeader();
		compute.servers(job).server("2c9cf542-3f1d-4c32-a3d3-34a370e8589f").restore();
	}
	
	/**
	 * 虚拟机彻底删除功能
	 */
	public void testVMForceDelete(){
		JobHeader job = new JobHeader();
		compute.servers(job).server("2c9cf542-3f1d-4c32-a3d3-34a370e8589f").forceDelete();
	}
	
	/**
	 * 迁移测试
	 */
	public void testOsColdMigrate() {
		JobHeader job = new JobHeader();
		compute.servers(job).server("9e7c7555-b176-4996-92b7-1b05a79567a3").osColdMigrate("ppenstack", "192.168.177.81");
	}
}
