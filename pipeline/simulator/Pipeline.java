package cao.pipeline.simulator;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pipeline {
	static CodeMemory code_memory;
	static DataMemory data_memory;
	ArrayList<InstructionInfo>  pipelineTODOs=new ArrayList<InstructionInfo>();
	int PC;
	IssueQueue issueQueue[]=new IssueQueue[Utils.ISSUE_QUEUE_SIZE];
	int issueQueuePtr;
	int cycleNo;
	LSQEntry Mem=null;
	PRF prf[]=new PRF[Utils.PRF_NO];
	ROB rob=new ROB();
	RAT[] rat=new RAT[16];
	ARF[] arf=new ARF[16];
	String[] instructions=new String[200];
	int[] instruct_no=new int[200];
	ArrayList<LSQEntry> lsq=new ArrayList<LSQEntry>();
	int MEM_counter=0;
	long id=1;
	boolean halt=false;
	boolean simulation_stopped=false;
	ArrayList<String> commit=new ArrayList<String>();
	public Pipeline() {
		int i;
		PC=3996;
		for(i=0;i<Utils.ISSUE_QUEUE_SIZE;i++) {
			issueQueue[i]=new IssueQueue();
		}
		for(i=0;i<rat.length;i++) {
			rat[i]=new RAT();
		}
		for(i=0;i<arf.length;i++) {
			arf[i]=new ARF();
		}
		for(i=0;i<prf.length;i++) {
			prf[i]=new PRF();
		}
		issueQueuePtr=-1;
	}
	private InstructionInfo getInstruction(String stageName) {
		for(InstructionInfo item : pipelineTODOs) {
			if(item.stage.equalsIgnoreCase(stageName))
				return item;
		}
		return null;
	}
	void displayCycles(int cycle) {
		System.out.println("===============================================================================");
		System.out.println("Cycle "+cycle+" :");
		
		InstructionInfo fetch=getInstruction("F");
		InstructionInfo decode=getInstruction("DRF");
		InstructionInfo intfu=getInstruction("INT");
		
		InstructionInfo mul1=getInstruction("MUL1");
		InstructionInfo mul2=getInstruction("MUL2");
		InstructionInfo div1=getInstruction("DIV1");
		
		InstructionInfo div2=getInstruction("DIV2");
		InstructionInfo div3=getInstruction("DIV3");
		InstructionInfo div4=getInstruction("DIV4");
		
		
		
		System.out.println("Fetch"+"\t"+": "+
		(fetch==null? "Empty" :("( I"+fetch.instructionNumber+" )"+" "+
				fetch.raw_instruction+" ")));
		
		System.out.println("DRF"+"\t"+": "+
				(decode==null? "Empty" :("( I"+decode.instructionNumber+" )"+" "+
						decode.raw_instruction+" ")));
		
		displayRAT();
		System.out.println();
		displayIQ();
		System.out.println();
		displayROB();
		System.out.println();
		System.out.println("Commit:");
		for(String commited_instruction : commit)
			System.out.println(commited_instruction);
		System.out.println();
		displayLSQ();
		System.out.println();
		System.out.println("INTFU"+"\t"+": "+
				(intfu==null? "Empty" :("( I"+intfu.instructionNumber+" )"+" "+
						intfu.instruction_string+" ")));
		
		System.out.println("MUL1"+"\t"+": "+
				(mul1==null? "Empty" :("( I"+mul1.instructionNumber+" )"+" "+
						mul1.instruction_string+" ")));
		
		System.out.println("MUL2"+"\t"+": "+
				(mul2==null? "Empty" :("( I"+mul2.instructionNumber+" )"+" "+
						mul2.instruction_string+" ")));
		
		System.out.println("DIV1"+"\t"+": "+
				(div1==null? "Empty" :("( I"+div1.instructionNumber+" )"+" "+
						div1.instruction_string+" ")));
		
		System.out.println("DIV2"+"\t"+": "+
				(div2==null? "Empty" :("( I"+div2.instructionNumber+" )"+" "+
						div2.instruction_string+" ")));
		
		System.out.println("DIV3"+"\t"+": "+
				(div3==null? "Empty" :("( I"+div3.instructionNumber+" )"+" "+
						div3.instruction_string+" ")));
		
		System.out.println("DIV4"+"\t"+": "+
				(div4==null? "Empty" :("( I"+div4.instructionNumber+" )"+" "+
						div4.instruction_string+" ")));
		
	
		displayMEM();
		
	
	
	
	
	
	
	
	
	
	
	
	
	}
	private void displayLSQ() {
		// TODO Auto-generated method stub
		if(lsq.isEmpty())
			System.out.println("<LSQ> :"+"Empty");
		else {
			System.out.println("<LSQ>");
			for(LSQEntry l : lsq)
				System.out.println("("+l.instruction_no+")"+" "+l.instruction_string);
		}
		
		
	}
	private void displayROB() {
		// TODO Auto-generated method stub
		if(rob.isEmpty())
			System.out.println("<ROB> :"+"Empty");
		else {
			System.out.println("<ROB>");
			rob.display_ROB(1);
		}
		
	}
	private void displayIQ() {
		// TODO Auto-generated method stub
		if(issueQueuePtr==-1)
			System.out.println("<IQ> :"+"Empty");
		else {
			System.out.println("<IQ>");
			for(InstructionInfo item: pipelineTODOs) {
				if(item.stage.equalsIgnoreCase("IQ"))
				System.out.println("(I"+item.instructionNumber+")"+" "+item.instruction_string);
			}
		}
		
	}
	private void displayRAT() {
		// TODO Auto-generated method stub
		int i;
		
		if(RATEmpty())
			System.out.println("<RENAME TABLE> :"+"Empty");
		else {
			System.out.println("<RENAME TABLE>");
			for(i=0;i<rat.length;i++) {
				if(rat[i].rp_bit=='P')
					System.out.println("R"+i+" "+": "+"P"+ rat[i].physical_register);
			}
		}
		
	}
	void displayMEM() {
		if(MEM_counter==0)
			System.out.println("MEM\t: Empty");
		else
			System.out.println("MEM\t:"+"(I"+Mem.instruction_no+")"+Mem.instruction_string);
	}
	boolean RATEmpty() {
		for(int i=0;i<rat.length;i++) {
			if(rat[i].rp_bit=='P')
				return false;
		}
		return true;
	}
	void display_pipelineSteps() {
			// TODO Auto-generated method stub
		
			System.out.println("*************************************************************************");
			for(InstructionInfo item : pipelineTODOs) {
				
				System.out.println("I"+item.instructionNumber+" "+item.stage);
			
				
				
				
				if(item.stage=="DRF")
				{
					instructions[1]=item.instruction_string;
					instruct_no[1]=item.instructionNumber;
				}
				if(item.stage=="F")
				{
					instructions[0]=item.instruction_string;
					instruct_no[0]=item.instructionNumber;
				}
				
	
			}
			
		/*	int u=1;
			for(int i=0;i<3;i++)
			{
				if(u==1)
				{	
				
				System.out.print("Fetch    :"); 
                if(instructions[0]==null)
                {
                    System.out.println("empty");
                }
                else
                {    
                System.out.println("(I"+instruct_no[0]+")"+ instructions[0]);
                }
                System.out.print("Decode   :"); 
                if(instructions[1]==null)
                {
                    System.out.println("empty");
                }
                else
                {    
                System.out.println("(I"+instruct_no[1]+")"+ instructions[1]);
                }
				}
				u=0;
				
			}*/
			
			System.out.println("$$$$$$$$$$$$$$$ROB Content$$$$$$$$$$$");
			rob.display_ROB();
			System.out.println("*************************************************************************");
			
	}
	void display() {
		int i;
	System.out.println("********Register status********");
	for(i=0;i<16;i++) 
		System.out.println(i+" => "+arf[i].ar_value);
	
	System.out.println("********Memory Status********");
	for(i=0;i<100;i++) 
		System.out.println("DataMemory["+i+"]="+data_memory.data_array[i]);
	
		
	}
	 void simulate(int n) {
			// TODO Auto-generated method stub
			//System.out.println("In simulate "+n);
			  int i;
			  System.out.println("Simulation begins...");
			  for(i=1;i<=n;i++) {
				  commit=new ArrayList<String>();
				  
				  cycleNo=i;
				  rob_commit();
				  lsq();
					mem();
				  if (!pipelineTODOs.isEmpty()) {
						executePipelineTODOs();
						}
					fetch();
					displayCycles(cycleNo);
					//rob.display_ROB();
					removeCompletedInstructions();
					updateTODOsForNextCycle();
				  
				  if(simulation_stopped) {
					  System.out.println("Exiting as HALT is encountered");
					  break;
				  }
				  
			  }
			
		  for(LSQEntry l:lsq)
			  System.out.println(l.cycleNo+" "+l.instruction_string+" "+l.insID+" "+l.address+" "+l.value+" "+l.load_reg_no);
		  }
		 
		private void removeCompletedInstructions() {
		// TODO Auto-generated method stub
		
		  int index=-1,i=0;
		  //REMOVE FROM INT FU
		  
		  //!!!!!!!!!!DONT FORGET TO ADD CONDITIONS FOR LOAD AND STORE LATER
			for(InstructionInfo item : pipelineTODOs) {
				
				if(item.stage.equalsIgnoreCase("INT")) {
					index=i;
					break;
				}
				i++;
			}
			
			if(index!=-1) 
				pipelineTODOs.remove(index);
			
			//REMOVE FROM MUL2 FU
			index=-1;i=0;
			for(InstructionInfo item : pipelineTODOs) {
				
				if(item.stage.equalsIgnoreCase("MUL2")) {
					index=i;
					break;
				}
				i++;
			}
			
			if(index!=-1) 
				pipelineTODOs.remove(index);
			
			//REMOVE FROM DIV4 FU
			index=-1;i=0;
			for(InstructionInfo item : pipelineTODOs) {
				
				if(item.stage.equalsIgnoreCase("DIV4")) {
					index=i;
					break;
				}
				i++;
			}
			
			if(index!=-1) 
				pipelineTODOs.remove(index);
	}

		private void updateTODOsForNextCycle() {
			// TODO Auto-generated method stub
		int issue[]= {-1,-1,-1};
			ArrayList<Integer> remove=new ArrayList<Integer>();
			int i;
			for(i=0;i<=issueQueuePtr;i++) {
				if(issueQueue[i].src1.valid==1 && issueQueue[i].src2.valid==1 && issue[issueQueue[i].FU_type]==-1 && issueQueue[i].inQ) {
					issue[issueQueue[i].FU_type]=issueQueue[i].instruction_no;
					remove.add(issueQueue[i].instruction_no);
				}
			}
			
			//Update for only the instructions in IQ that can be moved ahead
			int index=0;
			for(InstructionInfo item : pipelineTODOs) {
				if(item.stage.equalsIgnoreCase("IQ")) {
					if(item.instructionNumber==issue[0])
						item.stage="INT";
					else if(item.instructionNumber==issue[1])
						item.stage="MUL1";
					else if(item.instructionNumber==issue[2])
						item.stage="DIV1";
					pipelineTODOs.set(index, item);
				}
				index++;
			}
			
			
			for(int x: remove) {
				
				removeAndShift(x);
			}
			i=0;
			for(InstructionInfo item : pipelineTODOs) {
				
				String op[]=item.instruction_string.split(",");
				if(item.instructionNumber!=issue[0] && item.instructionNumber!=issue[1] && item.instructionNumber!=issue[2] && !item.stage.equalsIgnoreCase("IQ")) {
				item.stage=Utils.nextStage(item.stage,op[0]);
				pipelineTODOs.set(i, item);
				}
				i++;
			}
			
			if(MEM_counter==4) {
				MEM_counter=0;
				Mem=null;
			}
			
		}
		void removeAndShift(int instruction_no) {
			int i,k=-1;
			
			for(i=0;i<=issueQueuePtr;i++) {
				if(issueQueue[i].instruction_no==instruction_no) {
						k=i;
						break;
				}
			}
			
			for(i=k;i<issueQueuePtr;i++)
				issueQueue[i]=issueQueue[i+1];
			issueQueuePtr--;
			
			
			
			
		}
		

		private void executePipelineTODOs() {
			// TODO Auto-generated method stub
			
			reorderPipeline();
			//rob_commit();
			
			for(InstructionInfo item : pipelineTODOs) {
				switch(item.stage) {
				case "F": break;
				case "DRF" : decode(item);break;
				case "IQ"	:iq(item);break;
				case "INT" :int_FU(item);break;
				case "MUL1":break;
				case "MUL2" :mul2(item); break;
				case "DIV1" : break;
				case "DIV2" : break;
				case "DIV3" : break;
				case "DIV4" :div4(item); break;
				
				}
			}
			
			
		}

		private void iq(InstructionInfo item) {
			// TODO Auto-generated method stub
			//set inIQ field to true
			int i=0;
//4)Put the entry in issue queue
			if(!item.inIssueQ) {
			issueQueuePtr++;
			issueQueue[issueQueuePtr]=new IssueQueue();
			issueQueue[issueQueuePtr].src1=item.src1;
			issueQueue[issueQueuePtr].src2=item.src2;
			issueQueue[issueQueuePtr].dest_no=item.destination_no;
			issueQueue[issueQueuePtr].cycle_no=cycleNo;
			issueQueue[issueQueuePtr].instruction_no=item.instructionNumber;
			if(Utils.INT_FU_INSTRUCTIONS.contains(item.type)) {
				issueQueue[issueQueuePtr].FU_type=0;
			}
			else if(item.type.equalsIgnoreCase("MUL")) {
				issueQueue[issueQueuePtr].FU_type=1;
				
			}
			else if(item.type.equalsIgnoreCase("DIV") || item.type.equalsIgnoreCase("HALT")) {
				issueQueue[issueQueuePtr].FU_type=2;
			}
			item.inIssueQ=true;
			}
			
			//5)Put Entry in ROB
			if(!item.inROB) {
			ROB_Entry rob_entry=new ROB_Entry();
			if(item.type.equalsIgnoreCase("STORE"))
			rob_entry.dest_reg=-1;
			else
			rob_entry.dest_reg=item.destination_no;
			
			rob_entry.instruction_string=item.instruction_string;
			rob_entry.active=true;
			rob_entry.done_bit=false;
			rob_entry.insID=item.insID;
			rob_entry.instructionNumber=item.instructionNumber;
			
			if(Utils.BRANCH_INSTRUCTIONS.contains(item.type))
				rob_entry.done_bit=true;
			
			rob.insert(rob_entry);
			item.inROB=true;
			}
			
			if(!item.inLSQ) {
				//7)Put Entry in LSQ
				
				if(item.type.equalsIgnoreCase("LOAD") || item.type.equalsIgnoreCase("STORE")) {
					LSQEntry lq=new LSQEntry();
					
					if(item.type.equalsIgnoreCase("LOAD")) {
						lq.load_reg_no=item.destination_no;
						
						lq.load_or_store='L';
					}
					if(item.type.equalsIgnoreCase("STORE"))
						lq.load_or_store='S';
					lq.instruction_string=item.instruction_string;
					lq.instruction_no=item.instructionNumber;
					lq.insID=item.insID;
					
					lsq.add(lq);
					issueQueue[issueQueuePtr].objectHashLSQ=item.insID;
						
					item.inLSQ=true;	
				}
			}
			
					item.cycleNo=cycleNo;
					for(i=0;i<=issueQueuePtr;i++) {
						if(issueQueue[i].instruction_no==item.instructionNumber)
							issueQueue[i].inQ=true;
					}
			
		}

		private void rob_commit() {
			// TODO Auto-generated method stub
			for(int x=1;x<=2;x++) {
			ROB_Entry peek=rob.peek();
			int arf_dest=-1,i;
			if(peek!=null && peek.done_bit) {
				//1)Check if physical register is tagged to any arch register
				//2)If tagged then modify arf
				for(i=0;i<rat.length;i++) {
					if(rat[i].physical_register==peek.dest_reg && rat[i].rp_bit=='P') {
						arf[i].ar_value=peek.value;
						rat[i].physical_register=-1;
						rat[i].rp_bit='R';
						break;
					}
				}
				
				//3)Free physical register
				if(peek.dest_reg!=-1 && peek.dest_reg!=-999) {
				prf[peek.dest_reg].pr_allocated=false;
				prf[peek.dest_reg].pr_busy=false;
				}
				
				
				//4)remove from ROB & set active to false
			//	System.out.println("Removing "+peek.insID);
				commit.add("(I"+peek.instructionNumber+")"+" "+peek.instruction_string);
				if(peek.dest_reg==-999)
					simulation_stopped=true;
				rob.remove();
			}
			
			}
		}

		private void mem() {
			// TODO Auto-generated method stub
			if(MEM_counter==0 && !lsq.isEmpty())
			{	 
				LSQEntry head=lsq.get(0);
				if(head.load_or_store=='L' && head.address!=-1)
				{
					Mem=lsq.remove(0);
					MEM_counter++;

				}
				
				ROB_Entry peek=rob.peek();
				
				if(head.load_or_store=='S' && head.address!=-1 && head.insID==peek.insID)
				{
					Mem=lsq.remove(0);
					writetoROB(-1,0);
					MEM_counter++;

				}
				
				
			}
			
			if(Mem!=null)
			{
			//	System.out.println("In Memory for instruction "+Mem.instruction_string+" MemCycle="+MEM_counter);
				MEM_counter++;
			}
			if(MEM_counter==4)
			{
				if(Mem.load_or_store=='L')
				{
					prf[Mem.load_reg_no].pr_value=data_memory.data_array[Mem.address];
					prf[Mem.load_reg_no].pr_busy=false;
					writetoROB(Mem.load_reg_no,data_memory.data_array[Mem.address]);
					forwardResultToIQ(Mem.load_reg_no,data_memory.data_array[Mem.address]);
				}
				if(Mem.load_or_store=='S')
				{
					data_memory.data_array[Mem.address]=Mem.value;
					
				}
			}
		}
		private void lsq() {
			boolean forward =false, bypass = true;
			int store_index = -1;
			long store_insID=-1;
			int i=0,load_entry=-1,load_address=-1,n;
			//search for LOAD entry that can be checked for by passing or forwarding
			for(LSQEntry le: lsq) {
				if(le.cycleNo==cycleNo-1 && le.load_or_store=='L') {
					load_entry=i;
					
				}
				i++;
			}
			if(load_entry==-1)
				return;
			load_address=lsq.get(load_entry).address;
			
			//check for forwarding
			n=lsq.size();
			for(i=n-1;i>=0;i--) {
				LSQEntry le=lsq.get(i);
				if(le.load_or_store == 'S' && le.address == load_address) {
					forward = true;
					store_index = i;
					store_insID=le.insID;
					break;
				}
			}
			ROB_Entry peek=rob.peek();
			if(MEM_counter == 0 && store_index == 0 && peek.insID==store_insID) {
				forward = false;
			}
			if(forward) {
				LSQEntry load = lsq.get(load_entry);
				prf[load.load_reg_no].pr_value = lsq.get(store_index).value;
				prf[load.load_reg_no].pr_busy = false;
				writetoROB(load.load_reg_no,lsq.get(store_index).value);
				forwardResultToIQ(load.load_reg_no,lsq.get(store_index).value);
				return;
			}
			//check for bypassing
			for(i=n-1;i>=0;i--) {
				LSQEntry le=lsq.get(i);
				if(le.load_or_store == 'S' && le.address == -1) {
					bypass = false;
					return;
				}
				
			}
			
			//Handling bypassing
			i=0;
			int index=-1;
			for(LSQEntry lq : lsq) 
			{
				if((lq.address==-1 && lq.load_or_store=='L'))
				{
					index=i;
					break;
				}
				if(lq.load_or_store=='S')
				{
					if(MEM_counter == 0 && i == 0 && peek.insID==store_insID)
					{
						continue;
					}
					else
					{
						index=i;
						break;
					}
				}
				i++;
			}
			
			int temp=0;i=0;
			if(index!=-1)
			{
				LSQEntry lq= lsq.get(load_entry);
				lsq.add(index, lq);
				for(LSQEntry le: lsq)
				{
					if(le.cycleNo==cycleNo-1)
					{
						temp=i;
						break;
					}
					i++;
				}
				lsq.remove(temp);
			}
			
		}
		

		private void decode(InstructionInfo item) {
			// TODO Auto-generated method stub
			String type="";
			//1)tokenize for type
			//2)Rename Logic
			//3)Mark src as valid/invalid, collect operands and set destination no
			
			String[] tokens=new String[200];
		//	System.out.println("Decode "+item.instruction_string);
			
			if(item.instruction_string!=null)
            {    
       
              tokens=item.instruction_string.split(",");
              
               for(int x=0;x<tokens.length;x++)
               {   
                tokens[x]=tokens[x].replace(",", "");
             //   tokens[x]=tokens[x].replace("#", "");
             /*   if(!tokens[x].equalsIgnoreCase("STORE"))
                {    
                  tokens[x]=tokens[x].replace("R", "");
                }*/
    
         //   length=tokens.length;
            
               } 
               item.type=tokens[0];
              // System.out.println("item.type "+item.type);
               
               //RENAME LOGIC FOR REG-REG
               if(Utils.REG_TO_REG_INSTRUCTIONS.contains(item.type)) //to be added by anuja
               {
            	   int a=Integer.parseInt(tokens[1].replace("R",""));
            	   int b=Integer.parseInt(tokens[2].replace("R",""));
            	   int c=Integer.parseInt(tokens[3].replace("R",""));
            	   
            	   if(rat[b].rp_bit=='P')
            	   {
            		   tokens[2]="P"+rat[b].physical_register;
            	   }
            	   	   
            	   if(rat[c].rp_bit=='P')
            	   {
            		   tokens[3]="P"+rat[c].physical_register;
            	   }
            	   
            	   
            //	   if(rat[a].rp_bit=='R')
            //	   {	   
            	    rat[a].rp_bit='P';
            	    for(int i=0;i<prf.length;i++)
            	    { 	
            	    	if(prf[i].pr_busy==false && prf[i].pr_allocated==false )
            	    	{
            	    	rat[a].physical_register=i;
            	    	item.destination_no=i;
            	    	prf[i].pr_allocated=true;
            	    	tokens[1]="P"+i;
            	    	item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2]+","+tokens[3];
            	    	break;
            	    	}
            	    			
            	    }
            	    
            //	   }
            	    item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2]+","+tokens[3];
               }
               if(item.type.equalsIgnoreCase("MOVC")) //RENAME FOR MOVC
               {
            	   int a=Integer.parseInt(tokens[1].replace("R",""));
            	   item.literal=Integer.parseInt(tokens[2].replace("#",""));
            	    rat[a].rp_bit='P';
            	    
            	    for(int i=0;i<prf.length;i++)
            	    { 	
            	    	if(prf[i].pr_busy==false && prf[i].pr_allocated==false )
            	    	{
            	    	rat[a].physical_register=i;
            	    	item.destination_no=i;
            	    	prf[i].pr_allocated=true;
            	    	tokens[1]="P"+i;
            	    	item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2];
            	    	break;
            	    	}
            	    			
            	    }
            	    
            	    
            //	   }
            	    item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2];
               }
               
               if(item.type.equalsIgnoreCase("JAL")) //RENAME FOR JAL
               {
            	   int a=Integer.parseInt(tokens[1].replace("R",""));
            	   int b=Integer.parseInt(tokens[2].replace("R",""));
            	   item.literal=Integer.parseInt(tokens[3].replace("#",""));
            	   
            	   if(rat[b].rp_bit=='P')
            	   {
            		   tokens[2]="P"+rat[b].physical_register;
            	   }
            	   
            	   rat[a].rp_bit='P';
            
            	   for(int i=0;i<prf.length;i++)
           	      { 	
           	    	if(prf[i].pr_busy==false && prf[i].pr_allocated==false )
           	    	{
           	    	rat[a].physical_register=i;
           	    	item.destination_no=i;
           	    	prf[i].pr_allocated=true;
           	    	tokens[1]="P"+i;
           	    	item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2];
           	    	
           	    	}
           	    			
           	      }
            	   
            	   item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2]+","+tokens[3];
            	   
               }	   
            	   
            	   
               
               if(item.type.equalsIgnoreCase("JUMP")) //RENAME FOR JUMP
               {
            	   int a=Integer.parseInt(tokens[1].replace("R",""));
            	   item.literal=Integer.parseInt(tokens[2].replace("#",""));
           	       
            	   
            	   if(rat[a].rp_bit=='P')
            	   {
            		   tokens[1]="P"+rat[a].physical_register;
            	   }
            	   
            	   item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2];
               }
               
               if(item.type.equalsIgnoreCase("BZ")) //RENAME FOR BZ
               {
            	   item.literal=Integer.parseInt(tokens[1].replace("#",""));
     
               }
               if(item.type.equalsIgnoreCase("BNZ")) //RENAME FOR BZ
               {
            	   item.literal=Integer.parseInt(tokens[1].replace("#",""));
     
               }
               
               
               //RENAME LOGIC FOR LOAD AND STORE
               //RENAME LOGIC FOR LOAD
               if(item.type.equalsIgnoreCase("LOAD")) {
            	   
            	   int a=Integer.parseInt(tokens[1].replace("R",""));
            	   int b=Integer.parseInt(tokens[2].replace("R",""));
            	  
            	   
            	   if(rat[b].rp_bit=='P')
            	   {
            		   tokens[2]="P"+rat[b].physical_register;
            	   }
            	   	   
            	  
            	   
            	   
            //	   if(rat[a].rp_bit=='R')
            //	   {	   
            	    rat[a].rp_bit='P';
            	    for(int i=0;i<prf.length;i++)
            	    { 	
            	    	if(prf[i].pr_busy==false && prf[i].pr_allocated==false )
            	    	{
            	    	rat[a].physical_register=i;
            	    	item.destination_no=i;
            	    	prf[i].pr_allocated=true;
            	    	tokens[1]="P"+i;
            	    	item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2]+","+tokens[3];
            	    	break;
            	    	}
            	    			
            	    }
            	    
            //	   }
            	    item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2]+","+tokens[3];
            	   
               }
              //COLLECTING OPERANDS FOR LOAD AND DETERMINING DEPENDENCIES 
               
               if(item.type.equalsIgnoreCase("LOAD"))
               {
            	   tokens=item.instruction_string.split(",");
                   
                  
                   if(tokens[2].contains("R"))
                   {
                	   tokens[2]=tokens[2].replace("R", "");
                	   
                	   int b=Integer.parseInt(tokens[2]);
                	   
                	   
                	   
                	   item.src1.valid=1;
                	   item.src1.value=arf[b].ar_value;
                	   item.src1.type='R';
                	   item.src1.register_no=b;
                   }
                   else if(tokens[2].contains("P"))
                   {
                	   tokens[2]=tokens[2].replace("P", "");
                	   
                	   int b=Integer.parseInt(tokens[2]);
                	   
                	   if(prf[b].pr_busy)
                	   {	   
                		   item.src1.valid=0;
                	   
                	   }
                	   else
                	   {
                		   item.src1.valid=1; 
                		   item.src1.value=prf[b].pr_value;
                	   }
                	   item.src1.type='P';
                	   item.src1.register_no=b;
                   }
                  
                   item.literal=Integer.parseInt(tokens[3].replace("#",""));
            	   
               
            
            }
			
               //RENAME LOGIC FOR STORE
               
               if(item.type.equalsIgnoreCase("STORE")) //to be added by anuja
               {
            	  
            	   int b=Integer.parseInt(tokens[1].replace("R",""));
            	   int c=Integer.parseInt(tokens[2].replace("R",""));
            	   
            	   if(rat[b].rp_bit=='P')
            	   {
            		   tokens[1]="P"+rat[b].physical_register;
            	   }
            	   	   
            	   if(rat[c].rp_bit=='P')
            	   {
            		   tokens[2]="P"+rat[c].physical_register;
            	   }
            	   
            	   
           
            	    item.instruction_string=tokens[0]+","+tokens[1]+","+tokens[2]+","+tokens[3];
               }
               
               //COLLECTING DEPENDENCIES FOR STORE AND DETERMINING OPERANDS
               
               if(item.type.equalsIgnoreCase("STORE"))
               {
            	   tokens=item.instruction_string.split(",");
                   
                  
                   if(tokens[1].contains("R"))
                   {
                	   tokens[1]=tokens[1].replace("R", "");
                	   
                	   int b=Integer.parseInt(tokens[1]);
                	   
                	   
                	   
                	   item.src1.valid=1;
                	   item.src1.value=arf[b].ar_value;
                	   item.src1.type='R';
                	   item.src1.register_no=b;
                   }
                   else if(tokens[1].contains("P"))
                   {
                	   tokens[1]=tokens[1].replace("P", "");
                	   
                	   int b=Integer.parseInt(tokens[1]);
                	   
                	   if(prf[b].pr_busy)
                	   {	   
                		   item.src1.valid=0;
                	   
                	   }
                	   else
                	   {
                		   item.src1.valid=1; 
                		   item.src1.value=prf[b].pr_value;
                	   }
                	   item.src1.type='P';
                	   item.src1.register_no=b;
                   }
                   if(tokens[2].contains("R"))
                   {
                	   tokens[2]=tokens[2].replace("R", "");
                	   
                	   int c=Integer.parseInt(tokens[2]);
                	   
                	   item.src2.valid=1;
                	   item.src2.value=arf[c].ar_value;
                	   item.src2.type='R';
                	   item.src2.register_no=c;
                   }
                   else if(tokens[2].contains("P"))
                   {
                	   tokens[2]=tokens[2].replace("P", "");
                	   
                	   int c=Integer.parseInt(tokens[2]);
                	   
                	   if(prf[c].pr_busy)
                	   {	   
                		   item.src2.valid=0;
                	   
                	   }
                	   else
                	   {
                		   item.src2.valid=1; 
                		   item.src2.value=prf[c].pr_value;
                	   }
                	   item.src2.type='P';
                	   item.src2.register_no=c;
                   }
                   item.literal=Integer.parseInt(tokens[3].replace("#",""));
               
            
            }
               
               
             
               
               
               
               
               
               
               //COLLECTING OPERANDS FOR REG-REG AND DETERMINING DEPENDENCIES
               if(Utils.REG_TO_REG_INSTRUCTIONS.contains(item.type)  && !item.type.equalsIgnoreCase("MOVC"))
               {
            	   tokens=item.instruction_string.split(",");
                   
                  
                   if(tokens[2].contains("R"))
                   {
                	   tokens[2]=tokens[2].replace("R", "");
                	   
                	   int b=Integer.parseInt(tokens[2]);
                	   
                	   
                	   
                	   item.src1.valid=1;
                	   item.src1.value=arf[b].ar_value;
                	   item.src1.type='R';
                	   item.src1.register_no=b;
                   }
                   else if(tokens[2].contains("P"))
                   {
                	   tokens[2]=tokens[2].replace("P", "");
                	   
                	   int b=Integer.parseInt(tokens[2]);
                	   
                	   if(prf[b].pr_busy)
                	   {	   
                		   item.src1.valid=0;
                	   
                	   }
                	   else
                	   {
                		   item.src1.valid=1; 
                		   item.src1.value=prf[b].pr_value;
                	   }
                	   item.src1.type='P';
                	   item.src1.register_no=b;
                   }
                   if(tokens[3].contains("R"))
                   {
                	   tokens[3]=tokens[3].replace("R", "");
                	   
                	   int c=Integer.parseInt(tokens[3]);
                	   
                	   item.src2.valid=1;
                	   item.src2.value=arf[c].ar_value;
                	   item.src2.type='R';
                	   item.src2.register_no=c;
                   }
                   else if(tokens[3].contains("P"))
                   {
                	   tokens[3]=tokens[3].replace("P", "");
                	   
                	   int c=Integer.parseInt(tokens[3]);
                	   
                	   if(prf[c].pr_busy)
                	   {	   
                		   item.src2.valid=0;
                	   
                	   }
                	   else
                	   {
                		   item.src2.valid=1; 
                		   item.src2.value=prf[c].pr_value;
                	   }
                	   item.src2.type='P';
                	   item.src2.register_no=c;
                   }
            	   
               }
            if(item.type.equalsIgnoreCase("HALT")) {
            	
            	
            	//add ROB Entry
            	if(!halt) {
            	ROB_Entry rob_entry=new ROB_Entry();
    			
    			rob_entry.dest_reg=-999;
    			
    			rob_entry.instruction_string=item.instruction_string;
    			rob_entry.active=true;
    			rob_entry.done_bit=true;
    			rob_entry.insID=item.insID;
    			rob_entry.instructionNumber=item.instructionNumber;
    			rob.insert(rob_entry);
    			
    			item.inROB=true;
            	}
    			halt=true;
            	
            }
           }
			
			
			
			
			
			
			/*
			//4)Put the entry in issue queue
			
			issueQueuePtr++;
			issueQueue[issueQueuePtr]=new IssueQueue();
			issueQueue[issueQueuePtr].src1=item.src1;
			issueQueue[issueQueuePtr].src2=item.src2;
			issueQueue[issueQueuePtr].dest_no=item.destination_no;
			issueQueue[issueQueuePtr].cycle_no=cycleNo;
			issueQueue[issueQueuePtr].instruction_no=item.instructionNumber;
			if(Utils.INT_FU_INSTRUCTIONS.contains(item.type)) {
				issueQueue[issueQueuePtr].FU_type=0;
			}
			else if(item.type.equalsIgnoreCase("MUL")) {
				issueQueue[issueQueuePtr].FU_type=1;
				
			}
			else if(item.type.equalsIgnoreCase("DIV") || type.equalsIgnoreCase("HALT")) {
				issueQueue[issueQueuePtr].FU_type=2;
			}
			
			
			//5)Put Entry in ROB
			ROB_Entry rob_entry=new ROB_Entry();
			if(item.type.equalsIgnoreCase("STORE"))
			rob_entry.dest_reg=-1;
			else
			rob_entry.dest_reg=item.destination_no;
			
			rob_entry.active=true;
			rob_entry.done_bit=false;
			rob_entry.insID=item.insID;
			rob.insert(rob_entry);
			*/
			
			//6)Mark PRF of corresponding register as busy
			prf[item.destination_no].pr_busy=true;
			
			//7)Put Entry in LSQ
			/*
			if(item.type.equalsIgnoreCase("LOAD") || item.type.equalsIgnoreCase("STORE")) {
				LSQEntry lq=new LSQEntry();
				
				if(item.type.equalsIgnoreCase("LOAD")) {
					lq.load_reg_no=item.destination_no;
					
					lq.load_or_store='L';
				}
				if(item.type.equalsIgnoreCase("STORE"))
					lq.load_or_store='S';
				lq.instruction_string=item.instruction_string;
				lq.instruction_no=item.instructionNumber;
				lq.insID=item.insID;
				
				lsq.add(lq);
				issueQueue[issueQueuePtr].objectHashLSQ=item.insID;
					
				
			}
			
			*/
			
			
			
		}
		void writetoROB(int destination_no, int value){
			for(int i=0; i<Utils.ROB_SIZE; i++){
				if(rob.ROB_Entry[i].dest_reg == destination_no && rob.ROB_Entry[i].active){
					rob.ROB_Entry[i].value = value;
					rob.ROB_Entry[i].done_bit = true;
					break;
				}
			}
		}

		void int_FU(InstructionInfo ins){
			String type = ins.type;
			int result=0,address=-1;
			switch(type){
				case "ADD" : result = ins.src1.value + ins.src2.value;
							break;
				case "SUB" : result = ins.src1.value - ins.src2.value;
							break;
				case "EX-OR" : result = ins.src1.value ^ ins.src2.value;
							break;
				case "EXOR" : result = ins.src1.value ^ ins.src2.value;
							break;
				case  "OR" : result = ins.src1.value | ins.src2.value;
							break;
				case "AND" : result = ins.src1.value & ins.src2.value;
							break;
				case "MOVC" : result = ins.literal;
							break;
				case "LOAD" : 
							address=ins.src1.value+ins.literal;
							writetoLSQ(address,cycleNo,ins.insID);
							break;
				case "STORE":
							address=ins.src2.value+ins.literal;
							result=ins.src1.value;
							writetoLSQ(address,cycleNo,result,ins.insID);
							break;
			}
			if(!ins.type.equalsIgnoreCase("LOAD") && !ins.type.equalsIgnoreCase("STORE")) {
			prf[ins.destination_no].pr_value = result;		//write to prf
			prf[ins.destination_no].pr_busy=false;
			writetoROB(ins.destination_no, result);
			forwardResultToIQ(ins.destination_no, result);
			}
		}
		void writetoLSQ(int address,int cycleNo,long insID) {
			int i=0;
			for(LSQEntry le:lsq) {
				if(le.insID==insID) {
					le.address=address;
					le.cycleNo=cycleNo;
					lsq.set(i, le);
				}
				i++;
			}
			
		}
		void writetoLSQ(int address,int cycleNo,int result,long insID) {
			int i=0;
			for(LSQEntry le:lsq) {
				if(le.insID==insID) {
					le.address=address;
					le.cycleNo=cycleNo;
					le.value=result;
					lsq.set(i, le);
				}
				i++;
			}
			
		}
		private void forwardResultToIQ(int destination_no, int result) {
			// TODO Auto-generated method stub
			
			int i;
			for(i=0;i<=issueQueuePtr;i++) {
				if(issueQueue[i].src1.register_no==destination_no && issueQueue[i].inQ) {
					issueQueue[i].src1.valid=1;
					issueQueue[i].src1.value=result;
				}
				if(issueQueue[i].src2.register_no==destination_no && issueQueue[i].inQ) {
					issueQueue[i].src2.valid=1;
					issueQueue[i].src2.value=result;
				}
			}
			i=0;
			for(InstructionInfo item : pipelineTODOs) {
				if(item.stage.equalsIgnoreCase("IQ")) {
					if(item.src1.register_no==destination_no) {
						item.src1.valid=1;
						item.src1.value=result;
						
					}
					if(item.src2.register_no==destination_no) {
						item.src2.valid=1;
						item.src2.value=result;
					}
					pipelineTODOs.set(i, item);
					
				}
				i++;
			}
			
		}
		
		void mul2(InstructionInfo ins){
			int result=0;
			result = ins.src1.value * ins.src2.value;
			prf[ins.destination_no].pr_value = result;		//write to prf
			prf[ins.destination_no].pr_busy=false;
			writetoROB(ins.destination_no, result);	
			forwardResultToIQ(ins.destination_no, result);
		}

		void div4(InstructionInfo ins){
			int result=0;
			result = ins.src1.value / ins.src2.value;
			prf[ins.destination_no].pr_value = result;		//write to prf
			prf[ins.destination_no].pr_busy=false;
			writetoROB(ins.destination_no, result);
			forwardResultToIQ(ins.destination_no, result);
		}

		private void reorderPipeline() {
			 InstructionInfo ins;
			 
			 
			 for(int i=0;i<Utils.reorder.length;i++) {
				 ins=getTODO(Utils.reorder[i]);
				 if(ins!=null) {
					 pipelineTODOs.remove(ins);
					 pipelineTODOs.add(0,ins);
					 }
			 }
			 
			 
			 
		 }
		InstructionInfo getTODO(String stage) {
			 for(InstructionInfo item : pipelineTODOs) {
				 if(item.stage.equalsIgnoreCase(stage))
					 return item;
			 }
			 return null;
		 }

		void displayCodeMem() {
			// TODO Auto-generated method stub
			 for(int i=0;i<code_memory.code_line.length;i++) {
				 System.out.println(code_memory.code_line[i].getFileLineNumber()+":"+
						 			code_memory.code_line[i].getAddress()+":"+
						 			code_memory.code_line[i].getInstructionString()
						 );
			 }
			
		}
		

		 void writeFiletoCodeMemory() throws IOException {
			// TODO Auto-generated method stub
			 int count=0;
			 File f = new File("src/"+TestSimulator.file);
			 BufferedReader b = new BufferedReader(new FileReader(f));
			 String readLine = "";
			 try {

		           while ((readLine = b.readLine()) != null) {
		        	   readLine=readLine.trim();
		        	   if(readLine.length()!=0)
		                count++;
		            }

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			 
			 code_memory.code_line=new CodeLine[count];
			 
			 try {
				 int i=0,address=4000;
				 	
		            
				 b = new BufferedReader(new FileReader(f));
		         readLine = "";
		         while ((readLine = b.readLine()) != null) {
		        	 readLine=readLine.trim();
		        	   if(readLine.length()==0)
		        		   continue;
		                code_memory.code_line[i]=new CodeLine();
		                code_memory.code_line[i].setAddress(address);
		                code_memory.code_line[i].setFileLineNumber(i);
		                code_memory.code_line[i].setInstructionString(readLine);
		               
		                i++;
		                address+=4;
		                }

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			 
			 
			 
			
		}
		 void fetch() {
			 
			if(halt)
				return;
			CodeLine cl=fetchInstruction();
			if(cl==null)
				return;
			
			InstructionInfo info=new InstructionInfo();
			info.instructionNumber=cl.getFileLineNumber();
			info.instruction_string=cl.getInstructionString();
			info.raw_instruction=cl.getInstructionString();
			info.stage="F";
			info.priority=Utils.getPriority(info.instruction_string);
			info.insID=id++;
			pipelineTODOs.add(info);
			
			
			
			
			 
			 
			 
			 
			 
		 }
		 private CodeLine fetchInstruction() {
				// TODO Auto-generated method stub
			
				this.PC+=4;
				
				int i;
				for(i=0;i<code_memory.code_line.length;i++) {
					if(code_memory.code_line[i].getAddress()==PC) {
						 
						return code_memory.code_line[i];
						
					}
				}
				this.PC-=4;
				return null;
			}
		 void initializePipeline() {
			// TODO Auto-generated method stub
			code_memory=new CodeMemory();
			data_memory=new DataMemory();
			
			
			
		}
		
		
}
