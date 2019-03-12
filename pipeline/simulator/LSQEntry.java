package cao.pipeline.simulator;

public class LSQEntry {
	int status_bit,memory_address_valid,data_ready_bit;
	char load_or_store;
	int address,src_reg_no,value,instruction_no;
	int cycleNo;
	String instruction_string="";
	int load_reg_no;
	long insID;
	
	LSQEntry(){
		address=-1;
		instruction_no=-1;
		load_reg_no = -1;
		
	}
}
