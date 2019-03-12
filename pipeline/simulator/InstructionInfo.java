package cao.pipeline.simulator;

import java.util.ArrayList;

public class InstructionInfo {
	int PC;
	String instruction_string;
	String raw_instruction;
	int target_memory_addr,target_memory_data;
	int instructionNumber;
	String stage;
	boolean latency_stall;
	boolean stall;
	int priority;
	int destination_no;
	Source src1,src2;
	String type="";
	int literal;
	long insID;
	boolean inIssueQ,inLSQ,inROB;
	InstructionInfo(){
		latency_stall=false;
		stall=false;
		src1=new Source();
		src2=new Source();
		inIssueQ=inLSQ=inROB=false;
	}
	int cycleNo;
}
