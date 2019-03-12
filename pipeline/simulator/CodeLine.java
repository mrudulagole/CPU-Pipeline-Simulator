package cao.pipeline.simulator;

public class CodeLine {
	int file_line_number,address;
	String instruction_string;
	
	CodeLine(){
		file_line_number=0;
		address=4000;
		instruction_string="";
		}
	int getFileLineNumber() {
		return this.file_line_number;
	}
	void setFileLineNumber(int file_line_number) {
		this.file_line_number=file_line_number;
		
	}
	int getAddress() {
		return this.address;
	}
	void setAddress(int address) {
		this.address=address;
	}
	
	String getInstructionString() {
		return this.instruction_string;
	}
	
	void setInstructionString(String instruction_string) {
		this.instruction_string=instruction_string;
	}
	

}
