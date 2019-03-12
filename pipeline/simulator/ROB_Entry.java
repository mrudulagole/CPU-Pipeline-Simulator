package cao.pipeline.simulator;
public class ROB_Entry {
	int dest_reg;
	int value;
	boolean done_bit;
	boolean active;
	long insID;
	String instruction_string="";
	int instructionNumber=-1;
	ROB_Entry(){
		dest_reg = -1;
		value = 0;
		done_bit = false;
		active=false;
		instruction_string="";
		instructionNumber=-1;
	}
}
