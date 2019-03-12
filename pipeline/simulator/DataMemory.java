package cao.pipeline.simulator;

public class DataMemory {
	int base_address;
	int[] data_array=new int[4000];
	
	DataMemory() {
		base_address=0;
		
	}
	int getBaseAddress() {
		
		return this.base_address;
	}
	void setBaseAddress(int base_address) {
		
		this.base_address=base_address;
	}

}
