package cao.pipeline.simulator;

public class Utils {
	 static final String INT_FU_INSTRUCTIONS="ADD SUB LOAD STORE MOVC AND OR EX-OR BZ BNZ JUMP EXOR JAL";
	 static final String INT_ARITHMETIC_INSTRUCTIONS="ADD SUB AND OR EX-OR MUL EXOR DIV";
	 static final String REG_TO_REG_INSTRUCTIONS="ADD SUB AND OR EX-OR MUL EXOR DIV";
	 static final String PSW_FORWARD_INSTRUCTIONS="ADD SUB MUL DIV";
	 static final String LAST_FU_STAGE="INT MUL2 DIV4";
	 static final String reorder[]= {"F","DRF","IQ","INT","MUL1","MUL2","DIV1","DIV4","MEM"};
	 static final int ROB_SIZE=32;
	 static final int ISSUE_QUEUE_SIZE=16;
	 static final int PRF_NO=32;
	 static final String BRANCH_INSTRUCTIONS="JUMP JAL BZ BNZ";
	static boolean isValidInteger(String s) {
		int k=0;
		try {
			k=Integer.parseInt(s);
			if(k<=0)
				return false;
			
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	static int[] getSourceRegisters(String instruction) {
		String tokens[]=instruction.split(",");
		//instructions with 2 registers as sources
		if(INT_ARITHMETIC_INSTRUCTIONS.contains(tokens[0])) {
			String src1=tokens[2];
			src1=src1.replace("R","");
			
			String src2=tokens[3];
			src2=src2.replace("R","");
			int a[]= {Integer.parseInt(src1),Integer.parseInt(src2)};
			return a;
			
		}
		if(tokens[0].equalsIgnoreCase("LOAD")) {
			String src1=tokens[2];
			src1=src1.replace("R","");
			int a[]= {Integer.parseInt(src1)};
			return a;
		}
		if(tokens[0].equalsIgnoreCase("STORE")) {
			String src1=tokens[1];
			src1=src1.replace("R","");
			
			String src2=tokens[2];
			src2=src2.replace("R","");
			int a[]= {Integer.parseInt(src1),Integer.parseInt(src2)};
			return a;
			
		}
		if(tokens[0].equalsIgnoreCase("JUMP")) {
			String src1=tokens[1];
			src1=src1.replace("R","");
			int a[]= {Integer.parseInt(src1)};
			return a;
		}
		if(tokens[0].equalsIgnoreCase("BZ") || tokens[0].equalsIgnoreCase("BNZ")) {
			int a[]= {16};
			return a;
		}
		if(tokens[0].equalsIgnoreCase("JAL")) {
			String src=tokens[2];
			src=src.replace("R", "");
			int a[]= {Integer.parseInt(src)};
			return a;
		}
		
		
		int dummy[]= {};
		return dummy;
	}
	static int getDestinationRegister(String instruction) {
		String tokens[]=instruction.split(",");
		if(!tokens[0].equalsIgnoreCase("STORE") && !tokens[0].equalsIgnoreCase("BZ") && !tokens[0].equalsIgnoreCase("BNZ") && !tokens[0].equalsIgnoreCase("JUMP") && !tokens[0].equalsIgnoreCase("NOP") && !tokens[0].equalsIgnoreCase("HALT")) {
			String dest=tokens[1];
			if(dest.contains("R"))
				dest=dest.replace("R", "");
			else
				dest=dest.replace("P", "");
			return Integer.parseInt(dest);
			
			
		}
		return -1;
	}
	static int getPriority(String ins) {
		String instructionType=ins.split(",")[0];
			if(instructionType.equalsIgnoreCase("MUL"))
				return 2;
			if(instructionType.equalsIgnoreCase("DIV"))
				return 3;
			
		return 1;
		
	}
	static String getFU(String op) {
		if(op.equalsIgnoreCase("MUL"))
			return "MUL";
		if(op.equalsIgnoreCase("DIV") || op.equalsIgnoreCase("HALT"))
			return "DIV";
		
		return "ADD";
	}
	static String nextStage(String current_stage,String ins) {
		if(current_stage.equalsIgnoreCase("F"))
			return "DRF";
		if(current_stage.equalsIgnoreCase("DRF") && ins.equalsIgnoreCase("HALT")) 
			return "DRF";
		if(current_stage.equalsIgnoreCase("DRF")) 
			return "IQ";
		if(current_stage.equalsIgnoreCase("MUL1"))
			return "MUL2";
		
		if(current_stage.equalsIgnoreCase("DIV1"))
			return "DIV2";
		
		if(current_stage.equalsIgnoreCase("DIV2"))
			return "DIV3";
		
		if(current_stage.equalsIgnoreCase("DIV3"))
			return "DIV4";
		
		
		
			
		
		
		
		
		
		
		
		return "nil";
		
	}
	static String previousStage(String current_stage,String op) {
		if(current_stage.equalsIgnoreCase("DRF"))
			return "F";
		if(current_stage.equalsIgnoreCase("ADD"))
			return "DRF";
		
		if(current_stage.equalsIgnoreCase("MUL2"))
			return "MUL1";
		if (current_stage.equalsIgnoreCase("MUL1") )
			return "DRF";
		if(current_stage.equalsIgnoreCase("MEM")) {
			if(INT_FU_INSTRUCTIONS.contains(op)) {
				return "ADD";
				
			}
			else if(op.equalsIgnoreCase("MUL")) {
				return "MUL2";
			}
			else if(op.equalsIgnoreCase("DIV")) {
				return "DIV4";
			}
			
		}
			
		if(current_stage.equalsIgnoreCase("DIV1"))
			return "DRF";
		
		if(current_stage.equalsIgnoreCase("DIV2"))
			return "DIV1";
		if(current_stage.equalsIgnoreCase("DIV3"))
			return "DIV2";
		if(current_stage.equalsIgnoreCase("DIV4"))
			return "DIV3";
		
	
		
		return "nil";
	}
}
