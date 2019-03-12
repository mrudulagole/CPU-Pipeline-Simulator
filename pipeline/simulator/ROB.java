package cao.pipeline.simulator;

import java.io.DataInputStream;

  
class ROB{
	
        
        ROB_Entry rob =new ROB_Entry();
        ROB_Entry[] ROB_Entry =new ROB_Entry[Utils.ROB_SIZE] ;
        
	int front,rear;
	final int CAPACITY=Utils.ROB_SIZE;
	boolean empty=false;
        int i=0;
        
    ROB_Entry peek() {
    	if(!isEmpty())
    		return ROB_Entry[front];
    	return null;
    }
        
        
	ROB(){
            for(int i=0;i<ROB_Entry.length;i++)
            {  
                ROB_Entry[i]=new ROB_Entry();
            }    
		this.front=-1;
		this.rear=-1;
                
	}
	int get_rear(){
            
            
		if(rear<(CAPACITY-1))
			return (rear+1);
		else if(!isFull())
			return 0;
		return -1;

	}
	boolean isFull(){
		if((front==0 && rear==(CAPACITY-1 )))
                {    
                    System.out.println("ROB is full");
			return true;
                }       
                
		return false;
	}
	boolean isEmpty(){
		if(front==-1 && rear==-1)
			return true;
		if(front==-1 && rear==CAPACITY-1)
			return true;


		return empty;
	}
	void insert(ROB_Entry rob_entry){
		if(!isFull()){
			rear=get_rear();
		//	queue[rear]=x;
                     //   System.out.println("rear"+rear);
                        ROB_Entry[rear]=rob_entry;
                        
		//	System.out.println("Element "+rob_entry+" inserted in ROB at position "+rear);
			if(front==-1)
				front=0;
			empty=false;
		}
		else
			System.out.println("ERROR: ROB Full");
	}
	ROB_Entry remove(){
		if(!isEmpty()){
			if(front==-1)
				front++;
			//int head=queue[front];
			ROB_Entry head=ROB_Entry[front];
			ROB_Entry[front].active=false;
			empty=(front==rear);
			/*if(!empty)
			
			*/
			front=update_front();
			if(empty){
				front=-1;
				rear=-1;
			}
			
			return head;
			

		}
		else{
				System.out.println("ERROR: ROB Empty");
				return null;
		}
	}
	int update_front(){
		if(front<(CAPACITY-1))
			return front+1;
		else
			return 0;
	}
	void display_ROB(){
		int i;
		System.out.println("************************************************************************");
		if(isEmpty()){
			System.out.println("ROB is Empty");
			System.out.println("************************************************************************");
			return;
		}
		System.out.println("Index"+"\t"+"Value"+"\t"+"Destination"+"\t"+"Done");
		if(rear<front || front==-1){
			for(i=front;i<CAPACITY&&front!=-1;i++)
				System.out.println(i+"\t"+ROB_Entry[i].value+"\t"+ROB_Entry[i].dest_reg+"\t"+ROB_Entry[i].done_bit);
			for(i=0;i<=rear;i++)
				System.out.println(i+"\t"+ROB_Entry[i].value+"\t"+ROB_Entry[i].dest_reg+"\t"+ROB_Entry[i].done_bit);


		}
		else{
			for(i=front;i<=rear;i++)
				System.out.println(i+"\t"+ROB_Entry[i].value+"\t"+ROB_Entry[i].dest_reg+"\t"+ROB_Entry[i].done_bit);
		}
		/*System.out.println("\nFRONT="+front);
		System.out.println("REAR="+rear);
		System.out.println("************************************************************************");
*/
	}
	void display_ROB(int x){
		int i;
		
		if(isEmpty()){
			
			return;
		}
		
		if(rear<front || front==-1){
			for(i=front;i<CAPACITY&&front!=-1;i++)
				System.out.println("(I"+ROB_Entry[i].instructionNumber+")"+ROB_Entry[i].instruction_string);
			for(i=0;i<=rear;i++)
				System.out.println("(I"+ROB_Entry[i].instructionNumber+")"+ROB_Entry[i].instruction_string);


		}
		else{
			for(i=front;i<=rear;i++)
				System.out.println("(I"+ROB_Entry[i].instructionNumber+")"+ROB_Entry[i].instruction_string);
		}
		
	}

}
/*class CircularROB{
	public static void main(String[] args)throws Exception{
		ROB q=new ROB();
		int option=0,n=0;
		DataInputStream d=new DataInputStream(System.in);

		while(true){
			System.out.println("1.Insert element\n2.Remove element\n3.Exit");
			option=Integer.parseInt(d.readLine());
			if(option==3)
				break;
			switch(option){
				case 1 : 
						System.out.println("Enter element to be inserted");
						n=Integer.parseInt(d.readLine());
						q.insert(n);
						q.display_ROB();
						break;
				case 2 : 
						q.remove();
						q.display_ROB();
						break;

			}



		}
                 
	}
        
}

        
  */      
        
        
        
   //    System.out.println("value="+c); 
        
        
        
   // }
//}
