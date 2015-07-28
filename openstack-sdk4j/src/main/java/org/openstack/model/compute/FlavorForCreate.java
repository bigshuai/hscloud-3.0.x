package org.openstack.model.compute;

public interface FlavorForCreate {
	int getId();
	
	String getName();

	int getRam();

	int getVcpus();

	int getRxTxFactor();

	int getDisk();
	
	int getSwap();

	int getEphemeral();
	
	void setEphemeral(int ephemeral);
	
	void setSwap(int swap);
	
	void setId(int id);
	
	void setName(String name);

	void setRam(int ram);

	void setVcpus(int vcpus);

	void setRxTxFactor(int rxTxFactor);

	void setDisk(int disk);
}
