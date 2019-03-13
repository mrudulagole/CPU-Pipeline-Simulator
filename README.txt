Team Members:
	Anuja Bhide
	Mrudula Gole
	Neha Kulkarni

Representation of pipeline data structures
	•Register –class
	•Register file- array of objects of Register class
	•Instructions- class
	•Code Memory-class (has an array of objects of CodeLine class)
	•Data Memory –class (has an array for data)
	•PSW –class (displays only zero flag which is a member variable of class Flags)
	•Issue Queue -class (has fields for FU type, sources, destination, etc.)
	•LSQ -class
	•Rename Table -class
	•Re-order buffer(ROB) -class
	•ROB entry -class (has the fields: physical reg., value and done bit)
	•Physical Register file (PRF) -class
	•Architectural Register File(ARF) -class
	
	Rename Table:	
	Instructions get renamed in the D/RF stage. The destination register in the instruction is renamed with a physical register from 
	the list of free physical registers. This physical register is recorded as the stand in for the physical register in the RAT and 
	the source registers are replace with their most recent stand-ins.

	Reorder buffer:
	A reorder buffer with size 32 is implemented using a circular queue. Entries are made in this buffer from the tail and commits 
	are done from its head.

	Load Store Queue:
	An array list is used to implement the LSQ to maintain the order of the LOAD and STORE instructions. The LSQ supports forwarding 
	and bypassing. The LOAD and STORE instructions take 3 cycles to do the memory access.

COMMANDS:
	•initialize : initialize the pipeline
	•simulate n : simulate n cycles 
	•display : display status of pipeline for nth cycle, display architectural registers
	•exit : exit simulation

INSTRUCTION SET:
	ADD, SUB, MUL, OR, AND, EX-OR
	Destination:  Register Only
	Sources:   both Registers 
 
	LOAD 
	Destination:  Register Only
	2 Sources:     1 Register + 1 Literal 

	STORE (R1 R2 literal)
	store R1 value at memory whose address is obtained by adding R2 and literal

	MOVC
	1 Destination: Register Only.
	1 Source:  Literal 
 
	JUMP 1source_register 1literal

	HALT

	BZ 1literal

	BNZ 1literal

	JAL <dest> src literal
